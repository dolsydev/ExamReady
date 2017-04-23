package com.example.wolf.testseries.sqliteController;

/**
 * Created by WOLF on 25-03-2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

public class SqliteController
{
    private final String DATABASE = "test_series";
    private final String QUESTIONS = "questions";
    Context context;
    SQLiteDatabase database = null;
    ArrayList<String> results = new ArrayList<String>();

    public SqliteController(Context context)
    {
        this.context=context;
    }

    private void createNewDatabase(Context context)
    {
        try
        {
            database = context.openOrCreateDatabase(DATABASE, context.MODE_PRIVATE,
                    null);

            database.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE
                    + " (stationId VARCHAR, stationName VARCHAR,"
                    + " shoppingMallName VARCHAR,productName VARCHAR,"
                    + " purchased INT(3),UNIQUE(shoppingMallName, productName) ) ;");

            Log.d( " ", "---------database created-----------");

        }
        catch (SQLiteException se)
        {
            Log.e(getClass().getSimpleName(),
                    "Could not create or Open the database");
        }
    }
}
