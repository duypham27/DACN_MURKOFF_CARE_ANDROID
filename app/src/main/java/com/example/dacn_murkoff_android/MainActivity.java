package com.example.dacn_murkoff_android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
        TextView tvUserInfo = findViewById(R.id.tvUserInfo);
        tvUserInfo.setText(strPhoneNumber);
    }

    private void setTitleToolBar(){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Main Activity");
        }
    }

}