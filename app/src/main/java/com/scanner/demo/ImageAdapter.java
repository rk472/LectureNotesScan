package com.scanner.demo;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    AppCompatActivity a;
    List<Bitmap> list;

    public ImageAdapter(AppCompatActivity a, List<Bitmap> list) {
        this.a = a;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(a).inflate(R.layout.image_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        holder.setImage(list.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(a)
                        .setMessage("Do you really want to delete?")
                        .setPositiveButton("Yes,Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SingleMainActivity.inst.l.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No,Don't",null)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
