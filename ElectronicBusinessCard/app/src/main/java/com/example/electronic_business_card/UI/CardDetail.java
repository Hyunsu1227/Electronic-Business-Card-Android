package com.example.electronic_business_card.UI;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;

public class CardDetail extends AppCompatActivity {
    EditText name_edit, company_edit, position_edit, phoneNumber_edit, eMail_edit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail_acitivity);

        name_edit = findViewById(R.id.name_edit);
        company_edit = findViewById(R.id.company_edit);
        position_edit = findViewById(R.id.position_edit);
        phoneNumber_edit = findViewById(R.id.phoneNumber_edit);
        eMail_edit = findViewById(R.id.eMail_edit);

        disable_editText();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        if(intent.getExtras().getString("activity").equals("내명함")){
            getMenuInflater().inflate(R.menu.card_detail_menu, menu);
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.updateOrSave) {
            if(item.getTitle().equals("수정")) {
                item.setTitle("저장");
                enable_editText();
            }
            else if(item.getTitle().equals("저장")){
                item.setTitle("수정");
                disable_editText();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void disable_editText(){
        name_edit.setTag(name_edit.getKeyListener());
        name_edit.setKeyListener(null);
        company_edit.setTag(company_edit.getKeyListener());
        company_edit.setKeyListener(null);
        position_edit.setTag(position_edit.getKeyListener());
        position_edit.setKeyListener(null);
        phoneNumber_edit.setTag(phoneNumber_edit.getKeyListener());
        phoneNumber_edit.setKeyListener(null);
        eMail_edit.setTag(eMail_edit.getKeyListener());
        eMail_edit.setKeyListener(null);
    }
    public void enable_editText(){
        name_edit.setKeyListener((KeyListener) name_edit.getTag());
        company_edit.setKeyListener((KeyListener) company_edit.getTag());
        position_edit.setKeyListener((KeyListener) position_edit.getTag());
        phoneNumber_edit.setKeyListener((KeyListener) phoneNumber_edit.getTag());
        eMail_edit.setKeyListener((KeyListener) eMail_edit.getTag());
    }

}
