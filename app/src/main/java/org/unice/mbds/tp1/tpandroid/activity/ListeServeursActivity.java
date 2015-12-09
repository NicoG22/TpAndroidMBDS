package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_serveurs);

        listeServeurs = (ListView) findViewById(R.id.list_view_serveurs);

        new GetListPersonsTask().execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_view_delete_serveur) {
            new DeleteTask().execute((Integer) v.getTag());
        }
    }

    /****************** ASYNC TASK GET LISTE SEVEURS ******************/

    private class GetListPersonsTask extends AsyncTask<String, Void, List<Person>> {

        ProgressDialog progressDialog;

        public void showProgressDialog(boolean isVisible) {
            if (isVisible) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ListeServeursActivity.this);
                    progressDialog.setMessage("Patientez...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            progressDialog = null;
                        }
                    });
                    progressDialog.show();
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }

        @Override
        protected List<Person> doInBackground(String... params) {

            try {
                JSONArray jsonArray = new JSONArray(ApiCallService.getInstance().doGet(ApiUrlService.personURL));
                return Person.fromJson(jsonArray);

            } catch (Exception e) {
                Log.w("Erreur", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(List<Person> theResponse) {
            super.onPostExecute(theResponse);

            adapter = new PersonItemAdapter(getApplicationContext(), theResponse, ListeServeursActivity.this);

            listeServeurs.setAdapter(adapter);

            showProgressDialog(false);
            Toast.makeText(ListeServeursActivity.this, "Liste chargée", Toast.LENGTH_LONG).show();
        }
    }

    /****************** ASYNC TASK DELETE SEVEUR ******************/

    private class DeleteTask extends AsyncTask<Integer, Void, Void> {

        ProgressDialog progressDialog;
        String id;
        Person retourDelete;

        public void showProgressDialog(boolean isVisible) {
            if (isVisible) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ListeServeursActivity.this);
                    progressDialog.setMessage("Patientez...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            progressDialog = null;
                        }
                    });
                    progressDialog.show();
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }

        @Override
        protected Void doInBackground(Integer... params) {

            try {
                id = ((Person) adapter.getItem(params[0])).getId();

                JSONObject jsonObj = new JSONObject(ApiCallService.getInstance().doDelete(ApiUrlService.personURL + id));

                retourDelete = new Person(jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showProgressDialog(false);
            if (retourDelete != null) {
                Toast.makeText(ListeServeursActivity.this, "Supression ok " + id, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
        }
    }
}

