package com.example.notdefteri.dogrulama;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notdefteri.MainActivity;
import com.example.notdefteri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HesapDegistir extends AppCompatActivity {
    EditText otherlEmail,otherlSifre;
    Button otherloginNow;

    FirebaseAuth otherfAuth;
    FirebaseFirestore otherfStore;
    FirebaseUser otheruser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hesap Değiştir");

        otherlEmail = findViewById(R.id.otherEmail);
        otherlSifre = findViewById(R.id.otherlSifre);
        otherloginNow = findViewById(R.id.otherGirisBtn);
        otheruser = FirebaseAuth.getInstance().getCurrentUser();
        otherfAuth = FirebaseAuth.getInstance();
        otherfStore = FirebaseFirestore.getInstance();


        otherloginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = otherlEmail.getText().toString();
                String mPassword = otherlSifre.getText().toString();

                if(mEmail.isEmpty() || mPassword.isEmpty()){
                    Toast.makeText(HesapDegistir.this, "Tüm Alanları Doldurun.", Toast.LENGTH_SHORT).show();
                    return;
                }



                otherfAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        // notları sil
                        if(otherfAuth.getCurrentUser().isAnonymous()){
                            FirebaseUser user = otherfAuth.getCurrentUser();

                            otherfStore.collection("notlar").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(HesapDegistir.this, "Kayıtsız Notlar Silindi.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // geçici kullanıcı sil
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(HesapDegistir.this, "Kayıtsız Üye Silindi.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Toast.makeText(HesapDegistir.this, "Giriş Başarılı !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HesapDegistir.this, "Giriş Başarısız. " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
