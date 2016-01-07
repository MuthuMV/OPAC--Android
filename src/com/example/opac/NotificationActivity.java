package com.example.opac;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class NotificationActivity extends Activity {
     
    TextView mButtonLabel;
    
    private static final int MY_NOTIFICATION_ID=1;
    private NotificationManager notificationManager;
    private Notification myNotification;

    private final String myBlog = "http://android-er.blogspot.com/";

   
    // Counter of time since app started ,a background task
    private long mStartTime = 0L;
    private TextView mTimeLabel,mTimerLabel;
   
    // Handler to handle the message to the timer task
    private Handler mHandler = new Handler();
          
    static final int UPDATE_INTERVAL = 1000;
   
    String timerStop1;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
                                         
        mTimeLabel = (TextView) findViewById(R.id.countDownTv);
        mButtonLabel = (TextView) findViewById(R.id.countDown);              
       
        mButtonLabel = (TextView) findViewById(R.id.countDown);
       
       
        Button countDownButton = (Button) findViewById(R.id.countDown);      
        countDownButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                                   
                  CountDownTimer timer1 = new CountDownTimer(10000,1000){

                        @Override
                        public void onFinish() {
                             
                              mTimeLabel.setText("Done!");
                              notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
               			   myNotification = new Notification(R.drawable.notification, "Notification!", System.currentTimeMillis());
               			   Context context = getApplicationContext();
               			   String notificationTitle = "Resrvation Notification!";
               			   String notificationText = "The time limit for book reservation is reached.Please contact the library to ensure withdrawal of the book";
               			   Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
               			   PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
               			   myNotification.defaults |= Notification.DEFAULT_SOUND;
               			   myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
               			   myNotification.setLatestEventInfo(context, notificationTitle, notificationText, pendingIntent);
               			   notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
                        }

                        @Override
                        public void onTick(long millisUntilFinished) {                               
                             
                              int seconds = (int) (millisUntilFinished / 1000);
                              int minutes = seconds / 60;
                              seconds = seconds % 60;
                             
                              mTimeLabel.setText("" + minutes + ":" + String.format("%02d", seconds));
                             
                        }          
                  }.start();
                 
            }
        });    
    } // OnCreate
   
       
    private Runnable mUpdateTimeTask = new Runnable(){

            public void run() {
                 
                  final long start = mStartTime;
                  long millis = SystemClock.uptimeMillis()- start;
                             
                  int seconds = (int) (millis / 1000);
                  int minutes = seconds / 60;
                  seconds = seconds % 60;
                 
                  mTimerLabel.setText("" + minutes + ":"
                                                  + String.format("%02d", seconds));                             
                 
                  timerStop1 = minutes + ":"
                                + String.format("%02d", seconds);
                                               
                  mHandler.postDelayed(this, 200);               
                 
            }    
    };
   
} // class
