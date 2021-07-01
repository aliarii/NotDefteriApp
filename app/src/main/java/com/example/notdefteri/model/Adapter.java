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
import com.example.notdefteri.note.noteDetail;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    List<String> titles;
    List<String> content;
    List<String> password;

    public Adapter(List<String> title, List<String> content, List<String> password){
        this.titles = title;
        this.content = content;
        this.password = password;

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, final int position) {

        holder.noteTitle.setText(titles.get(position));
        holder.noteContent.setText(content.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), noteDetail.class);
                i.putExtra("baslik", titles.get(position));
                i.putExtra("icerik", content.get(position));
                i.putExtra("sifre", password.get(position));

                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mCardView = itemView.findViewById(R.id.noteCard);
            view = itemView;
        }
    }
}
