package com.example.dacn_murkoff_android.EmailPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn_murkoff_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_android.Helper.Tooltip;
import com.example.dacn_murkoff_android.R;

public class EmailPageActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private final FragmentManager manager = getSupportFragmentManager();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_page);


        setupEmailFragment1();
        setupComponent();
        setupEvent();


    }



    private void setupEmailFragment1() {
        String fragmentTag = "EmailFragment1";
        Fragment fragment = new EmailFragment1();


        /*Step 1*/
        FragmentTransaction transaction = manager.beginTransaction();

        /*Step 2*/
//        String serviceId = getIntent().getStringExtra("serviceId");

        /*Step 3*/
//        Bundle bundle = new Bundle();
//        bundle.putString("serviceId", serviceId);
//        fragment.setArguments(bundle);

        /*Step 4*/
        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }


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


    private void setupEvent() {
        btnBack.setOnClickListener(view-> finish());
    }

}