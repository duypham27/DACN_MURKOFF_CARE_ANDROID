package com.example.dacn_murkoff_android.LoginPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.dacn_murkoff_android.Helper.Dialog;
import com.example.dacn_murkoff_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_android.HomePage.HomePageActivity;
import com.example.dacn_murkoff_android.Model.User;
import com.example.dacn_murkoff_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    public static final String TAG = VerificationActivity.class.getName();

    private EditText editOTP;
    private Button btnSendOTPCode;
    private TextView tvSendOTPAgain;

    private FirebaseAuth mAuth;
    private String phoneNumber;
    private String mVerificationId;

    private LoginViewModel viewModel;
    private LoadingScreen loadingScreen;
    private Dialog dialog;


    private SharedPreferences sharedPreferences;
    private GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enter_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getDataIntent();
        setupComponent();
        setupViewModel();
        setupEvent();


    }


    /** GET DATA INTENT **/
    private void getDataIntent() {
        mVerificationId = getIntent().getStringExtra("mVerificationId");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

//        if(TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(mVerificationId) )
//        {
//            Toast.makeText(this, R.string.empty_phone_number_or_verificationId, Toast.LENGTH_SHORT).show();
//            finish();
//        }

    }

    /** SETTING UP COMPONENT **/
    private void setupComponent() {
        editOTP = findViewById(R.id.editOTP );
        btnSendOTPCode = findViewById(R.id.btnSendOTPCode);
        tvSendOTPAgain = findViewById(R.id.tvSendOTPAgain);

        mAuth = FirebaseAuth.getInstance();
        loadingScreen = new LoadingScreen(this);
        dialog = new Dialog(this);

        globalVariable = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);

    }

    /** SETTING UP VIEWMODEL **/
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

        /*Set up Dialog*/
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

            /*Case 1 - Login Successfully*/
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
            /*Case 2 - Login Failed*/
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


//        tvSendOTPAgain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickSendOTPAgain();
//            }
//        });
    }



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


//    private void onClickSendOTPAgain() {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // (optional) Activity for callback binding
//                        .setForceResendingToken(mForceResendingToken)
//                        // If no activity is passed, reCAPTCHA verification can not be used.
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                signInWithPhoneAuthCredential(phoneAuthCredential);
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Toast.makeText(VerificationActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String verificationId,
//                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                super.onCodeSent(verificationId, forceResendingToken);
//                                mVerificationId = verificationId;
//                                mForceResendingToken = forceResendingToken;
//                            }
//                        })          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }

//    private void goToHomePageActivity(String phoneNumber) {
//        Intent intent = new Intent(this, HomePageActivity.class);
//        intent.putExtra("phone_number", phoneNumber);
//        startActivity(intent);
//    }

}