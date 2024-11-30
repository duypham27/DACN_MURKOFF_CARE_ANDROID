package com.example.dacn_murkoff_care_android.AppointmentPage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.LoadingScreen;
import com.example.dacn_murkoff_care_android.Helper.Tooltip;
import com.example.dacn_murkoff_care_android.Model.Appointment;
import com.example.dacn_murkoff_care_android.R;
import com.example.dacn_murkoff_care_android.RecyclerView.AppointmentRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentPageFragment extends Fragment {
    private final String TAG = "Appointment_Page_Fragment";

    private RecyclerView appointmentRecyclerView;

    private Context context;
    private Activity activity;
    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private Map<String, String> header;
    private LinearLayout lytNoAppointment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_page, container, false);

        setupComponent(view);
        setupViewModel();

        return view;
    }



    /** SETUP COMPONENT **/
    private void setupComponent(View view) {
        context = requireContext();
        activity = requireActivity();
        GlobalVariable globalVariable = (GlobalVariable) activity.getApplication();
        header = globalVariable.getHeaders();

        loadingScreen = new LoadingScreen(activity);
        dialog = new Dialog(context);

        lytNoAppointment = view.findViewById(R.id.lytNoAppointment);
        appointmentRecyclerView = view.findViewById(R.id.recyclerView);
    }


    /** SETUP VIEW MODEL **/
    private void setupViewModel() {
        /*DECLARE*/
        AppointmentPageViewModel viewModel = new ViewModelProvider(this).get(AppointmentPageViewModel.class);
        viewModel.instantiate();


        /*SEND REQUEST*/
        Map<String, String> parameter = new HashMap<>();
        String today = Tooltip.getToday();
        parameter.put("date", today);
        viewModel.readAll(header, parameter);


        /*LISTEN FOR RESPONSE*/
        viewModel.getReadAllResponse().observe((LifecycleOwner) context, response->{
            try
            {
                int result = response.getResult();
                /*result == 1 => luu thong tin nguoi dung va vao homepage*/
                if( result == 1)
                {
                    List<Appointment> appointments = response.getData();
                    setupRecyclerView(appointments);
                }
                /*result == 0 => thong bao va thoat ung dung*/
                if( result == 0)
                {
                    System.out.println(TAG + "- result: " + result);
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
                System.out.println(TAG + "- exception: " + ex.getMessage());
            }
        });/*end LISTEN FOR RESPONSE*/

        /*ANIMATION*/
        viewModel.getAnimation().observe((LifecycleOwner) context, aBoolean -> {
            if( aBoolean )
            {
                loadingScreen.start();
            }
            else
            {
                loadingScreen.stop();
            }
        });/*end ANIMATION*/
    }

    /** SETUP RECYCLER VIEW **/
    private void setupRecyclerView(List<Appointment> list)
    {
        if( list.size() == 0 )
        {
            lytNoAppointment.setVisibility(View.VISIBLE);
            appointmentRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            lytNoAppointment.setVisibility(View.GONE);
            appointmentRecyclerView.setVisibility(View.VISIBLE);

            AppointmentRecyclerView appointmentAdapter = new AppointmentRecyclerView(context, list);
            appointmentRecyclerView.setAdapter(appointmentAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            appointmentRecyclerView.setLayoutManager(manager);
        }
    }
}