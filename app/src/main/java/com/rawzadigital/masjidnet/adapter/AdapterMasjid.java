package com.rawzadigital.masjidnet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.model.ModelMasjid;

import java.util.List;

public class AdapterMasjid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModelMasjid> data;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ModelMasjid obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterMasjid(List<ModelMasjid> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_people_chat, viewGroup,
                false);
        return new ViewHolderRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (viewHolder instanceof ViewHolderRecyclerView) {
            ViewHolderRecyclerView view = (ViewHolderRecyclerView) viewHolder;
            Log.e("Laporan Gamar"+i, data.get(i).getImage());
            if (!data.get(i).getImage().equals("")) {
                Glide.with(context).load(data.get(i).getImage())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view.image);
            }
            view.name.setText(data.get(i).getName());
            view.adress.setText(data.get(i).getaddress());
            view.latitu.setText(data.get(i).getlatitude());
            view.lotitu.setText(data.get(i).getlongitude());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, data.get(i), i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class ViewHolderRecyclerView extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        TextView adress,latitu,lotitu;
        public View lyt_parent;

        ViewHolderRecyclerView(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            adress = v.findViewById(R.id.description);
            latitu = v.findViewById(R.id.latitu);
            lotitu = v.findViewById(R.id.lotitu);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }
}
