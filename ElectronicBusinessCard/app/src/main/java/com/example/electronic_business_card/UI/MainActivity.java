package com.example.electronic_business_card.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.electronic_business_card.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final Intent mainIntent = getIntent();

        Button myCard = findViewById(R.id.myCard);
        myCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardDetail.class);
                intent.putExtra("activity", "내명함");
                intent.putExtra("token", mainIntent.getStringExtra("token"));
                startActivity(intent);
            }
        });

        Button cardList = findViewById(R.id.cardList);
        cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardListActivity.class);
                intent.putExtra("activity", "명함집");
                intent.putExtra("token", mainIntent.getStringExtra("token"));
                startActivity(intent);
            }
        });

        Button card_send_receive = findViewById(R.id.card_send_receive);
        card_send_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LinkQrNFCActvity.class);
                intent.putExtra("activity", "명함주고받기");
                intent.putExtra("token", mainIntent.getStringExtra("token"));
                startActivity(intent);
            }
        });
    }
}