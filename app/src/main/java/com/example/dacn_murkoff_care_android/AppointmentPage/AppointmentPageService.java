package com.example.dacn_murkoff_care_android.AppointmentPage;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dacn_murkoff_care_android.Configuration.HTTPRequest;
import com.example.dacn_murkoff_care_android.Configuration.HTTPService;
import com.example.dacn_murkoff_care_android.Container.AppointmentQueue;
import com.example.dacn_murkoff_care_android.Container.NotificationCreate;
import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
import com.example.dacn_murkoff_care_android.Helper.Tooltip;
import com.example.dacn_murkoff_care_android.Model.Queue;
import com.example.dacn_murkoff_care_android.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
/** APS NOTE:
 * This intent service run every 30 seconds to update appointment queue for
 * The patient. If next patient or next third patient is user. The device goes off
 * With a notification and sound to remind the user
 * Every 45 seconds, the service will send a GET request to check current patient.
 */
public class AppointmentPageService extends IntentService {

    public static final String TAG = "Appointment_Page_Service";
    private PowerManager.WakeLock wakeLock;
    private GlobalVariable globalVariable;
    private MediaPlayer mediaPlayer;


    private String recordId;// record id is the id of appointment that user is waiting for examining
    private String recordType;// record type is the key to create message for BOOKING or APPOINTMENT
    private String position;// it is the position in the queue that if current Position = position=> it's the user's turn
    private String doctorId;// is the id of doctor that the user are waiting for.
    private String doctorName;// is the name of doctor
    private boolean isNotify = false;

    /** NOTE:
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * Name Used to name the worker thread, important only for debugging.
     **/
    public AppointmentPageService(String name) {
        super("Notification_Service");
        setIntentRedelivery(true);
    }


    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();

        globalVariable = (GlobalVariable) this.getApplication();
        Log.d(TAG, "onCreate");

        /*Phone screen can turn off but CPU keeps running to complete the task*/
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Appointment-page Service - WakeLock");
        wakeLock.acquire(2*60*1000L /*2 minutes*/);//turn off wake lock after 2 minutes.
        Log.d(TAG, "onCreate - wakeLock acquire");

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            /** NEED FIX **/
            startForeground(1, notification);
        }

    }

    /** NOTE:
     * Intent is the intent sent from Appointment Recycler View
     * This function run first whenever the service is called.
     */
    boolean running = canServiceRun();
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        assert intent != null;
        doctorId = intent.getStringExtra("doctorId");
        doctorName = intent.getStringExtra("doctorName");
        position = intent.getStringExtra("position");
        recordId = intent.getStringExtra("recordId");
        recordType = intent.getStringExtra("recordType");

        int interval = 1000*45;/*Run this function every 45 seconds*/
        System.out.println("First create - is running: " + running);

        while(running)
        {
            /*DEBUG*/
            /*print time now on console & notification*/
//            TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
//            Calendar calendar = Calendar.getInstance(timeZone);
//            int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
//            int nowMinute = calendar.get(Calendar.MINUTE);

            /*show message*/
//            String nowValue = "It's " + nowHour + ":" + nowMinute;
//            System.out.println("===============================");
//            System.out.println(nowValue);
//            System.out.println("is Notify = " + isNotify);

            /*end DEBUG*/

            /*GET request every 30 second*/
            running = canServiceRun();/*Run function canServiceRun() to know it can be continue or not*/
            System.out.println("is running: " + running);
            if( !running )
            {
                //stopSelf();
                System.out.println("intent service is killed!");
                return;
            }
            getAppointmentQueue();
            SystemClock.sleep(interval);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        wakeLock.release();
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        Log.d(TAG, "onDestroy - wakeLock release");
    }


    public void getAppointmentQueue()
    {
        /*Step 1 - prepare header & parameters*/
        Map<String, String> headers = globalVariable.getHeaders();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("doctor_id", doctorId);
        parameters.put("date", Tooltip.getToday());
        parameters.put("order[column]", "position" );
        parameters.put("order[dir]", "asc");
        parameters.put("length", "3");
        parameters.put("status", "processing");


        /*Step 2 - prepare API*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 3*/
        Call<AppointmentQueue> container = api.appointmentQueue(headers, parameters);

        /*Step 4*/
        container.enqueue(new Callback<AppointmentQueue>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentQueue> call, @NonNull Response<AppointmentQueue> response) {
                if(response.isSuccessful())
                {
                    AppointmentQueue content = response.body();
                    assert content != null;
                    List<Queue> list = content.getData();
                    checkNextPatientAndShowNotification(list);
                }
                if(response.errorBody() != null)
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    }
                    catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentQueue> call, @NonNull Throwable t) {
                System.out.println("Appointment-page Service - doSomething - error: " + t.getMessage());
            }
        });
    }


    /** NOTE:
     This function returns boolean flag that this service can be run or not?
     YES, isNotify == false || 7 <= hour <= 18
     NO, if the device have gone off notification and sound || current time <= 7 || current time > 18
     **/
    public boolean canServiceRun()
    {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Calendar now = Calendar.getInstance(timeZone);
        int hour = now.get(Calendar.HOUR_OF_DAY);

        if(isNotify)
        {
            System.out.println("can not run by isNotify");
            return false;
        }
        if(  hour < 7 || hour > 18)
        {
            System.out.println("can not run by working hour");
            return false;
        }
        return true;
    }


    /** NOTE:
     * Text is the short text
     * BigText is the full text
     **/
    public void showMessageInDevice(String text, String bigText)
    {
        /*tao noi dung cho Notification*/
        com.example.dacn_murkoff_care_android.Helper.MyNotification notification = new com.example.dacn_murkoff_care_android.Helper.MyNotification(this);
        String title = this.getString(R.string.app_name);
        notification.setup(title, text, bigText );
        notification.show();


        /*phat am thanh*/
        mediaPlayer= MediaPlayer.create(this, R.raw.alarm_sound_3);
        int duration = 1000*10;// media player run for 10 second
        int interval = 1000;// count down every 1 second
        CountDownTimer timer = new CountDownTimer(duration, interval) {
            public void onTick(long millisUntilFinished) {
                mediaPlayer.start();
            }

            public void onFinish() {
                mediaPlayer.stop();
            }
        };
        timer.start();
    }



    /** NOTE:
     * This function loop through the list of next 3 patients
     * If one patient of the list is the user's position, the device will show notification and sound to remind user.
     **/

    private void checkNextPatientAndShowNotification(List<Queue> list)
    {
        String text = this.getString(R.string.it_is_your_turn);
        String bigText = globalVariable.getAuthUser().getName() +" ơi! " +
                "Hãy chuẩn bị, sắp đến lượt khám của bạn với " + doctorName + " rồi";


        for(Queue element: list)
        {
            int positionInQueue = element.getPosition();
            if( positionInQueue == Integer.parseInt(position) )
            {
                isNotify = true;
                showMessageInDevice(text, bigText);
                createNotificationInServer(bigText);
                super.onDestroy();
                return;
            }
        }
    }


    /** NOTE:
     * Create notification in server
     * This function creates a notification in server to help users to watch notification again.
     **/
    private void createNotificationInServer(String message)
    {
        /*Step 1 - prepare header & parameters*/
        Map<String, String> headers = globalVariable.getHeaders();


        /*Step 2 - prepare API*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 3 - send request to server*/
        Call<NotificationCreate> container = api.notificationCreate(headers, message, recordId, recordType);

        /*Step 4 - listen for response*/
        container.enqueue(new Callback<NotificationCreate>() {
            @Override
            public void onResponse(@NonNull Call<NotificationCreate> call, @NonNull Response<NotificationCreate> response) {
                if(response.isSuccessful())
                {
                    NotificationCreate content = response.body();
                    assert content != null;
                    System.out.println(TAG);
                    System.out.println(content.getMsg());
                }
                if(response.errorBody() != null)
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    }
                    catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationCreate> call, @NonNull Throwable t) {
                System.out.println("Appointment Page Service - Create notification in server - error: " + t.getMessage());
            }
        });/*end Step 4*/
    }


}