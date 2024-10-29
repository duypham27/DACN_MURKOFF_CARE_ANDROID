package com.example.dacn_murkoff_android.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_android.AlarmPage.AlarmPageActivity;
import com.example.dacn_murkoff_android.Configuration.Constant;
import com.example.dacn_murkoff_android.EmailPage.EmailPageActivity;
import com.example.dacn_murkoff_android.HomePage.HomePageActivity;
import com.example.dacn_murkoff_android.Model.Setting;
import com.example.dacn_murkoff_android.R;
import com.example.dacn_murkoff_android.SettingsPage.AppearanceActivity;
import com.example.dacn_murkoff_android.SettingsPage.AppointmentHistoryActivity;
import com.example.dacn_murkoff_android.SettingsPage.BookingHistoryActivity;
import com.example.dacn_murkoff_android.SettingsPage.InformationActivity;
import com.example.dacn_murkoff_android.WebPage.WebPageActivity;

import java.util.List;

public class SettingRecyclerView extends RecyclerView.Adapter<SettingRecyclerView.ViewHolder>{
    private final Context context;
    private final List<Setting> list;

    public SettingRecyclerView(Context context, List<Setting> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_element_setting, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Setting element = list.get(position);


        holder.icon.setImageResource( element.getIcon() );
        holder.name.setText( element.getName() );
        holder.layout.setOnClickListener(view -> {
            Intent intent;
            switch (element.getId()){
                case "aboutUs":
                    intent = new Intent(context, WebPageActivity.class);
                    intent.putExtra("url", Constant.VIDEO_PATH());
                    context.startActivity(intent);
                    break;
                case "appointmentHistory":
                    intent = new Intent(context, AppointmentHistoryActivity.class);
                    context.startActivity(intent);
                    break;
                case "bookingHistory":
                    intent = new Intent(context, BookingHistoryActivity.class);
                    context.startActivity(intent);
                    break;
                case "reminder":
                    intent = new Intent(context, AlarmPageActivity.class);
                    context.startActivity(intent);
                    break;
                case "information":
                    intent = new Intent(context, InformationActivity.class);
                    context.startActivity(intent);
                    break;
                case "appearance":
                    intent = new Intent(context, AppearanceActivity.class);
                    context.startActivity(intent);
                    break;
                case "emailUs":
                    intent = new Intent(context, EmailPageActivity.class);
                    context.startActivity(intent);
                    break;
                case "guide":
//                    intent = new Intent(context, GuidePageActivity.class);
//                    context.startActivity(intent);
//                    break;
                case "exit":
                    HomePageActivity.getInstance().exit();
                    break;
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final ImageView icon;
        private final LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.elementName);
            icon = itemView.findViewById(R.id.elementIcon);
            layout = itemView.findViewById(R.id.elementLayout);
        }
    }
}
