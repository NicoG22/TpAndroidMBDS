package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.adapter.PersonItemAdapter;
import org.unice.mbds.tp1.tpandroid.object.Person;
import org.unice.mbds.tp1.tpandroid.utils.ApiCallService;
import org.unice.mbds.tp1.tpandroid.utils.ApiUrlService;

import java.util.List;

public class ListeServeursActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listeServeurs;
    PersonItemAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_serveurs);

        listeServeurs = (ListView) findViewById(R.id.list_view_serveurs);

        setProgressDialog();

        new GetListPersonsTask().execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_view_delete_serveur) {
            new DeleteTask().execute((Integer) v.getTag());
        }
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

    private class GetListPersonsTask extends AsyncTask<String, Void, List<Person>> {

        @Override
        protected List<Person> doInBackground(String... params) {

            try {
                JSONArray jsonArray = new JSONArray(ApiCallService.getInstance().doGet(ListeServeursActivity.this,
                        progressDialog, ApiUrlService.personURL).getResult());
                return Person.fromJson(jsonArray);

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
        protected void onPostExecute(List<Person> theResponse) {
            super.onPostExecute(theResponse);

            adapter = new PersonItemAdapter(getApplicationContext(), theResponse, ListeServeursActivity.this);

            listeServeurs.setAdapter(adapter);

            Toast.makeText(ListeServeursActivity.this, "Liste chargée", Toast.LENGTH_LONG).show();
        }
    }

    /******************
     * ASYNC TASK DELETE SEVEUR
     ******************/

    private class DeleteTask extends AsyncTask<Integer, Void, Void> {

        String id;
        Person retourDelete;
        int pos;

        @Override
        protected Void doInBackground(Integer... params) {

            try {
                pos = params[0];
                id = ((Person) adapter.getItem(pos)).getId();

                JSONObject jsonObj = new JSONObject(ApiCallService.getInstance().doDelete(ListeServeursActivity.this, progressDialog, ApiUrlService.personURL + id).getResult());

                retourDelete = new Person(jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (retourDelete != null) {
                Toast.makeText(ListeServeursActivity.this, "Supression ok " + id, Toast.LENGTH_LONG).show();
                adapter.person.remove(pos);
            }
            adapter.notifyDataSetChanged();
        }
    }

}

