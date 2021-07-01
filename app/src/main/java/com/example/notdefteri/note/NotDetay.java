package com.example.notdefteri.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notdefteri.R;

public class NotDetay extends AppCompatActivity {
    Intent veri;
    String gelenSifre;
    TextView content,title,tarih;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        veri = getIntent();

        content = findViewById(R.id.notIcerikDetay);
        title = findViewById(R.id.notDetayBaslik);
        tarih = findViewById(R.id.notIcerikDetayTarih);
        content.setMovementMethod(new ScrollingMovementMethod());
        gelenSifre=veri.getStringExtra("sifre");
        if (gelenSifre!=null)
        {
            passwordAlert();
        }
        else
        {
            tarih.setText(veri.getStringExtra("tarih"));
            content.setText(veri.getStringExtra("icerik"));
            title.setText(veri.getStringExtra("baslik"));
        }


    }
    public void passwordAlert(){

        final AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Sifre Gir").setCancelable(false);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);
        alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String sifreKontrol = input.getText().toString().trim();
                if(!sifreKontrol.equals(gelenSifre))
                {
                    Toast.makeText(NotDetay.this, "Yanlış Şifre.", Toast.LENGTH_SHORT).show();
                    passwordAlert();
                }
                if (sifreKontrol.equals(gelenSifre))
                {
                    tarih.setText(veri.getStringExtra("tarih"));
                    content.setText(veri.getStringExtra("icerik"));
                    title.setText(veri.getStringExtra("baslik"));
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                onBackPressed();
            }
        });
        alert.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        else if(item.getItemId()== R.id.edit)
        {
            Intent i = new Intent(getApplicationContext(),NotDuzenle.class);
            i.putExtra("baslik",veri.getStringExtra("baslik"));
            i.putExtra("icerik",veri.getStringExtra("icerik"));
            i.putExtra("notId",veri.getStringExtra("notId"));
            startActivity(i);
            finish();

        }
        else if (item.getItemId() == R.id.close)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
