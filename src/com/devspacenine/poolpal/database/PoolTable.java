package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PoolTable {

	public static final String TABLE = "pool";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_ADDRESS = "address_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_VOLUME = "volume";
	public static final String KEY_POOL_LOCALE = "pool_locale";
	public static final String KEY_FINISH = "finish";
	public static final String KEY_SANITIZER = "sanitizer";
	public static final String KEY_PUMP_BRAND = "pump_brand";
	public static final String KEY_PUMP_MODEL = "pump_model";
	public static final String KEY_FILTER = "filter";
	public static final String KEY_CLEANER_BRAND = "cleaner_brand";
	public static final String KEY_CLEANER_MODEL = "cleaner_model";
	public static final String KEY_TRAFFIC = "traffic";
	public static final String KEY_MIN_DEPTH = "min_depth";
	public static final String KEY_MAX_DEPTH = "max_depth";
	public static final String KEY_TILING = "tiling";
	public static final String KEY_COVER = "cover";
	public static final String KEY_ATTACHED_SPA = "attached_spa";
	public static final String KEY_HEATER = "heater";
	public static final String KEY_DIVING_BOARD = "diving_board";
	public static final String KEY_SLIDE = "slide";
	public static final String KEY_LADDER = "ladder";
	public static final String KEY_FOUNTAINS = "fountains";
	public static final String KEY_ROCK_WATERFALL = "rock_waterfall";
	public static final String KEY_LIGHTS = "lights";
	public static final String KEY_INFINITY = "infinity";
	public static final String KEY_SPORTING_EQUIPMENT = "sporting_equipment";
	public static final String KEY_BEACH_ENTRY = "beach_entry";
	public static final String KEY_SAND = "sand";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_WEATHER_NOTIFICATIONS = "weather_notifications";
	public static final String KEY_WATER_TEST_REMINDERS = "water_test_reminders";
	public static final String KEY_FILTER_REMINDERS = "filter_reminders";
	public static final String KEY_SAFETY_NOTIFICATIONS = "safety_notifications";
	public static final String KEY_MAINTENANCE_REMINDERS = "maintenance_reminders";
	public static final String KEY_CUSTOM_NOTIFICATIONS = "custom_notifications";
	public static final String KEY_COUPON_NOTIFICATIONS = "coupon_notifications";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_ADDRESS = TABLE + "." + KEY_ADDRESS;
	public static final String CONCRETE_NAME = TABLE + "." + KEY_NAME;
	public static final String CONCRETE_VOLUME = TABLE + "." + KEY_VOLUME;
	public static final String CONCRETE_POOL_LOCALE = TABLE + "." + KEY_POOL_LOCALE;
	public static final String CONCRETE_FINISH = TABLE + "." + KEY_FINISH;
	public static final String CONCRETE_SANITIZER = TABLE + "." + KEY_SANITIZER;
	public static final String CONCRETE_PUMP_BRAND = TABLE + "." + KEY_PUMP_BRAND;
	public static final String CONCRETE_PUMP_MODEL = TABLE + "." + KEY_PUMP_MODEL;
	public static final String CONCRETE_FILTER = TABLE + "." + KEY_FILTER;
	public static final String CONCRETE_CLEANER_BRAND = TABLE + "." + KEY_CLEANER_BRAND;
	public static final String CONCRETE_CLEANER_MODEL = TABLE + "." + KEY_CLEANER_MODEL;
	public static final String CONCRETE_TRAFFIC = TABLE + "." + KEY_TRAFFIC;
	public static final String CONCRETE_MIN_DEPTH = TABLE + "." + KEY_MIN_DEPTH;
	public static final String CONCRETE_MAX_DEPTH = TABLE + "." + KEY_MAX_DEPTH;
	public static final String CONCRETE_TILING = TABLE + "." + KEY_TILING;
	public static final String CONCRETE_COVER = TABLE + "." + KEY_COVER;
	public static final String CONCRETE_ATTACHED_SPA = TABLE + "." + KEY_ATTACHED_SPA;
	public static final String CONCRETE_HEATER = TABLE + "." + KEY_HEATER;
	public static final String CONCRETE_DIVING_BOARD = TABLE + "." + KEY_DIVING_BOARD;
	public static final String CONCRETE_SLIDE = TABLE + "." + KEY_SLIDE;
	public static final String CONCRETE_LADDER = TABLE + "." + KEY_LADDER;
	public static final String CONCRETE_FOUNTAINS = TABLE + "." + KEY_FOUNTAINS;
	public static final String CONCRETE_ROCK_WATERFALL = TABLE + "." + KEY_ROCK_WATERFALL;
	public static final String CONCRETE_LIGHTS = TABLE + "." + KEY_LIGHTS;
	public static final String CONCRETE_INFINITY = TABLE + "." + KEY_INFINITY;
	public static final String CONCRETE_SPORTING_EQUIPMENT = TABLE + "." + KEY_SPORTING_EQUIPMENT;
	public static final String CONCRETE_BEACH_ENTRY = TABLE + "." + KEY_BEACH_ENTRY;
	public static final String CONCRETE_SAND = TABLE + "." + KEY_SAND;
	public static final String CONCRETE_IMAGE = TABLE + "." + KEY_IMAGE;
	public static final String CONCRETE_WEATHER_NOTIFICATIONS = TABLE + "." + KEY_WEATHER_NOTIFICATIONS;
	public static final String CONCRETE_WATER_TEST_REMINDERS = TABLE + "." + KEY_WATER_TEST_REMINDERS;
	public static final String CONCRETE_FILTER_REMINDERS = TABLE + "." + KEY_FILTER_REMINDERS;
	public static final String CONCRETE_SAFETY_NOTIFICATIONS = TABLE + "." + KEY_SAFETY_NOTIFICATIONS;
	public static final String CONCRETE_MAINTENANCE_REMINDERS = TABLE + "." + KEY_MAINTENANCE_REMINDERS;
	public static final String CONCRETE_CUSTOM_NOTIFICATIONS = TABLE + "." + KEY_CUSTOM_NOTIFICATIONS;
	public static final String CONCRETE_COUPON_NOTIFICATIONS = TABLE + "." + KEY_COUPON_NOTIFICATIONS;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, address_id integer default 0, name text not null default \'My Pool\', "
			+ "volume real not null default -1, pool_locale text, finish text, sanitizer text, "
			+ "pump_brand text, pump_model text, filter text, cleaner_brand text, cleaner_model text, "
			+ "traffic text not null default \'Private - Family & Friends\', min_depth real not null default 3, max_depth real not null default 9, "
			+ "tiling integer not null default 0, cover integer not null default 0, "
			+ "attached_spa integer not null default 0, heater integer not null default 0, diving_board integer not null default 0, "
			+ "slide integer not null default 0, ladder integer not null default 0, fountains integer not null default 0, "
			+ "rock_waterfall integer not null default 0, lights integer not null default 0, infinity integer not null default 0, "
			+ "sporting_equipment integer not null default 0, beach_entry integer not null default 0, "
			+ "sand integer not null default 0, image text, weather_notifications integer not null default 1, "
			+ "water_test_reminders integer not null default 1, filter_reminders integer not null default 1, "
			+ "safety_notifications integer not null default 1, maintenance_reminders integer not null default 1, "
			+ "custom_notifications integer not null default 1, coupon_notifications integer not null default 1);";

	public static void onCreate(SQLiteDatabase database) {

		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		Log.w(PoolTable.class.getName(), "Upgrading database from version "
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

		return new String[] {KEY_ID, KEY_ADDRESS, KEY_NAME, KEY_VOLUME, KEY_POOL_LOCALE,
				KEY_FINISH, KEY_SANITIZER, KEY_PUMP_BRAND, KEY_PUMP_MODEL, KEY_FILTER,
				KEY_CLEANER_BRAND, KEY_CLEANER_MODEL, KEY_TRAFFIC,
				KEY_MIN_DEPTH, KEY_MAX_DEPTH, KEY_TILING, KEY_COVER, KEY_ATTACHED_SPA,
				KEY_HEATER, KEY_DIVING_BOARD, KEY_SLIDE, KEY_LADDER, KEY_FOUNTAINS,
				KEY_ROCK_WATERFALL, KEY_LIGHTS, KEY_INFINITY, KEY_SPORTING_EQUIPMENT, KEY_BEACH_ENTRY,
				KEY_SAND, KEY_IMAGE, KEY_WEATHER_NOTIFICATIONS, KEY_WATER_TEST_REMINDERS, KEY_FILTER_REMINDERS,
				KEY_SAFETY_NOTIFICATIONS, KEY_MAINTENANCE_REMINDERS, KEY_CUSTOM_NOTIFICATIONS,
				KEY_COUPON_NOTIFICATIONS};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_ADDRESS, CONCRETE_NAME, CONCRETE_VOLUME, CONCRETE_POOL_LOCALE,
				CONCRETE_FINISH, CONCRETE_SANITIZER, CONCRETE_PUMP_BRAND, CONCRETE_PUMP_MODEL,
				CONCRETE_FILTER, CONCRETE_CLEANER_BRAND, CONCRETE_CLEANER_MODEL, CONCRETE_TRAFFIC,
				CONCRETE_MIN_DEPTH, CONCRETE_MAX_DEPTH, CONCRETE_TILING, CONCRETE_COVER, CONCRETE_ATTACHED_SPA,
				CONCRETE_HEATER, CONCRETE_DIVING_BOARD, CONCRETE_SLIDE, CONCRETE_LADDER, CONCRETE_FOUNTAINS,
				CONCRETE_ROCK_WATERFALL, CONCRETE_LIGHTS, CONCRETE_INFINITY, CONCRETE_SPORTING_EQUIPMENT,
				CONCRETE_BEACH_ENTRY, CONCRETE_SAND, CONCRETE_IMAGE, CONCRETE_WEATHER_NOTIFICATIONS,
				CONCRETE_WATER_TEST_REMINDERS, CONCRETE_FILTER_REMINDERS,
				CONCRETE_SAFETY_NOTIFICATIONS, CONCRETE_MAINTENANCE_REMINDERS, CONCRETE_CUSTOM_NOTIFICATIONS,
				CONCRETE_COUPON_NOTIFICATIONS};
	}

	/**
	 * Returns an ordered list of column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] detailsColumnProjection() {

		return new String[] {KEY_ID, KEY_ADDRESS, KEY_NAME, KEY_VOLUME};
	}
}
