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
        Gson gson = new Gson();
        JuiceRoot root = gson.fromJson(TestData.data, JuiceRoot.class);
        Log.d("hahahah", root.toString());

        requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // Use HTTP GET to retrieve the data from the NASA API
                buildUrl(), // This is the actual URL used to retrieve the data
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    // The callback for handling the response
                    public void onResponse(JSONObject response) {
                        //Log.d(LOGTAG, "Volley response: " + response.toString());
                        Gson gson = new Gson();
                        JuiceRoot root = gson.fromJson(response.toString(), JuiceRoot.class);
                        Log.d("test","test");
                        openMap(root);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // The callback for handling a transmission error
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        //Log.e("haha", error.getLocalizedMessage());
                        finish();
                        //listener.onPhotoError(new Error(error.getLocalizedMessage()));
                    }
                }
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

        //Log.d("test",request.toUrl());
        return request.toUrl();
    }
}