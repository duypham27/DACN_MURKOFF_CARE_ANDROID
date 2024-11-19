package com.example.dacn_murkoff_care_android.BookingPage;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn_murkoff_care_android.R;

public class BookingPageActivity extends AppCompatActivity {

    private final String TAG = "Booking_Page_Activity";

    private ImageButton btnBack;
    private final FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        setupBookingFragment1();
        setupComponent();
        setupEvent();

    }

    private void setupBookingFragment1()
    {
        String fragmentTag = "BookingFragment1";
        Fragment fragment = new BookingFragment1();


        /*Step 1*/
        FragmentTransaction transaction = manager.beginTransaction();

        /*Step 2*/
        String serviceId = getIntent().getStringExtra("serviceId");
        String doctorId = getIntent().getStringExtra("doctorId");

        /*Step 3*/
        Bundle bundle = new Bundle();
        bundle.putString("serviceId", serviceId);
        bundle.putString("doctorId", doctorId);
        fragment.setArguments(bundle);

        /*Step 4*/
        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /** SETUP COMPONENT **/
    private void setupComponent()
    {
        btnBack = findViewById(R.id.btnBack);
    }

    /** SETTING UP EVENT **/
    private void setupEvent()
    {
        btnBack.setOnClickListener(view-> finish());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}