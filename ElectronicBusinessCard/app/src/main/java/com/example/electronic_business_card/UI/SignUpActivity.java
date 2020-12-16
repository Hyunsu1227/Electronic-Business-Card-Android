package com.example.electronic_business_card.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class SignUpActivity extends AppCompatActivity {
    EditText id, password, password2, nickName;
    Button signUp;
    String result = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        id = findViewById(R.id.signUpEmailEditText);
        password = findViewById(R.id.password1EditText);
        password2 = findViewById(R.id.password2EditText);
        nickName = findViewById(R.id.nicknameEditText);
        signUp = findViewById(R.id.signUpReqButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().equals(password2.getText().toString())){
                    Toast.makeText(SignUpActivity.this , "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    Log.d("######",password.getText().toString());
                    Log.d("######",password2.getText().toString());
                    return;
                }
                SignUpAsync signUpAsync = new SignUpAsync();
                signUpAsync.execute();

            }
        });
    }
    class SignUpAsync extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Query Error")){
                Toast.makeText(SignUpActivity.this , "해당 ID가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SignUpActivity.this , "가입 완료!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String urlStr = "https://15.164.216.57:5001/join?" + "id=" + id.getText().toString() + "&pw=" + password.getText().toString()
                    + "&nickname=" + nickName.getText().toString();
            try {
                // Open the connection
                URL url = new URL(urlStr);

                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
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
