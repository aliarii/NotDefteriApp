package com.example.notdefteri;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notdefteri.dogrulama.Giris;
import com.example.notdefteri.dogrulama.HesapDegistir;
import com.example.notdefteri.dogrulama.Kayit;
import com.example.notdefteri.model.Note;
import com.example.notdefteri.note.NotDetay;
import com.example.notdefteri.note.NotEkle;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView nav_view;
    RecyclerView noteLists;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Note,NoteViewHolder> noteAdapter;
    FirebaseUser user;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        Query query = fStore.collection("notlar").document(user.getUid()).collection("notlarim").orderBy("tarih", Query.Direction.DESCENDING);
        //
        final FirestoreRecyclerOptions<Note> allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {

            @Override
            public void onBindViewHolder(@NonNull final NoteViewHolder noteViewHolder, final int i, @NonNull final Note note) {

                if(note.getSifre()!=null)
                {
                    noteViewHolder.notBaslik.setText(note.getBaslik());
                    noteViewHolder.notIcerik.setText("İçerik Şifreli");
                    noteViewHolder.notTarih.setText(note.getTarih());
                }
                else
                {
                    noteViewHolder.notBaslik.setText(note.getBaslik());
                    noteViewHolder.notIcerik.setText(note.getIcerik());
                    noteViewHolder.notTarih.setText(note.getTarih());
                }



                final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(v.getContext(), NotDetay.class);
                        i.putExtra("baslik",note.getBaslik());
                        i.putExtra("icerik",note.getIcerik());
                        i.putExtra("tarih",note.getTarih());
                        i.putExtra("notId",docId);
                        i.putExtra("sifre",note.getSifre());

                        v.getContext().startActivity(i);

                    }
                });
                ImageView menuIcon = noteViewHolder.view.findViewById(R.id.menuIcon);
                menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                        PopupMenu menu = new PopupMenu(v.getContext(),v);
                        menu.setGravity(Gravity.END);
                        menu.getMenu().add("Sil").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if(noteViewHolder.notIcerik.getText().toString()=="İçerik Şifreli")
                                {
                                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this).setTitle("Sifre Gir").setCancelable(false);
                                    final EditText input = new EditText(MainActivity.this);
                                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    alert.setView(input);
                                    alert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String sifreKontrol = input.getText().toString().trim();
                                            if(!sifreKontrol.equals(note.getSifre()))
                                            {
                                                Toast.makeText(MainActivity.this, "Yanlış Şifre.", Toast.LENGTH_SHORT).show();

                                            }
                                            else if (sifreKontrol.equals(note.getSifre()))
                                            {
                                                DocumentReference docRef = fStore.collection("notlar").document(user.getUid()).collection("notlarim").document(docId);
                                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity.this, "Not Silindi.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(MainActivity.this, "Not silinirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
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
                                else
                                {
                                    DocumentReference docRef = fStore.collection("notlar").document(user.getUid()).collection("notlarim").document(docId);
                                    docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MainActivity.this, "Not Silindi.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "Not silinirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                return false;
                            }
                        });
                        menu.show();

                    }
                });

            }
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };
        noteLists = findViewById(R.id.notelist);
        drawerLayout = findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        noteLists.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(noteAdapter);

        View headerView = nav_view.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.kullaniciEkranIsim);
        TextView userEmail = headerView.findViewById(R.id.kullaniciEkranEmail);

        if(user.isAnonymous()){
            userEmail.setVisibility(View.GONE);
            username.setText("Geçici Kullanıcı");
        }else {
            userEmail.setText(user.getEmail());
            username.setText(user.getDisplayName());
        }
        FloatingActionButton fab = findViewById(R.id.addNoteFloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotEkle.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);

            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.notEkle:
                startActivity(new Intent(this, NotEkle.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;

            case R.id.baglan:
                if(user.isAnonymous()){
                    startActivity(new Intent(this, Giris.class));
                    overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                }else {
                    Toast.makeText(this, "Hesabınız Zaten Bağlı.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.degistir:
                if(user.isAnonymous()){
                    Toast.makeText(this, "Önce Giriş Yapmalısınız.", Toast.LENGTH_SHORT).show();
                }else {

                    startActivity(new Intent(this, HesapDegistir.class));
                    overridePendingTransition(R.anim.slide_up,R.anim.slide_down);

                }
                break;
            case R.id.cikis:
                checkUser();
                break;
                default:
                Toast.makeText(this, "Yakında.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private void checkUser() {
        // if user is real or not
        if(user.isAnonymous()){
            displayAlert();
        }else {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Splash.class));
            finish();
            overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        }
    }
    private void displayAlert() {
        AlertDialog.Builder warning = new AlertDialog.Builder(this)
                .setTitle("UYARI !!!")
                .setMessage("Geçici bir hesap kullanıyorsunuz. Hesaptan çıkış yapmak bütün notlarınızın silinmesine yol açar. " +
                        "Yeni bir hesap açarak var olan notlarınızı saklayabilirsiniz.")
                .setPositiveButton("Hesap Oluştur", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),Kayit.class));
                        finish();
                    }
                }).setNegativeButton("Sil ve Çık", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ToDO: delete all the notes created by the Anon user
                        // TODO: delete the anon user
                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),Splash.class));
                                finish();
                                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                            }
                        });
                    }
                });
        warning.show();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Toast.makeText(this, "Ayarlar Tıklandı.", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.noteAdd)
        {
            startActivity(new Intent(getApplicationContext(), NotEkle.class));
            overridePendingTransition(R.anim.slide_up,R.anim.slide_down);

        }
        return super.onOptionsItemSelected(item);
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView notBaslik,notIcerik,notTarih;
        View view;
        CardView mCardView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notBaslik = itemView.findViewById(R.id.basliklar);
            notIcerik = itemView.findViewById(R.id.icerik);
            notTarih = itemView.findViewById(R.id.tarih);
            mCardView = itemView.findViewById(R.id.noteCard);
            view = itemView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Çıkış Yapmak istiyor musunuz?");
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Evet'e basılınca yapılacak işlemleri yazınız
                finish();


            }
        });
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                // Hayır'a baslınca yapılacak işmeleri yazınız
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}