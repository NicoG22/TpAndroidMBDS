package org.unice.mbds.tp1.tpandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Uri chemin = Uri.parse("http://www.google.fr");
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("contact", "theContact");
        startActivity(i);
    }
}
