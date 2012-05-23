package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TreatmentTable {

	public static final String TABLE = "treatment";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_POOL = "pool_id";
	public static final String KEY_TASK = "task_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_CHLORINE_TABS = "chlorine_tabs";
	public static final String KEY_BROMINE_TABS = "bromine_tabs";
	public static final String KEY_SANITIZER_SIZE = "sanitizer_size";
	public static final String KEY_CHLORINE_LIQUID = "chlorine_liquid";
	public static final String KEY_CHLORINE_GRANULES = "chlorine_granules";
	public static final String KEY_BROMINE_GRANULES = "bromine_granules";
	public static final String KEY_SALT = "salt";
	public static final String KEY_CHLORINE_SHOCK = "chlorine_shock";
	public static final String KEY_NON_CHLORINE_SHOCK = "non_chlorine_shock";
	public static final String KEY_QUAT_ALGAECIDE = "quat_algaecide";
	public static final String KEY_POLYQUAT_ALGAECIDE = "polyquat_algaecide";
	public static final String KEY_METAL_ALGAECIDE = "metal_algaecide";
	public static final String KEY_OTHER_ALGAECIDE = "other_algaecide";
	public static final String KEY_PHOSPHATE_REMOVER = "phosphate_remover";
	public static final String KEY_SODIUM_CARBONATE = "sodium_carbonate";
	public static final String KEY_OTHER_PH_INCREASER = "other_ph_increaser";
	public static final String KEY_BORAX = "borax";
	public static final String KEY_BORIC_ACID = "boric_acid";
	public static final String KEY_SODIUM_BISULFATE = "sodium_bisulfate";
	public static final String KEY_ACID = "acid";
	public static final String KEY_OTHER_PH_REDUCER = "other_ph_reducer";
	public static final String KEY_HARDNESS_INCREASER = "hardness_increaser";
	public static final String KEY_STABILIZER = "stabilizer";
	public static final String KEY_DIATOMACEOUS_EARTH = "diatomaceous_earth";
	public static final String KEY_NET_RAKED = "net_raked";
	public static final String KEY_SKIMMER_CLEANED = "skimmer_cleaned";
	public static final String KEY_PUMP_BASKET_CLEANED = "pump_basket_cleaned";
	public static final String KEY_CLEANER_EMPTIED = "cleaner_emptied";
	public static final String KEY_VACUUMED = "vacuumed";
	public static final String KEY_BRUSHED = "brushed";
	public static final String KEY_BACKWASHED = "backwashed";
	public static final String KEY_FILTER_WASHED = "filter_washed";
	public static final String KEY_DRAINED = "drained";
	public static final String KEY_WATER_ADDED = "water_added";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_POOL = TABLE + "." + KEY_POOL;
	public static final String CONCRETE_TASK = TABLE + "." + KEY_TASK;
	public static final String CONCRETE_DATE = TABLE + "." + KEY_DATE;
	public static final String CONCRETE_CHLORINE_TABS = TABLE + "." + KEY_CHLORINE_TABS;
	public static final String CONCRETE_BROMINE_TABS = TABLE + "." + KEY_BROMINE_TABS;
	public static final String CONCRETE_SANITIZER_SIZE = TABLE + "." + KEY_SANITIZER_SIZE;
	public static final String CONCRETE_CHLORINE_LIQUID = TABLE + "." + KEY_CHLORINE_LIQUID;
	public static final String CONCRETE_CHLORINE_GRANULES = TABLE + "." + KEY_CHLORINE_GRANULES;
	public static final String CONCRETE_BROMINE_GRANULES = TABLE + "." + KEY_BROMINE_GRANULES;
	public static final String CONCRETE_SALT = TABLE + "." + KEY_SALT;
	public static final String CONCRETE_CHLORINE_SHOCK = TABLE + "." + KEY_CHLORINE_SHOCK;
	public static final String CONCRETE_NON_CHLORINE_SHOCK = TABLE + "." + KEY_NON_CHLORINE_SHOCK;
	public static final String CONCRETE_QUAT_ALGAECIDE = TABLE + "." + KEY_QUAT_ALGAECIDE;
	public static final String CONCRETE_POLYQUAT_ALGAECIDE = TABLE + "." + KEY_POLYQUAT_ALGAECIDE;
	public static final String CONCRETE_METAL_ALGAECIDE = TABLE + "." + KEY_METAL_ALGAECIDE;
	public static final String CONCRETE_OTHER_ALGAECIDE = TABLE + "." + KEY_OTHER_ALGAECIDE;
	public static final String CONCRETE_PHOSPHATE_REMOVER = TABLE + "." + KEY_PHOSPHATE_REMOVER;
	public static final String CONCRETE_SODIUM_CARBONATE = TABLE + "." + KEY_SODIUM_CARBONATE;
	public static final String CONCRETE_OTHER_PH_INCREASER = TABLE + "." + KEY_OTHER_PH_INCREASER;
	public static final String CONCRETE_BORAX = TABLE + "." + KEY_BORAX;
	public static final String CONCRETE_BORIC_ACID = TABLE + "." + KEY_BORIC_ACID;
	public static final String CONCRETE_SODIUM_BISULFATE = TABLE + "." + KEY_SODIUM_BISULFATE;
	public static final String CONCRETE_ACID = TABLE + "." + KEY_ACID;
	public static final String CONCRETE_OTHER_PH_REDUCER = TABLE + "." + KEY_OTHER_PH_REDUCER;
	public static final String CONCRETE_HARDNESS_INCREASER = TABLE + "." + KEY_HARDNESS_INCREASER;
	public static final String CONCRETE_STABILIZER = TABLE + "." + KEY_STABILIZER;
	public static final String CONCRETE_DIATOMACEOUS_EARTH = TABLE + "." + KEY_DIATOMACEOUS_EARTH;
	public static final String CONCRETE_NET_RAKED = TABLE + "." + KEY_NET_RAKED;
	public static final String CONCRETE_SKIMMER_CLEANED = TABLE + "." + KEY_SKIMMER_CLEANED;
	public static final String CONCRETE_PUMP_BASKET_CLEANED = TABLE + "." + KEY_PUMP_BASKET_CLEANED;
	public static final String CONCRETE_CLEANER_EMPTIED = TABLE + "." + KEY_CLEANER_EMPTIED;
	public static final String CONCRETE_VACUUMED = TABLE + "." + KEY_VACUUMED;
	public static final String CONCRETE_BRUSHED = TABLE + "." + KEY_BRUSHED;
	public static final String CONCRETE_BACKWASHED = TABLE + "." + KEY_BACKWASHED;
	public static final String CONCRETE_FILTER_WASHED = TABLE + "." + KEY_FILTER_WASHED;
	public static final String CONCRETE_DRAINED = TABLE + "." + KEY_DRAINED;
	public static final String CONCRETE_WATER_ADDED = TABLE + "." + KEY_WATER_ADDED;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, pool_id integer not null, task_id integer, "
			+ "date text not null, chlorine_tabs integer, bromine_tabs integer, sanitizer_size text, "
			+ "chlorine_liquid real, chlorine_granules real, bromine_granules real, salt real, "
			+ "chlorine_shock real, non_chlorine_shock real, quat_algaecide real, "
			+ "polyquat_algaecide real, metal_algaecide real, other_algaecide real, "
			+ "phosphate_remover real, sodium_carbonate real, other_ph_increaser real, borax real, "
			+ "boric_acid real, sodium_bisulfate real, acid real, other_ph_reducer real, "
			+ "hardness_increaser real, stabilizer real, diatomaceous_earth real, "
			+ "net_raked integer not null, skimmer_cleaned integer not null, "
			+ "pump_basket_cleaned integer not null, cleaner_emptied integer not null, "
			+ "vacuumed integer not null, brushed integer not null, backwashed integer not null, "
			+ "filter_washed integer not null, drained integer not null, water_added integer not null);";

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

		return new String[] {KEY_ID, KEY_POOL, KEY_TASK, KEY_DATE, KEY_CHLORINE_TABS,
				KEY_BROMINE_TABS, KEY_SANITIZER_SIZE, KEY_CHLORINE_LIQUID,
				KEY_CHLORINE_GRANULES, KEY_BROMINE_GRANULES, KEY_SALT,
				KEY_CHLORINE_SHOCK, KEY_NON_CHLORINE_SHOCK, KEY_QUAT_ALGAECIDE,
				KEY_POLYQUAT_ALGAECIDE, KEY_METAL_ALGAECIDE, KEY_OTHER_ALGAECIDE,
				KEY_PHOSPHATE_REMOVER, KEY_SODIUM_CARBONATE, KEY_OTHER_PH_INCREASER,
				KEY_BORAX, KEY_BORIC_ACID, KEY_SODIUM_BISULFATE, KEY_ACID,
				KEY_OTHER_PH_REDUCER, KEY_HARDNESS_INCREASER, KEY_STABILIZER,
				KEY_DIATOMACEOUS_EARTH, KEY_NET_RAKED, KEY_SKIMMER_CLEANED,
				KEY_PUMP_BASKET_CLEANED, KEY_CLEANER_EMPTIED, KEY_VACUUMED, KEY_BRUSHED,
				KEY_BACKWASHED, KEY_FILTER_WASHED, KEY_DRAINED, KEY_WATER_ADDED};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_POOL, CONCRETE_TASK, CONCRETE_DATE, CONCRETE_CHLORINE_TABS,
				CONCRETE_BROMINE_TABS, CONCRETE_SANITIZER_SIZE, CONCRETE_CHLORINE_LIQUID,
				CONCRETE_CHLORINE_GRANULES, CONCRETE_BROMINE_GRANULES, CONCRETE_SALT,
				CONCRETE_CHLORINE_SHOCK, CONCRETE_NON_CHLORINE_SHOCK, CONCRETE_QUAT_ALGAECIDE,
				CONCRETE_POLYQUAT_ALGAECIDE, CONCRETE_METAL_ALGAECIDE, CONCRETE_OTHER_ALGAECIDE,
				CONCRETE_PHOSPHATE_REMOVER, CONCRETE_SODIUM_CARBONATE, CONCRETE_OTHER_PH_INCREASER,
				CONCRETE_BORAX, CONCRETE_BORIC_ACID, CONCRETE_SODIUM_BISULFATE, CONCRETE_ACID,
				CONCRETE_OTHER_PH_REDUCER, CONCRETE_HARDNESS_INCREASER, CONCRETE_STABILIZER,
				CONCRETE_DIATOMACEOUS_EARTH, CONCRETE_NET_RAKED, CONCRETE_SKIMMER_CLEANED,
				CONCRETE_PUMP_BASKET_CLEANED, CONCRETE_CLEANER_EMPTIED, CONCRETE_VACUUMED, CONCRETE_BRUSHED,
				CONCRETE_BACKWASHED, CONCRETE_FILTER_WASHED, CONCRETE_DRAINED, CONCRETE_WATER_ADDED};
	}
}
