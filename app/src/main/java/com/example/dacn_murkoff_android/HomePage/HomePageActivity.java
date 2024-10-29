package com.example.dacn_murkoff_android.HomePage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn_murkoff_android.AppointmentPage.AppointmentPageFragment;
import com.example.dacn_murkoff_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_android.LoginPage.LoginActivity;
import com.example.dacn_murkoff_android.NotificationPage.NotificationFragment;
import com.example.dacn_murkoff_android.R;
import com.example.dacn_murkoff_android.SettingsPage.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {

    private final String TAG = "HomePage Activity";
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*Enable HomeFragment by default*/
        fragment = new HomePageFragment();
        fragmentTag = "homePageFragment";
        enableFragment(fragment, fragmentTag);

        //Necessary Functions
        setupVariable();
        setupEvent();
        getDataIntent();


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

    private void getDataIntent() {
        String strPhoneNumber = getIntent().getStringExtra("phone_number");
        String username = getIntent().getStringExtra("display_name");


        TextView tvUserInfo = findViewById(R.id.tvUserInfo);

        if (strPhoneNumber != null && !strPhoneNumber.isEmpty()) {
            tvUserInfo.setText("Phone: " + strPhoneNumber);
        }
        else if (username != null && !username.isEmpty()) {
            tvUserInfo.setText("Welcome, " + username);
        }
        else {
            tvUserInfo.setText("No user info available");
        }
    }


    /**
     * activate a fragment right away
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

    /**
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