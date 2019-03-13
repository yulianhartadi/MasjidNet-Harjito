package com.rawzadigital.masjidnet.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rawzadigital.masjidnet.database.HisnDatabaseInfo;
import com.rawzadigital.masjidnet.model.Doa;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoaGroupLoader extends AbstractQueryLoader<List<Doa>> {

    public DoaGroupLoader(Context context) {
        super(context);
    }
    public Locale deviceLocale;
    public String groupTitleLanguage;

    @Override
    public List<Doa> loadInBackground() {

        /*Locale mArabicLocale = new Locale("ar_EG", "ar_IL");

        deviceLocale = Resources.getSystem().getConfiguration().locale;

        if (deviceLocale.equals(Locale.ENGLISH))
            groupTitleLanguage = HisnDatabaseInfo.DuaGroupTable.ENGLISH_TITLE;
        else if (deviceLocale.equals(mArabicLocale))
            groupTitleLanguage = HisnDatabaseInfo.DuaGroupTable.ARABIC_TITLE;*/

        List<Doa> results = null;
        Cursor doaGroupCursor = null;
        try {
            final SQLiteDatabase database = mDbHelper.getDb();
            doaGroupCursor = database.query(HisnDatabaseInfo.DoaGroupTable.TABLE_NAME,
                    new String[]{HisnDatabaseInfo.DoaGroupTable._ID,
                            HisnDatabaseInfo.DoaGroupTable.ENGLISH_TITLE},
                    null,
                    null,
                    null,
                    null,
                    HisnDatabaseInfo.DoaGroupTable._ID);

            if (doaGroupCursor != null && doaGroupCursor.moveToFirst()) {
                results = new ArrayList<>();
                do {
                    int doa_group_id = doaGroupCursor.getInt(0);
                    String doa_group_title = doaGroupCursor.getString(1);
                    results.add(new Doa(doa_group_id, doa_group_title));
                } while (doaGroupCursor.moveToNext());
            }
        } finally {
            if (doaGroupCursor != null) {
                doaGroupCursor.close();
            }
        }

        return results;
    }
}