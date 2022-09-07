package com.spinnycatalogue.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spinnycatalogue.R;
import com.spinnycatalogue.data.local.User;
import com.spinnycatalogue.databinding.ActivityCreateAccountBinding;
import com.spinnycatalogue.views.base.BaseActivity;

import java.util.Date;

public class CreateAccountActivity extends BaseActivity<ActivityCreateAccountBinding> {
    Context context;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_account;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        //Text Watchers
        addTextWatchers();

        dataBinding.btnLogin.setOnClickListener(v -> {
            hideKeyboard(v);
            finish();
        });


        dataBinding.btnCreateAccount.setOnClickListener(v -> {
            hideKeyboard(v);
            if (isInternetAvailable()) {
                if (checkData()) {
                    continueRegister();
                }
            } else {
                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addTextWatchers() {

        //Name
        dataBinding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataBinding.inputLayoutName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Email
        dataBinding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataBinding.inputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dataBinding.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataBinding.inputLayoutPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkData() {
        String name = dataBinding.editName.getEditableText().toString().trim();
        String email = dataBinding.editEmail.getEditableText().toString().trim();
        String password = dataBinding.editPassword.getEditableText().toString().trim();


        if (name.isEmpty()) {
            dataBinding.inputLayoutName.setErrorEnabled(true);
            dataBinding.inputLayoutName.setError(getString(R.string.error_required));
            dataBinding.editName.requestFocus();
        } else if (email.isEmpty()) {

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

    private void continueRegister() {
        String name = dataBinding.editName.getEditableText().toString().trim();
        String email = dataBinding.editEmail.getEditableText().toString().trim();
        String password = dataBinding.editPassword.getEditableText().toString().trim();

        setProgressMessage("Creating your account");
        showProgressDialog();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        hideProgressDialog();
                        mPreferencesManager.setBooleanValue(LOGIN_STATUS, true);
                        Toast.makeText(context, R.string.login_successful, Toast.LENGTH_SHORT).show();
                        AuthResult authResult = task.getResult();
                        if (authResult.getUser() != null) {
                            authResult.getUser().sendEmailVerification();
                            updateUserProfile(authResult, name);
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

    private void updateUserProfile(AuthResult authResult, String name) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        if (authResult.getUser() != null) {
            authResult.getUser().updateProfile(userProfileChangeRequest)
                    .addOnSuccessListener(unused -> addUserData(authResult))
                    .addOnFailureListener(e -> addUserData(authResult));
        } else {
            hideProgressDialog();
            somethingWentWrong();
        }


    }

    private void addUserData(AuthResult authResult) {
        String name = dataBinding.editName.getEditableText().toString().trim();
        String email = dataBinding.editEmail.getEditableText().toString().trim();

        User user = new User();
        user.setUid(authResult.getUser().getUid());
        user.setCreated_time(new Timestamp(new Date()));
        user.setName(name);
        user.setEmail(email);

        CollectionReference usersCollection = firebaseFirestore.collection(COLLECTION_USERS);

        usersCollection.document(user.getUid())
                .set(user)
                .addOnSuccessListener(unused -> {
                    goToMainActivity(context);
                    finish();
                })
                .addOnFailureListener(e -> {
                    hideProgressDialog();
                    somethingWentWrong();
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
