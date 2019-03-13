package com.rawzadigital.masjidnet.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.database.ExternalDbOpenHelper;
import com.rawzadigital.masjidnet.database.HisnDatabaseInfo;
import com.rawzadigital.masjidnet.model.Doa;
import com.mikepenz.iconics.view.IconicsButton;


import java.util.List;

public class DoaDetailAdapter extends BaseAdapter {
    private static Typeface sCachedTypeface = null;

    private List<Doa> mList;
    private LayoutInflater mInflater;

    private final float prefArabicFontSize;
    private final float prefOtherFontSize;
    private final String prefArabicFontTypeface;

    ExternalDbOpenHelper mDbHelper;

    private String myToolbarTitle;

    public DoaDetailAdapter(Context context, List<Doa> items, String toolbarTitle) {
        mInflater = LayoutInflater.from(context);
        mList = items;

        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        prefArabicFontTypeface =
                sharedPreferences.getString(
                        context.getResources().getString(R.string.pref_font_arabic_typeface),
                        context.getString(R.string.pref_font_arabic_typeface_default));
        prefArabicFontSize =
                sharedPreferences.getInt(
                        context.getResources().getString(R.string.pref_font_arabic_size),
                        context.getResources().getInteger(R.integer.pref_font_arabic_size_default));
        prefOtherFontSize =
                sharedPreferences.getInt(
                        context.getResources().getString(R.string.pref_font_other_size),
                        context.getResources().getInteger(R.integer.pref_font_other_size_default));

        if (sCachedTypeface == null) {
            sCachedTypeface = Typeface.createFromAsset(
                    context.getAssets(), prefArabicFontTypeface);
        }

        myToolbarTitle = toolbarTitle;
    }

    public void setData(List<Doa> items) {
        mList = items;
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder mHolder;
        final Doa p = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.doa_detail_item_card, parent, false);

            mHolder = new ViewHolder();
            mHolder.tvDuaNumber = (TextView) convertView.findViewById(R.id.txtDuaNumber);

            mHolder.tvDuaArabic = (TextView) convertView.findViewById(R.id.txtDuaArabic);
            mHolder.tvDuaArabic.setTypeface(sCachedTypeface);
            mHolder.tvDuaArabic.setTextSize(prefArabicFontSize);

            mHolder.tvDuaTranslation = (TextView) convertView.findViewById(R.id.txtDuaTranslation);
            mHolder.tvDuaTranslation.setTextSize(prefOtherFontSize);

            mHolder.tvDuaReference = (TextView) convertView.findViewById(R.id.txtDuaReference);
            mHolder.tvDuaReference.setTextSize(prefOtherFontSize);

            mHolder.shareButton = (IconicsButton) convertView.findViewById(R.id.button_share);
            mHolder.favButton = (IconicsButton) convertView.findViewById(R.id.button_star);

            final ViewHolder finalHolder = mHolder;
            mHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View convertView) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,
                            myToolbarTitle + "\n\n" +
                                    finalHolder.tvDuaArabic.getText() + "\n\n" +
                                    finalHolder.tvDuaTranslation.getText() + "\n\n" +
                                    finalHolder.tvDuaReference.getText() + "\n\n" +
                                    convertView.getResources().getString(R.string.action_share_credit)
                    );
                    intent.setType("text/plain");
                    convertView.getContext().startActivity(
                            Intent.createChooser(
                                    intent,
                                    convertView.getResources().getString(R.string.action_share_title)
                            )
                    );
                }
            });

            final View finalConvertView = convertView;
            mHolder.favButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View ConvertView) {
                    boolean isFav = !p.getFav();

                    // Following snippet taken from:
                    // http://developer.android.com/training/basics/data-storage/databases.html#UpdateDbRow
                    mDbHelper = new ExternalDbOpenHelper(finalConvertView.getContext().getApplicationContext());

                    SQLiteDatabase db = mDbHelper.getReadableDatabase();

                    // New value for one column
                    ContentValues values = new ContentValues();
                    values.put(HisnDatabaseInfo.DoaTable.FAV, isFav);

                    // Which row to update, based on the ID
                    String selection = HisnDatabaseInfo.DoaTable.DUA_ID + " LIKE ?";
                    String[] selectionArgs = {String.valueOf(finalHolder.tvDuaNumber.getText().toString())};

                    int count = db.update(
                            HisnDatabaseInfo.DoaTable.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);

                    if (count == 1) {
                        if (isFav) {
                            finalHolder.favButton.setText("{faw-star}");
                        } else {
                            finalHolder.favButton.setText("{faw-star-o}");
                        }
                        p.setFav(isFav);
                    }
                }
            });
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }


        if (p != null) {
            mHolder.tvDuaNumber.setText("" + p.getReference());
            mHolder.tvDuaArabic.setText(Html.fromHtml(p.getArabic()));
            mHolder.tvDuaTranslation.setText(Html.fromHtml(p.getTranslation()));

            if (p.getBook_reference() != null)
                mHolder.tvDuaReference.setText(Html.fromHtml(p.getBook_reference()));
            else
                mHolder.tvDuaReference.setText("null");

            if (p.getFav()) {
                mHolder.favButton.setText("{faw-star}");
            } else {
                mHolder.favButton.setText("{faw-star-o}");
            }

            Log.d("DoaDetailAdapter", "getFav");
            Log.d("DoaDetailAdapter", "asdasds");
            Log.d("DoaDetailAdapter", Boolean.toString(p.getFav()));
        }


        return convertView;
    }

    public static class ViewHolder {
        TextView tvDuaNumber;
        TextView tvDuaArabic;
        TextView tvDuaReference;
        TextView tvDuaTranslation;
        IconicsButton shareButton;
        IconicsButton favButton;
    }
}