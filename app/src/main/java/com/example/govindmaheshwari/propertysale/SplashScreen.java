package com.example.govindmaheshwari.propertysale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.StaticLayout;
import android.widget.Toast;

/**
 * Created by Govind Maheshwari on 5/30/2015.
 */
public class SplashScreen extends Activity {
    Boolean isConnected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ConnectivityManager check = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (int i = 0; i<info.length; i++){
            if (info[i].getState() == NetworkInfo.State.CONNECTED){
                isConnected=true;
            }
        }
        setContentView(R.layout.splash);
        if(isConnected==false)
            Toast.makeText(getApplicationContext(), "Internet is not connected", Toast.LENGTH_SHORT).show();
        Thread timerThread = new Thread(){
              public void run(){
                    try{
                        sleep(3000);
                    }catch(InterruptedException e){
                         e.printStackTrace();
                    }finally{
                        if(isConnected==false)
                        {
                            finish();
                        }else {
                            Intent i = new Intent("com.example.govindmaheshwari.propertysale.MAINACTIVITY");
                            startActivity(i);
                        }
                    }
              }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}