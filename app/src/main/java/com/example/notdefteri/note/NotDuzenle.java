package com.example.notdefteri.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notdefteri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotDuzenle extends AppCompatActivity {
    Intent data;
    String gelenSifre;
    EditText notBaslikDuzenle,notIcerikDuzenle;
    String noteTitle,noteContent,notSifre;
    FirebaseFirestore fStore;
    ProgressBar spinner;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = fStore.getInstance();
        spinner = findViewById(R.id.progressBar2);
        user = FirebaseAuth.getInstance().getCurrentUser();

        data = getIntent();

        notIcerikDuzenle = findViewById(R.id.notIcerikDuzenle);
        notBaslikDuzenle = findViewById(R.id.notBaslikDuzenle);


        noteTitle = data.getStringExtra("baslik");
        noteContent = data.getStringExtra("icerik");
        notBaslikDuzenle.setText(noteTitle);
        notIcerikDuzenle.setText(noteContent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Toast.makeText(this,"İptal Edildi.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else if(item.getItemId() == R.id.passwordEkle){

            final AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Sifre Gir");
            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    notSifre = input.getText().toString().trim();

                }
            });
            alert.setNeutralButton("Şifreyi Kaldır",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton) {
                    notSifre = null;

                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            alert.show();

        }
        else if(item.getItemId() == R.id.close){

            Toast.makeText(this,"İptal Edildi.", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }
        else if(item.getItemId()==R.id.save)
        {
            String nTitle = notBaslikDuzenle.getText().toString();
            String nContent = notIcerikDuzenle.getText().toString();
            String nSifre = notSifre;

            if(nTitle.isEmpty() || nContent.isEmpty()){
                Toast.makeText(NotDuzenle.this, "Boş Alanlarla Kayıt Edilemez.", Toast.LENGTH_SHORT).show();
            }
            else{
                spinner.setVisibility(View.VISIBLE);

                // save note

                DocumentReference docref = fStore.collection("notlar").document(user.getUid()).collection("notlarim").document(data.getStringExtra("notId"));

                Map<String,Object> note = new HashMap<>();
                note.put("baslik",nTitle);
                note.put("icerik",nContent);
                note.put("sifre",nSifre);
                docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NotDuzenle.this, "Not Kaydedildi.", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NotDuzenle.this, "Hata, Tekrar Deneyin.", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.VISIBLE);
                    }
                });
            }


        }
        return super.onOptionsItemSelected(item);
    }

}
