package com.rawzadigital.masjidnet.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.database.ExternalDbOpenHelper;
import com.rawzadigital.masjidnet.database.HisnDatabaseInfo;
import com.rawzadigital.masjidnet.model.Doa;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoaGroupAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Doa> mList;
    private CharSequence mSearchText = "";

    public DoaGroupAdapter(Context context, List<Doa> list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }

    public void setData(List<Doa> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        // return a filter that filters data based on a constraint
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final ExternalDbOpenHelper helper = ExternalDbOpenHelper.getInstance(mContext);
                final SQLiteDatabase db = helper.openDataBase();

                final List<Doa> duas = new ArrayList<>();
                Cursor c = null;
                try {
                    c = db.query(HisnDatabaseInfo.DoaGroupTable.TABLE_NAME, null,
                            HisnDatabaseInfo.DoaGroupTable.ENGLISH_TITLE + " like ?",
                            new String[]{"%" + constraint + "%"}, null, null, null);
                    if (c != null && c.moveToFirst()) {
                        do {
                            final Doa doa = new Doa(c.getInt(0), c.getString(2));
                            duas.add(doa);
                        } while (c.moveToNext());
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }

                final FilterResults results = new FilterResults();
                results.values = duas;
                results.count = duas.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                mSearchText = constraint;
                if (results.count > 0) {
                    mList = (List<Doa>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Doa getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.doa_group_item_card, parent, false);
            mHolder = new ViewHolder();
            mHolder.tvReference = (TextView) convertView.findViewById(R.id.txtReference);
            mHolder.tvDuaName = (TextView) convertView.findViewById(R.id.txtDuaName);
            mHolder.shape = (GradientDrawable) mHolder.tvReference.getBackground();
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        Doa p = getItem(position);
        if (p != null) {
            mHolder.tvReference.setText("" + p.getReference());
            mHolder.tvDuaName.setText(p.getTitle());

            String filter = mSearchText.toString();
            String itemValue = mHolder.tvDuaName.getText().toString();

            int startPos = itemValue.toLowerCase(Locale.US).indexOf(filter.toLowerCase(Locale.US));
            int endPos = startPos + filter.length();

            if (startPos != -1) { // This should always be true, just a sanity check
                Spannable spannable = new SpannableString(itemValue);
                mHolder.tvDuaName.setText(spannable);
            } else {
                mHolder.tvDuaName.setText(itemValue);
            }
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tvDuaName;
        TextView tvReference;
        GradientDrawable shape;
    }
}