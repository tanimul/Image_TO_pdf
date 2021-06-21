package com.example.imagetopdf.Adapter;

import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagetopdf.R;

import java.util.ArrayList;

public class AdapterImageList extends RecyclerView.Adapter<AdapterImageList.Imageviewholder> {
    private static final String TAG = "AdapterImageList";
    private ArrayList<Uri> imagelist;

    public AdapterImageList(ArrayList<Uri> imagelist) {
        this.imagelist = imagelist;
    }

    @NonNull
    @Override
    public Imageviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_image, parent, false);

        return new Imageviewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterImageList.Imageviewholder holder, int position) {
        holder.imageview_choose.setImageURI(imagelist.get(position));
        holder.textView_sl.setText("Sl." + (position + 1));
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public class Imageviewholder extends RecyclerView.ViewHolder {
        private ImageView imageview_choose;
        private TextView textView_sl;

        public Imageviewholder(@NonNull View itemView) {
            super(itemView);
            imageview_choose = itemView.findViewById(R.id.layout_image);
            textView_sl = itemView.findViewById(R.id.layout_sl);
        }
    }
}
