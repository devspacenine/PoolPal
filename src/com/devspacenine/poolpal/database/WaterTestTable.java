package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WaterTestTable {

	public static final String TABLE = "water_test";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_POOL = "pool_id";
	public static final String KEY_TASK = "task_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_CHLORINE = "chlorine";
	public static final String KEY_BROMINE = "bromine";
	public static final String KEY_PH = "ph";
	public static final String KEY_ALKALINITY = "alkalinity";
	public static final String KEY_HARDNESS = "hardness";
	public static final String KEY_STABILIZER = "stabilizer";
	public static final String KEY_TEMPERATURE = "temperature";
	public static final String KEY_TDS = "tds";
	public static final String KEY_BORATE = "borate";
	public static final String KEY_LSI = "lsi";
	public static final String KEY_RSI = "rsi";
	public static final String KEY_PSI = "psi";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_POOL = TABLE + "." + KEY_POOL;
	public static final String CONCRETE_TASK = TABLE + "." + KEY_TASK;
	public static final String CONCRETE_DATE = TABLE + "." + KEY_DATE;
	public static final String CONCRETE_CHLORINE = TABLE + "." + KEY_CHLORINE;
	public static final String CONCRETE_BROMINE = TABLE + "." + KEY_BROMINE;
	public static final String CONCRETE_PH = TABLE + "." + KEY_PH;
	public static final String CONCRETE_ALKALINITY = TABLE + "." + KEY_ALKALINITY;
	public static final String CONCRETE_HARDNESS = TABLE + "." + KEY_HARDNESS;
	public static final String CONCRETE_STABILIZER = TABLE + "." + KEY_STABILIZER;
	public static final String CONCRETE_TEMPERATURE = TABLE + "." + KEY_TEMPERATURE;
	public static final String CONCRETE_TDS = TABLE + "." + KEY_TDS;
	public static final String CONCRETE_BORATE = TABLE + "." + KEY_BORATE;
	public static final String CONCRETE_LSI = TABLE + "." + KEY_LSI;
	public static final String CONCRETE_RSI = TABLE + "." + KEY_RSI;
	public static final String CONCRETE_PSI = TABLE + "." + KEY_PSI;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, pool_id integer not null, "
			+ "task_id integer not null, date text not null, chlorine real, bromine real, ph real, "
			+ "alkalinity integer, hardness integer, stabilizer integer, "
			+ "temperature integer, tds integer, borate integer, lsi real, "
			+ "rsi real, psi real);";

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

		return new String[] {KEY_ID, KEY_POOL, KEY_TASK, KEY_DATE, KEY_CHLORINE, KEY_BROMINE,
				KEY_PH, KEY_ALKALINITY, KEY_HARDNESS, KEY_STABILIZER, KEY_TEMPERATURE,
				KEY_TDS, KEY_BORATE, KEY_LSI, KEY_RSI, KEY_PSI};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_POOL, CONCRETE_TASK, CONCRETE_DATE,
				CONCRETE_CHLORINE, CONCRETE_BROMINE,
				CONCRETE_PH, CONCRETE_ALKALINITY, CONCRETE_HARDNESS, CONCRETE_STABILIZER,
				CONCRETE_TEMPERATURE,
				CONCRETE_TDS, CONCRETE_BORATE, CONCRETE_LSI, CONCRETE_RSI, CONCRETE_PSI};
	}
}
