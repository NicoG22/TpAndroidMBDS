package org.unice.mbds.tp1.tpandroid.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.unice.mbds.tp1.tpandroid.R;
import org.unice.mbds.tp1.tpandroid.utils.ApiCallService;
import org.unice.mbds.tp1.tpandroid.utils.ApiUrlService;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText editPrenom;
    EditText editNom;
    RadioGroup groupGenre;
    EditText editMail;
    EditText editPhone;
    EditText editPass;
    EditText editConfirmPass;
    ProgressDialog progressDialog;

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
            if (v.getId() == R.id.button_register) {

                try {
                    String sexe = "";
                    if (Integer.toString(groupGenre.getCheckedRadioButtonId()).equals("2131492978")) {
                        sexe = "M";
                    } else {
                        sexe = "F";
                    }
                    String[] params = new String[]{prenom, nom, sexe, phone, email, password};
                    new RegisterTask().execute(params);
                } catch (Exception e) {
                    Log.w("Erreur register", e.getMessage());
                }

            }
        }
    }

    public void setProgressDialog() {
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Patientez...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            try {

                HashMap<String, Object> postParams = new HashMap<>();
                postParams.put("nom", params[1]);
                postParams.put("prenom", params[0]);
                postParams.put("sexe", params[2]);
                postParams.put("telephone", params[3]);
                postParams.put("email", params[4]);
                postParams.put("createdby", params[0]);
                postParams.put("password", params[5]);

                return ApiCallService.getInstance()
                        .doPost(RegisterActivity.this, progressDialog, ApiUrlService.personURL, postParams).getResult();

            } catch (Exception e) {
                Log.w("Erreur création : ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String theResponse) {
            super.onPostExecute(theResponse);

            Toast.makeText(RegisterActivity.this, "Inscription ok", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
            startActivity(intent);
        }
    }
}
