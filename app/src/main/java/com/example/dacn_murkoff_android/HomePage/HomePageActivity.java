package com.example.dacn_murkoff_android.HomePage;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.dacn_murkoff_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;

public class HomePageActivity extends AppCompatActivity {

    private final String TAG = "Homepage Activity";
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
        //enableFragment(fragment, fragmentTag);

        getDataIntent();

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




}