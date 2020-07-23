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
import java.util.Locale;

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

    public void insertHelpData(String clgName,
                               String post, String name, String email,
                               String contact, String entryYear) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CollegeName", clgName);
            contentValues.put("Post", post);
            contentValues.put("Name", name);
            contentValues.put("EmailID", email);
            contentValues.put("Contact", contact);
            contentValues.put("Entry_year", entryYear);
            long row = mDb.insert("Help", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampDetails(String clgName, String campFrom,
                                  String campTo, String campVen,
                                  String campPost, String campTal, String campDist) {

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
                //Toast.makeText(mContext, "Too bad no data in CampDetails", Toast.LENGTH_SHORT).show();
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

    public Cursor getClgList() {
        try {
            String sql = "SELECT CollegeName FROM CollegeNames";
            Cursor mCur = mDb.rawQuery(sql, null);
            Log.e("CollegeNames", "" + mCur.getCount());
            if (mCur.getCount() == 0) {
                Toast.makeText(mContext, "Too bad no data in CollegeNames", Toast.LENGTH_SHORT).show();
            } else {
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void insertClgList(String clgId, String clgName) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("College_id", clgId);
            contentValues.put("CollegeName", clgName);

            long row = mDb.insert("CollegeNames", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data to CollegeNames");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public Cursor getCampId(String campActName) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT CampId FROM CampActivityList WHERE CampActivityName=\"%s\"", campActName);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur.getCount() == 0) {
                Toast.makeText(mContext, "Too bad no data in CollegeNames", Toast.LENGTH_SHORT).show();
            } else {
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void insertCampActList(String campActName, String campId) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CampActivityName", campActName);
            contentValues.put("CampId", campId);

            long row = mDb.insert("CampActivityList", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data to CampActivityList");
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampActListAll(String campActTitle, String campActDec, String day, int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CampActivityTitle", campActTitle);
            contentValues.put("CampActivityDescription", campActDec);
            contentValues.put("CampDay", day);
            contentValues.put("Sync", sync);

            long row = mDb.insert("CampActivities", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data to CampActivities");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertActAdmin(int id, String clgName, String actType, String actName,
                               String hours, String assignedDate) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("College_Name", clgName);
            contentValues.put("activityType", actType);
            contentValues.put("ActivityName", actName);
            contentValues.put("HoursAssigned", hours);
            contentValues.put("AssignedDate", assignedDate);

            long row = mDb.insert("ActivityListByAdmin", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data to ActivityListByAdmin");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertAct(String vec, String actCode,
                          String assignedDate,
                          String actName,
                          String hours, int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("VEC", vec);
            contentValues.put("ActivityCode", actCode);
            contentValues.put("Date", assignedDate);
            contentValues.put("ActivityName", actName);
            contentValues.put("HoursWorked", hours);
            contentValues.put("If_Added", 1);
            contentValues.put("Sync", sync);

            long row = mDb.insert("DailyActivity", null, contentValues);
            if (row != -1)
                Log.i(TAG, "Entered data to DailyActivity");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    /*public Cursor getActListAdmin(int yr) {
        try {
            String sql = "SELECT * FROM ActivityListByAdmin WHERE activityType LIKE "+yr+'%';
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("ActivityListByAdmin", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                Toast.makeText(mContext, "Too bad no data in ActivityListByAdmin", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }*/

    public Cursor getActListOff() {
        try {
            //String a = String.format("aaa %d", act);
            Log.e("AOOO", "getting");
            String sql = "SELECT * FROM DailyActivity WHERE sync=0";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in DailyActivity", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActId(String actCode) {
        try {
            //String a = String.format("aaa %d", act);
            String sql = "SELECT * FROM CampActivityListByAdmin WHERE AssignedActivityName=" + actCode;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in DailyActivity", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActList(String act) {
        try {
            //String a = String.format("aaa %d", act);
            String sql = String.format(Locale.ENGLISH, "SELECT Date, ActivityName, HoursWorked FROM DailyActivity WHERE ActivityCode=\"%s\"", (act));
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in DailyActivity", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActAssigActName(int act) {
        Log.e("Assign", "" + act);
        try {
            String sql = "SELECT * FROM ActivityListByAdmin WHERE activityType=" + act;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());

            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in ActivityListByAdmin", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActAssigActId(String actName) {
        Log.e("Assign", "" + actName);
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM ActivityListByAdmin WHERE ActivityName=\"%s\"", actName);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());

            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in ActivityListByAdmin", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActList() {
        try {
            String sql = "SELECT * FROM CampActivityList";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in CampActivityList", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListAll() {
        try {
            String sql = "SELECT * FROM CampActivities";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in CampActivityList", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public int getSumHours(String actCode){
        try {
            String sql = String.format("SELECT sum(HoursWorked) FROM DailyActivity WHERE ActivityCode=\"%s\"", actCode);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("sumHours", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in CampActivityList", Toast.LENGTH_SHORT).show();
                return -1;
            }
            mCur2.moveToFirst();
            return Integer.parseInt(mCur2.getString(0));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListOff(int sync) {
        try {
            String sql = "SELECT * FROM CampActivities WHERE sync=" + sync;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.e("getCampDetails", "" + mCur2.getCount());
            if (mCur2.getCount() == 0) {
                //Toast.makeText(mContext, "Too bad no data in CampActivityList", Toast.LENGTH_SHORT).show();
            }
            return mCur2;
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
                //Toast.makeText(mContext, "Too bad no data", Toast.LENGTH_SHORT).show();
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

    public void setSync(String table, int sync) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE %s SET Sync = %d", table, sync);
            mDb.execSQL(sql);
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
