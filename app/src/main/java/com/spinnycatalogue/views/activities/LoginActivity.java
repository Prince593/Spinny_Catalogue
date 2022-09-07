package com.spinnycatalogue.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spinnycatalogue.R;
import com.spinnycatalogue.databinding.ActivityLoginBinding;
import com.spinnycatalogue.views.base.BaseActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    Context context;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference userDocument;
    ProgressDialog progressDialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        //AddTextWatchers
        addTextWatchers();


        //Click Listeners
        dataBinding.btnCreateAccount.setOnClickListener(v -> startActivity(new Intent(context, CreateAccountActivity.class)));


        dataBinding.btnLogin.setOnClickListener(v -> {
            hideKeyboard(v);
            if (isInternetAvailable()) {
                if (checkData())
                    tryEmailLogin();
            } else {
                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkData() {

        String email = dataBinding.editEmail.getEditableText().toString().trim();
        String password = dataBinding.editPassword.getEditableText().toString().trim();


        if (email.isEmpty()) {

            dataBinding.inputLayoutEmail.setErrorEnabled(true);
            dataBinding.inputLayoutEmail.setError(getString(R.string.error_required));
            dataBinding.editEmail.requestFocus();
        } else if (!isValidEmail(email)) {
            dataBinding.inputLayoutEmail.setErrorEnabled(true);
            dataBinding.inputLayoutEmail.setError(getString(R.string.error_not_valid_email));
            dataBinding.editEmail.requestFocus();

        } else if (password.isEmpty()) {

            dataBinding.inputLayoutPassword.setErrorEnabled(true);
            dataBinding.inputLayoutPassword.setError(getString(R.string.error_required));
            dataBinding.editPassword.requestFocus();
        } else if (password.length() < 8) {
            dataBinding.inputLayoutPassword.setErrorEnabled(true);
            dataBinding.inputLayoutPassword.setError(getString(R.string.password_min_error));
            dataBinding.editPassword.requestFocus();

        } else
            return true;
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            Toast.makeText(context, getString(R.string.already_logged_in, email), Toast.LENGTH_SHORT).show();
            goToMainActivity(context);
            finish();

        }
    }

    /**
     * This method is to add text watchers to the input fields
     */
    private void addTextWatchers() {

        //Email Address
        dataBinding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataBinding.inputLayoutEmail.setErrorEnabled(false);
                dataBinding.editEmail.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Password
        dataBinding.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataBinding.inputLayoutPassword.setErrorEnabled(false);
                dataBinding.editPassword.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * Method for login using email and password
     */
    private void tryEmailLogin() {
        String email = dataBinding.editEmail.getEditableText().toString().trim();
        String password = dataBinding.editPassword.getEditableText().toString().trim();


        setProgressMessage("Logging you in...");
        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hideProgressDialog();
                        mPreferencesManager.setBooleanValue(LOGIN_STATUS, true);
                        Toast.makeText(context, R.string.login_successful, Toast.LENGTH_SHORT).show();
                        AuthResult authResult = task.getResult();
                        if (authResult.getUser() != null) {

                            goToMainActivity(context);
                            finish();

                        } else
                            showToast(context, R.string.error_went_wrong);


                    } else {
                        hideProgressDialog();


                        Exception exception = task.getException();
                        if (exception != null) {
                            String error = ((FirebaseAuthException) exception).getErrorCode();
                            if (error.equalsIgnoreCase("ERROR_WRONG_PASSWORD")) {
                                dataBinding.inputLayoutPassword.setErrorEnabled(true);
                                dataBinding.inputLayoutPassword.setError(getString(R.string.wrong_password));
                            } else if (error.equalsIgnoreCase("ERROR_USER_NOT_FOUND")) {
                                dataBinding.inputLayoutEmail.setErrorEnabled(true);
                                dataBinding.inputLayoutEmail.setError(getString(R.string.account_not_found));
                            }
                        }
                    }
                });

    }


    private void setProgressMessage(String s) {
        if (progressDialog != null)
            progressDialog.setMessage(s);
    }

    private void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.hide();
        }
    }


}
