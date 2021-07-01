package com.example.notdefteri.note;

import android.content.DialogInterface;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class addNote extends AppCompatActivity {
    String noteDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

    FirebaseFirestore fStore;
    EditText noteTitle, noteContent;
    String notePassword;
    ProgressBar progressBarSave;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = FirebaseFirestore.getInstance();
        noteContent = findViewById(R.id.noteAddContent);
        noteTitle = findViewById(R.id.noteAddTitle);
        progressBarSave = findViewById(R.id.progressBar);

        user = FirebaseAuth.getInstance().getCurrentUser();

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
            //ana ekrana dön
            onBackPressed();
        }
        else if(item.getItemId() == R.id.addPassword){

            final AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Sifre Gir");
            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    notePassword = input.getText().toString().trim();

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
            //ana ekrana dön
            onBackPressed();

        }
        else if(item.getItemId()==R.id.save)
        {
            String nTitle = noteTitle.getText().toString();
            String nContent = noteContent.getText().toString();
            String nPassword = notePassword;
            String nDate = noteDate;
            if(nTitle.isEmpty()){
                nTitle ="Başlıksız";
            }
            if(nContent.isEmpty()){
                nContent ="Boş";
            }

            progressBarSave.setVisibility(View.VISIBLE);

            // save note

            DocumentReference docref = fStore.collection("notlar").document(user.getUid()).collection("notlarim").document();
            Map<String,Object> note = new HashMap<>();
            note.put("baslik", nTitle);
            note.put("icerik", nContent);
            note.put("sifre", nPassword);
            note.put("tarih", nDate);
            docref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(addNote.this, "Not Eklendi.", Toast.LENGTH_SHORT).show();
                    //ana ekrana dön
                    onBackPressed();
                    //finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addNote.this, "Hata, Tekrar Deneyin.", Toast.LENGTH_SHORT).show();
                    progressBarSave.setVisibility(View.VISIBLE);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

}
