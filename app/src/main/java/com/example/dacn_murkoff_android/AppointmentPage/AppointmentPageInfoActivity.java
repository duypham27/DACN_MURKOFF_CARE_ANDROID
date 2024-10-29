package com.example.dacn_murkoff_android.AppointmentPage;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dacn_murkoff_android.Helper.Dialog;
import com.example.dacn_murkoff_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_android.R;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentPageInfoActivity extends AppCompatActivity {

    private final String TAG = "Appointment-page Info Activity";
    private Map<String, String> header;

    private Dialog dialog;
    private LoadingScreen loadingScreen;

    /*Data from recycler view*/
    private String appointmentId;// is the id of appointment that the patient are waiting for his/her turn
    private String myPosition;// is the patient position in queue
    private String doctorId;
    private boolean appointmentStatus = true;// is appointment status == false, we hide recycler view appointment queue


    private CircleImageView imgDoctorAvatar;
    private TextView txtDoctorName;

    private TextView txtSpecialityName;
    private TextView txtLocation;

    private TextView txtPatientName;
    private TextView txtNumericalOrder;
    private TextView txtPatientBirthday;

    private TextView txtPosition;
    private TextView txtPatientPhone;
    private TextView txtPatientReason;

    private TextView txtAppointmentDate;
    private TextView txtAppointmentTime;

    private TextView txtStatusCancel;
    private TextView txtStatusDone;
    private TextView txtStatusProcessing;

    private AppCompatButton btnWatchMedicalRecord;
    private AppCompatButton btnWatchMedicalTreatment;

    private ImageButton btnBack;
    private RecyclerView appointmentQueueRecyclerView;
    private TextView appointmentQueueTitle;

    private AppointmentPageViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_page_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupComponent();
        setupViewModel();
        setupEvent();
        setupUpdateAutomatically();

    }

    /** SETTING UP COMPONENT */
    private void setupComponent() {
        GlobalVariable globalVariable = (GlobalVariable) this.getApplication();
        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);

        header = globalVariable.getHeaders();

        /*data from appointment recycler view*/
        appointmentId = getIntent().getStringExtra("id");
        myPosition = getIntent().getStringExtra("position");
        doctorId = getIntent().getStringExtra("doctorId");

        txtPosition = findViewById(R.id.txtPosition);
        imgDoctorAvatar = findViewById(R.id.imgDoctorAvatar);
        txtDoctorName = findViewById(R.id.txtDoctorName);

        txtSpecialityName = findViewById(R.id.txtSpecialityName);
        txtLocation = findViewById(R.id.txtLocation);

        txtPatientName = findViewById(R.id.txtPatientName);
        txtNumericalOrder = findViewById(R.id.txtNumericalOrder);
        txtPatientBirthday = findViewById(R.id.txtPatientBirthday);

        txtPatientPhone = findViewById(R.id.txtPatientPhone);
        txtPatientReason = findViewById(R.id.txtPatientReason);

        txtAppointmentDate = findViewById(R.id.txtDate);
        txtAppointmentTime = findViewById(R.id.txtAppointmentTime);

        txtStatusCancel = findViewById(R.id.txtStatusCancel);
        txtStatusDone = findViewById(R.id.txtStatusDone);
        txtStatusProcessing = findViewById(R.id.txtStatusProcessing);

        btnWatchMedicalRecord = findViewById(R.id.btnWatchMedicalRecord);
        btnWatchMedicalTreatment = findViewById(R.id.btnWatchMedicalTreatment);

        btnBack = findViewById(R.id.btnBack);
        appointmentQueueRecyclerView = findViewById(R.id.appointmentQueueRecyclerView);
        appointmentQueueTitle = findViewById(R.id.appointmentQueueTitle);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

    }


    private void setupViewModel() {

    }


    private void setupEvent() {

    }


    private void setupUpdateAutomatically() {

    }

}