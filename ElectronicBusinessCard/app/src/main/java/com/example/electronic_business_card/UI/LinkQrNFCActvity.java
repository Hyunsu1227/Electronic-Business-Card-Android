package com.example.electronic_business_card.UI;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;

public class LinkQrNFCActvity extends AppCompatActivity {
    Button link, qr, nfc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_qrcode_nfc);

        link = findViewById(R.id.link);
        qr = findViewById(R.id.qr);
        nfc = findViewById(R.id.nfc);

//        link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LinkQrNFCActvity.this, CardDetail.class);
//                intent.putExtra("activity", "link");
//                startActivity(intent);
//            }
//        });

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LinkQrNFCActvity.this, SendReceiveActivity.class);
                intent.putExtra("activity", "qr");
                startActivity(intent);

                intent.getExtras().getString("activity").equals("내명함");
            }
        });

//        nfc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LinkQrNFCActvity.this, CardDetail.class);
//                intent.putExtra("activity", "nfc");
//                startActivity(intent);
//            }
//        });
    }
}
