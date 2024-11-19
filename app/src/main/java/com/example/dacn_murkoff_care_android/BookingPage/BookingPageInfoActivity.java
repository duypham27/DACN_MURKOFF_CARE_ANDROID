package com.example.dacn_murkoff_care_android.BookingPage;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.Model.Booking;
import com.example.dacn_murkoff_care_android.R;

import java.util.Map;

public class BookingPageInfoActivity extends AppCompatActivity {

    private final String TAG = "Booking_Page_Info_Activity";

    private String bookingId;
    private String bookingStatus;


    private TextView txtBookingName;
    private TextView txtBookingPhone;
    private TextView txtPatientName;
    private TextView txtPatientGender;

    private TextView txtPatientBirthday;
    private TextView txtPatientAddress;
    private TextView txtPatientReason;
    private TextView txtDatetime;

    private TextView txtBookingStatus;

    private ImageView imgServiceAvatar;
    private TextView txtServiceName;

    private androidx.appcompat.widget.AppCompatButton btnPhoto;
    private androidx.appcompat.widget.AppCompatButton btnCancel;
    private ImageButton btnBack;

    private Map<String, String> header;

    private Dialog dialog;
    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page_info);

        setupComponent();
        setupViewModel();
        setupEvent();

    }

    /** SETTING UP COMPONENT **/
    private void setupComponent() {
        GlobalVariable globalVariable = (GlobalVariable) this.getApplication();
        header = globalVariable.getHeaders();
        bookingId = getIntent().getStringExtra("id");

        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);

        txtBookingName =findViewById(R.id.txtBookingName);
        txtBookingPhone = findViewById(R.id.txtBookingPhone);
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPatientAddress = findViewById(R.id.txtPatientAddress);
        txtPatientBirthday = findViewById(R.id.txtPatientBirthday);
        txtDatetime = findViewById(R.id.txtDatetime);
        txtPatientGender = findViewById(R.id.txtPatientGender);
        txtPatientReason = findViewById(R.id.txtPatientReason);
        txtBookingStatus = findViewById(R.id.txtBookingStatus);

        btnPhoto = findViewById(R.id.btnPhoto);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        imgServiceAvatar = findViewById(R.id.imgServiceAvatar);
        txtServiceName = findViewById(R.id.txtServiceName);
    }


    /** SETTING UP VIEWMODEL **/
    private void setupViewModel() {
        /*declare*/
        BookingPageViewModel viewModel = new ViewModelProvider(this).get(BookingPageViewModel.class);
        viewModel.instantiate();

        /*SEND REQUEST TO SERVER*/
        viewModel.bookingReadByID(header, bookingId);
        viewModel.getBookingReadByIdResponse().observe(this, response->{
            try
            {
                int result = response.getResult();
                /*result == 1 => luu thong tin nguoi dung va vao homepage*/
                if( result == 1)
                {
                    Booking booking = response.getData();
                    printBookingInfo(booking);
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
            if( aBoolean )
            {
                loadingScreen.start();
            }
            else
            {
                loadingScreen.stop();
            }
        });
        /*end ANIMATION*/
    }


    /** SETTING UP EVENT **/
    private void setupEvent() {

    }

    /** SHOW DETAIL INFO BOOKING **/
    private void printBookingInfo(Booking booking) {

    }

}