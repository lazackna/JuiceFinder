package com.lazackna.juicefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lazackna.juicefinder.util.JuiceRoot;
import com.lazackna.juicefinder.util.TestData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), getPackageManager().GET_META_DATA);

        String val = ai.metaData.getString("ocmKey");

        Toast.makeText(this,val,Toast.LENGTH_LONG).show();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        JuiceRoot root = gson.fromJson(TestData.data, JuiceRoot.class);
        Log.d("hahahah", root.toString());

    }
}