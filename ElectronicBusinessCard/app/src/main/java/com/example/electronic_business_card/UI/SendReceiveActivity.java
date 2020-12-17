package com.example.electronic_business_card.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;

public class SendReceiveActivity extends AppCompatActivity {
    Button send, receive;
    String activity;
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive);

        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);

        intent = getIntent();
        activity = intent.getExtras().getString("activity");
        switch (activity) {
            case "qr":
                send.setText("QR code 생성");
                receive.setText("QR code 스캔");
                break;
        }

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                switch (activity){
                    case "qr":
                        Intent intent = new Intent(SendReceiveActivity.this, CreateQR.class);
                        startActivity(intent);
                        break;
                }

            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (activity){
                    case "qr":
                        Intent intent = new Intent(SendReceiveActivity.this, ScanQR.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }
}
