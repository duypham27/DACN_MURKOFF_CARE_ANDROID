package com.example.dacn_murkoff_care_android.LoginPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.HomePage.HomePageActivity;
import com.example.dacn_murkoff_care_android.Model.User;
import com.example.dacn_murkoff_care_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificationActivity extends AppCompatActivity {

    private final String TAG = "Verification_Activity";

    private EditText editOTP;
    private Button btnSendOTPCode;
    private TextView tvSendOTPAgain;

    private FirebaseAuth mAuth;
    private String phoneNumber;
    private String mVerificationId;

    private LoadingScreen loadingScreen;

    private LoginViewModel viewModel;
    //private LoadingScreen loadingScreen;
    private Dialog dialog;

    private SharedPreferences sharedPreferences;
    private GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otpactivity);

        // Gọi phương thức để lấy dữ liệu từ Intent
        getDataIntent();

        // Cài đặt các thành phần giao diện
        setupComponent();

        setupViewModel();

        // Xử lý sự kiện khi bấm nút gửi OTP
        setupEvent();
    }


    private void getDataIntent() {
        mVerificationId = getIntent().getStringExtra("verificationId");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(mVerificationId)) {
            Toast.makeText(this, R.string.empty_phone_number_or_verificationId, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    /** SETUP COMPONENT **/
    private void setupComponent() {
        editOTP = findViewById(R.id.editOTP);
        btnSendOTPCode = findViewById(R.id.btnSendOTPCode);
        tvSendOTPAgain = findViewById(R.id.tvSendOTPAgain);

        mAuth = FirebaseAuth.getInstance();
        loadingScreen = new LoadingScreen(this);
        dialog = new Dialog(this);

        globalVariable = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);

        // Khi bấm nút "Resend OTP"
        //tvSendOTPAgain.setOnClickListener(view -> resendOTP());
    }


    /** SETUP VIEWMODEL **/
    private void setupViewModel()
    {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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

        /*set up dialog*/
        dialog.announce();
        dialog.btnOK.setOnClickListener(view->dialog.close());

        viewModel.getLoginWithPhoneResponse().observe(this, loginResponse -> {

            if (loginResponse == null) {
                dialog.show(getString(R.string.attention),
                        getString(R.string.oops_there_is_an_issue),
                        R.drawable.ic_close);
                return;
            }

            int result = loginResponse.getResult();
            String message = loginResponse.getMsg();

            /*Case 1 - login successfully*/
            if( result == 1)
            {
                /*Lay du lieu tu API ra*/
                String token = loginResponse.getAccessToken();
                User user = loginResponse.getData();

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                /*Lay du lieu vao Global Variable*/
                globalVariable.setAccessToken( "JWT " + token );
                globalVariable.setAuthUser(user);
                Log.d(TAG,"ACCESS TOKEN: " + globalVariable.getAccessToken());

                /*luu accessToken vao Shared Reference*/
                sharedPreferences.edit().putString("accessToken", "JWT " + token.trim()).apply();

                /*hien thi thong bao la dang nhap thanh cong*/
                Toast.makeText(this, getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VerificationActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
            /*Case 2 - login failed*/
            else
            {
                System.out.println(TAG);
                System.out.println("result: " + result);
                System.out.println("msg: " + message);
                dialog.show(getString(R.string.attention),
                        message,
                        R.drawable.ic_close);
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();

            }

        });
    }



    /** SETUP EVENT **/
    private void setupEvent()
    {
        btnSendOTPCode.setOnClickListener(view->{
            /*Step 1 - get verificationCode and create credential*/
            String verificationCode = editOTP.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);


            System.out.println(TAG);
            System.out.println("Credential: " + credential);

            /*Step 2 - verify and go ahead*/
            signInWithPhoneAuthCredential(credential);
        });
    }


    /** NOTE:
     This function calls LoginViewModel login() function to get ACCESS TOKEN
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user = task.getResult().getUser();

                        assert user != null;
                        String phone = "0" + phoneNumber;// append the zero letter in the first position of phone number
                        String password = user.getUid();

                        System.out.println(TAG);
                        System.out.println("signInWithPhoneAuthCredential");
                        System.out.println("Phone: " + phone);
                        System.out.println("Password: " + password);

                        viewModel.loginWithPhone(phone, password);

                    }
                    else
                    {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            Toast.makeText(VerificationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error: " + task.getException() );
                        }
                    }
                });
    }//** end signInWithPhoneAuthCredential
}
