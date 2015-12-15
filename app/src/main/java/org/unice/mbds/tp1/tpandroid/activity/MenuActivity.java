package org.unice.mbds.tp1.tpandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.object.Order;

public class MenuActivity extends AppCompatActivity {

    TextView viewNomPrenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        viewNomPrenom = (TextView) findViewById(R.id.txt_menu_nom_prenom);
        viewNomPrenom.setText("Jean Michel");
    }

    public void redirect(View v) {
        Intent i;

        if(v.getId() == R.id.btn_list_produits_view) {
            i = new Intent(this, ListeProduitsActivity.class);
            Order.order.clear();
            startActivity(i);
        }
        else if(v.getId() == R.id.btn_list_serveurs_view) {
            i = new Intent(this, ListeServeursActivity.class);
            startActivity(i);
        }
    }
}
