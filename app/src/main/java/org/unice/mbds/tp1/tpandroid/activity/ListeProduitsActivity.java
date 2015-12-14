package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.adapter.ProductItemAdapter;
import org.unice.mbds.tp1.tpandroid.object.Order;
import org.unice.mbds.tp1.tpandroid.object.Product;
import org.unice.mbds.tp1.tpandroid.utils.ApiCallService;
import org.unice.mbds.tp1.tpandroid.utils.ApiUrlService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeProduitsActivity extends AppCompatActivity {

    ExpandableListView listeProduits;
    ProductItemAdapter adapter;
    List<String> categories;
    List<Map<String, List<Product>>> products;
    ProgressDialog progressDialog;
    public TextView sum_panier;
    public boolean isBasketEnabled = false;

    private static final int ALL_PRODUCTS_POS = 0;
    private static final int ORDERED_PRODUCTS_POS = 1;

    public ListeProduitsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_liste_produits);

        listeProduits = (ExpandableListView) findViewById(R.id.list_view_produits);

        selectData();

        invalidateOptionsMenu();
    }

    private void selectData() {
        if (isBasketEnabled) {
            processProducts(Order.order, ORDERED_PRODUCTS_POS);
            this.setTitle(R.string.title_activity_liste_commande);
        } else {
            setProgressDialog();
            new GetListProductsTask().execute();
            this.setTitle(R.string.title_activity_liste_produits);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_lien_panier, menu);

        MenuItem menu_badge = menu.findItem(R.id.badge_basket);

        MenuItemCompat.setActionView(menu_badge, R.layout.badge_menu_item);
        RelativeLayout badgeLayout = (RelativeLayout) MenuItemCompat.getActionView(menu_badge);
        sum_panier = (TextView) badgeLayout.findViewById(R.id.txt_badge_panier);

        if (Order.order.size() == 0) {
            sum_panier.setVisibility(View.INVISIBLE);
        } else {
            sum_panier.setText(String.valueOf(Order.order.size()));
        }

        AQuery aq = new AQuery(this);

        aq.id(badgeLayout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBasketEnabled = !isBasketEnabled;
                selectData();
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    private void processProducts(List<Product> data, int pos) {
        Map<String, List<Product>> currProds;
/*
        if (pos == ALL_PRODUCTS_POS || pos == ORDERED_PRODUCTS_POS) {

            if (products == null) {
                products = new ArrayList<>();
            } else if (products.get(pos) == null) {
                products.add(pos, new HashMap<String, List<Product>>());
            }*/

        categories = new ArrayList<>();
        List<Product> productsTmp;
        currProds = new HashMap<>();


        for (Product p : data) {
            String type = p.getType();

            if (!categories.contains(type)) {
                categories.add(type);
            }

            if (!currProds.containsKey(type)) {
                currProds.put(type, new ArrayList<Product>());
            }

            productsTmp = currProds.get(type);
            productsTmp.add(p);
            currProds.put(type, productsTmp);

            //products.add(pos, currProds);
        }

        adapter = new ProductItemAdapter(getApplicationContext(), categories, currProds, this);

        listeProduits.setAdapter(adapter);
//        }
    }

    public void setProgressDialog() {
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Patientez...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    /******************
     * ASYNC TASK GET LISTE SEVEURS
     ******************/

    private class GetListProductsTask extends AsyncTask<String, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(String... params) {

            try {
                JSONArray jsonArray = new JSONArray(ApiCallService.getInstance().doGet(ListeProduitsActivity.this,
                        progressDialog, ApiUrlService.productURL).getResult());
                return Product.fromJson(jsonArray);

            } catch (Exception e) {
                Log.w("Erreur", e.getMessage());
                return null;
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Product> theResponse) {
            super.onPostExecute(theResponse);

            processProducts(theResponse, ALL_PRODUCTS_POS);

            Toast.makeText(ListeProduitsActivity.this, "Liste chargée", Toast.LENGTH_LONG).show();
        }
    }
}

