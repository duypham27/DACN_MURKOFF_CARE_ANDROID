package com.example.dacn_murkoff_care_android.SettingsPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.Model.Booking;
import com.example.dacn_murkoff_care_android.R;
import com.example.dacn_murkoff_care_android.RecyclerView.BookingRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingHistoryActivity extends AppCompatActivity {

    private final String TAG = "Booking_History_Activity";
    private ImageButton btnBack;
    private RecyclerView bookingRecyclerView;

    private Map<String, String> header;
    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);


        setupComponent();
        setupViewModel();
        setupEvent();


    }


    /** SETUP COMPONENT **/
    private void setupComponent() {
        btnBack = findViewById(R.id.btnBack);
        bookingRecyclerView = findViewById(R.id.bookingRecyclerView);

        GlobalVariable globalVariable = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);

        header = globalVariable.getHeaders();
        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);
    }


    /** SETUP VIEWMODEL **/
    private void setupViewModel() {
        /*declare*/
        SettingsPageViewModel viewModel = new ViewModelProvider(this).get(SettingsPageViewModel.class);
        viewModel.instantiate();

        /*SEND REQUEST*/
        Map<String, String> parameters = new HashMap<>();
        parameters.put("length", "20");
        viewModel.bookingReadAll(header, parameters);

        viewModel.getBookingReadAll().observe(this, response->{
            try
            {
                int result = response.getResult();
                /*result == 1 => luu thong tin nguoi dung va vao homepage*/
                if( result == 1)
                {
                    List<Booking> list = response.getData();
                    setupRecyclerView(list);
                }
                /*result == 0 => thong bao va thoat ung dung*/
                if( result == 0)
                {
                    dialog.announce();
                    dialog.show(R.string.attention, getString(R.string.check_your_internet_connection), R.drawable.ic_info);
                    dialog.btnOK.setOnClickListener(view->{
                        dialog.close();
                        finish();
                    });
                }

            }
            catch(Exception ex)
            {
                /*Neu truy van lau qua ma khong nhan duoc phan hoi thi cung dong ung dung*/
                System.out.println(TAG);
                System.out.println(ex);

                dialog.announce();
                dialog.show(R.string.attention, getString(R.string.check_your_internet_connection), R.drawable.ic_info);
                dialog.btnOK.setOnClickListener(view->{
                    dialog.close();
                    finish();
                });
            }

        });/*end SEND REQUEST*/

        /*ANIMATION*/
        viewModel.getAnimation().observe(this, aBoolean -> {
            if(aBoolean) loadingScreen.start();
            else loadingScreen.stop();
        });
    }


    /** SETUP EVENT **/
    private void setupEvent() {
        btnBack.setOnClickListener(view->finish());
    }


    /** SETUP EVENT **/
    private void setupRecyclerView(List<Booking> list) {
        BookingRecyclerView appointmentAdapter = new BookingRecyclerView(this, list);
        bookingRecyclerView.setAdapter(appointmentAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookingRecyclerView.setLayoutManager(manager);
    }

}