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

    public static final String CREATE_PLAYER_TABLE =
            "CREATE TABLE player (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "login STRING UNIQUE NOT NULL," +
                    "password STRING NOT NULL, " +
                    "level INTEGER NOT NULL, " +
                    "xp INTEGER NOT NULL, " +
                    "cash INTEGER NOT NULL);";

    public static final String CREATE_LEVEL_TABLE =
            "CREATE TABLE level (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name STRING UNIQUE NOT NULL," +
                    "xp INTEGER NOT NULL);";

    public static final String CREATE_CARD_TABLE =
            "CREATE TABLE card (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "value INTEGER NOT NULL," +
                    "type STRING NOT NULL);";

    public static final String DROP_PLAYER_TABLE =
            "DROP TABLE IF EXISTS player";
    public static final String DROP_LEVEL_TABLE =
            "DROP TABLE IF EXISTS level";
    public static final String DROP_CARD_TABLE =
            "DROP TABLE IF EXISTS card";

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PLAYER_TABLE);
            db.execSQL(CREATE_LEVEL_TABLE);
            db.execSQL(CREATE_CARD_TABLE);

            // Insert default player (ID, NAME, PASSWORD, LEVEL, XP, CASH)
            db.execSQL("INSERT INTO player VALUES (1, 'admin', 'admin', 1, 0, 10000)");

            // Insert player levels
            db.execSQL("INSERT INTO level VALUES (1, 'Rookie', 100)");
            db.execSQL("INSERT INTO level VALUES (2, 'Player', 200)");
            db.execSQL("INSERT INTO level VALUES (3, 'Card shark', 500)");

            // Insert playable cards
            for (int i = 1; i < 14; i++) {
                db.execSQL("INSERT INTO card VALUES (" + i + ", " + i + ", 'Spade')");
                db.execSQL("INSERT INTO card VALUES (" + (i + 13) + ", " + i + ", 'Heart')");
                db.execSQL("INSERT INTO card VALUES (" + (i + 26) + ", " + i + ", 'Club')");
                db.execSQL("INSERT INTO card VALUES (" + (i + 39) + ", " + i + ", 'Diamond')");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("AssignmentDB", "Upgrading db from version "
                    + oldVersion + "to" + newVersion);
            db.execSQL(AssignmentDB.DROP_PLAYER_TABLE);
            db.execSQL(AssignmentDB.DROP_LEVEL_TABLE);
            db.execSQL(AssignmentDB.DROP_CARD_TABLE);
            onCreate(db);
        }
    }

    // Constructor
    public AssignmentDB (Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // Private methods
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

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
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

    public void insertPlayer(Player player) {
        ContentValues cv = new ContentValues();
        cv.put("id", player.getId());
        cv.put("login", player.getName());
        cv.put("password", player.getPassword());
        cv.put("level", player.getLevel());
        cv.put("xp", player.getXp());
        cv.put("cash", player.getCash());

        this.openWriteableDB();
        db.insert("player", null, cv);
        this.closeDB();
    }

    public int updatePlayer(Player player) {
        ContentValues cv = new ContentValues();
        cv.put("id", player.getId());
        cv.put("login", player.getName());
        cv.put("password", player.getPassword());
        cv.put("level", player.getLevel());
        cv.put("xp", player.getXp());
        cv.put("cash", player.getCash());

        String where = "id = ?";
        String[] whereArgs = { String.valueOf(player.getId()) };

        this.openWriteableDB();
        int rowCount = db.update("player", cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deletePlayer(String id) {
        String where = "id = ?";
        String[] whereArgs = { id };
        this.openWriteableDB();
        int rowCount = db.delete("player", where, whereArgs);
        this.closeDB();
        return rowCount;
    }

    public ArrayList<Card> getCards() {
        ArrayList<Card> cards = new ArrayList<>();
        String selectQuery = "SELECT * FROM card";
        this.openReadableDB();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            cards.add(getCardFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return cards;
    }

    public Card getCard(int id) {
        String where = "id = ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query("card", null, where, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        Card card = getCardFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return card;
    }

    private static Card getCardFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            try {
                Card card = new Card(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2));
                return card;
            }
            catch (Exception e) {
                return null;
            }
        }
    }
}