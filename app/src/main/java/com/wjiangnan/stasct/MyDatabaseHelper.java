package com.wjiangnan.stasct;

/**
 * Created by happts on 2017/3/7.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by happts on 2017/3/6.
 * 建立数据库
 *
 * 建表语句定义成字符串常量
 * 在onCreate()方法中调用  execSQL()方法执行建表语句
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    //一张表
    public static final String CREATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement, "
            + "address text, "
            + "teacher text,"
            + "week integer, "
            + "first integer,"
            + "last integer,"
            + "each integer,"
            + "start integer,"
            + "num integer,"
            + "name text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        onCreate(db);

    }

}
