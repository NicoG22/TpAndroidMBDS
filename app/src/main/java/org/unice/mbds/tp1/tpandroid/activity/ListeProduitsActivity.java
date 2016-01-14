package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
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
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.adapter.ProductItemAdapter;
import org.unice.mbds.tp1.tpandroid.object.Product;
import org.unice.mbds.tp1.tpandroid.object.UserManager;
import org.unice.mbds.tp1.tpandroid.utils.ApiCallService;
import org.unice.mbds.tp1.tpandroid.utils.ApiUrlService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeProduitsActivity extends AppCompatActivity {

    ExpandableListView listeProduits;
    ProductItemAdapter adapter;
    List<List<String>> categories;
    List<Map<String, List<Product>>> products;
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
            processProducts(UserManager.getUser().getOrder(), ORDERED_PRODUCTS_POS);
            this.setTitle(R.string.title_activity_liste_commande);
        } else {
            if (products == null) {
                initActivity();
            } else {
                updateAdapter(ALL_PRODUCTS_POS);
            }
            this.setTitle(R.string.title_activity_liste_produits);
        }
    }

    private void updateAdapter(int pos) {
        adapter.setCategories(categories.get(pos));
        adapter.setProduits(products.get(pos));
        adapter.notifyDataSetChanged();
    }

    private void initActivity() {
        ApiCallService.getInstance().doGet(ListeProduitsActivity.this, setProgressDialog(), ApiUrlService.productURL);

        AQuery aq = new AQuery(this);
        aq.id(this.findViewById(R.id.btn_list_products_panier_valider)).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sumValue = 0;
                int sumReduc = 0;
                int nbProduits = 0;

                HashMap<String, Object> postParams = new HashMap();
                HashMap<String, Object> server = new HashMap();
                HashMap<String, Object> cooker = new HashMap();
                ArrayList<HashMap<String, Object>> items = new ArrayList();

                // Ajout des objets serveur et cuisinier
                server.put("id", UserManager.getUser().getId());
                cooker.put("id", 12345678);

                // Pour chaque produit de la liste du panier
                for(int i = 0; i < UserManager.getUser().getOrder().size(); i++) {
                    nbProduits++;

                    Product produit = UserManager.getUser().getOrder().get(i);

                    // On ajoute son id à la liste des items de la commande
                    HashMap<String, Object> idProduc = new HashMap();
                    idProduc.put("id", produit.getId());
                    items.add(idProduc);

                    // On ajoute son prix et sa reduction a la somme de la commande
                    sumReduc += produit.getDiscount();
                    sumValue += produit.getPrix();
                }

                // Si la valeur de la commande depasse 0 (donc on a des produits)
                if(sumValue != 0) {
                    // On remplis l'objet JSON que l'on va envoyer au serveur
                    postParams.put("price", sumValue);
                    postParams.put("discount", sumReduc/nbProduits);
                    postParams.put("server", server);
                    postParams.put("cooker", cooker);
                    postParams.put("items", items);

                    Log.w("VALEUR COMMANDE :", postParams.toString());

                    ApiCallService.getInstance().doPost(ListeProduitsActivity.this, setProgressDialog(), ApiUrlService.orderURL, postParams);
                }
            }
        });
    }

    /**
     * Gère le callback d'AQuery (via la méthode doGet)
     * Envoie la liste des produits à la méthode processProducts
     *
     * @param url
     * @param response
     * @param status
     */
    public void ajaxCallback(String url, String response, AjaxStatus status) {
        if (response == null || "".equals(response)) {
            Log.w("Erreur", "Veuillez réessayer");
        } else {

            if(url == ApiUrlService.productURL) {
                try {
                    processProducts(Product.fromJson(new JSONArray(response)), ALL_PRODUCTS_POS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(ListeProduitsActivity.this, "Liste chargée", Toast.LENGTH_LONG).show();

            } else if(url == ApiUrlService.orderURL) {
                Toast.makeText(ListeProduitsActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Affiche le bouton panier sur la barre de menu, et initialise le badge
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_lien_panier, menu);

        MenuItem menu_badge = menu.findItem(R.id.badge_basket);

        MenuItemCompat.setActionView(menu_badge, R.layout.badge_menu_item);
        RelativeLayout badgeLayout = (RelativeLayout) MenuItemCompat.getActionView(menu_badge);
        sum_panier = (TextView) badgeLayout.findViewById(R.id.txt_badge_panier);

        if (UserManager.getUser().getOrder().size() == 0) {
            sum_panier.setVisibility(View.INVISIBLE);
        } else {
            sum_panier.setText(String.valueOf(UserManager.getUser().getOrder().size()));
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

    /**
     * Initialise la liste de produits passée en entrée dans l'élément des listes products
     * et categories correspondant.
     * L'adapter est ensuite défini
     * <p/>
     * ALL_PRODUCTS_POS pour ajouter a la carte du restaurant
     * ORDERED_PRODUCTS_POS pour ajouter au panier
     *
     * @param data Les données à afficher
     * @param pos  La position dans les listes products et categories
     */
    private void processProducts(List<Product> data, int pos) {
        Map<String, List<Product>> currProds;


        if (products == null) {
            products = new ArrayList<>();
        }

        try {
            products.get(pos);
        } catch (IndexOutOfBoundsException e) {
            products.add(pos, new HashMap<String, List<Product>>());
        }

        if (categories == null) {
            categories = new ArrayList<>();
        }

        try {
            categories.get(pos);
        } catch (IndexOutOfBoundsException e) {
            categories.add(pos, new ArrayList<String>());
        }

        List<Product> productsTmp;
        currProds = new HashMap<>();


        for (Product p : data) {
            String type = p.getType();

            if (!categories.get(pos).contains(type)) {
                categories.get(pos).add(type);
            }

            if (!currProds.containsKey(type)) {
                currProds.put(type, new ArrayList<Product>());
            }

            productsTmp = currProds.get(type);
            productsTmp.add(p);
            currProds.put(type, productsTmp);

            products.add(pos, currProds);
        }

        if (adapter == null) {
            adapter = new ProductItemAdapter(getApplicationContext(), categories.get(pos), products.get(pos), this);
            listeProduits.setAdapter(adapter);
        } else {
            updateAdapter(pos);
        }
    }

    public ProgressDialog setProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Patientez...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return progressDialog;
    }
}
