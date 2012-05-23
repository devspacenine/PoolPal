package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressTable {

	public static final String TABLE = "address";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_LINE_ONE = "line_one"; // 1234 Main St.
	public static final String KEY_LINE_TWO = "line_two"; // Apt. 2211
	public static final String KEY_LINE_THREE = "line_three"; // Manvel, TX 77578
	public static final String KEY_FEATURE = "feature"; // 1234
	public static final String KEY_ADMIN = "admin"; // TX
	public static final String KEY_LOCALITY = "locality"; // Manvel
	public static final String KEY_THOROUGHFARE = "thoroughfare"; // Main St.
	public static final String KEY_COUNTRY_CODE = "country_code"; // US
	public static final String KEY_COUNTRY_NAME = "country_name"; // United States
	public static final String KEY_LATITUDE = "latitude"; // 39.477036
	public static final String KEY_LONGITUDE = "longitude"; // -75.360766
	public static final String KEY_POSTAL_CODE = "postal_code"; // 77578

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_LINE_ONE = TABLE + "." + KEY_LINE_ONE;
	public static final String CONCRETE_LINE_TWO = TABLE + "." + KEY_LINE_TWO;
	public static final String CONCRETE_LINE_THREE = TABLE + "." + KEY_LINE_THREE;
	public static final String CONCRETE_FEATURE = TABLE + "." + KEY_FEATURE;
	public static final String CONCRETE_ADMIN = TABLE + "." + KEY_ADMIN;
	public static final String CONCRETE_LOCALITY = TABLE + "." + KEY_LOCALITY;
	public static final String CONCRETE_THOROUGHFARE = TABLE + "." + KEY_THOROUGHFARE;
	public static final String CONCRETE_COUNTRY_CODE = TABLE + "." + KEY_COUNTRY_CODE;
	public static final String CONCRETE_COUNTRY_NAME = TABLE + "." + KEY_COUNTRY_NAME;
	public static final String CONCRETE_LATITUDE = TABLE + "." + KEY_LATITUDE;
	public static final String CONCRETE_LONGITUDE = TABLE + "." + KEY_LONGITUDE;
	public static final String CONCRETE_POSTAL_CODE = TABLE + "." + KEY_POSTAL_CODE;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, "
			+ "line_one text, line_two text, line_three text, "
			+ "feature text, admin text, locality text, thoroughfare text, "
			+ "country_code text, country_name text, latitude real, longitude real, "
			+ "postal_code text);";

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

		return new String[] {KEY_ID, KEY_LINE_ONE, KEY_LINE_TWO, KEY_LINE_THREE,
				KEY_FEATURE, KEY_ADMIN, KEY_LOCALITY, KEY_THOROUGHFARE, KEY_COUNTRY_CODE,
				KEY_COUNTRY_NAME, KEY_LATITUDE, KEY_LONGITUDE, KEY_POSTAL_CODE};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_LINE_ONE, CONCRETE_LINE_TWO, CONCRETE_LINE_THREE,
				CONCRETE_FEATURE, CONCRETE_ADMIN, CONCRETE_LOCALITY, CONCRETE_THOROUGHFARE, CONCRETE_COUNTRY_CODE,
				CONCRETE_COUNTRY_NAME, CONCRETE_LATITUDE, CONCRETE_LONGITUDE, CONCRETE_POSTAL_CODE};
	}

	public static String[] detailsColumnProjection() {

		return new String[] {KEY_ID, KEY_LINE_ONE, KEY_LINE_TWO, KEY_LINE_THREE};
	}
}
