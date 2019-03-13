package com.rawzadigital.masjidnet.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rawzadigital.masjidnet.database.HisnDatabaseInfo;
import com.rawzadigital.masjidnet.model.Doa;

import java.util.ArrayList;
import java.util.List;

public class DoaDetailsLoader extends AbstractQueryLoader<List<Doa>> {
    private int mGroup;

    public DoaDetailsLoader(Context context, int group) {
        super(context);
        mGroup = group;
    }

    @Override
    public List<Doa> loadInBackground() {
        List<Doa> results = null;
        Cursor duaDetailCursor = null;
        try {
            final SQLiteDatabase database = mDbHelper.getDb();
            duaDetailCursor = database.query(HisnDatabaseInfo.DoaTable.TABLE_NAME,
                    new String[]{HisnDatabaseInfo.DoaTable._ID,
                            HisnDatabaseInfo.DoaTable.FAV,
                            HisnDatabaseInfo.DoaTable.ARABIC_DUA,
                            HisnDatabaseInfo.DoaTable.ENGLISH_TRANSLATION,
                            HisnDatabaseInfo.DoaTable.ENGLISH_REFERENCE},
                    HisnDatabaseInfo.DoaTable.GROUP_ID + "=" + mGroup,
                    null, null, null, null);
            results = new ArrayList<>();
            if (duaDetailCursor != null && duaDetailCursor.moveToFirst()) {
                do {
                    int reference = Integer.parseInt(duaDetailCursor.getString(0));
                    boolean fav = duaDetailCursor.getInt(1) == 1;
                    String arabic = duaDetailCursor.getString(2);
                    String translation = duaDetailCursor.getString(3);
                    String book_reference = duaDetailCursor.getString(4);
                    results.add(new Doa(reference, fav, arabic, translation, book_reference));
                } while (duaDetailCursor.moveToNext());
            }
        } finally {
            if (duaDetailCursor != null) {
                duaDetailCursor.close();
            }
        }
        return results;
    }
}