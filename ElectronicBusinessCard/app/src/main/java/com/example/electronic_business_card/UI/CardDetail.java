package com.example.electronic_business_card.UI;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.json.JSONTokener;

public class CardDetail extends AppCompatActivity {
    EditText name_edit;
    EditText company_edit;
    EditText position_edit;
    EditText phoneNumber_edit;
    EditText eMail_edit;
    Button delete_button;
    String card_id = "";
    String result = "";
    String name;
    String address;
    String phone;
    String face_photo;
    String description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail_acitivity);
        name_edit = findViewById(R.id.name_edit);
        company_edit = findViewById(R.id.company_edit);
        position_edit = findViewById(R.id.position_edit);
        phoneNumber_edit = findViewById(R.id.phoneNumber_edit);
        eMail_edit = findViewById(R.id.eMail_edit);
    /*
        delete_button = findViewById(R.id.delete_card);
        delete_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteCard deleteCard = new deleteCard();
                deleteCard.execute();
            }
        });
*/
        readCard readCard = new readCard();
        readCard.execute();

//        기본적으로 명함 정보를 Read해옴.
        disable_editText();
    }
    /*
     * https://15.164.216.57:5001 일단 이게 서버 주소 - GET방식으로..?
     *
     * Create - 내 명함을 만들어 DB에 적제
     * Read - 내 명함을 DB 에서 읽음
     * Update - 내 명함 정보를 갱신함
     * Delete -  내 명함 정보를 삭제함
     * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        if (intent.getExtras().getString("activity").equals("내명함")) {
            getMenuInflater().inflate(R.menu.card_detail_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.updateOrSave) {
            if (item.getTitle().equals("수정")) {
                item.setTitle("저장");
                enable_editText();
            } else if (item.getTitle().equals("저장")) {
                item.setTitle("수정");
                disable_editText();

                createCard createCard = new createCard();
                createCard.execute();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void disable_editText() {
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

    public void enable_editText() {
        name_edit.setKeyListener((KeyListener) name_edit.getTag());
        company_edit.setKeyListener((KeyListener) company_edit.getTag());
        position_edit.setKeyListener((KeyListener) position_edit.getTag());
        phoneNumber_edit.setKeyListener((KeyListener) phoneNumber_edit.getTag());
        eMail_edit.setKeyListener((KeyListener) eMail_edit.getTag());
    }



    class createCard extends AsyncTask<Void, Void, String> {
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Error: such id/pw not exists")) {
                Log.d("[FAIL] : ", "create_Card failure");
            } else {
                Log.d("[SUCCESS] : ", result);
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String getToken = getIntent().getStringExtra("token");
//            Log.d("TOKEN", getToken);

/*            String urlStr = "https://15.164.216.57:5001/card_create?" + "token=" + getToken
                    + "&name=홍길동&address=KNU&phone_number=01012340987&url=&description=";
*/
            String urlStr = "https://15.164.216.57:5001/card_update?token=" + getToken +
                    "&name=" + name_edit.getText().toString() +
                    "&address=" + company_edit.getText().toString() +
                    "&phone_number=" + phoneNumber_edit.getText().toString() +
                    "&url=&description=";

//            https://15.164.216.57:5001/card_create?token=BZgokJ419HaF1Vkh5Ia&name=a&address=a&phone_number=01011112222&url=&description=
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
                if (conn.getResponseCode() == 400) {
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

                Log.d("######", result);

                conn.disconnect();
            } catch (Exception e) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e.getMessage());
                e.printStackTrace();
            }

            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }
    }

    class readCard extends AsyncTask<Void, Void, String> {
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Error: such id/pw not exists")) {
                Log.d("[psc] : ", "read card failure");
            } else {
                Log.d("[psc] : ", result);
                name_edit.setText(name);
                company_edit.setText(description);
                position_edit.setText(address);
                phoneNumber_edit.setText(phone);
                eMail_edit.setText(face_photo);
            }

        }

        @Override
        protected String doInBackground(Void... strings) {
            String getToken = getIntent().getStringExtra("token");
//            Log.d("TOKEN", getToken);

            String urlStr0 = "https://15.164.216.57:5001/card_id?token="+getToken;
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


                Log.d("######", result);
                card_id = result;
                conn.disconnect();
            } catch (Exception e0) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e0.getMessage());
                e0.printStackTrace();
            }

            String urlStr = "https://15.164.216.57:5001/card_read?token="+getToken + "&card_id="+card_id;

//            https://15.164.216.57:5001/card_create?token=BZgokJ419HaF1Vkh5Ia&name=a&address=a&phone_number=01011112222&url=&description=
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

                if (conn.getResponseCode() == 400) {
                    result = "Error: s not exists";
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

                Log.d("######", result);
                JSONArray arr = new JSONArray(result);
                JSONObject obj = arr.getJSONObject(0);
                name = obj.getString("name");
                address = obj.getString("address");
                phone = obj.getString("phone_number");
                face_photo = obj.getString("face_photo");
                description = obj.getString("description");
                Log.d("[psc]","name:"+name);

                name_edit.setText(name);
                company_edit.setText(description);
                position_edit.setText(address);
                phoneNumber_edit.setText(phone);
                eMail_edit.setText(face_photo);
                //JSONTokener tokener = new JSONTokener(result);
                //JSONObject json = new JSONObject(tokener);
                //JSONArray jsonArray = json.getJSONArray()
                //name_edit.setText(json.getString("name"));

                conn.disconnect();
            } catch (Exception e) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e.getMessage());
                e.printStackTrace();
            }
            //Toast.makeText(CardDetail.this, result, Toast.LENGTH_SHORT).show();
            Log.d("[psc]", result);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }
    }

}
