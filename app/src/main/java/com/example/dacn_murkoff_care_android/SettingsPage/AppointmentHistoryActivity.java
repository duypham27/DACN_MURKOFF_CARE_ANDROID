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
import com.example.dacn_murkoff_care_android.Model.Appointment;
import com.example.dacn_murkoff_care_android.R;
import com.example.dacn_murkoff_care_android.RecyclerView.Appointment2RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private final String TAG = "Appointment_History_Activity";
    private ImageButton btnBack;
    private RecyclerView appointmentRecyclerView;

    private Map<String, String> header;
    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        setupComponent();
        setupViewModel();
        setupEvent();

    }


    /** SETUP COMPONENT **/
    private void setupComponent() {
        GlobalVariable globalVariable1 = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable1.getSharedReferenceKey(), MODE_PRIVATE);

        btnBack = findViewById(R.id.btnBack);
        appointmentRecyclerView = findViewById(R.id.appointmentRecyclerView);

        GlobalVariable globalVariable = (GlobalVariable) this.getApplication();
        header = globalVariable.getHeaders();
        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);
    }


    /** SETUP VIEWMODEL **/
    private void setupViewModel() {
        SettingsPageViewModel viewModel = new ViewModelProvider(this).get(SettingsPageViewModel.class);
        viewModel.instantiate();

        /*SEND REQUEST*/
        Map<String, String> parameters = new HashMap<>();
        parameters.put("order[dir]", "desc");
        parameters.put("order[column]", "date");
        viewModel.readAll(header, parameters);
        viewModel.getReadAllResponse().observe(this, response->{
            try {
                int result = response.getResult();
                /*result == 1 => luu thong tin nguoi dung va vao homepage*/
                if (result == 1) {
                    List<Appointment> list = response.getData();
                    setupRecyclerView(list);
                }
                /*result == 0 => thong bao va thoat ung dung*/
                if (result == 0) {
                    System.out.println(TAG);;
                    System.out.println("READ ALL");
                    System.out.println("shut down by result == 0");
                    System.out.println("msg: " + response.getMsg());
                    dialog.announce();
                    dialog.show(R.string.attention, getString(R.string.oops_there_is_an_issue), R.drawable.ic_info);
                    dialog.btnOK.setOnClickListener(view -> {
                        dialog.close();
                        finish();
                    });
                }

            } catch (Exception ex) {
                System.out.println(TAG);
                System.out.println("READ ALL");
                System.out.println("shut down by exception");
                System.out.println(ex);
                /*Neu truy van lau qua ma khong nhan duoc phan hoi thi cung dong ung dung*/
                dialog.announce();
                dialog.show(R.string.attention, getString(R.string.check_your_internet_connection), R.drawable.ic_info);
                dialog.btnOK.setOnClickListener(view -> {
                    dialog.close();
                    finish();
                });
            }
        });/*end SEND REQUEST*/


        /*ANIMATION*/
        viewModel.getAnimation().observe(this, aBoolean -> {
            if( aBoolean )
            {
                loadingScreen.start();
            }
            else
            {
                loadingScreen.stop();
            }
        });
    }


    /** SETUP EVENT **/
    private void setupEvent() {
        btnBack.setOnClickListener(view->finish());
    }


    /** SETUP RECYCLERVIEW **/
    private void setupRecyclerView(List<Appointment> list)
    {
        Appointment2RecyclerView appointmentAdapter = new Appointment2RecyclerView(this, list);
        appointmentRecyclerView.setAdapter(appointmentAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appointmentRecyclerView.setLayoutManager(manager);
    }

}