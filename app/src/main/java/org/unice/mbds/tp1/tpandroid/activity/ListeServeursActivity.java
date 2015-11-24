package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.adapter.PersonItemAdapter;
import org.unice.mbds.tp1.tpandroid.object.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListeServeursActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listeServeurs;
    PersonItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_serveurs);

        listeServeurs = (ListView) findViewById(R.id.list_view_serveurs);

        new MyTask().execute();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txt_view_delete_serveur){
            deletePerson((Integer) v.getTag());
        }
    }

    private class MyTask extends AsyncTask<String, Void, List<Person>> {

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

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://92.243.14.22/person/");

                // add header
                get.setHeader("Content-Type", "application/json");
                JSONObject obj = new JSONObject();

                HttpResponse response = client.execute(get);
                Log.w("Sending :", "\nSending 'POST' request to URL : http://92.243.14.22/person/");
                Log.w("Params :", "Post parameters : " + response.getEntity());
                Log.w("Response :", "Response Code : " +
                        response.getStatusLine().getStatusCode());

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                JSONArray jsonArray = new JSONArray(result.toString());

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

    public void deletePerson(int position) {
        new DeleteTask().execute(position);
    }

    private class DeleteTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            String id = ((Person) adapter.getItem(params[0])).getId();

            HttpClient client = new DefaultHttpClient();
            HttpDelete delete = new HttpDelete("http://92.243.14.22/person/" + id);

            // add header
            delete.setHeader("Content-Type", "application/json");
            try {
                Log.w("Sending :", "\nSending 'DELETE' request to URL : http://92.243.14.22/person/" + id);
                HttpResponse response = client.execute(delete);
                Log.w("Response :", "Response Code : " +
                        response.getStatusLine().getStatusCode());
                adapter.person.remove(params[0]);
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

