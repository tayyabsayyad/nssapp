package com.test.nss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class TestAdapter {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public TestAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public TestAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public TestAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public ArrayList<String> getActType() {
        ArrayList<String> mylistAct = new ArrayList<>();
        try {
            String sql = "SELECT * FROM NatureOfActivity";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur.getCount() == 0) {
                Toast.makeText(mContext, "Too bad no data", Toast.LENGTH_SHORT).show();
            } else {
                while (mCur.moveToNext()) {
                    mylistAct.add(mCur.getString(0));
                }
            }
            mCur.close();
            return mylistAct;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void insertHelpData(String post, String name, String email, String contact) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CollegeName", "");
            contentValues.put("Post", post);
            contentValues.put("Name", name);
            contentValues.put("EmailID", email);
            contentValues.put("Contact", contact);
            contentValues.put("Entry_year", "");
            long row = mDb.insert("Help", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data");
            else
                Log.e(TAG, "Something went wrong");
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampDetails(String clgName, String campFrom,
                                  String campTo, String campVen,
                                  String campPost, String campTal, String campDist) {
        //String sql = "DELETE FROM CampDetails";
        //mDb.execSQL(sql);
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("College_name", clgName);
            contentValues.put("Camp_from", campFrom);
            contentValues.put("Camp_to", campTo);
            contentValues.put("Camp_venue", campVen);
            contentValues.put("Camp_post", campPost);
            contentValues.put("Camp_taluka", campTal);
            contentValues.put("Camp_district", campDist);
            long row = mDb.insert("CampDetails", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data to CampDetails");
            else
                Log.e(TAG, "Something went wrong");
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public Cursor getCampDetails() {
        try {
            String sql = "SELECT * FROM CampDetails";
            Cursor mCur = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur.getCount());
            if (mCur.getCount() == 0) {
                Toast.makeText(mContext, "Too bad no data in CampDetails", Toast.LENGTH_SHORT).show();
            } else {
                /*while (mCur.moveToNext()) {
                    //int i = 0;
                    Log.e("College:", "" + mCur.getString(mCur.getColumnIndex("College_name")));
                    Log.e("College:", "" + mCur.getString(mCur.getColumnIndex("Camp_taluka")));
                    //Log.e("College:", "" + mCur.getString(2));
                    //i++;
                }*/
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public ArrayList<String> getHelpData() {
        ArrayList<String> res = new ArrayList<>();
        try {
            String sql = "SELECT EmailID, Contact FROM Help";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur.getCount() == 0) {
                Toast.makeText(mContext, "Too bad no data", Toast.LENGTH_SHORT).show();
            } else {
                if (mCur.moveToNext()) {
                    res.add(mCur.getString(0));
                    res.add(mCur.getString(1));
                }
            }
            mCur.close();
            return res;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
    /*public Cursor getTestData() {
        try {
            String sql ="SELECT * FROM NatureOfActivity";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }*/
}
