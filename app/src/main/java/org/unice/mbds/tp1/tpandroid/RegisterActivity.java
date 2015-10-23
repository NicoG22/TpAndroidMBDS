package org.unice.mbds.tp1.tpandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

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
        if (TextUtils.isEmpty(phone)|| !TextUtils.isDigitsOnly(phone) || !isPhoneValid(phone)) {
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
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        }
    }
}
