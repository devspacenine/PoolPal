package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TaskTable {

	public static final String TABLE = "task";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_POOL = "pool_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DATE = "date";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_REPITITION = "repitition";
	public static final String KEY_REPITITION_SCALE = "repitition_scale";
	public static final String KEY_NEXT_ON_COMPLETION = "next_on_completion";
	public static final String KEY_REMINDERS = "reminders";
	public static final String KEY_LATE_REMINDERS = "late_reminders";
	public static final String KEY_TIMER = "timer_id";
	public static final String KEY_ITEM = "item_id";
	public static final String KEY_FAVORITE = "favorite_id";
	public static final String KEY_COMPLETED = "completed";
	public static final String KEY_SAVE = "save";
	public static final String KEY_ADDRESS = "address_id";
	public static final String KEY_TASK_TYPE = "task_type";
	public static final String KEY_TASKS = "tasks";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_POOL = TABLE + "." + KEY_POOL;
	public static final String CONCRETE_TITLE = TABLE + "." + KEY_TITLE;
	public static final String CONCRETE_DATE = TABLE + "." + KEY_DATE;
	public static final String CONCRETE_MESSAGE = TABLE + "." + KEY_MESSAGE;
	public static final String CONCRETE_REPITITION = TABLE + "." + KEY_REPITITION;
	public static final String CONCRETE_REPITITION_SCALE = TABLE + "." + KEY_REPITITION_SCALE;
	public static final String CONCRETE_NEXT_ON_COMPLETION = TABLE + "." + KEY_NEXT_ON_COMPLETION;
	public static final String CONCRETE_REMINDERS = TABLE + "." + KEY_REMINDERS;
	public static final String CONCRETE_LATE_REMINDERS = TABLE + "." + KEY_LATE_REMINDERS;
	public static final String CONCRETE_TIMER = TABLE + "." + KEY_TIMER;
	public static final String CONCRETE_ITEM = TABLE + "." + KEY_ITEM;
	public static final String CONCRETE_FAVORITE = TABLE + "." + KEY_FAVORITE;
	public static final String CONCRETE_COMPLETED = TABLE + "." + KEY_COMPLETED;
	public static final String CONCRETE_SAVE = TABLE + "." + KEY_SAVE;
	public static final String CONCRETE_ADDRESS = TABLE + "." + KEY_ADDRESS;
	public static final String CONCRETE_TASK_TYPE = TABLE + "." + KEY_TASK_TYPE;
	public static final String CONCRETE_TASKS = TABLE + "." + KEY_TASKS;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, pool_id integer, "
			+ "title text not null, date text not null, message text not null, repitition integer, repitition_scale integer, "
			+ "next_on_completion integer not null, reminders text, late_reminders integer not null, timer_id integer, "
			+ "item_id integer, favorite_id integer, completed integer not null, save integer not null, "
			+ "address_id integer, task_type integer not null, tasks text);";

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

		return new String[] {KEY_ID, KEY_POOL, KEY_TITLE, KEY_DATE, KEY_MESSAGE,
				KEY_REPITITION, KEY_REPITITION_SCALE, KEY_NEXT_ON_COMPLETION, KEY_REMINDERS,
				KEY_LATE_REMINDERS, KEY_TIMER, KEY_ITEM,
				KEY_FAVORITE, KEY_COMPLETED, KEY_SAVE, KEY_ADDRESS, KEY_TASK_TYPE, KEY_TASKS};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_POOL, CONCRETE_TITLE, CONCRETE_DATE, CONCRETE_MESSAGE,
				CONCRETE_REPITITION, CONCRETE_REPITITION_SCALE, CONCRETE_NEXT_ON_COMPLETION,
				CONCRETE_REMINDERS, CONCRETE_LATE_REMINDERS, CONCRETE_TIMER, CONCRETE_ITEM,
				CONCRETE_FAVORITE, CONCRETE_COMPLETED, CONCRETE_SAVE, CONCRETE_ADDRESS, CONCRETE_TASK_TYPE,
				CONCRETE_TASKS};
	}
}
