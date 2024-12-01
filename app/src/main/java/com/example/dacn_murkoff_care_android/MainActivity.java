package com.example.dacn_murkoff_care_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.MyNotification;
import com.example.dacn_murkoff_care_android.Helper.Tooltip;
import com.example.dacn_murkoff_care_android.HomePage.HomePageActivity;
import com.example.dacn_murkoff_care_android.LoginPage.LoginActivity;
import com.example.dacn_murkoff_care_android.Model.User;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Main_Activity";
    private SharedPreferences sharedPreferences;
    private GlobalVariable globalVariable;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*Step 0 - declare sharedPreferences & globalVariable*/
        globalVariable = (GlobalVariable)this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        dialog = new Dialog(this);


        //If we wanna use notification on Android 8 or higher, this function must be run
        MyNotification notification = new MyNotification(this);
        notification.createChannel();


        /*Step 1 - does the application connect to Internet? - NO, close the application*/
        boolean isConnected = isInternetAvailable();
        if( !isConnected )
        {
            dialog.announce();
            dialog.show(R.string.attention, getString(R.string.check_your_internet_connection), R.drawable.ic_info);
            dialog.btnOK.setOnClickListener(view->{
                dialog.close();
                finish();
            });
            return;
        }


        /*Step 2 - is dark mode turned on?*/
        int value = sharedPreferences.getInt("darkMode", 1);
        AppCompatDelegate.setDefaultNightMode(value);


        /*Step 3 - what is the language of application?
         * Find onResume to watch*/


        /*Step 4 - is AccessToken null?*/
        String accessToken = sharedPreferences.getString("accessToken", null);
        System.out.println(TAG);
        System.out.println(accessToken);
        if(accessToken != null)
        {
            /*global variable chi hoat dong trong phien lam viec nen phai gan lai accessToken cho no*/
            globalVariable.setAccessToken(accessToken);

            /*cai dat header voi yeu cau doc thong tin ca nhan cua mot benh nhan*/
            Map<String, String> headers = globalVariable.getHeaders();

            /*gui yeu cau doc thong tin benh nhan*/
            viewModel.readPersonalInformation(headers);

            /*lang nghe phan hoi*/
            viewModel.getResponse().observe(this, response->{
                try
                {
                    int result = response.getResult();
                    /*result == 1 => luu thong tin nguoi dung va vao homepage*/
                    if( result == 1)
                    {
                        /*cap nhat thong tin nguoi dung*/
                        User user = response.getData();
                        globalVariable.setAuthUser( user );

                        /*bat dau vao trang chu*/
                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    /*result == 0 => thong bao va cho dang nhap lai*/
                    if( result == 0)
                    {
                        System.out.println(TAG);
                        System.out.println("result: " + result);
                        System.out.println("msg: " + response.getMsg());
                        sharedPreferences.edit().putString("accessToken",null).apply();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                catch(Exception ex)
                {
                    /*Neu truy van lau qua ma khong nhan duoc phan hoi thi cung dong ung dung*/
                    System.out.println(TAG + "- exception: " + ex.getMessage());
                    dialog.announce();
                    dialog.show(R.string.attention, getString(R.string.check_your_internet_connection), R.drawable.ic_info);
                    dialog.btnOK.setOnClickListener(view->{
                        dialog.close();
                        finish();
                    });
                }
            });

        }
        else
        {
            /*delay 1s before starting HomeActivity*/
            Handler handler = new Handler(Looper.myLooper());
            handler.postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            },1000);
        }
    }/*end OnCreate*/


    @Override
    protected void onResume() {
        super.onResume();
        Tooltip.setLocale(this, sharedPreferences);
    }

    /** NOTE:
     * BOOLEAN
     * True if we are connecting with Internet
     * False if we aren't
     */
    public boolean isInternetAvailable() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }

}