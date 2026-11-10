package com.example.sms_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText numberEditText;
    private EditText messageEditText;
    private Button aboutBtn;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the UI elements
        numberEditText = findViewById(R.id.numberEditText);
        messageEditText = findViewById(R.id.messageEditText);
        aboutBtn = findViewById(R.id.aboutbtn);

        // Set click listener for the move button to navigate to About activity
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });
    }

    // Method called when the "Send" button is clicked
    public void sendSMS(View view) {
        // Read the phone number and message from the EditText fields
        String number = numberEditText.getText().toString();
        String message = messageEditText.getText().toString();

        // Check if the SEND_SMS permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        } else {
            // If permission is already granted, send the SMS
                sendSMSMessage(number, message);
        }
    }

    // Method to send an SMS message
    private void sendSMSMessage(String number, String message) {
        try {
            // Create an instance of SmsManager to send the SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(this, "SMS sent to " + number, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Handle any exceptions that occur while sending the SMS
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Callback method that handles the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, send the SMS
                String number = numberEditText.getText().toString();
                String message = messageEditText.getText().toString();
                sendSMSMessage(number, message);
            } else {
                // If permission is denied, show a toast message
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
