package com.example.dacn_murkoff_care_android.BookingPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.dacn_murkoff_care_android.GuidePage.GuidePageActivity;
import com.example.dacn_murkoff_care_android.HomePage.HomePageActivity;
import com.example.dacn_murkoff_care_android.R;

/** NOTE:
 * This fragment shows message and 2 buttons HOW TO EXAM & GO BACK TO HOMEPAGE
 * When users create booking successfully
 * Road: BookingFragment1 -> BookingFragment2 -> BookingFragment3
 */

public class BookingFragment3 extends Fragment {

    private AppCompatButton btnHowToExam;
    private AppCompatButton btnHomepage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking3, container, false);

        setupComponent(view);
        setupEvent();

        return view;
    }


    /** SETUP COMPONENT **/
    private void setupComponent(View view) {
        btnHomepage = view.findViewById(R.id.btnHomepage);
        btnHowToExam = view.findViewById(R.id.btnHowToExam);
    }


    /** SETUP EVENT **/
    private void setupEvent() {
        btnHomepage.setOnClickListener(view->{
            Intent intent = new Intent(requireContext(), HomePageActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        btnHowToExam.setOnClickListener(view->{
            Intent intent = new Intent(requireContext(), GuidePageActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
