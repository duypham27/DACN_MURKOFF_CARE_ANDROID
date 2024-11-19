package com.example.dacn_murkoff_care_android.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn_murkoff_care_android.Model.Queue;
import com.example.dacn_murkoff_care_android.R;

import java.util.List;

public class AppointmentQueueRecyclerView extends RecyclerView.Adapter<AppointmentQueueRecyclerView.ViewHolder>{

    private final Context context;
    private final List<Queue> list;
    private final int myPosition;

    public AppointmentQueueRecyclerView(Context context, List<Queue> list, int  myPosition)
    {
        this.context = context;
        this.list = list;
        this.myPosition = myPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_element_appointment_queue, parent, false);

        return new AppointmentQueueRecyclerView.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Queue appointment = list.get(position);


        int yourPosition = appointment.getPosition();// this is your position in queue
        String numericalOrder = String.valueOf(appointment.getNumericalOrder());// this is your numerical order when you go to hospital
        String patientName = appointment.getPatientName();

        holder.elementNumericalOrder.setText(numericalOrder);
        holder.elementPatientName.setText(patientName);


        /*position == 0 means he is the first one of the list*/
        if( position == 0 )
        {
            holder.elementStatus.setVisibility(View.VISIBLE);
            holder.elementNumericalOrder.setTextColor(context.getResources().getColor(R.color.colorGreen, null));
            holder.elementPatientName.setTextColor(context.getResources().getColor(R.color.colorGreen, null));
        }
        else
        {
            holder.elementStatus.setVisibility(View.GONE);
            holder.elementNumericalOrder.setTextColor(context.getResources().getColor(R.color.textColorBlack, null));
            holder.elementPatientName.setTextColor(context.getResources().getColor(R.color.textColorBlack, null));
        }

        /*if your position in queue lays on the list-> highlight your position with orange color & send notification*/
        if( yourPosition == myPosition)
        {
            /*to mau CAM cho ten nguoi dung*/
            holder.elementStatus.setVisibility(View.VISIBLE);
            holder.elementStatus.setText(R.string.you);

            holder.elementStatus.setTextColor(context.getResources().getColor(R.color.colorOrange, null));
            holder.elementPatientName.setTextColor(context.getResources().getColor(R.color.colorOrange, null));
            holder.elementNumericalOrder.setTextColor(context.getResources().getColor(R.color.colorOrange, null));

            /*tao noi dung cho Notification*/
            com.example.dacn_murkoff_care_android.Helper.MyNotification notification = new com.example.dacn_murkoff_care_android.Helper.MyNotification(context);
            String title = context.getString(R.string.app_name);
            String text = context.getString(R.string.it_is_your_turn);
            String bigText = patientName + " ơi! Hãy chuẩn bị, sắp tới lượt khám của bạn rồi!";
            notification.setup(title, text, bigText );
            notification.show();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView elementNumericalOrder;
        private final TextView elementPatientName;
        private final TextView elementStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            elementNumericalOrder = itemView.findViewById(R.id.elementNumericalOrder);
            elementPatientName = itemView.findViewById(R.id.elementPatientName);
            elementStatus = itemView.findViewById(R.id.elementStatus);
        }
    }
}
