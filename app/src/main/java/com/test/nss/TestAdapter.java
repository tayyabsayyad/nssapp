package com.test.nss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
                Log.e(TAG, "getActType: ");
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
            Log.i(TAG, "getCampDetails: ");
            if (mCur.getCount() == 0) {
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
            Log.i(TAG, "getClgList: ");
            if (mCur.getCount() == 0) {
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
                Log.i(TAG, "insertClgList: ");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public Cursor getCampId(String campActName) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT CampId FROM CampActivityList WHERE CampActivityName=\"%s\"", campActName);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur.getCount() == 0) {
                Log.i(TAG, "getCampId: ");
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
                Log.i(TAG, "insertCampActList: ");
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampActListAll(int id, String clgName, String state,
                                     String campActTitle, String campActDec,
                                     String day, String vec, int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("College_Name", clgName);
            contentValues.put("State", state);

            contentValues.put("CampActivityTitle", campActTitle);
            contentValues.put("CampActivityDescription", campActDec);
            contentValues.put("CampDay", day);
            contentValues.put("VEC", vec);
            contentValues.put("Sync", sync);

            long row = mDb.insert("CampActivities", null, contentValues);
            if (row != -1)
                Log.i(TAG, "insertCampActListAll: ");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampActListAllOff(String campActTitle, String campActDec, String day, int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CampActivityTitle", campActTitle);
            contentValues.put("CampActivityDescription", campActDec);
            contentValues.put("CampDay", day);
            contentValues.put("Sync", sync);

            long row = mDb.insert("CampActivities", null, contentValues);
            if (row != -1)
                Log.i(TAG, "insertCampActListAll: ");

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
                Log.i(TAG, "insertActAdmin: ");
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertAct(String vec, String actCode,
                          String id,
                          String assignedDate,
                          String actName,
                          String hours,
                          String state,
                          int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("actID", id);
            contentValues.put("VEC", vec);
            contentValues.put("ActivityCode", actCode);
            contentValues.put("Date", assignedDate);
            contentValues.put("ActivityName", actName);
            contentValues.put("HoursWorked", hours);
            contentValues.put("State", state);
            contentValues.put("If_Added", 1);
            contentValues.put("Sync", sync);

            long row = mDb.insert("DailyActivity", null, contentValues);
            if (row != -1)
                Log.i(TAG, "insertAct: ");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertActOff(String vec, String actCode,
                             String assignedDate,
                             String actName,
                             String hours,
                             int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("VEC", vec);
            contentValues.put("ActivityCode", actCode);
            contentValues.put("Date", assignedDate);
            contentValues.put("ActivityName", actName);
            contentValues.put("HoursWorked", hours);
            contentValues.put("If_Added", 1);
            contentValues.put("State", "Submitted");
            contentValues.put("Sync", sync);

            long row = mDb.insert("DailyActivity", null, contentValues);
            if (row != -1)
                Log.i(TAG, "insertActOff: ");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public Cursor getActListOff() {
        try {
            //String a = String.format("aaa %d", act);
            String sql = "SELECT * FROM DailyActivity WHERE sync=0";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActListOff: ");
            if (mCur2.getCount() == 0) {
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
            Log.i(TAG, "getActId: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in DailyActivity", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActList(String act) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM DailyActivity WHERE ActivityCode=\"%s\"", (act));
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActList: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in DailyActivity", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    /*public Cursor getActAssigActName(int act) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM DailyActivity WHERE activityType='%d' AND (not State=\"Deleted\")", act);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActAssigActName: ");
            if (mCur2.getCount() == 0) {
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }*/

    public Cursor getActAssigActNameOff(String act) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM ActivityListByAdmin WHERE activityType LIKE '%s'", act);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActAssigActName: ");
            if (mCur2.getCount() == 0) {
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActAssigName(String act) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM ActivityListByAdmin WHERE activityName=\"%s\"", act);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActAssigActName: ");
            if (mCur2.getCount() == 0) {
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActAssigActNameAdmin(String act) {
        try {
            String sql = String.format("SELECT * FROM ActivityListByAdmin WHERE ActivityName=\"%s\"", act);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActAssigActNameAdmin: ");
            if (mCur2.getCount() == 0) {
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getActAssigActId(String actName) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM ActivityListByAdmin WHERE ActivityName=\"%s\"", actName);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActAssigActId: ");
            if (mCur2.getCount() == 0) {
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
            Log.i(TAG, "getCampActList: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListId(String actName) {
        try {
            String sql = String.format("SELECT CampId FROM CampActivityList WHERE CampActivityName=\"%s\"", actName);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getCampActList: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListAllById(int id) {
        try {
            String sql = "SELECT * FROM CampActivities WHERE id=" + id;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getCampActListAll: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListAll() {
        try {
            String sql = "SELECT * FROM CampActivities WHERE (not State=\"Deleted\")";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getCampActListAll: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public int getSumHours(String actCode){
        try {
            String sql = String.format("SELECT sum(HoursWorked) FROM DailyActivity WHERE ActivityCode=\"%s\" AND (not State=\"Deleted\")", actCode);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getSumHours: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in DailyActivity", )();
                return -1;
            }
            mCur2.moveToFirst();
            return mCur2.getInt(0);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListOff(int sync) {
        try {
            String sql = "SELECT * FROM CampActivities WHERE sync=" + sync;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getCampActListOff: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
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
                //Log.e(mContext, "Too bad no data", )();
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

    public void setDetails(int hour, int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE DailyActivity SET HoursWorked = %d WHERE actId=\"%d\"", hour, id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void dropDetails(int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "DELETE FROM DailyActivity WHERE actID=\"%d\"", id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setDetailsCamp(String desc, int day, int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE CampActivities SET CampActivityDescription = \"%s\", CampDay = %d WHERE id=\"%d\"", desc, day, id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void dropDetailsCamp(int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "DELETE FROM CampActivities WHERE id=\"%d\"", id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setSyncActDetails(int sync, int actId) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE DailyActivity SET Sync = %d WHERE actID=\"%d\"", sync, actId);
            mDb.execSQL(sql);
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
