package com.example.dacn_murkoff_care_android.EmailPage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.dacn_murkoff_care_android.Helper.Dialog;
import com.example.dacn_murkoff_care_android.R;

public class EmailFragment1 extends Fragment {

    private EditText txtTitle;
    private EditText txtDescription;
    private EditText txtContent;

    private AppCompatButton btnNext;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email1, container, false);

        setupComponent(view);
        setupEvent();

        return view;
    }


    /** SETUP COMPONENT **/
    private void setupComponent(View view) {
        txtTitle = view.findViewById(R.id.txtTitle);
        txtContent = view.findViewById(R.id.txtContent);
        txtDescription = view.findViewById(R.id.txtDescription);
        btnNext = view.findViewById(R.id.btnNext);
        dialog = new Dialog(requireContext());
    }


    /** SETUP EVENT **/
    private void setupEvent() {
        btnNext.setOnClickListener(view->{
            String title = txtTitle.getText().toString();
            String content = txtContent.getText().toString();
            String description = txtDescription.getText().toString();


            dialog.announce();
            dialog.btnOK.setOnClickListener(view1->dialog.close());
            if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(description))
            {
                dialog.show(R.string.attention, requireContext().getString(R.string.you_are_missing_mandatory_field), R.drawable.ic_close);
                return;
            }

            Fragment fragment = new EmailFragment2();

            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("description", description);
            bundle.putString("content", content);
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment, "EmailFragment2")
                    .addToBackStack(null)
                    .commit();
        });
    }
}
