package com.example.dacn_murkoff_care_android.BookingPage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.Model.Photo;
import com.example.dacn_murkoff_care_android.R;
import com.example.dacn_murkoff_care_android.RecyclerView.BookingPhotoRecyclerView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookingPagePhotoActivity extends AppCompatActivity {

    private final String TAG = "Booking-page Photo Activity";
    private String bookingId;
    private String bookingStatus;
    private ImageButton btnBack;

    private Map<String, String> header;
    private LoadingScreen loadingScreen;
    private Dialog dialog;


    private BookingPhotoRecyclerView bookingPhotoAdapter;
    private RecyclerView recyclerView;
    private LinearLayout layout;
    private List<Photo> list;

    private AppCompatButton btnUpload;
    private int photoId;

    private GlobalVariable globalVariable;

    private BookingPageViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page_photo);

        setupComponent();
        setupViewModel();
        setupEvent();

    }


    private void setupComponent() {
        if( getIntent().getStringExtra("bookingId") != null)
        {
            bookingId = getIntent().getStringExtra("bookingId");
            bookingStatus = getIntent().getStringExtra("bookingStatus");
        }
        else
        {
            System.out.println(TAG);
            System.out.println("Booking ID is empty !");
            Toast.makeText(this, getString(R.string.oops_there_is_an_issue), Toast.LENGTH_SHORT).show();
            this.finish();
        }

        globalVariable = (GlobalVariable) this.getApplication();
        header = globalVariable.getHeaders();
        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);
        layout = findViewById(R.id.bookingLinearLayout);

        btnUpload = findViewById(R.id.btnUpload);

        if(Objects.equals(bookingStatus, "processing"))
        {
            btnUpload.setVisibility(View.VISIBLE);
        }
        else
        {
            btnUpload.setVisibility(View.GONE);
        }
    }


    private void setupViewModel() {
        /*DECLARE*/
        viewModel = new ViewModelProvider(this).get(BookingPageViewModel.class);
        viewModel.instantiate();


        /*SEND REQUEST*/
        viewModel.bookingPhotoReadAll(header, bookingId);
        viewModel.getBookingPhotoReadAllResponse().observe(this, response->{
            try
            {
                int result = response.getResult();
                /*result == 1 => luu thong tin nguoi dung va vao homepage*/
                if( result == 1)
                {
                    list = response.getData();
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
            if( aBoolean ) loadingScreen.start();
            else loadingScreen.stop();
        });
    }

    private void setupRecyclerView(List<Photo> list) {
    }


    private void setupEvent() {

    }


}