package com.example.dacn_murkoff_android.LoginPage;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    ImageView btnGithub;
    private EditText editPhoneNumber;
    private Button btnGetAuthCode;
    private String phoneNumber;
    private AppCompatImageButton btnGoogleAuth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    public static final int REQ_ONE_TAP = 100;


    private Dialog dialog;
    private LoadingScreen loadingScreen;
    private LoginViewModel viewModel;

    private GlobalVariable globalVariable;
    private SharedPreferences sharedPreferences;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        setupComponent();
        setupEvent();
        setupViewModel();


        btnGoogleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleGoogleAuth();
            }
        });

        /*CONNECT TO GOOGLE CLOUD*/
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("621143880788-8mcllov3ka3hama6slolndal88se6e8s.apps.googleusercontent.com") // TODO
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();


    }

    private void setupComponent() {

        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnGetAuthCode = findViewById(R.id.btnGetAuthCode);
        btnGoogleAuth = findViewById(R.id.google_btn);
        btnGithub = findViewById(R.id.github_btn);

        //login with phone number
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("vi");

        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);

        globalVariable = (GlobalVariable)this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);

    }



    private void setupEvent()
    {
        /*BUTTON GET CONFIRM CODE*/
        btnGetAuthCode.setOnClickListener(view -> {
            phoneNumber = editPhoneNumber.getText().toString();

            /*Step 1 - verify input */
            if(TextUtils.isEmpty(phoneNumber) )
            {
                Toast.makeText(this, R.string.do_not_let_phone_number_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            if(phoneNumber.length() == 10)
            {
                Toast.makeText(this, R.string.only_enter_number_except_first_zero, Toast.LENGTH_SHORT).show();
                return;
            }
            String phoneNumberFormatted = "+84" + phoneNumber;

            /*Step 2 - setup phone auth options*/
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumberFormatted)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                            // This callback will be invoked in two situations:
                                            // 1 - Instant verification. In some cases the phone number can be instantly
                                            //     verified without needing to send or enter a verification code.
                                            // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                            //     detect the incoming verification SMS and perform verification without
                                            //     user action.
                                            //Log.d(TAG, "onVerificationCompleted:" + credential);

                                            System.out.println(TAG);
                                            System.out.println("onVerificationCompleted");
                                            System.out.println("signInWithPhoneAuthCredential has been called !");
                                            signInWithPhoneAuthCredential(credential);
                                        }



                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            // This callback is invoked in an invalid request for verification is made,
                                            // for instance if the the phone number format is not valid.

                                            Toast.makeText(LoginActivity.this, getString(R.string.verification_failed), Toast.LENGTH_SHORT).show();
                                            System.out.println(TAG);
                                            System.out.println("Error: "+e.getMessage());
                                            System.out.println(e);

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationId,
                                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                            // The SMS verification code has been sent to the provided phone number, we
                                            // now need to ask the user to enter the code and then construct a credential
                                            // by combining the code with a verification ID.

                                            System.out.println(TAG);
                                            System.out.println("onCodeSent");
                                            System.out.println("phone number: " + phoneNumber);
                                            System.out.println("verification Id: " + verificationId);

                                            Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                                            intent.putExtra("verificationId", verificationId);
                                            intent.putExtra("phoneNumber", phoneNumber);
                                            startActivity(intent);
                                        }


                                        @Override
                                        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                                            super.onCodeAutoRetrievalTimeOut(s);
                                            System.out.println(TAG);
                                            System.out.println("onCodeAutoRetrievalTimeOut");
                                            System.out.println(s);
                                        }
                                    }
                            )          // OnVerificationStateChangedCallbacks
                            .build();/*end Step 2*/

            /*Step 3 - setup phone auth provider*/
            PhoneAuthProvider.verifyPhoneNumber(options);
        });/*end BUTTON GET CONFIRM CODE*/



        /*BUTTON GITHUB LOGIN*/
        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, GithubAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }

    private void setupViewModel(){
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

        /** OPTION LOGIN WITH PHONE NUMBER **/
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


                /*Lay du lieu vao Global Variable*/
                globalVariable.setAccessToken( "JWT " + token );
                globalVariable.setAuthUser(user);

                /*luu accessToken vao Shared Reference*/
                sharedPreferences.edit().putString("accessToken", "JWT " + token.trim()).apply();

                /*hien thi thong bao la dang nhap thanh cong*/
                Toast.makeText(this, getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
            /*Case 2 - login failed*/
            else
            {
                dialog.show(getString(R.string.attention),
                        message,
                        R.drawable.ic_close);
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();

            }

        });/*end OPTION LOGIN WITH PHONE NUMBER*/



    }

    /*BUTTON GOOGLE LOGIN*/
    private void handleGoogleAuth() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
            @Override
            public void onSuccess(BeginSignInResult result) {
                try {
                    startIntentSenderForResult(
                            result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                            null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                }
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    String username = credential.getId();
                    String password = credential.getPassword();
                    //Toast.makeText(this, "Authentication done.\nUsername is " + username, Toast.LENGTH_SHORT).show();

                    // Chuyển sang HomePageActivity và truyền thông tin username
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    intent.putExtra("display_name", username);  // Truyền username
                    startActivity(intent);
                    finish();  // Đóng LoginActivity để không quay lại trang này

                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d(TAG, "Got ID token.");
                    } else if (password != null) {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d(TAG, "Got password.");
                    }
                } catch (ApiException e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /*END BUTTON GOOGLE LOGIN*/



    /*begin signInWithPhoneAuthCredential*/
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
                        System.out.println("phone: " + phone);
                        System.out.println("password: " + password);

                        viewModel.loginWithPhone(phone, password);
                    }
                    else
                    {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(LoginActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "error: " + task.getException() );
                        }
                    }
                });
    }
    //** end signInWithPhoneAuthCredential



//    private void goToHomePageActivity(String phoneNumber) {
//        Intent intent = new Intent(this, HomePageActivity.class);
//        intent.putExtra("phone_number", phoneNumber);
//        startActivity(intent);
//    }
//
//    private void goToEnterOTPActivity(String strPhoneNumber, String verificationId) {
//        Intent intent = new Intent(this, EnterOTPActivity.class);
//        intent.putExtra("phone_number", strPhoneNumber);
//        intent.putExtra("verification_id", verificationId);
//        startActivity(intent);
//    }

}