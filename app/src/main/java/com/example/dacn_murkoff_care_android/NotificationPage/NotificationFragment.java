package com.example.dacn_murkoff_care_android.NotificationPage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.Model.Notification;
import com.example.dacn_murkoff_care_android.R;
import com.example.dacn_murkoff_care_android.RecyclerView.NotificationRecyclerView;

import java.util.List;
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        setupComponent(view);
        setupViewModel();
        setupEvent();

        return null;
    }


    /** THIS VIEW IS THE VIEW OF FRAGMENT **/
    private void setupComponent(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        GlobalVariable globalVariable = (GlobalVariable) requireActivity().getApplication();


        context = requireContext();
        activity = requireActivity();

        dialog = new Dialog(context);
        loadingScreen = new LoadingScreen(activity);

        txtMarkAllAsRead = view.findViewById(R.id.txtMarkAllAsRead);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        header = globalVariable.getHeaders();
    }


    private void setupViewModel() {
        /*Step 1 - declare*/
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        viewModel.instantiate();

        /*Step 2 - prepare header & parameters*/
        viewModel.readAll(header);

        /*Step 3 - listen for response*/
        viewModel.getReadAllResponse().observe((LifecycleOwner) context, response->{
            try
            {
                int result = response.getResult();
                /*result == 1 => luu thong tin nguoi dung va vao homepage*/
                if( result == 1)
                {


                    /*print notification*/
                    List<Notification> list = response.getData();
                    setupRecyclerView(list);
                }
                /*result == 0 => thong bao va thoat ung dung*/
                if( result == 0)
                {
                    dialog.announce();
                    dialog.show(R.string.attention, getString(R.string.check_your_internet_connection), R.drawable.ic_info);
                    dialog.btnOK.setOnClickListener(view->{
                        dialog.close();
                        activity.finish();
                    });
                }

            }
            catch(Exception ex)
            {
                /*Neu truy van lau qua ma khong nhan duoc phan hoi thi cung dong ung dung*/
                dialog.announce();
                dialog.show(R.string.attention, this.getString(R.string.check_your_internet_connection), R.drawable.ic_info);
                dialog.btnOK.setOnClickListener(view->{
                    dialog.close();
                    activity.finish();
                });
            }
        });

        /*Step 4 - get animation*/
        viewModel.getAnimation().observe((LifecycleOwner) context, aBoolean -> {
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


    private void setupEvent() {

    }


    private void setupRecyclerView(List<Notification> list)
    {
        NotificationRecyclerView notificationAdapter = new NotificationRecyclerView(context, list, this);
        recyclerView.setAdapter(notificationAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }


    @Override
    public void markAsRead(String notificationId) {

    }
}