package com.example.dacn_murkoff_android.LoginPage;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
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

import com.example.dacn_murkoff_android.HomePage.HomePageActivity;
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

    private EditText editPhoneNumber;
    private Button btnGetAuthCode;
    private String phoneNumber;
    private AppCompatImageButton btnGoogleAuth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    public static final int REQ_ONE_TAP = 100;

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

        setTitleToolBar();
        initUI();
        setupEvent();

        mAuth = FirebaseAuth.getInstance();

        btnGoogleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleGoogleAuth();
            }
        });

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


    private void setTitleToolBar(){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Verify Phone Number");

        }
    }


    private void initUI() {

        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnGetAuthCode = findViewById(R.id.btnGetAuthCode);

        btnGoogleAuth = findViewById(R.id.google_btn);

        //login with phone number
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("vi");
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
                            .setActivity(this)                 // (optional) Activity for callback binding
                            // If no activity is passed, reCAPTCHA verification can not be used.
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        signInWithPhoneAuthCredential(phoneAuthCredential);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(LoginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(verificationId, forceResendingToken);
                                        goToEnterOTPActivity(phoneNumberFormatted, verificationId);
                                }
                            })          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }); /*End BUTTON GET CONFIRM CODE*/

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


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goToHomePageActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity.this,
                                        "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToHomePageActivity(String phoneNumber) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
    }

    private void goToEnterOTPActivity(String strPhoneNumber, String verificationId) {
        Intent intent = new Intent(this, EnterOTPActivity.class);
        intent.putExtra("phone_number", strPhoneNumber);
        intent.putExtra("verification_id", verificationId);
        startActivity(intent);
    }

}