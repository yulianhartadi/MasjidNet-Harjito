package com.rawzadigital.masjidnet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.internal.act;
import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.model.Image;
import com.rawzadigital.masjidnet.model.Slidder;
import com.rawzadigital.masjidnet.utils.Tools;

import java.util.List;

public class AdapterSlidder extends PagerAdapter {
    private List<Slidder> data;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public int getCount() {
        return data.size();
    }

    public void setItems(List<Slidder> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final Slidder o = data.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_slider_image, container, false);

        ImageView image = v.findViewById(R.id.image);
        MaterialRippleLayout lyt_parent = v.findViewById(R.id.lyt_parent);

        lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v,data.get(position), position);
                }
            }
        });

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

    }

    public interface OnItemClickListener {
        void onItemClick(View view, Slidder obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
}
