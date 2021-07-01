package com.example.notdefteri.dogrulama;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.notdefteri.MainActivity;
import com.example.notdefteri.R;

public class loginPage extends AppCompatActivity {
    EditText lEmail, lPassword;
    Button loginNow;
    TextView forgotPassword, createAccount;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Not Defterine Giriş");

        lEmail = findViewById(R.id.email);
        lPassword = findViewById(R.id.lPassword);
        loginNow = findViewById(R.id.loginBtn);

        forgotPassword = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.createAccount);
        user = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        showWarning();

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = lEmail.getText().toString();
                String mPassword = lPassword.getText().toString();
                if(mEmail.isEmpty() || mPassword.isEmpty()){
                    Toast.makeText(loginPage.this, "Tüm Alanları Doldurun.", Toast.LENGTH_SHORT).show();
                    return;
                }

                fAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // kayıtsız notları sil

                        if(fAuth.getCurrentUser().isAnonymous()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            fStore.collection("notlar").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(loginPage.this, "Kayıtsız Notlar Silindi.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // geçici kullanıcı sil
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(loginPage.this, "Kayıtsız Üye Silindi.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        Toast.makeText(loginPage.this, "Giriş Başarılı !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginPage.this, "Giriş Başarısız. " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registerPage.class));
            }
        });
    }

    private void showWarning() {
        final AlertDialog.Builder uyari = new AlertDialog.Builder(this)
                .setTitle("UYARI !!!")
                .setMessage("Var olan bir hesabı bağlamak kayıtsız notların silinmesine yol açar. Var olan notları kaydetmek için yeni hesap oluştur.")
                .setPositiveButton("Hesap Oluştur", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), registerPage.class));
                        finish();
                    }
                }).setNegativeButton("Sil ve Giriş Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        uyari.show();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
