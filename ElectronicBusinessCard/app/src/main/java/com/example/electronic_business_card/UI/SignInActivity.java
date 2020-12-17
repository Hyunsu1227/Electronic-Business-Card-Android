package com.example.electronic_business_card.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.electronic_business_card.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class SignInActivity extends AppCompatActivity {
    EditText id, pw;
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        id = findViewById(R.id.emailEditText);
        pw = findViewById(R.id.passwordEditText);
        Button login = findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInAsync signInAsync = new SignInAsync();
                signInAsync.execute();

                createCard createCard = new createCard();
                createCard.execute();
            }
        });

        Button signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


    class SignInAsync extends AsyncTask<Void, Void, String>{
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Error: such id/pw not exists")) {
                Toast.makeText(SignInActivity.this, "해당 ID/PW가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SignInActivity.this , "로그인 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                result = result.substring(1,result.length() - 1); // "" 제거
                intent.putExtra("token", result);

                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String urlStr = "https://15.164.216.57:5001/login_by_id?" + "id=" + id.getText().toString() + "&pw=" + pw.getText().toString();
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
            String urlStr = "https://15.164.216.57:5001/card_create?token=PdYw6j80oKvVp2D7aZ6&name=b&address=b&phone_number=b&url=&description=b";

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
}