package com.users.findo;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.users.findo.activities.FirstScreen;

import java.net.URL;
import java.util.Objects;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    String title, messageBody;
    FirebaseAuth mAuth;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("Notification Received", "From: " + remoteMessage.getFrom());
        mAuth = FirebaseAuth.getInstance();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0 ) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            title = Objects.requireNonNull(remoteMessage.getData()).get("title");
            messageBody = remoteMessage.getData().toString();
            Log.e("msgBody", messageBody);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (Objects.equals(Objects.requireNonNull(remoteMessage.getData().get("title")), "logout")) {
                    mAuth.signOut();
                }
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null ) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
            messageBody = remoteMessage.getNotification().getBody();
            Log.e("msgBody", messageBody);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                assert messageBody != null;
                if (!Objects.equals(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), "logout")) {
                    sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), String.valueOf(remoteMessage.getNotification().getImageUrl()));
                }else{
                    mAuth.signOut();
                }
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void sendNotification(String title, String messageBody, String imageUrl) {
        Intent intent = new Intent(this, FirstScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "findoshop090";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.findo_logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                URL url = new URL(imageUrl);
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                if (bitmap != null) {
                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
                } else {
                    // Handle the case where the bitmap is null after decoding
                    // For example, show the notification without the image
                    showNotificationWithoutImage(notificationBuilder);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle URL parsing or connection exceptions here
                // Show the notification without the image in case of an exception
                showNotificationWithoutImage(notificationBuilder);
            }
        } else {
            // If imageUrl is null or empty, show the notification without the image
            showNotificationWithoutImage(notificationBuilder);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Create the default notification channel if it does not exist
        NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
        if (channel == null) {
            channel = new NotificationChannel(channelId,
                    "findoshop090",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = (int) System.currentTimeMillis();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void showNotificationWithoutImage(NotificationCompat.Builder notificationBuilder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        String channelId = "findoshop090"; // Assuming this is your channel ID

        // Build the notification without the image
        notificationBuilder.setSmallIcon(R.drawable.findo_logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // Create the default notification channel if it does not exist
        NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
        if (channel == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel(channelId,
                        "findoshop090",
                        NotificationManager.IMPORTANCE_HIGH);
            }
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = (int) System.currentTimeMillis();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

}