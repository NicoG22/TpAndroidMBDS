package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.unice.mbds.tp1.tpandroid.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RegisterActivity extends AppCompatActivity {

    EditText editPrenom;
    EditText editNom;
    RadioGroup groupGenre;
    EditText editMail;
    EditText editPhone;
    EditText editPass;
    EditText editConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editConfirmPass = (EditText) findViewById(R.id.edit_text_register_password_confirm);
        editMail = (EditText) findViewById(R.id.edit_text_register_email);
        editNom = (EditText) findViewById(R.id.edit_text_register_nom);
        editPrenom = (EditText) findViewById(R.id.edit_text_register_prenom);
        editPass = (EditText) findViewById(R.id.edit_text_register_password);
        editPhone = (EditText) findViewById(R.id.edit_text_register_phone);
        groupGenre = (RadioGroup) findViewById(R.id.radio_grp_register);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() == 10;
    }

    private boolean isAlpha(String param) {
        return param.matches("[a-zA-Z]+");
    }

    public void attemptSignup(View v) {

        boolean cancel = false;
        View focusView = null;

        String email = editMail.getText().toString();
        String nom = editNom.getText().toString();
        String prenom = editPrenom.getText().toString();
        String phone = editPhone.getText().toString();
        String password = editPass.getText().toString();
        String confirmPassword = editConfirmPass.getText().toString();

        // Check for a valid confirm password
        if (TextUtils.isEmpty(confirmPassword) || !confirmPassword.equals(password)) {
            editConfirmPass.setError(getString(R.string.error_invalid_confirm_password));
            focusView = editConfirmPass;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            editPass.setError(getString(R.string.error_invalid_password));
            focusView = editPass;
            cancel = true;
        }

        // Check for a valid email
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            editMail.setError(getString(R.string.error_invalid_email));
            focusView = editMail;
            cancel = true;
        }

        // Check for a valid phone
        if (TextUtils.isEmpty(phone) || !TextUtils.isDigitsOnly(phone) || !isPhoneValid(phone)) {
            editPhone.setError(getString(R.string.error_invalid_phone));
            focusView = editPhone;
            cancel = true;
        }

        // Check for a valid surname
        if (TextUtils.isEmpty(prenom) || !isAlpha(prenom)) {
            editPrenom.setError(getString(R.string.error_invalid_empty));
            focusView = editPrenom;
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(nom) || !isAlpha(nom)) {
            editNom.setError(getString(R.string.error_invalid_empty));
            focusView = editNom;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if(v.getId()==R.id.button_register) {

                try {
                    String sexe = "";
                    if(Integer.toString(groupGenre.getCheckedRadioButtonId()).equals("2131492978")) {
                        sexe = "M";
                    } else {
                        sexe = "F";
                    }
                    String[] params = new String[]{prenom, nom, sexe, phone, email, password};
                    new MyTask().execute(params);
                } catch (Exception e) {
                    Log.w("Erreur register", e.getMessage());
                }

            }
        }
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        public void showProgressDialog(boolean isVisible) {
            if (isVisible) {
                if(progressDialog==null) {
                    progressDialog = new ProgressDialog(RegisterActivity.this);
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
            }
            else {
                if(progressDialog!=null) {
                    progressDialog.dismiss();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://92.243.14.22/person/");

                // add header
                post.setHeader("Content-Type", "application/json");
                JSONObject obj = new JSONObject();
                obj.put("nom", params[1]);
                obj.put("prenom", params[0]);
                obj.put("sexe", params[2]);
                obj.put("telephone", params[3]);
                obj.put("email", params[4]);
                obj.put("createdby", params[0]);
                obj.put("password", params[5]);

                StringEntity entity = new StringEntity(obj.toString());
                post.setEntity(entity);

                HttpResponse response = client.execute(post);
                Log.w("Sending :", "\nSending 'POST' request to URL : http://92.243.14.22/person/");
                Log.w("Params :", "Post parameters : " + post.getEntity());
                Log.w("Response :", "Response Code : " +
                        response.getStatusLine().getStatusCode());

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                System.out.println(result.toString());
                return result.toString();
            } catch (Exception e) {
                Log.w("Erreur création : ", e.getMessage());
                return "An error occured....";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(String theResponse) {
            super.onPostExecute(theResponse);
            showProgressDialog(false);
            Toast.makeText(RegisterActivity.this, "Inscription ok", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
            startActivity(intent);
        }
    }
}
