package com.example.dacn_murkoff_care_android.TreatmentPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.Tooltip;
import com.example.dacn_murkoff_care_android.R;

public class TreatmentPageActivity extends AppCompatActivity {

    private final String TAG = "Treatment_Page_Activity";
    private SharedPreferences sharedPreferences;

    private ImageButton btnBack;
    private final FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_page);

        setupComponent();
        setupEvent();
        setupTreatmentFragment1();

    }

    /** SETUP COMPONENT **/
    private void setupComponent() {
        btnBack = findViewById(R.id.btnBack);
        GlobalVariable globalVariable = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tooltip.setLocale(this, sharedPreferences);
    }

    private void setupTreatmentFragment1()
    {
        String fragmentTag = "Treatment_Fragment1";
        Fragment fragment = new TreatmentFragment1();


        /*Step 1*/
        FragmentTransaction transaction = manager.beginTransaction();

        /*Step 2*/
        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupEvent() {
        btnBack.setOnClickListener(view-> this.finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}