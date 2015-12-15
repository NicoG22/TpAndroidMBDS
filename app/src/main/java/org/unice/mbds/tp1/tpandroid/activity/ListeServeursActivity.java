package org.unice.mbds.tp1.tpandroid.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.adapter.PersonItemAdapter;
import org.unice.mbds.tp1.tpandroid.object.Person;
import org.unice.mbds.tp1.tpandroid.utils.ApiCallService;
import org.unice.mbds.tp1.tpandroid.utils.ApiUrlService;

public class ListeServeursActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listeServeurs;
    PersonItemAdapter adapter;

    private String idPersonToRemove;
    private boolean doDelete = false;
    private int idxPersonToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_serveurs);

        listeServeurs = (ListView) findViewById(R.id.list_view_serveurs);

        get();
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.txt_view_delete_serveur) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.delete_serveur)
                    .setMessage(R.string.really_delete)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDelete = true;

                            delete((Integer) v.getTag());
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }

    private void delete(int pos) {
        idxPersonToRemove = pos;
        idPersonToRemove = ((Person) adapter.getItem(pos)).getId();


        ApiCallService.getInstance().doDelete(ListeServeursActivity.this, setProgressDialog(), ApiUrlService.personURL + idPersonToRemove);
    }

    private void get() {
        ApiCallService.getInstance().doGet(ListeServeursActivity.this, setProgressDialog(), ApiUrlService.personURL);
    }


    public void ajaxCallback(String url, String response, AjaxStatus status) {
        if ("".equals(response) || response == null) {
            Log.w("Erreur", "Veuillez réessayer");
        } else {
            if (doDelete) {
                adapter.person.remove(idxPersonToRemove);
                Toast.makeText(ListeServeursActivity.this, "Supression ok " + idPersonToRemove, Toast.LENGTH_LONG).show();

                adapter.notifyDataSetChanged();
            } else {

                try {
                    adapter = new PersonItemAdapter(getApplicationContext(), Person.fromJson(new JSONArray(response)), ListeServeursActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listeServeurs.setAdapter(adapter);

                Toast.makeText(ListeServeursActivity.this, "Liste chargée", Toast.LENGTH_LONG).show();
            }
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

