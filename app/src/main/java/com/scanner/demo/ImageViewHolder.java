package com.scanner.demo;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private ImageView img;
    ImageButton delete;
    public ImageViewHolder(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.row_image);
        delete=itemView.findViewById(R.id.delete_image);
    }
    public void setImage(Bitmap bitmap){
        img.setImageBitmap(bitmap);
    }
}
