package org.unice.mbds.tp1.tpandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.androidquery.AQuery;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.activity.DetailProduitActivity;
import org.unice.mbds.tp1.tpandroid.activity.ListeProduitsActivity;
import org.unice.mbds.tp1.tpandroid.object.Order;
import org.unice.mbds.tp1.tpandroid.object.Product;

import java.util.List;
import java.util.Map;

/**
 * Created by Gael on 10/12/2015.
 */
public class ProductItemAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> categories; // header titles
    // child data in format of header title, child title
    private Map<String, List<Product>> produits;
    private Activity caller;

    public ProductItemAdapter(Context context, List<String> categories,
                              Map<String, List<Product>> produits, Activity caller) {
        this.context = context;
        this.categories = categories;
        this.produits = produits;
        this.caller = caller;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setProduits(Map<String, List<Product>> produits) {
        this.produits = produits;
    }

    public Map<String, List<Product>> getProduits() {
        return produits;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.produits.get(this.categories.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Product p = (Product) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_view_products_elem, null);
        }

        AQuery aq = new AQuery(convertView);

        aq.id(convertView.findViewById(R.id.txt_view_product_list_name)).text(p.getNom());
        aq.id(convertView.findViewById(R.id.txt_view_product_list_discount)).text("Réduction :\n" + String.valueOf(p.getDiscount()) + " %");

        if (((ListeProduitsActivity) caller).isBasketEnabled) {
            double finalPrice = p.getPrix() * (1 - p.getDiscount() / 100);

            aq.id(convertView.findViewById(R.id.btn_list_products_add)).visibility(View.GONE);

            aq.id(convertView.findViewById(R.id.txt_view_product_list_calories)).visible().text("Calories :\n" + String.valueOf(p.getCalories()));
            aq.id(convertView.findViewById(R.id.txt_view_product_list_prix)).visible().text("Prix avec réduction :\n" + String.valueOf(finalPrice) + " €");

        } else {
            aq.id(convertView.findViewById(R.id.txt_view_product_list_calories)).visibility(View.GONE);
            aq.id(convertView.findViewById(R.id.txt_view_product_list_prix)).visibility(View.GONE);

            aq.id(convertView.findViewById(R.id.btn_list_products_add)).visible().clicked(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order.order.add(p);
                    caller.invalidateOptionsMenu();
                    ((ListeProduitsActivity) caller).sum_panier.setText(String.valueOf(Order.order.size()));
                }
            });

        }

        aq.id(convertView.findViewById(R.id.btn_list_products_panier_valider)).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * I DUNNO WHY I'M HERE
                */
            }
        });

        aq.id(convertView.findViewById(R.id.layout_view_product_list_item)).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailProduitActivity.class);
                i.putExtra("produit", p);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            }
        });

        aq.id(convertView.findViewById(R.id.img_view_product_list)).image(p.getImg()).width(130);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.produits.get(this.categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.categories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.categories.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupe = (String) getGroup(groupPosition);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_view_products_group, null);
        }

        AQuery aq = new AQuery(convertView);

        aq.id(R.id.txt_list_view_products_group).text(groupe);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}