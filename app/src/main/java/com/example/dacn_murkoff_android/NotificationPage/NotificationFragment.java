package com.example.dacn_murkoff_android.NotificationPage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dacn_murkoff_android.Helper.Dialog;
import com.example.dacn_murkoff_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_android.R;
import com.example.dacn_murkoff_android.RecyclerView.NotificationRecyclerView;

import java.util.Map;

public class NotificationFragment extends Fragment implements NotificationRecyclerView.Callback {

    private final String TAG = "NotificationFragment";

    private RecyclerView recyclerView;

    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private Context context;
    private Activity activity;

    private TextView txtMarkAllAsRead;
    private NotificationViewModel viewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Map<String, String> header;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return null;
    }




    @Override
    public void markAsRead(String notificationId) {

    }
}