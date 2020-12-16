package com.example.electronic_business_card.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.DM.CardData;
import com.example.electronic_business_card.R;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {
    ArrayList<CardData> total = new ArrayList<CardData>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        //(2) ListView에 보여줄 데이터
        CardData m1 = new CardData();

        m1.name = "박세찬";
        m1.position = "개발자";
        m1.company = "모앱";
        m1.phoneNumber = "010-2222-3333";
        m1.eMail = "박세찬@naver.com";

        CardData m2 = new CardData();

        m2.name = "이정열";
        m2.position = "개발자";
        m2.company = "모앱";
        m2.phoneNumber = "010-4444-5555";
        m2.eMail = "이정열@gmail.com";

        total.add(m1);
        total.add(m2);

        //(3) ListView에 Data를 중계해줄 Adapter
        ListAdapter myAdapter = new ListAdapter(this, total);
        //(4) ListView에 Adapter 연동
        listView.setAdapter(myAdapter);

        setContentView(listView);
    }
}
