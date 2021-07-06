package com.example.imagetopdf.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagetopdf.Interface.ImageOnClickListener;
import com.example.imagetopdf.R;

import java.util.ArrayList;

public class AdapterImageList extends RecyclerView.Adapter<AdapterImageList.Imageviewholder> {
    private static final String TAG = "AdapterImageList";
    private ArrayList<Uri> imagelist;
    private ImageOnClickListener imageOnClickListener;
    private Context context;

    public AdapterImageList(Context context, ArrayList<Uri> imagelist, ImageOnClickListener imageOnClickListener) {
        this.context = context;
        this.imagelist = imagelist;
        this.imageOnClickListener = imageOnClickListener;
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
        if (position > 9) {
            holder.textView_sl.setText("" + (position + 1));
        } else {
            holder.textView_sl.setText("0" + (position + 1));
        }

        holder.imagebutton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOnClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public class Imageviewholder extends RecyclerView.ViewHolder {
        private ImageView imageview_choose;
        private TextView textView_sl;
        private ImageButton imagebutton_delete;

        public Imageviewholder(@NonNull View itemView) {
            super(itemView);
            imageview_choose = itemView.findViewById(R.id.layout_image);
            textView_sl = itemView.findViewById(R.id.textview_layout_sl);
            imagebutton_delete=itemView.findViewById(R.id.imagebutton_layout_trash);
        }
    }
}
