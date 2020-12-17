package com.example.electronic_business_card.UI;

import android.content.Intent;
import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronic_business_card.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class CreateQR extends AppCompatActivity {
    private ImageView iv;
    private String text;
    String token;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        intent = getIntent();
        token = intent.getExtras().getString("token");

        iv = (ImageView)findViewById(R.id.qrcode);
        CheckCardIDAsync checkCardIDAsync = new CheckCardIDAsync();
        checkCardIDAsync.execute();
    }

    class CheckCardIDAsync extends AsyncTask<Void, Void, String> {
        String result;
        // 결과값 받아 온 후 ui 실행
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("null")) {
                Toast.makeText(CreateQR.this, "아직 자신의 명함을 생성하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(CreateQR.this, "CardID 읽어오기 성공 " + result, Toast.LENGTH_SHORT).show();
                text = result;

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    iv.setImageBitmap(bitmap);
                }catch (Exception e){}
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
}
