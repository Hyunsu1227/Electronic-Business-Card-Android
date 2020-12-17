package com.example.electronic_business_card.UI;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class LinkActivity extends AppCompatActivity {
    TextView linkText;
    EditText editReceiveLink;
    String result = "hello";
    String token;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        intent = getIntent();
        token = intent.getExtras().getString("token");

        linkText = findViewById(R.id.linkText);
        editReceiveLink = findViewById(R.id.editReceiveLink);

        Button createLink = findViewById(R.id.createLink);
        createLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckCardIDAsync checkCardIDAsync = new CheckCardIDAsync();
                checkCardIDAsync.execute();
            }
        });

        Button receiveLink = findViewById(R.id.receiveLink);
        receiveLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendCardIDAsync addFriendCardIDAsync = new AddFriendCardIDAsync();
                addFriendCardIDAsync.execute();
            }
        });


    }

    class CheckCardIDAsync extends AsyncTask<Void, Void, String> {
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("null")) {
                Toast.makeText(LinkActivity.this, "아직 자신의 명함을 생성하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LinkActivity.this , "CardID 읽어오기 성공", Toast.LENGTH_SHORT).show();
                linkText.setText(result);
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String urlStr = "https://15.164.216.57:5001/card_id?token=" + token;
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

                InputStream is = new BufferedInputStream(conn.getInputStream());

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
                is.close();
            }
            catch (Exception e) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e.getMessage());
                e.printStackTrace();
            }

            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }
    }

    class AddFriendCardIDAsync extends AsyncTask<Void, Void, String> {
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("error")) {
                Toast.makeText(LinkActivity.this, "올바르지 않은 link 거나 login이 만료되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LinkActivity.this , "명함 추가 성공", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String urlStr = "https://15.164.216.57:5001/friend_set?token=" + token
                    + "&friend_card=" + editReceiveLink.getText().toString();
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
                    result = "error";
                    return result;
                }
                InputStream is = new BufferedInputStream(conn.getInputStream());

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
                is.close();
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
