package com.example.dacn_murkoff_care_android.Helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dacn_murkoff_care_android.Configuration.Constant;
import com.example.dacn_murkoff_care_android.HomePage.HomePageActivity;
import com.example.dacn_murkoff_care_android.R;


/** NOTE:
 * This class is used to create notification for this application
 **/
public class MyNotification extends android.app.Notification {

    int notificationID = 1896;
    private final Context context;
    private final String CHANNEL_ID = Constant.APP_NAME();
    private final NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;

    public MyNotification(Context context) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);
    }


    /** NOTE:
     * This function always runs whenever the application opens
     * Notice 1:  that the NotificationCompat.Builder constructor requires that you provide a channel ID.
     * This is required for compatibility with Android 8.0 (API level 26) and higher,
     * But is ignored by older versions
     * Notice 2: Before you can deliver the notification on Android 8.0 and higher,
     * You must register your app's notification channel with the system by passing
     * An instance of NotificationChannel to createNotificationChannel().
     * So the following code is blocked by a condition on the SDK_INT version:
     */
    public void createChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.app_description);


            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);


            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /** SETUP CONTENT FOR NOTIFICATION NOTE:
     * Title   is the title of notification
     * Text    is the short content of notification
     * BigText is the full content of notification
     **/
    public void setup(String title, String text, String bigText) {
        /*Mo app khi nhan vao notification*/
        Intent intent = new Intent(context, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        this.builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.murkoff_logo)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)// Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);//Notice this code calls setAutoCancel(), which automatically removes the notification when the user taps it.
    }

    public void show()
    {
        NotificationCompat.Builder builder = this.builder;
        /** NEED TURN ON**/
        //notificationManager.notify(notificationID, builder.build());
    }
}