package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.adapter.ProductItemAdapter;
import org.unice.mbds.tp1.tpandroid.object.Person;
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
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_serveurs);

        listeProduits = (ExpandableListView) findViewById(R.id.list_view_produits);

        setProgressDialog();

        new GetListProductsTask().execute();
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
                        progressDialog, ApiUrlService.personURL).getResult());
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

            List<String> categories = new ArrayList<>();
            Map<String, List<Product>> products = new HashMap<>();

            adapter = new ProductItemAdapter(getApplicationContext(), categories, products);

            listeProduits.setAdapter(adapter);

            Toast.makeText(ListeProduitsActivity.this, "Liste chargée", Toast.LENGTH_LONG).show();
        }
    }
}

