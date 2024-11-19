package com.example.dacn_murkoff_care_android.AlarmPage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn_murkoff_care_android.R;

public class AlarmPageActivity extends AppCompatActivity {

    private final String TAG = "AlarmPage_Activity";
    private final FragmentManager manager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_page);


        String fragmentTag = "AlarmFragment";
        Fragment fragment = new AlarmPageFragment();

        /*Step 1*/
        FragmentTransaction transaction = manager.beginTransaction();

        try
        {
            /*Step 2*/
            transaction.replace(R.id.frameLayout, fragment, fragmentTag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        catch (Exception ex)
        {
            System.out.println(TAG);
            ex.getStackTrace();
        }

    }


}