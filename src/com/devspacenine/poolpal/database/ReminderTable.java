package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReminderTable {

	public static final String TABLE = "reminder";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_PERIOD = "period";
	public static final String KEY_NOTIFICATION = "notification";
	public static final String KEY_SMS = "sms";
	public static final String KEY_EMAIL = "email";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_DATE = TABLE + "." + KEY_DATE;
	public static final String CONCRETE_PERIOD = TABLE + "." + KEY_PERIOD;
	public static final String CONCRETE_NOTIFICATION = TABLE + "." + KEY_NOTIFICATION;
	public static final String CONCRETE_SMS = TABLE + "." + KEY_SMS;
	public static final String CONCRETE_EMAIL = TABLE + "." + KEY_EMAIL;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, "
			+ "date text not null, period integer not null, notification integer not null, "
			+ "sms integer not null, email integer not null);";

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

		return new String[] {KEY_ID, KEY_DATE, KEY_PERIOD, KEY_NOTIFICATION,
				KEY_SMS, KEY_EMAIL};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_DATE, CONCRETE_PERIOD, CONCRETE_NOTIFICATION,
				CONCRETE_SMS, CONCRETE_EMAIL};
	}
}
