package com.example.notdefteri.dogrulama;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.example.notdefteri.MainActivity;
import com.example.notdefteri.R;

public class Kayit extends AppCompatActivity {
    EditText rkullaniciIsim,rkullaniciEmail,rsifre,rsifreDogrula;
    Button hesapOlustur;
    TextView girisYap;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Not Defterine Kayıt Olun");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rkullaniciIsim = findViewById(R.id.kullaniciIsim);
        rkullaniciEmail = findViewById(R.id.kullaniciEmail);
        rsifre = findViewById(R.id.sifre);
        rsifreDogrula = findViewById(R.id.sifreDogrula);
        hesapOlustur = findViewById(R.id.hesapOlustur);
        girisYap = findViewById(R.id.giris);
        progressBar = findViewById(R.id.progressBar4);
        fAuth = FirebaseAuth.getInstance();
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Giris.class));
            }
        });
        hesapOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String uUsername = rkullaniciIsim.getText().toString();
                String uUserEmail = rkullaniciEmail.getText().toString();
                String uUserPass = rsifre.getText().toString();
                String uConfPass = rsifreDogrula.getText().toString();
                if(uUserEmail.isEmpty() || uUsername.isEmpty() || uUserPass.isEmpty() || uConfPass.isEmpty()){
                    Toast.makeText(Kayit.this, "Tüm Alanları Doldurun.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!uUserPass.equals(uConfPass)){
                    rsifreDogrula.setError("Şifre Yanlış.");
                }
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential = EmailAuthProvider.getCredential(uUserEmail,uUserPass);
                fAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Kayit.this, "Notlar Senkronize Edildi.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        FirebaseUser usr = fAuth.getCurrentUser();
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uUsername)
                                .build();
                        usr.updateProfile(request);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Kayit.this, "Bağlanılamadı, Tekrar Deneyin.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
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
