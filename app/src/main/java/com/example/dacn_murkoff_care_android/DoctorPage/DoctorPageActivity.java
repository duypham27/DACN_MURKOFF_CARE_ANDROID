package com.example.dacn_murkoff_care_android.DoctorPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.dacn_murkoff_care_android.BookingPage.BookingPageActivity;
import com.example.dacn_murkoff_care_android.Configuration.Constant;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.Model.Doctor;
import com.example.dacn_murkoff_care_android.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorPageActivity extends AppCompatActivity {

    private static final String TAG = "Doctor-page Activity";

    private String doctorId;
    private CircleImageView imgAvatar;
    private TextView txtName;

    private TextView txtSpeciality;
    private TextView txtPhoneNumber;
    private WebView wvwDescription;

    private DoctorPageViewModel viewModel;
    private GlobalVariable globalVariable;
    private LoadingScreen loadingScreen;

    private ImageButton btnBack;
    private AppCompatButton btnCreateBooking;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_page);

        setupComponent();
        setupViewModel();
        setupEvent();

    }


    /** NEED ON RESUME FUNC **/


    /** SETUP COMPONENT **/
    private void setupComponent() {
        doctorId = getIntent().getStringExtra("doctorId");
        wvwDescription = findViewById(R.id.wvwDescription);

        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtSpeciality = findViewById(R.id.txtSpeciality);

        globalVariable = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);
        loadingScreen = new LoadingScreen(this);
        btnBack = findViewById(R.id.btnBack);
        btnCreateBooking = findViewById(R.id.btnCreateBooking);
    }


    /** SETUP VIEWMODEL **/
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DoctorPageViewModel.class);
        viewModel.instantiate();

        Map<String, String> headers = globalVariable.getHeaders();
        viewModel.readById(headers, doctorId);

        viewModel.getResponse().observe(this, response->{
            try
            {
                int result = response.getResult();
                if( result == 1)
                {
                    Doctor doctor = response.getData();
                    printDoctorInfo(doctor);
                }
                else
                {
                    Toast.makeText(this, getString(R.string.oops_there_is_an_issue), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            catch (Exception ex)
            {
                System.out.println(TAG);
                System.out.println(ex.getMessage());
                Toast.makeText(this, getString(R.string.oops_there_is_an_issue), Toast.LENGTH_SHORT).show();
                finish();
            }


        });

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


    /** PRINT DOCTOR INFO **/
    private void printDoctorInfo(Doctor doctor)
    {
        String name = doctor.getName();
        String avatar = Constant.UPLOAD_URI() + doctor.getAvatar();
        String phone = doctor.getPhone();
        String description = "<html>" +
                "<style>body{font-size: 11px}</style>"+
                "<body>"+  doctor.getDescription() +
                "</body>" +
                "</html>";
        String speciality = doctor.getSpeciality().getName();

        txtName.setText(name);
        txtSpeciality.setText(speciality);
        txtPhoneNumber.setText(phone);
        Picasso.get().load(avatar).into(imgAvatar);
        wvwDescription.loadDataWithBaseURL(null, description, "text/HTML", "UTF-8", null);
    }


    /** SETUP EVENT **/
    private void setupEvent() {
        btnBack.setOnClickListener(view->finish());

        btnCreateBooking.setOnClickListener(view->{
            Intent intent = new Intent(this, BookingPageActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });
    }
}