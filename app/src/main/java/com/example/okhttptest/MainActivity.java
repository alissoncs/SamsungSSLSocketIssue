package com.example.okhttptest;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final String API_URL = "https://sicmobi.hom.sicredi.net/middleware/MWServlet";
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.api_url);

        tv.setText(API_URL);
        tvResponse = findViewById(R.id.response);

        FloatingActionButton fab = findViewById(R.id.fab);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                NativeSslCode.createRequest();
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RESPONSE", "CLIQUED");
                new Thread(r).start();
            }
        });


//        r.run();


//        final Handler handler = new Handler();
//        final Runnable r = new Runnable() {
//            public void run() {
//                handler.postDelayed(this, 3000);
//                request();
//            }
//        };
//        r.run();

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return super.onWindowStartingActionMode(callback);
    }

    protected void request() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        // OkHttpClient client = suites.getClient();

        String url = API_URL;

        RequestBody formBody = new FormBody.Builder()
                .add("serviceID", "datahora")
                .add("appID", "sicredimobi")
                .add("channel", "rc")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                appendResponse("EXCEPTION: " + e.getMessage() + "\n");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                appendResponse("\nSUCCESS: " + response.body().string() + "\n");

            }
        });
    }

    private void appendResponse(final String res) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                tvResponse.setText(tvResponse.getText() + res);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
