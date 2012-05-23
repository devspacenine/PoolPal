package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrderTable {

	public static final String TABLE = "store_order";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_ORDER_NUMBER = "order_number";
	public static final String KEY_DATE = "date";
	public static final String KEY_SUBTOTAL = "subtotal";
	public static final String KEY_TAX = "tax";
	public static final String KEY_SHIPPING = "shipping";
	public static final String KEY_SAVINGS = "savings";
	public static final String KEY_TOTAL = "total";
	public static final String KEY_FORMATTED_SUBTOTAL = "formatted_subtotal";
	public static final String KEY_FORMATTED_TAX = "formatted_tax";
	public static final String KEY_FORMATTED_SHIPPING = "formatted_shipping";
	public static final String KEY_FORMATTED_SAVINGS = "formatted_savings";
	public static final String KEY_FORMATTED_TOTAL = "formatted_total";
	public static final String KEY_CART_ID = "cart_id";
	public static final String KEY_HMAC = "hmac";
	public static final String KEY_PURCHASE_URL = "purchase_url";
	public static final String KEY_CURRENCY_CODE = "currency_code";
	public static final String KEY_FAVORITE = "favorite";
	public static final String KEY_TITLE = "title";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_ORDER_NUMBER = TABLE + "." + KEY_ORDER_NUMBER;
	public static final String CONCRETE_DATE = TABLE + "." + KEY_DATE;
	public static final String CONCRETE_SUBTOTAL = TABLE + "." + KEY_SUBTOTAL;
	public static final String CONCRETE_TAX = TABLE + "." + KEY_TAX;
	public static final String CONCRETE_SHIPPING = TABLE + "." + KEY_SHIPPING;
	public static final String CONCRETE_SAVINGS = TABLE + "." + KEY_SAVINGS;
	public static final String CONCRETE_TOTAL = TABLE + "." + KEY_TOTAL;
	public static final String CONCRETE_FORMATTED_SUBTOTAL = TABLE + "." + KEY_FORMATTED_SUBTOTAL;
	public static final String CONCRETE_FORMATTED_TAX = TABLE + "." + KEY_FORMATTED_TAX;
	public static final String CONCRETE_FORMATTED_SHIPPING = TABLE + "." + KEY_FORMATTED_SHIPPING;
	public static final String CONCRETE_FORMATTED_SAVINGS = TABLE + "." + KEY_FORMATTED_SAVINGS;
	public static final String CONCRETE_FORMATTED_TOTAL = TABLE + "." + KEY_FORMATTED_TOTAL;
	public static final String CONCRETE_CART_ID = TABLE + "." + KEY_CART_ID;
	public static final String CONCRETE_HMAC = TABLE + "." + KEY_HMAC;
	public static final String CONCRETE_PURCHASE_URL = TABLE + "." + KEY_PURCHASE_URL;
	public static final String CONCRETE_CURRENCY_CODE = TABLE + "." + KEY_CURRENCY_CODE;
	public static final String CONCRETE_FAVORITE = TABLE + "." + KEY_FAVORITE;
	public static final String CONCRETE_TITLE = TABLE + "." + KEY_TITLE;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, order_number text, "
			+ "date text, subtotal real not null, tax real not null, "
			+ "shipping real not null, savings real not null, total real not null, "
			+ "formatted_subtotal text not null, formatted_tax text not null, "
			+ "formatted_shipping text not null, formatted_savings text not null, "
			+ "formatted_total text not null, cart_id text, hmac text, "
			+ "purchase_url text, currency_code text not null, "
			+ "favorite integer not null, title text);";

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

		return new String[] {KEY_ID, KEY_ORDER_NUMBER, KEY_DATE, KEY_SUBTOTAL,
				KEY_TAX, KEY_SHIPPING, KEY_SAVINGS, KEY_TOTAL, KEY_FORMATTED_SUBTOTAL,
				KEY_FORMATTED_TAX, KEY_FORMATTED_SHIPPING, KEY_FORMATTED_SAVINGS,
				KEY_FORMATTED_TOTAL, KEY_CART_ID, KEY_HMAC, KEY_PURCHASE_URL,
				KEY_CURRENCY_CODE, KEY_FAVORITE, KEY_TITLE};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_ORDER_NUMBER, CONCRETE_DATE, CONCRETE_SUBTOTAL,
				CONCRETE_TAX, CONCRETE_SHIPPING, CONCRETE_SAVINGS, CONCRETE_TOTAL, CONCRETE_FORMATTED_SUBTOTAL,
				CONCRETE_FORMATTED_TAX, CONCRETE_FORMATTED_SHIPPING, CONCRETE_FORMATTED_SAVINGS,
				CONCRETE_FORMATTED_TOTAL, CONCRETE_CART_ID, CONCRETE_HMAC, CONCRETE_PURCHASE_URL,
				CONCRETE_CURRENCY_CODE, CONCRETE_FAVORITE, CONCRETE_TITLE};
	}
}
