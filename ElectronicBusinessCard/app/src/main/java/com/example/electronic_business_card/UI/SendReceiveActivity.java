package com.example.electronic_business_card.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;

public class SendReceiveActivity extends AppCompatActivity {
    Button send, receive;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive);

        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);

        Intent intent = getIntent();
        if(intent.getExtras().getString("activity").equals("qr")){
            send.setText("QR code 생성");
            receive.setText("QR code 스캔");
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LinkQrNFCActvity.this, CardDetail.class);
//                intent.putExtra("activity", "link");
//                startActivity(intent);
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LinkQrNFCActvity.this, CardDetail.class);
//                intent.putExtra("activity", "qr");
//                startActivity(intent);
//
//
            }
        });
    }
}
