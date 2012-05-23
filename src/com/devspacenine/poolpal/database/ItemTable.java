package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemTable {

	public static final String TABLE = "item";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_ASIN = "asin";
	public static final String KEY_EAN = "ean";
	public static final String KEY_ISBN = "isbn";
	public static final String KEY_EISBN = "eisbn";
	public static final String KEY_MPN = "mpn";
	public static final String KEY_SKU = "sku";
	public static final String KEY_TITLE = "title";
	public static final String KEY_PRODUCT_GROUP = "product_group";
	public static final String KEY_MANUFACTURER = "manufacturer";
	public static final String KEY_BRAND = "brand";
	public static final String KEY_MODEL = "model";
	public static final String KEY_LIST_PRICE = "list_price";
	public static final String KEY_FORMATTED_LIST_PRICE = "formatted_list_price";
	public static final String KEY_PRICE = "price";
	public static final String KEY_FORMATTED_PRICE = "formatted_price";
	public static final String KEY_CURRENCY_CODE = "currency_code";
	public static final String KEY_RATING = "rating";
	public static final String KEY_URL = "url";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_ASIN = TABLE + "." + KEY_ASIN;
	public static final String CONCRETE_EAN = TABLE + "." + KEY_EAN;
	public static final String CONCRETE_ISBN = TABLE + "." + KEY_ISBN;
	public static final String CONCRETE_EISBN = TABLE + "." + KEY_EISBN;
	public static final String CONCRETE_MPN = TABLE + "." + KEY_MPN;
	public static final String CONCRETE_SKU = TABLE + "." + KEY_SKU;
	public static final String CONCRETE_TITLE = TABLE + "." + KEY_TITLE;
	public static final String CONCRETE_PRODUCT_GROUP = TABLE + "." + KEY_PRODUCT_GROUP;
	public static final String CONCRETE_MANUFACTURER = TABLE + "." + KEY_MANUFACTURER;
	public static final String CONCRETE_BRAND = TABLE + "." + KEY_BRAND;
	public static final String CONCRETE_MODEL = TABLE + "." + KEY_MODEL;
	public static final String CONCRETE_LIST_PRICE = TABLE + "." + KEY_LIST_PRICE;
	public static final String CONCRETE_FORMATTED_LIST_PRICE = TABLE + "." + KEY_FORMATTED_LIST_PRICE;
	public static final String CONCRETE_PRICE = TABLE + "." + KEY_PRICE;
	public static final String CONCRETE_FORMATTED_PRICE = TABLE + "." + KEY_FORMATTED_PRICE;
	public static final String CONCRETE_CURRENCY_CODE = TABLE + "." + KEY_CURRENCY_CODE;
	public static final String CONCRETE_RATING = TABLE + "." + KEY_RATING;
	public static final String CONCRETE_URL = TABLE + "." + KEY_URL;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, asin text not null, ean text, "
			+ "isbn text, eisbn text, mpn text, sku text, "
			+ "title text not null, product_group text, manufacturer text, "
			+ "brand text, model text, list_price real, "
			+ "formatted_list_price text, price real not null, "
			+ "formatted_price text not null, currency_code text not null, rating real, "
			+ "url text not null);";

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

		return new String[] {KEY_ID, KEY_ASIN, KEY_EAN, KEY_ISBN, KEY_EISBN, KEY_MPN,
				KEY_SKU, KEY_TITLE, KEY_PRODUCT_GROUP, KEY_MANUFACTURER, KEY_BRAND,
				KEY_MODEL, KEY_LIST_PRICE, KEY_FORMATTED_LIST_PRICE, KEY_PRICE,
				KEY_FORMATTED_PRICE, KEY_CURRENCY_CODE, KEY_RATING, KEY_URL};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_ASIN, CONCRETE_EAN, CONCRETE_ISBN, CONCRETE_EISBN, CONCRETE_MPN,
				CONCRETE_SKU, CONCRETE_TITLE, CONCRETE_PRODUCT_GROUP, CONCRETE_MANUFACTURER, CONCRETE_BRAND,
				CONCRETE_MODEL, CONCRETE_LIST_PRICE, CONCRETE_FORMATTED_LIST_PRICE, CONCRETE_PRICE,
				CONCRETE_FORMATTED_PRICE, CONCRETE_CURRENCY_CODE, CONCRETE_RATING, CONCRETE_URL};
	}
}
