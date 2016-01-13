package org.unice.mbds.tp1.tpandroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.object.UserManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // If user is connected then redirect to menu
        if(checkConnected()) {
            redirectMenu();
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }

    private Boolean checkConnected() {
        if(UserManager.getUser() != null) {
            return true;
        }

        return false;
    }

    public void redirectMenu() {
        Intent i = new Intent(this, MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
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
