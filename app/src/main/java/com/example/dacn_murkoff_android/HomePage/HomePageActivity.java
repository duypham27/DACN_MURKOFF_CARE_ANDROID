package com.example.dacn_murkoff_android.HomePage;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dacn_murkoff_android.R;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getDataIntent();
        setTitleToolBar();

    }

    private void getDataIntent() {
        String strPhoneNumber = getIntent().getStringExtra("phone_number");
        String username = getIntent().getStringExtra("display_name");


        TextView tvUserInfo = findViewById(R.id.tvUserInfo);

        if (strPhoneNumber != null && !strPhoneNumber.isEmpty()) {
            tvUserInfo.setText("Phone: " + strPhoneNumber);
        }
        else if (username != null && !username.isEmpty()) {
            tvUserInfo.setText("Welcome, " + username);
        }
        else {
            tvUserInfo.setText("No user info available");
        }
    }

    private void setTitleToolBar(){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Main Activity");
        }
    }
}