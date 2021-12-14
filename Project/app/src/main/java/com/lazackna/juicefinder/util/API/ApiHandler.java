package com.lazackna.juicefinder.util.API;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ApiHandler {

    private RequestQueue requestQueue;
    private String apiKey = "";
    private Context context;

    private static ApiHandler instance;

    public static ApiHandler getInstance(Context context) {
        if (instance == null) instance = new ApiHandler(context);
        return instance;
    }

    private ApiHandler(Context context) {
        this.context = context;
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), context.getPackageManager().GET_META_DATA);

            apiKey = ai.metaData.getString("ocmKey");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(context);
    }

    public void makeVolleyObjectRequest(@NonNull Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // Use HTTP GET to retrieve the data from the ChargeMap API
                buildUrl(), // This is the actual URL used to retrieve the data
                null,
                responseListener,
                errorListener
        );
        requestQueue.add(request);
    }

    private String buildUrl() {
        OpenChargeMapRequest request = new OpenChargeMapRequest(
                new OpenChargeMapRequestBuilder().BuildRequest(apiKey)
                        .Location(51.6002069, 4.6965766)
                        .IncludeComments()
                        .Distance(100)
                        .MaxResults(50)
                        .CountryCode("NL"));

        return request.toUrl();
    }
}
