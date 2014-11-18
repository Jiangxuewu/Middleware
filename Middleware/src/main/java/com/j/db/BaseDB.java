package com.j.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public abstract class BaseDB extends SQLiteOpenHelper {

    protected static final String UPDATE_DATA = "update_data";

    /**
     * 数据库存储list，用#区分开每个item
     */
    protected static final String SPE_CHAR = "#";

    protected Context mContext;

    protected abstract String getCreateTableSql();

    protected abstract ContentValues getValues();

    protected abstract String getTabName();

    private static String DB_NAME = "savvy_j";
    private static final int DB_VERSION = 1;

    public static void setDBName(String dbName) {
        if (!TextUtils.isEmpty(dbName)) {
            DB_NAME = dbName;
        }
    }

    public BaseDB(Context context) {
        this(context, null);
    }

    public BaseDB(Context context, CursorFactory factory) {
        this(context, DB_NAME, factory, DB_VERSION);
    }

    private BaseDB(Context context, String name, CursorFactory factory,
                   int version) {
        super(context, name, factory, version);
        mContext = context;
        onCreate();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (null == db) {
            return;
        }
        db.execSQL(getCreateTableSql());
    }

    public void onCreate() {
        getWritableDatabase().execSQL(getCreateTableSql());
    }

    protected long insert() {
        return getWritableDatabase().insert(getTabName(), null, getValues());
    }

    protected long update(String whereClause, String[] whereArgs) {
        ContentValues cv = getValues();
        if (null == cv || cv.size() <= 0) {
            return -1;
        }
        return getWritableDatabase().update(getTabName(), cv, whereClause,
                whereArgs);
    }

    protected void delete(String whereClause, String[] whereArgs) {
        getWritableDatabase().delete(getTabName(), whereClause, whereArgs);
    }

    protected Cursor query(String[] columns, String selection,
                           String[] selectionArgs, String groupBy, String having,
                           String orderBy) {
        return getReadableDatabase().query(getTabName(), columns, selection,
                selectionArgs, groupBy, having, orderBy);
    }

    public void clear() {
        String sql = "DELETE FROM " + getTabName();
        getWritableDatabase().execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
