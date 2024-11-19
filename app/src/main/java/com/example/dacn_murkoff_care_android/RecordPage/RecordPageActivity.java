package com.example.dacn_murkoff_care_android.RecordPage;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.R;

import java.util.Map;

/**
 * Record Page Activity
 * This activity show full information about a appointment's medical record
 **/

public class RecordPageActivity extends AppCompatActivity {

    private final String TAG = "Record-page activity";
    private String appointmentId;

    private Dialog dialog;
    private LoadingScreen loadingScreen;
    private Map<String, String> header;

    private ImageView imgDoctorAvatar;
    private TextView txtDoctorName;
    private TextView txtSpecialityName;
    private TextView txtDatetime;

    private TextView txtAppointmentReason;
    private TextView txtStatusBefore;
    private TextView txtStatusAfter;

    private TextView txtReason;
    private WebView wvwDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupComponent();
        setupViewModel();
    }


    /**
     * SETTING UP COMPONENT
     **/
    private void setupComponent() {
        dialog = new Dialog(this);
        GlobalVariable globalVariable = (GlobalVariable) this.getApplication();
        header = globalVariable.getHeaders();
        loadingScreen = new LoadingScreen(this);

        appointmentId = getIntent().getStringExtra("appointmentId");
        if(TextUtils.isEmpty(appointmentId) )
        {
            System.out.println(TAG);
            System.out.println("appointmentId is empty !");
            dialog.announce();
            dialog.show(R.string.attention, getString(R.string.oops_there_is_an_issue), R.drawable.ic_close);
            dialog.btnOK.setOnClickListener(view -> {
                dialog.close();
                this.finish();
            });
        }


        imgDoctorAvatar = findViewById(R.id.imgDoctorAvatar);
        txtDoctorName = findViewById(R.id.txtDoctorName);

        txtSpecialityName = findViewById(R.id.txtSpecialityName);
        txtDatetime = findViewById(R.id.txtDatetime);

        txtAppointmentReason = findViewById(R.id.txtAppointmentReason);
        txtStatusAfter = findViewById(R.id.txtStatusAfter);

        txtStatusBefore = findViewById(R.id.txtStatusBefore);
        txtReason = findViewById(R.id.txtReason);
        wvwDescription = findViewById(R.id.wvwDescription);
    }



    private void setupViewModel() {

    }
}