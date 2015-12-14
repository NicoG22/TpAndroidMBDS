package org.unice.mbds.tp1.tpandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.androidquery.AQuery;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.activity.DetailProduitActivity;
import org.unice.mbds.tp1.tpandroid.object.Order;
import org.unice.mbds.tp1.tpandroid.object.Product;

import java.util.List;
import java.util.Map;

/**
 * Created by Gael on 10/12/2015.
 */
public class ProductItemAdapter extends BaseExpandableListAdapter    {

    private Context context;
    private List<String> categories; // header titles
    // child data in format of header title, child title
    private Map<String, List<Product>> produits;

    private Product productToShow;

    public ProductItemAdapter(Context context, List<String> categories,
                              Map<String, List<Product>> produits) {
        this.context = context;
        this.categories = categories;
        this.produits = produits;
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
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_view_products_elem, null);
        }

        AQuery aq = new AQuery(convertView);

        aq.id(convertView.findViewById(R.id.txt_view_product_list_name)).text(p.getNom());
        aq.id(convertView.findViewById(R.id.txt_view_product_list_discount)).text("Réduction :\n" + String.valueOf(p.getDiscount()) + " %");
        aq.id(convertView.findViewById(R.id.btn_list_products_add)).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Order.order.add(p);
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


/*    @Override
    public void onClick(View v) {
        Intent i = new Intent(context, DetailProduitActivity.class);
        v.getT
        i.putExtra("produit", productToShow);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }*/

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
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_view_products_group, null);
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