package com.example.dacn_murkoff_care_android.SettingsPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.R;

public class AppearanceActivity extends AppCompatActivity {

    private final String TAG = "Appearance Activity";
    private ImageButton btnBack;
    private Spinner sprLanguage;
    private SwitchCompat switchDarkMode;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);

        setupComponent();
        setupEvent();
        //setupOptionLanguage();

    }


    /** SETTING UP COMPONENT **/
    private void setupComponent() {
        GlobalVariable globalVariable = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);

        int darkMode = sharedPreferences.getInt("darkMode", 1);// 1 is off, 2 is on

        btnBack = findViewById(R.id.btnBack);
        //sprLanguage = findViewById(R.id.sprLanguage);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchDarkMode.setChecked(false);
        if(darkMode == 2)
        {
            switchDarkMode.setChecked(true);
        }
    }

    /** SETTING UP EVENT **/
    private void setupEvent() {
        btnBack.setOnClickListener(view->finish());

        /*Switch On == turn on dark mode | Switch Off == turn off dark mode*/
        switchDarkMode.setOnCheckedChangeListener((compoundButton, flag) -> {
            int value;
            if(flag)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                value = AppCompatDelegate.MODE_NIGHT_YES;
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                value = AppCompatDelegate.MODE_NIGHT_NO;
            }
            sharedPreferences.edit().putInt("darkMode",value).apply();
        });
    }

    /** SETTING UP OPTION LANGUAGE **/
    private void setupOptionLanguage() {

    }


}