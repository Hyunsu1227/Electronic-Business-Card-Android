package com.example.electronic_business_card.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.DM.CardData;
import com.example.electronic_business_card.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class CardListActivity extends AppCompatActivity {
    ArrayList<CardData> total = new ArrayList<CardData>();
    EditText id, pw;
    String result = "";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent priorIntent = getIntent();
        Toast.makeText(CardListActivity.this , "token is" + priorIntent.getStringExtra("token"), Toast.LENGTH_SHORT).show();
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

    class SignInAsync extends AsyncTask<Void, Void, String> {
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Error: such id/pw not exists")) {
                Toast.makeText(CardListActivity.this, "해당 ID/PW가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(CardListActivity.this , "로그인 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CardListActivity.this, MainActivity.class);
                intent.putExtra("token", result);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String urlStr = "https://15.164.216.57:5001/friend_read?" + "id=" + id.getText().toString() + "&pw=" + pw.getText().toString();
            try {
                // Open the connection
                URL url = new URL(urlStr);

                // 모든 host 허용
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                Log.d("#####", String.valueOf(conn.getResponseCode()));
                if(conn.getResponseCode() == 400){
                    result = "Error: such id/pw not exists";
                    return result;
                }

                InputStream is = conn.getInputStream();

                // Get the stream
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                // Set the result
                result = builder.toString();

                Log.d("######",result);

                conn.disconnect();
            }
            catch (Exception e) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e.getMessage());
                e.printStackTrace();
            }

            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }
    }
}
