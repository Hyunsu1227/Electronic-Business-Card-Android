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

import org.json.JSONArray;
import org.json.JSONObject;

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

    String name;
    String phone;
    String description;
    String address;
    String face_photo;
    String card_id;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent priorIntent = getIntent();
        //Toast.makeText(CardListActivity.this , "token is" + priorIntent.getStringExtra("token"), Toast.LENGTH_SHORT).show();
        ListView listView = new ListView(this);
        //(2) ListView에 보여줄 데이터
        /*
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
        */
        readCard readCard = new readCard();
        readCard.execute();


        //(3) ListView에 Data를 중계해줄 Adapter
        ListAdapter myAdapter = new ListAdapter(this, total);
        //(4) ListView에 Adapter 연동
        listView.setAdapter(myAdapter);

        setContentView(listView);
    }
    //친구 카드 아이디 읽고, 배열 따라서 다시 카드 정보 읽고 그 카드 정보 total에 붙이고 post에서 setContnent View
    class readCard extends AsyncTask<Void, Void, String> {
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Error: such id/pw not exists")) {
                Log.d("[psc] : ", "read card failure");
            } else {
                Log.d("[psc] : ", result);
                ListView listView = new ListView(CardListActivity.this);
                ListAdapter myAdapter = new ListAdapter(CardListActivity.this, total);
                //(4) ListView에 Adapter 연동
                listView.setAdapter(myAdapter);

                setContentView(listView);

            }
            Toast.makeText(CardListActivity.this, result, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(Void... strings) {
            String getToken = getIntent().getStringExtra("token");
//            Log.d("TOKEN", getToken);

            String urlStr0 = "https://15.164.216.57:5001/friend_read?token="+getToken;
            try {
                // Open the connection
                URL url = new URL(urlStr0);

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

                if (conn.getResponseCode() == 400) {
                    result = "Error: such token not exists:"+ getToken;
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
                //JSONTokener tokener = new JSONTokener(result);
                //JSONObject json = new JSONObject(tokener);
                //Toast.makeText(CardDetail.this, "card id : " + json.getInt("card_id"), Toast.LENGTH_SHORT).show();
                Log.d("[psc0]", result);
                JSONArray arr = new JSONArray(result);
                for(int i=0; i<arr.length();i++){
                    JSONObject obj = arr.getJSONObject(0);
                    int int_card_id = obj.getInt("friends_cards");
                    String tmp_card_id = Integer.toString(int_card_id);

                    String urlStr = "https://15.164.216.57:5001/card_read?token="+getToken + "&card_id="+tmp_card_id;

//            https://15.164.216.57:5001/card_create?token=BZgokJ419HaF1Vkh5Ia&name=a&address=a&phone_number=01011112222&url=&description=
                    try {
                        // Open the connection
                        URL url_tmp = new URL(urlStr);

                        // 모든 host 허용
                        HostnameVerifier allHostsValid_tmp = new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        };
                        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid_tmp);

                        HttpURLConnection conn_tmp = (HttpURLConnection) url_tmp.openConnection();

                        conn_tmp.setRequestMethod("GET");

                        Log.d("#####", String.valueOf(conn_tmp.getResponseCode()));

                        if (conn_tmp.getResponseCode() == 400) {
                            result = "Error: s not exists";
                            return result;
                        }

                        InputStream is_tmp = conn_tmp.getInputStream();

                        // Get the stream
                        StringBuilder builder_tmp = new StringBuilder();
                        BufferedReader reader_tmp = new BufferedReader(new InputStreamReader(is_tmp, "UTF-8"));
                        String line_tmp;
                        while ((line_tmp = reader_tmp.readLine()) != null) {
                            builder_tmp.append(line_tmp);
                        }

                        // Set the result
                        result = builder_tmp.toString();
                        Log.d("[psc]", result);
                        Log.d("######", result);
                        JSONArray arr_tmp = new JSONArray(result);
                        JSONObject obj_tmp = arr_tmp.getJSONObject(0);
                        name = obj_tmp.getString("name");
                        address = obj_tmp.getString("address");
                        phone = obj_tmp.getString("phone_number");
                        face_photo = obj_tmp.getString("face_photo");
                        description = obj_tmp.getString("description");

                        CardData m1 = new CardData();

                        m1.name = name;
                        m1.position = address;
                        m1.company = description;
                        m1.phoneNumber = phone;
                        m1.eMail = face_photo;

                        total.add(m1);


                        conn_tmp.disconnect();
                    } catch (Exception e) {
                        // Error calling the rest api
                        Log.e("REST_API", "GET method failed: " + e.getMessage());
                        e.printStackTrace();
                    }

                }


                Log.d("######", result);
                card_id = result;
                conn.disconnect();
            } catch (Exception e0) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e0.getMessage());
                e0.printStackTrace();
            }


            //Toast.makeText(CardDetail.this, result, Toast.LENGTH_SHORT).show();
            Log.d("[psc]", result);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }
    }
}
