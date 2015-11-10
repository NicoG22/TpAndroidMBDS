package org.unice.mbds.tp1.tpandroid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.object.Person;

import java.util.List;

/**
 * Created by Nicolas on 30/10/2015.
 */
public class PersonItemAdapter extends BaseAdapter {

    private Context context;
    public List<Person> person;

    public PersonItemAdapter(Context context, List<Person> person) {
        this.context = context;
        this.person = person;
    }

    @Override
    public int getCount() {
        return person.size();
    }

    @Override
    public Object getItem(int arg0) {

        return person.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        View v = convertView;

        PersonViewHolder viewHolder = null;
        if(v==null){
            v = View.inflate(context, R.layout.list_view_serveurs_elem, null);
            viewHolder = new PersonViewHolder();
            viewHolder.nom_prenom= (TextView)v.findViewById(R.id.txt_view_buzz_username);
            viewHolder.date_creation= (TextView)v.findViewById(R.id.txt_view_buzz_is_connected);
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (PersonViewHolder) v.getTag();
        }
        Person personne = person.get(position);
        viewHolder.nom_prenom.setText(personne.getNom());
        viewHolder.date_creation.setText(personne.etat());
        return v;
    }

    class PersonViewHolder{
        TextView nom_prenom;
        TextView date_creation;
    }

}
