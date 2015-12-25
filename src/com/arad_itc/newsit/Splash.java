package com.arad_itc.newsit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Newsit_DBOpenHelper helper = new Newsit_DBOpenHelper(this);
        try {
            helper.createDataBase();
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                super.run();
                Intent intent = new Intent(Splash.this, Main.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 3000);
    }

}
