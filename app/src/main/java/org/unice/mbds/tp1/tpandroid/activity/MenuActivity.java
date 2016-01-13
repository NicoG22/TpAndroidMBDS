package org.unice.mbds.tp1.tpandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.object.UserManager;

public class MenuActivity extends AppCompatActivity {

    TextView viewNomPrenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        checkConnected();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        viewNomPrenom = (TextView) findViewById(R.id.txt_menu_nom_prenom);
        viewNomPrenom.setText(UserManager.getUser().getNom() + " " + UserManager.getUser().getPrenom());
    }

    private void checkConnected() {
        if(UserManager.getUser() == null) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public void redirect(View v) {
        Intent i;

        if(v.getId() == R.id.btn_list_produits_view) {
            i = new Intent(this, ListeProduitsActivity.class);
            UserManager.getUser().getOrder().clear();
            startActivity(i);
        }
        else if(v.getId() == R.id.btn_list_serveurs_view) {
            i = new Intent(this, ListeServeursActivity.class);
            startActivity(i);
        }
        else if(v.getId() == R.id.btn_log_out) {
            UserManager.logOut();
            i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}
