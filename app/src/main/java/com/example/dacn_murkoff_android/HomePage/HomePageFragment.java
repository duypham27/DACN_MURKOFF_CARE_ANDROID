package com.example.dacn_murkoff_android.HomePage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_android.Model.Doctor;
import com.example.dacn_murkoff_android.Model.Speciality;
import com.example.dacn_murkoff_android.R;
import com.example.dacn_murkoff_android.RecyclerView.DoctorRecyclerView;
import com.example.dacn_murkoff_android.RecyclerView.SpecialityRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageFragment extends Fragment {

    private final String TAG = "Home Page Fragment";

    private GlobalVariable globalVariable;

    private RecyclerView recyclerViewSpeciality;
    private RecyclerView recyclerViewDoctor;
    private RecyclerView recyclerViewHandbook;
    private RecyclerView recyclerViewRecommendedPages;


    private EditText searchBar;
    private TextView txtReadMoreSpeciality;
    private TextView txtReadMoreDoctor;

    private Context context;
    private RecyclerView recyclerViewButton;

    private TextView txtDate;
    private TextView txtWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        setupComponent(view);


        return view;
    }

    private void setupComponent(View view)
    {
        context = requireContext();
        globalVariable = (GlobalVariable) requireActivity().getApplication();

        recyclerViewSpeciality = view.findViewById(R.id.recyclerViewSpeciality);
        recyclerViewDoctor = view.findViewById(R.id.recyclerViewDoctor);
        recyclerViewButton = view.findViewById(R.id.recyclerViewButton);
        recyclerViewHandbook = view.findViewById(R.id.recyclerViewHandbook);
        recyclerViewRecommendedPages = view.findViewById(R.id.recyclerViewRecommendedPages);

        searchBar = view.findViewById(R.id.searchBar);
        txtReadMoreSpeciality = view.findViewById(R.id.txtReadMoreSpeciality);
        txtReadMoreDoctor = view.findViewById(R.id.txtReadMoreDoctor);

        txtWeather = view.findViewById(R.id.txtWeather);
        txtDate = view.findViewById(R.id.txtDate);

    }


    private void setupViewModel()
    {
        /*Step 1 - Khởi tạo ViewModel*/
        HomePageViewModel viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        viewModel.instantiate();

        /*Step 2 - Chuẩn bị header & tham số*/
        Map<String, String> header = globalVariable.getHeaders();
        header.put("type", "patient");


        /*Step 3 - Listen Speciality Read All */
        Map<String, String> paramsSpeciality = new HashMap<>();
        viewModel.specialityReadAll(header, paramsSpeciality);

        viewModel.getSpecialityReadAllResponse().observe(getViewLifecycleOwner(), response->{
            int result = response.getResult();
            if( result == 1)
            {
                List<Speciality> list = response.getData();
                setupRecyclerViewSpeciality(list);
            }
        });


        /*Step 4 - Listen Doctor read all*/
        Map<String, String> paramsDoctor = new HashMap<>();
        viewModel.doctorReadAll(header, paramsDoctor);

//        for (Map.Entry<String,String> entry : paramsDoctor.entrySet())
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());
//
//        for (Map.Entry<String,String> entry : header.entrySet())
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());

        viewModel.getDoctorReadAllResponse().observe(getViewLifecycleOwner(), response->{
            int result = response.getResult();
            if( result == 1)
            {
                List<Doctor> list = response.getData();
                setupRecyclerViewDoctor(list);
            }
        });
    }

    /* Setup Recycler View Speciality */
    private void setupRecyclerViewSpeciality(List<Speciality> list)
    {
        SpecialityRecyclerView specialityAdapter = new SpecialityRecyclerView(requireActivity(), list, R.layout.recycler_view_element_speciality);
        recyclerViewSpeciality.setAdapter(specialityAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSpeciality.setLayoutManager(manager);
    }


    /* Setup Recycler View Doctor */
    private void setupRecyclerViewDoctor(List<Doctor> list)
    {
        DoctorRecyclerView doctorAdapter = new DoctorRecyclerView(requireActivity(), list);
        recyclerViewDoctor.setAdapter(doctorAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDoctor.setLayoutManager(manager);
    }


}