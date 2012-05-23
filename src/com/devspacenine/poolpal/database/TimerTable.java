package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TimerTable {

	public static final String TABLE = "timer";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_START_DATE = "start_date";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_END_DATE = "end_date";
	public static final String KEY_NOTIFICATION = "notification";
	public static final String KEY_SMS = "sms";
	public static final String KEY_EMAIL = "email";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_TITLE = TABLE + "." + KEY_TITLE;
	public static final String CONCRETE_MESSAGE = TABLE + "." + KEY_MESSAGE;
	public static final String CONCRETE_START_DATE = TABLE + "." + KEY_START_DATE;
	public static final String CONCRETE_DURATION = TABLE + "." + KEY_DURATION;
	public static final String CONCRETE_END_DATE = TABLE + "." + KEY_END_DATE;
	public static final String CONCRETE_NOTIFICATION = TABLE + "." + KEY_NOTIFICATION;
	public static final String CONCRETE_SMS = TABLE + "." + KEY_SMS;
	public static final String CONCRETE_EMAIL = TABLE + "." + KEY_EMAIL;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, "
			+ "title text not null, message text not null, start_date text not null, "
			+ "duration integer not null, end_date text not null, "
			+ "notification integer not null, sms integer not null, email integer not null);";

	public static void onCreate(SQLiteDatabase database) {

		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		Log.w(TaskTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(database);
	}

	/**
	 * Returns an ordered list of column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] columnProjection() {

		return new String[] {KEY_ID, KEY_TITLE, KEY_MESSAGE,
				KEY_START_DATE, KEY_DURATION, KEY_END_DATE, KEY_NOTIFICATION,
				KEY_SMS, KEY_EMAIL};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_TITLE, CONCRETE_MESSAGE,
				CONCRETE_START_DATE, CONCRETE_DURATION, CONCRETE_END_DATE, CONCRETE_NOTIFICATION,
				CONCRETE_SMS, CONCRETE_EMAIL};
	}

}
