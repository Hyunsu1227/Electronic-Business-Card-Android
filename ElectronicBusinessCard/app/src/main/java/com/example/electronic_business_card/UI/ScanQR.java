package com.example.electronic_business_card.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class ScanQR extends AppCompatActivity {
    private IntentIntegrator qrScan;
    String token;
    String content;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        intent = getIntent();
        token = intent.getExtras().getString("token");

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("QR 코드 입력화면");
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                content = result.getContents();
                AddFriendCardIDAsync addFriendCardIDAsync = new AddFriendCardIDAsync();
                addFriendCardIDAsync.execute();
                // todo
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class AddFriendCardIDAsync extends AsyncTask<Void, Void, String> {
        String result;
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("error")) {
                Toast.makeText(ScanQR.this, "올바르지 않은 QRcode 거나 login이 만료되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ScanQR.this , "명함 추가 성공", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            String urlStr = "https://15.164.216.57:5001/friend_set?token=" + token
                    + "&friend_card=" + content;
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
