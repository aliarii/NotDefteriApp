package com.example.notdefteri.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.notdefteri.R;
import com.example.notdefteri.note.NotDetay;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    List<String> basliklar;
    List<String> icerik;
    List<String> sifre;

    public Adapter(List<String> baslik,List<String> icerik,List<String> sifre){
        this.basliklar = baslik;
        this.icerik = icerik;
        this.sifre = sifre;

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, final int position) {

        holder.notBaslik.setText(basliklar.get(position));
        holder.notIcerik.setText(icerik.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NotDetay.class);
                i.putExtra("baslik",basliklar.get(position));
                i.putExtra("icerik",icerik.get(position));
                i.putExtra("sifre",sifre.get(position));

                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return basliklar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notBaslik,notIcerik;
        View view;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notBaslik = itemView.findViewById(R.id.basliklar);
            notIcerik = itemView.findViewById(R.id.icerik);
            mCardView = itemView.findViewById(R.id.noteCard);
            view = itemView;
        }
    }
}
