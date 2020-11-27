package com.example.electronic_business_card.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.electronic_business_card.DM.CardData;
import com.example.electronic_business_card.R;

public class ListItemView extends LinearLayout{
    TextView name,position,company,phoneNumber,eMail; // 우측 text 값 3개

    public ListItemView(Context context, CardData data) {
        super(context);
        //R.layout.line에 정의된 한줄의 모양을 사용
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listitem, this, true);

        setListItem(data);
    }

    public void setListItem(CardData data) {
        name = findViewById(R.id.name);
        position = findViewById(R.id.position);
        company = findViewById(R.id.company);
        phoneNumber = findViewById(R.id.phoneNumber);
        eMail = findViewById(R.id.eMail);

        name.setText(data.name);
        position.setText(data.position);
        company.setText(data.company);
        phoneNumber.setText(data.phoneNumber);
        eMail.setText(data.eMail);
    }
}
