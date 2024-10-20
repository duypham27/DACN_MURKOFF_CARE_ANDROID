package com.example.dacn_murkoff_android.Helper;



/*Lớp Dialog dùng để hiển thị phần thông báo trên màn hình*/

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dacn_murkoff_android.R;


public class Dialog {
    private View viewAlert;
    private AlertDialog alert;
    private Context context;
    private TextView msgText, alertTitle;
    private ImageView iconAlert;
    public Button btnOK;
    public Button btnCancel;

    /* CONSTRUCTOR nhận context */
    public Dialog(Context context) {
        this.context = context;
    }

    /* CONSTRUCTOR nhận type*/
    public Dialog(Context context, int type) {
        this.context = context;
        if(type == 1){
            this.announce();
        }else{
            this.confirm();
        }
    }

    /* announe hiển thị thông báo 1 xác nhận */
    private void announce() {
        viewAlert = View.inflate(context, R.layout.dialog_annouce, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewAlert);

        alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.setCancelable(false);

        setControl();
    }

    /* confirm hiển thị thông báo xác nhận 2 lựa chọn yes/no */
    private void confirm() {
        viewAlert = View.inflate(context, R.layout.dialog_confirm, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewAlert);

        alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.setCancelable(false);

        setControl();
    }

    private void setControl() {
        msgText = viewAlert.findViewById(R.id.msgText);
        alertTitle = viewAlert.findViewById(R.id.alertTitle);
        iconAlert = viewAlert.findViewById(R.id.iconAlert);
        btnOK = viewAlert.findViewById(R.id.btnOK);
        btnCancel = viewAlert.findViewById(R.id.btnCancel);

    }


    /*tilte - thông báo , msg - nội dung thông báo, icon - icon lựa chọn của dialog*/
    @SuppressLint("NonConstantResourceId")
    public void show(String title, String msg, Integer icon){
        if (icon == R.drawable.ic_close) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_danger);
        } else if (icon == R.drawable.ic_info) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_info);
        } else if (icon == R.drawable.ic_check) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_success);
        }
        iconAlert.setImageResource(icon);
        msgText.setText(msg);
        alertTitle.setText(title);
        alert.show();
    }

    /*tilte - thông báo nhưng dụng R.id.string, msg - nội dung thông báo, icon - icon lựa chọn của dialog*/
    @SuppressLint("NonConstantResourceId")
    public void show(Integer resid, String msg, Integer icon){
        String title = context.getResources().getString(resid);
        if (icon == R.drawable.ic_close) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_danger);
        } else if (icon == R.drawable.ic_info) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_info);
        } else if (icon == R.drawable.ic_check) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_success);
        }
        iconAlert.setImageResource(icon);
        msgText.setText(msg);
        alertTitle.setText(title);
        alert.show();
    }

    /* Đóng Dialog lại hoàn toàn */
    public void close(){
        alert.dismiss();
    }
}
