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
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_android.R;

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


        /*Step 2 - Chuẩn bị header & tham số*/



        /*Step 3 - listen speciality Read All */



        /*Step 4 - listen doctor read all*/

    }



}