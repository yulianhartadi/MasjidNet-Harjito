package com.rawzadigital.masjidnet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.model.ModelEvent;

import java.util.List;

public class AdapterEvent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModelEvent> data;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private OnBagiClickListener mOnBagiClickListener;

    public AdapterEvent(List<ModelEvent> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
    public void setOnBagiClickListener(final OnBagiClickListener mItemClickListener) {
        this.mOnBagiClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, viewGroup,
                false);
        return new ViewHolderRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof ViewHolderRecyclerView) {
            final ViewHolderRecyclerView view = (ViewHolderRecyclerView) viewHolder;
            Log.e("Laporan Gamar"+i, data.get(i).getbrochure());
            if (!data.get(i).getbrochure().equals("")) {
                Glide.with(context).load(data.get(i).getbrochure())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view.image);
            }
            view.materi.setText(data.get(i).getmateri());
            view.diskripsi.setText(data.get(i).getdeskripsi());
            view.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, data.get(i), i);
                    }
                }
            });
            view.bagikan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnBagiClickListener!=null){
                        mOnBagiClickListener.onItemClick(view, data.get(i), i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ModelEvent obj, int position);
    }
    public interface OnBagiClickListener {
        void onItemClick(View view, ModelEvent obj, int position);
    }

    private class ViewHolderRecyclerView extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView materi;
        TextView diskripsi;
        public View lyt_parent;
        public Button detail;
        public Button bagikan;

        ViewHolderRecyclerView(View v) {
            super(v);
            image = v.findViewById(R.id.imagebrosur);
            materi = v.findViewById(R.id.materi);
            diskripsi = v.findViewById(R.id.diskripsi);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            detail=v.findViewById(R.id.detail);
            bagikan=v.findViewById(R.id.bagikan);
        }
    }

}
