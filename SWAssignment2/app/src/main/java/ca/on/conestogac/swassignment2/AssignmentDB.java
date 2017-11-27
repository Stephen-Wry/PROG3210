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

    public static final String CREATE_PLAYER_TABLE =
            "CREATE TABLE player (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "login STRING UNIQUE NOT NULL," +
                    "password STRING NOT NULL, " +
                    "level INTEGER NOT NULL, " +
                    "xp INTEGER NOT NULL, " +
                    "cash INTEGER NOT NULL);";

    public static final String DROP_PLAYER_TABLE =
            "DROP TABLE IF EXISTS player";

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PLAYER_TABLE);

            // Insert default player (ID, NAME, PASSWORD, LEVEL, XP, CASH)
            db.execSQL("INSERT INTO player VALUES (1, 'admin', 'admin', 1, 0, 100)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + "to" + newVersion);
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
/*
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
    }*/

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        String selectQuery = "SELECT * FROM player";
        this.openReadableDB();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            players.add(getPlayerFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return players;
    }
/*
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
    }*/

    public Player getPlayer(String name) {
        String where = "login = ?";
        String[] whereArgs = { name };

        this.openReadableDB();
        Cursor cursor = db.query("player", null, where, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        Player player = getPlayerFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return player;
    }

    private static Player getPlayerFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            try {
                Log.d("cursor:", (Integer.toString(cursor.getInt(3))));
                Log.d("cursor:", (Integer.toString(cursor.getInt(4))));
                Log.d("cursor:", (Integer.toString(cursor.getInt(5))));
                Player player = new Player(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5));
                return player;
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
