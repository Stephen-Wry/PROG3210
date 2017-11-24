package ca.on.conestogac.swassignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class AssignmentDB {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // Database constants
    public static final String DB_NAME = "assignment.db";
    public static final int DB_VERSION = 1;

    // task table constants
    public static final String TASK_TABLE = "task";
    public static final String TASK_ID = "_id";
    public static final int TASK_ID_COL = 0;
    public static final String TASK_LIST_ID = "list_id";
    public static final int TASK_LIST_ID_COL = 1;
    public static final String TASK_NAME = "task_name";
    public static final int TASK_NAME_COL = 2;
    public static final String TASK_NOTES = "notes";
    public static final int TASK_NOTES_COL = 3;
    public static final String TASK_COMPLETED = "date_completed";
    public static final int TASK_COMPLETED_COL = 4;
    public static final String TASK_HIDDEN = "hidden";
    public static final int TASK_HIDDEN_COL = 5;

    public static final String CREATE_LIST_TABLE =
            "CREATE TABLE list (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "list_name TEXT NOT NULL UNIQUE);";

    public static final String CREATE_TASK_TABLE =
            "CREATE TABLE task (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "list_id INTEGER NOT NULL," +
                    "task_name TEXT NOT NULL, " +
                    "notes TEXT, " +
                    "date_completed TEXT, " +
                    "hidden TEXT);";

    public static final String CREATE_PLAYER_TABLE =
            "CREATE TABLE player (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "login STRING NOT NULL," +
                    "password STRING NOT NULL, " +
                    "level INTEGER);";

    public static final String DROP_LIST_TABLE =
            "DROP TABLE IF EXISTS list";

    public static final String DROP_TASK_TABLE =
            "DROP TABLE IF EXISTS task";

    public static final String DROP_PLAYER_TABLE =
            "DROP TABLE IF EXISTS player";

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_LIST_TABLE);
            db.execSQL(CREATE_TASK_TABLE);
            db.execSQL(CREATE_PLAYER_TABLE);

            // insert default lists
            db.execSQL("INSERT INTO list VALUES (1, 'Personal')");
            db.execSQL("INSERT INTO list VALUES (2, 'Business')");

            // insert sample tasks
            db.execSQL(
                    "INSERT INTO task VALUES (1, 1, 'Pay bills', " +
                            "'Rent\nPhone\nInternet', '0', '0')");
            db.execSQL(
                    "INSERT INTO task VALUES (2, 1, 'Get hair cut', " +
                            "'', '0', '0')");

            // insert default player
            db.execSQL("INSERT INTO player VALUES (1, 'admin', 'admin', 1)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Task list", "Upgrading db from version "
                + oldVersion + "to" + newVersion);
            db.execSQL(AssignmentDB.DROP_LIST_TABLE);
            db.execSQL(AssignmentDB.DROP_TASK_TABLE);
            db.execSQL(AssignmentDB.DROP_PLAYER_TABLE);
            onCreate(db);
        }
    }

    // constructor
    public AssignmentDB (Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    public ArrayList<Task> getTasks(String listName) {
        String where =
                TASK_LIST_ID + "= ? AND " +
                        TASK_HIDDEN + "!='1'";
        int listID = 1;//getList(listName).getId();
        String[] whereArgs = { Integer.toString(listID) };

        this.openReadableDB();
        Cursor cursor = db.query(TASK_TABLE, null, where, whereArgs,
                null, null, null);
        ArrayList<Task> tasks = new ArrayList<Task>();
        while (cursor.moveToNext()) {
            tasks.add(getTaskFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return tasks;
    }

    public Task getTask(int id) {
        String where = TASK_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(TASK_TABLE, null, where, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        Task task = getTaskFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return task;
    }

    private static Task getTaskFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            try {
                Task task = new Task(
                        cursor.getInt(TASK_ID_COL),
                        cursor.getInt(TASK_LIST_ID_COL),
                        cursor.getString(TASK_NAME_COL),
                        cursor.getString(TASK_NOTES_COL),
                        cursor.getString(TASK_COMPLETED_COL),
                        cursor.getString(TASK_HIDDEN_COL));
                return task;
            }
            catch (Exception e) {
                return null;
            }
        }
    }

    public long insertTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_LIST_ID, task.getListId());
        cv.put(TASK_NAME, task.getName());
        cv.put(TASK_NOTES, task.getNotes());
        cv.put(TASK_COMPLETED, task.getCompleted());
        cv.put(TASK_HIDDEN, task.getHidden());

        this.openWriteableDB();
        long rowID = db.insert(TASK_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_LIST_ID, task.getListId());
        cv.put(TASK_NAME, task.getName());
        cv.put(TASK_NOTES, task.getNotes());
        cv.put(TASK_COMPLETED, task.getCompleted());
        cv.put(TASK_HIDDEN, task.getHidden());

        String where = TASK_ID + "= ?";
        String[] whereArgs = { String.valueOf(task.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(TASK_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteTask(long id) {
        String where = TASK_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(TASK_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }
}
