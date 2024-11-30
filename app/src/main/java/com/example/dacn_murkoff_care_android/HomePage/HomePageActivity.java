package com.example.dacn_murkoff_care_android.HomePage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn_murkoff_care_android.AppointmentPage.AppointmentPageFragment;
import com.example.dacn_murkoff_care_android.Configuration.HTTPRequest;
import com.example.dacn_murkoff_care_android.Configuration.HTTPService;
import com.example.dacn_murkoff_care_android.Container.NotificationReadAll;
import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.LoginPage.LoginActivity;
import com.example.dacn_murkoff_care_android.NotificationPage.NotificationFragment;
import com.example.dacn_murkoff_care_android.R;
import com.example.dacn_murkoff_care_android.SettingsPage.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePageActivity extends AppCompatActivity {

    private final String TAG = "Home_Page_Activity";
    private Dialog dialog;
    private GlobalVariable globalVariable;

    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private String fragmentTag;

    private SharedPreferences sharedPreferences;

    /*Weak Activity & GETTER*/
    public static WeakReference<HomePageActivity> weakActivity;
    public static HomePageActivity getInstance() {
        return weakActivity.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        weakActivity = new WeakReference<>(HomePageActivity.this);

        /*Enable HomeFragment by default*/
        fragment = new HomePageFragment();
        fragmentTag = "homePageFragment";
        enableFragment(fragment, fragmentTag);

        //Necessary Functions
        setupVariable();
        setupEvent();
        setNumberOnNotificationIcon();
        //Non Necessary Functions
        //getDataIntent();


    }



    /* Thiết lập xử lý các biến */
    private void setupVariable()
    {
        globalVariable = (GlobalVariable) this.getApplication();
        dialog = new Dialog(this);

        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
    }

    /* Thiết lập xử lý các lựa chọn */
    @SuppressLint("NonConstantResourceId")
    private void setupEvent(){
        /*set up event when users click on item in bottom navigation view*/
        bottomNavigationView.setOnItemSelectedListener(item -> {
            /*When ever users click on any icon, we updates the number of unread notifications*/


            int shortcut = item.getItemId();

            if(shortcut == R.id.shortcutHome)
            {
                //setNumberOnNotificationIcon(); (not necessary)
                fragment = new HomePageFragment();
                fragmentTag = "homeFragment";
            } else if (shortcut == R.id.shortcutNotification) {
                //setNumberOnNotificationIcon(); (necessary)
                fragment = new NotificationFragment();
                fragmentTag = "notificationFragment";
            } else if (shortcut == R.id.shortcutAppointment) {
                fragment = new AppointmentPageFragment();
                fragmentTag = "appointmentFragment";
            } else if (shortcut == R.id.shortcutPersonality) {
                fragment = new SettingsFragment();
                fragmentTag = "settingsFragment";
            }

            enableFragment(fragment, fragmentTag);
            return true;
        });
    }

//    private void getDataIntent() {
//        String strPhoneNumber = getIntent().getStringExtra("phone_number");
//        String username = getIntent().getStringExtra("display_name");
//
//
//        TextView tvUserInfo = findViewById(R.id.tvUserInfo);
//
//        if (strPhoneNumber != null && !strPhoneNumber.isEmpty()) {
//            tvUserInfo.setText("Phone: " + strPhoneNumber);
//        }
//        else if (username != null && !username.isEmpty()) {
//            tvUserInfo.setText("Welcome, " + username);
//        }
//        else {
//            tvUserInfo.setText("No user info available");
//        }
//    }


    /** NOTE:
     * Activate a fragment right away
     * */
    public void enableFragment(Fragment fragment, String fragmentTag)
    {
        /*Step 0 - update again fragmentTag to handle onBackPress()*/
        this.fragmentTag = fragmentTag;

        /*Step 1*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        /*Step 2*/
        Map<String, String > headers = ((GlobalVariable)getApplication()).getHeaders();
        String accessToken = headers.get("Authorization");
        String contentType = headers.get("Content-Type");


        /*Step 3*/
        Bundle bundle = new Bundle();
        bundle.putString("accessToken", accessToken);
        bundle.putString("contentType", contentType);
        fragment.setArguments(bundle);


        /*Step 4*/
        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        dialog.confirm();
        dialog.show(getString(R.string.attention),
                getString(R.string.are_you_sure_about_that), R.drawable.ic_info);
        dialog.btnOK.setOnClickListener(view->{
            super.onBackPressed();
            finish();
        });
        dialog.btnCancel.setOnClickListener(view-> dialog.close());
    }


    /** NOTE:
     * This function sets number on the right-top on notification icon
     * Which lays on Bottom Navigation View
     */
    public void setNumberOnNotificationIcon()
    {
        /*Step 1 - setup Retrofit*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2 - prepare header*/
        Map<String, String> header = globalVariable.getHeaders();

        /*Step 3*/
        Call<NotificationReadAll> container = api.notificationReadAll(header);

        /*Step 4*/
        container.enqueue(new Callback<NotificationReadAll>() {
            @Override
            public void onResponse(@NonNull Call<NotificationReadAll> call, @NonNull Response<NotificationReadAll> response) {
                /*if successful, update the number of unread notification*/
                if(response.isSuccessful())
                {
                    NotificationReadAll content = response.body();
                    assert content != null;
                    /*update the number of unread notification*/
                    int quantityUnread = content.getQuantityUnread();
                    bottomNavigationView
                            .getOrCreateBadge(R.id.shortcutNotification)
                            .setNumber(quantityUnread);
                }
                /*if fail, show exception*/
                if(response.errorBody() != null)
                {
                    System.out.println(response);
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    }
                    catch (Exception e) {
                        System.out.println(TAG);
                        System.out.println("Exception: " + e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationReadAll> call, @NonNull Throwable t) {
                System.out.println(TAG);
                System.out.println("setNumberOnNotificationIcon - error: " + t.getMessage());
            }
        });
    }


    /** NOTE:
     * Exit the application
     * This function is called in settingRecyclerView
     */
    public void exit()
    {
        SharedPreferences sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);

        sharedPreferences.edit().putString("accessToken", null).apply();
        sharedPreferences.edit().putInt("darkMode", 1).apply();// 1 is off, 2 is on
        sharedPreferences.edit().putString("language", getString(R.string.vietnamese)).apply();


        System.out.println(TAG);
        System.out.println("access token: " + sharedPreferences.getString("accessToken", null) );

        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

}