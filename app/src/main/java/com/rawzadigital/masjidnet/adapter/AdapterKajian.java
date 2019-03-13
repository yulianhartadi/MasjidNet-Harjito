package com.rawzadigital.masjidnet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.model.Kajian;

import java.util.List;

public class AdapterKajian extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Kajian> kajians;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public AdapterKajian(List<Kajian> kajians, Context context) {
        this.kajians = kajians;
        this.context = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kajian, viewGroup, false);
        return new ViewHolderRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (viewHolder instanceof ViewHolderRecyclerView) {
            final ViewHolderRecyclerView view = (ViewHolderRecyclerView) viewHolder;
            view.judul.setText(kajians.get(i).getjudul());
            view.materi.setText(kajians.get(i).getmateri());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, kajians.get(i), i);
                    }
                }
            });
            Log.e("Laporan Kajian Adapter", kajians.get(i).getjudul()+" "+kajians.get(i).getmateri());
        }
    }

    @Override
    public int getItemCount() {
        return kajians.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Kajian kajian, int i);
    }

    private class ViewHolderRecyclerView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        TextView judul, materi;

        ViewHolderRecyclerView( View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            materi = itemView.findViewById(R.id.materi);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);
        }
    }
}
