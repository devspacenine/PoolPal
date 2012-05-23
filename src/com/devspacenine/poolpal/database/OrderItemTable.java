package com.devspacenine.poolpal.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrderItemTable {

	public static final String TABLE = "order_item";

	// Column keys
	public static final String KEY_ID = "_id";
	public static final String KEY_ITEM = "item_id";
	public static final String KEY_ORDER = "order_id";
	public static final String KEY_CART_ITEM_ID = "cart_item_id";
	public static final String KEY_ASIN = "asin";
	public static final String KEY_OFFER_LISTING_ID = "offer_listing_id";
	public static final String KEY_SELLER_NICKNAME = "seller_nickname";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_TITLE = "title";
	public static final String KEY_PRODUCT_GROUP = "product_group";
	public static final String KEY_PRICE = "price";
	public static final String KEY_TOTAL = "total";
	public static final String KEY_FORMATTED_PRICE = "formatted_price";
	public static final String KEY_FORMATTED_TOTAL = "formatted_total";
	public static final String KEY_CURRENCY_CODE = "currency_code";

	// Concrete keys
	public static final String CONCRETE_ID = TABLE + "." + KEY_ID;
	public static final String CONCRETE_ITEM = TABLE + "." + KEY_ITEM;
	public static final String CONCRETE_ORDER = TABLE + "." + KEY_ORDER;
	public static final String CONCRETE_CART_ITEM_ID = TABLE + "." + KEY_CART_ITEM_ID;
	public static final String CONCRETE_ASIN = TABLE + "." + KEY_ASIN;
	public static final String CONCRETE_OFFER_LISTING_ID = TABLE + "." + KEY_OFFER_LISTING_ID;
	public static final String CONCRETE_SELLER_NICKNAME = TABLE + "." + KEY_SELLER_NICKNAME;
	public static final String CONCRETE_QUANTITY = TABLE + "." + KEY_QUANTITY;
	public static final String CONCRETE_TITLE = TABLE + "." + KEY_TITLE;
	public static final String CONCRETE_PRODUCT_GROUP = TABLE + "." + KEY_PRODUCT_GROUP;
	public static final String CONCRETE_PRICE = TABLE + "." + KEY_PRICE;
	public static final String CONCRETE_TOTAL = TABLE + "." + KEY_TOTAL;
	public static final String CONCRETE_FORMATTED_PRICE = TABLE + "." + KEY_FORMATTED_PRICE;
	public static final String CONCRETE_FORMATTED_TOTAL = TABLE + "." + KEY_FORMATTED_TOTAL;
	public static final String CONCRETE_CURRENCY_CODE = TABLE + "." + KEY_CURRENCY_CODE;

	private static final String DATABASE_CREATE = "create table " + TABLE
			+ " (_id integer primary key autoincrement, "
			+ "item_id integer not null default 0, "
			+ "order_id integer not null default 0, "
			+ "cart_item_id text, "
			+ "asin text, "
			+ "offer_listing_id text, "
			+ "seller_nickname text, "
			+ "quantity integer not null default 0, "
			+ "title text not null, "
			+ "product_group text, "
			+ "price real not null, "
			+ "total real not null, "
			+ "formatted_price text not null, "
			+ "formatted_total text not null, "
			+ "currency_code text not null);";

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

		return new String[] {KEY_ID, KEY_ITEM, KEY_ORDER, KEY_CART_ITEM_ID,
				KEY_ASIN, KEY_OFFER_LISTING_ID, KEY_SELLER_NICKNAME, KEY_QUANTITY,
				KEY_TITLE, KEY_PRODUCT_GROUP, KEY_PRICE, KEY_TOTAL, KEY_FORMATTED_PRICE,
				KEY_FORMATTED_TOTAL, KEY_CURRENCY_CODE};
	}

	/**
	 * Returns an ordered list of concrete column keys
	 *
	 * @return - String[] a list of column keys for this database
	 */
	public static String[] concreteColumnProjection() {

		return new String[] {CONCRETE_ID, CONCRETE_ITEM, CONCRETE_ORDER, CONCRETE_CART_ITEM_ID,
				CONCRETE_ASIN, CONCRETE_OFFER_LISTING_ID, CONCRETE_SELLER_NICKNAME, CONCRETE_QUANTITY,
				CONCRETE_TITLE, CONCRETE_PRODUCT_GROUP, CONCRETE_PRICE, CONCRETE_TOTAL, CONCRETE_FORMATTED_PRICE,
				CONCRETE_FORMATTED_TOTAL, CONCRETE_CURRENCY_CODE};
	}
}
