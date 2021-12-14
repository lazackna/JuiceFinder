package com.lazackna.juicefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lazackna.juicefinder.util.juiceroot.JuiceRoot;
import com.lazackna.juicefinder.util.TestData;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private String apiKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), getPackageManager().GET_META_DATA);

        apiKey = ai.metaData.getString("ocmKey");

        Toast.makeText(this,apiKey,Toast.LENGTH_LONG).show();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        makeVolleyRequest();

    }

    private void makeVolleyRequest(){
        requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // Use HTTP GET to retrieve the data from the ChargeMap API
                buildUrl(), // This is the actual URL used to retrieve the data
                null,
                response -> {
                    Gson gson1 = new Gson();
                    JuiceRoot root = gson1.fromJson(response.toString(), JuiceRoot.class);
                    Log.d("test","test");
                },
                error -> finish()
        );
        requestQueue.add(request);
    }

    private void openMap(JuiceRoot root) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("juice", root);
        startActivity(intent);
    }

    private String buildUrl() {
        OpenChargeMapRequest request = new OpenChargeMapRequest(
                new OpenChargeMapRequestBuilder().BuildRequest(apiKey)
                        .Location(51.6002069, 4.6965766)
                        .IncludeComments()
                        .Distance(100)
                        .MaxResults(10)
                        .CountryCode("NL"));

        return request.toUrl();
    }
}