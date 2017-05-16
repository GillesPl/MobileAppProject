package gilles.firemessage.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import gilles.firemessage.Constants;
import gilles.firemessage.R;
import gilles.firemessage.Views.MainActivity;

/**
 * Created by Gilles on 16-May-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String EMAIL = "email";
    private static final String UID = "uid";
    private static final String TEXT = "text";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0){
            Map<String, String> data = remoteMessage.getData();

            String email = data.get(EMAIL);
            String uid = data.get(UID);
            String text = data.get(TEXT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.message)).setSmallIcon(R.mipmap.message);


            mBuilder.setContentTitle(email);
            mBuilder.setContentText(text);

            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}
