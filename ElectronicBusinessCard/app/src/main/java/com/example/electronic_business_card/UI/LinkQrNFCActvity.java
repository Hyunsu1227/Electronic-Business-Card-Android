package com.example.electronic_business_card.UI;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;

public class LinkQrNFCActvity extends AppCompatActivity {
    Button link, qr, nfc;
    String activity;
    String token;
    Intent intent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_qrcode_nfc);

        link = findViewById(R.id.link);
        qr = findViewById(R.id.qr);
        nfc = findViewById(R.id.nfc);

        intent = getIntent();
        activity = intent.getExtras().getString("activity");
        token = intent.getExtras().getString("token");

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LinkQrNFCActvity.this, LinkActivity.class);
                intent.putExtra("activity", "link");
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LinkQrNFCActvity.this, SendReceiveActivity.class);
                intent.putExtra("activity", "qr");
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LinkQrNFCActvity.this, NFCActivity.class);
                intent.putExtra("activity", "nfc");
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
    }
}
