package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String ID = "id";
    private static final String USER_TABLE = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TODO_TABLE = "todo";
    private static final String USER_ID = "user_id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME + " TEXT, "
            + PASSWORD + " TEXT)";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK + " TEXT, "
            + STATUS + " INTEGER, "
            + USER_ID + " INTEGER, "
            + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + ID + "))";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the existing tables
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        //Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertUser(UserModel user) {
        ContentValues cv = new ContentValues();
        cv.put(USERNAME, user.getUsername());
        cv.put(PASSWORD, user.getPassword());
        db.insert(USER_TABLE, null, cv);
    }

    public UserModel getUser(String username) {
        UserModel user = null;
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.rawQuery("select * from " + USER_TABLE +" where " +  USERNAME + "=?", new String[]{username});
            if (cur.moveToFirst()) {
                user = new UserModel();
                user.setId(cur.getInt(cur.getColumnIndex(ID)));
                user.setUsername(cur.getString(cur.getColumnIndex(USERNAME)));
                user.setPassword(cur.getString(cur.getColumnIndex(PASSWORD)));
            }
        } catch (Exception e) {
            Log.e("GET USER", "getUser: Error");
        } finally {
            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
        }
        return user;
    }

    public void insertTask(ToDoModel task, int userId) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, userId);
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getUserTasks(int userId) {
        List<ToDoModel> taskList = new ArrayList<>();
        String stringUserId = String.valueOf(userId);
        Cursor cur = null;
        db.beginTransaction();
        try {
            //cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            cur = db.rawQuery("select * from " + TODO_TABLE +" where " + USER_ID + " =?", new String[]{stringUserId});
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    } while (cur.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("Get All Tasks", "getAllTasks: Error");
        } finally {
            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}
