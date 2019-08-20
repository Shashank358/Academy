package com.iiysoftware.academy;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private TextView loginText, jumpToReg;
    private EditText userEmail, userPass;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginText = findViewById(R.id.login_user_text);
        jumpToReg = findViewById(R.id.jump_to_registration);
        userEmail = findViewById(R.id.user_email_input_login);
        userPass = findViewById(R.id.user_pass_login);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount(v);
            }
        });

        jumpToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToRegistration(v);
            }
        });
    }

    private void loginAccount(View v) {

        String email = userEmail.getText().toString();
        String password = userPass.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            dialog.setMessage("Logging user");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            proceedToLogin(email, password);
        }else {
            Snackbar.make(findViewById(R.id.login_container), "Please fill all the above fields", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void jumpToRegistration(View v) {

        Intent regIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
        regIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(regIntent);

    }

    private void proceedToLogin(String email, String password) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }else {
                    dialog.hide();
                    Snackbar.make(findViewById(R.id.login_container), "Error please try again", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.hide();
                Snackbar.make(findViewById(R.id.login_container), "Please check your internet and try again", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

    }

}
