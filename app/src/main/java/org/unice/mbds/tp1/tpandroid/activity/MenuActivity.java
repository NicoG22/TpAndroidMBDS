package org.unice.mbds.tp1.tpandroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.unice.mbds.tp1.tpandroid.R;

public class MenuActivity extends AppCompatActivity {

    TextView viewNomPrenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        viewNomPrenom = (TextView) findViewById(R.id.txt_menu_nom_prenom);

        /** TODO LOAD FROM DATABASE **/
        viewNomPrenom.setText("Jean Michel");
    }
}
