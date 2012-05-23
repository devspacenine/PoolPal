package com.devspacenine.poolpal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PoolPalDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "poolpal.db";
	private static final int DATABASE_VERSION = 1;

	public PoolPalDatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		PoolTable.onCreate(database);
		TaskTable.onCreate(database);
		ReminderTable.onCreate(database);
		AddressTable.onCreate(database);
		TimerTable.onCreate(database);
		OrderTable.onCreate(database);
		OrderItemTable.onCreate(database);
		ItemTable.onCreate(database);
		WaterTestTable.onCreate(database);
		TreatmentTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

		PoolTable.onUpgrade(database, oldVersion, newVersion);
		TaskTable.onUpgrade(database, oldVersion, newVersion);
		ReminderTable.onUpgrade(database, oldVersion, newVersion);
		AddressTable.onUpgrade(database, oldVersion, newVersion);
		TimerTable.onUpgrade(database, oldVersion, newVersion);
		OrderTable.onUpgrade(database, oldVersion, newVersion);
		OrderItemTable.onUpgrade(database, oldVersion, newVersion);
		ItemTable.onUpgrade(database, oldVersion, newVersion);
		WaterTestTable.onUpgrade(database, oldVersion, newVersion);
		TreatmentTable.onUpgrade(database, oldVersion, newVersion);
	}

}
