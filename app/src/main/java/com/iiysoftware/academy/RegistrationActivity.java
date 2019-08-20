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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userMobile, userEmail, userPassword;
    private TextView registerText, jumpToLoginText;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userName = findViewById(R.id.user_name_input);
        userMobile = findViewById(R.id.user_mobile_no_input);
        userEmail = findViewById(R.id.user_email_input_registration);
        userPassword = findViewById(R.id.user_pass_registration);
        registerText = findViewById(R.id.register_user_text);
        jumpToLoginText = findViewById(R.id.jump_to_login);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
            }
        });

        jumpToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToLogin(v);
            }
        });


    }

    private void registerUser(View v) {

        String name = userName.getText().toString();
        String phone = userMobile.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            dialog.setMessage("Registering user");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            proceedToRegistration(name, phone, email, password);
        }else {
            Snackbar.make(findViewById(R.id.reg_container), "Please fill all the above fields", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void jumpToLogin(View v) {
        Intent regIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
        regIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(regIntent);

    }

    private void proceedToRegistration(final String name, final String phone, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    currentUserId = mAuth.getCurrentUser().getUid();
                    HashMap<String, Object> regMap = new HashMap<>();
                    regMap.put("user_name", name);
                    regMap.put("user_phone", phone);
                    regMap.put("user_email", email);
                    regMap.put("user_uid", currentUserId);
                    regMap.put("user_thumbImage", "default");
                    regMap.put("user_image", "default");

                    db.collection("Administrator").document(currentUserId).set(regMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        Intent mainIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(mainIntent);
                                    }else {
                                        dialog.hide();
                                        Snackbar.make(findViewById(R.id.reg_container), "Error please try again", Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.hide();
                Snackbar.make(findViewById(R.id.reg_container), "Please check your internet and try again", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
