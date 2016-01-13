package org.unice.mbds.tp1.tpandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.androidquery.AQuery;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.object.Product;
import org.unice.mbds.tp1.tpandroid.object.UserManager;

public class DetailProduitActivity extends AppCompatActivity {

    public DetailProduitActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produit);

        Intent i = getIntent();

        final Product p = (Product) i.getSerializableExtra("produit");
        String prixFinal = Double.toString(p.getPrix() * (1 - p.getDiscount() / 100));

        AQuery aq = new AQuery(this);

        aq.id(R.id.img_view_detail_produit).image(aq.getCachedImage(p.getImg()));
        aq.id(R.id.txt_detail_produit_nom).text(p.getNom());
        aq.id(R.id.txt_view_detail_produit_calories).text(getString(R.string.txt_detail_produit_calories) + p.getCalories());
        aq.id(R.id.txt_view_detail_produit_discount).text(getString(R.string.txt_detail_produit_discount) + p.getDiscount() + " %");
        aq.id(R.id.txt_view_detail_produit_type).text(getString(R.string.txt_detail_produit_type) + p.getType());
        aq.id(R.id.txt_view_detail_produit_prix).text(getString(R.string.txt_detail_produit_prix) + prixFinal);
        aq.id(R.id.txt_view_detail_produit_description).text(p.getDescription());

        aq.id(R.id.btn_view_detail_produit_add).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getUser().getOrder().add(p);
            }
        });

    }
}
