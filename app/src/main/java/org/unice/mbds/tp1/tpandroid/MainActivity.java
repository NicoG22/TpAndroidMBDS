package org.unice.mbds.tp1.tpandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void signin(View view){
        Intent i = new Intent(this, SigninActivity.class);
        startActivity(i);
    }
}
