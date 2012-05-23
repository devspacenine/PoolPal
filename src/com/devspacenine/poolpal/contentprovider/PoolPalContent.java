package com.devspacenine.poolpal.contentprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.devspacenine.poolpal.database.AddressTable;
import com.devspacenine.poolpal.database.ItemTable;
import com.devspacenine.poolpal.database.OrderItemTable;
import com.devspacenine.poolpal.database.OrderTable;
import com.devspacenine.poolpal.database.PoolPalDatabaseHelper;
import com.devspacenine.poolpal.database.PoolTable;
import com.devspacenine.poolpal.database.ReminderTable;
import com.devspacenine.poolpal.database.TaskTable;
import com.devspacenine.poolpal.database.TimerTable;
import com.devspacenine.poolpal.database.TreatmentTable;
import com.devspacenine.poolpal.database.WaterTestTable;

public class PoolPalContent extends ContentProvider {

	private PoolPalDatabaseHelper database;

	public static final Pattern ON_PATTERN = Pattern.compile("^([a-zA-z][a-zA-Z_0-9]*)"
			+ "\\.([a-zA-z][a-zA-Z_0-9]*)=([a-zA-z][a-zA-Z_0-9]*)\\.([a-zA-z][a-zA-Z_0-9]*)$");
	public static final Pattern QUERY_SELECTION_PATTERN = Pattern.compile("(?:\\s(AND|OR)\\s)?([a-zA-z][a-zA-Z_0-9]*)"
			+ "\\.([a-zA-z][a-zA-Z_0-9]*)=\\?", Pattern.CASE_INSENSITIVE);
	public static final Pattern QUERY_SORT_ORDER_PATTERN = Pattern.compile("(?:(,)\\s?)?([a-zA-z][a-zA-Z_0-9]*)"
			+ "\\.([a-zA-z][a-zA-Z_0-9]*)(?:\\s(ASC|DESC))?", Pattern.CASE_INSENSITIVE);

	public static final String[] TABLE_ALIASES = new String[] {"a", "b", "c",
		"d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};

	private static final int POOLS = 1010;
	private static final int POOL_ID = 1011;
	private static final int POOL_ID_TASKS = 1012;
	private static final int POOL_ID_WATER_TESTS = 1013;
	private static final int POOL_ID_TREATMENTS = 1014;
	private static final int TASKS = 1020;
	private static final int TASK_ID = 1021;
	private static final int TASKS_FOR_POOLS = 1022;
	private static final int ADDRESSES = 1030;
	private static final int ADDRESS_ID = 1031;
	private static final int REMINDERS = 1040;
	private static final int REMINDER_ID = 1041;
	private static final int TIMERS = 1050;
	private static final int TIMER_ID = 1051;
	private static final int ORDERS = 1060;
	private static final int ORDERS_FAVORITES = 1061;
	private static final int ORDER_ID = 1062;
	private static final int ORDER_ID_ITEMS = 1063;
	private static final int ORDER_ITEMS = 1070;
	private static final int ORDER_ITEM_ID = 1071;
	private static final int ITEMS = 1080;
	private static final int ITEM_ID = 1081;
	private static final int WATER_TESTS = 1090;
	private static final int WATER_TEST_ID = 1091;
	private static final int TREATMENTS = 1100;
	private static final int TREATMENT_ID = 1101;

	private static final String AUTHORITY = "com.devspacenine.poolpal.contentprovider";

	private static final String POOLS_PATH = "pools";
	private static final String TASKS_PATH = "tasks";
	private static final String ADDRESSES_PATH = "addresses";
	private static final String REMINDERS_PATH = "reminders";
	private static final String TIMERS_PATH = "timers";
	private static final String ORDERS_PATH = "orders";
	private static final String ORDER_ITEMS_PATH = "order-items";
	private static final String FAVORITES_PATH = "favorites";
	private static final String ITEMS_PATH = "items";
	private static final String WATER_TESTS_PATH = "water-tests";
	private static final String TREATMENTS_PATH = "treatments";

	public static final Uri POOLS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + POOLS_PATH);
	public static final Uri TASKS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TASKS_PATH);
	public static final Uri ADDRESSES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ADDRESSES_PATH);
	public static final Uri REMINDERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + REMINDERS_PATH);
	public static final Uri TIMERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TIMERS_PATH);
	public static final Uri ORDERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ORDERS_PATH);
	public static final Uri ORDER_ITEMS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ORDER_ITEMS_PATH);
	public static final Uri ITEMS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ITEMS_PATH);
	public static final Uri WATER_TESTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + WATER_TESTS_PATH);
	public static final Uri TREATMENTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TREATMENTS_PATH);

	public static final String POOL_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/pools";
	public static final String POOL_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/pool";
	public static final String TASK_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/tasks";
	public static final String TASK_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/task";
	public static final String ADDRESS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/addresses";
	public static final String ADDRESS_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/address";
	public static final String REMINDER_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/reminders";
	public static final String REMINDER_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/reminder";
	public static final String TIMER_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/timers";
	public static final String TIMER_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/timer";
	public static final String ORDER_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/orders";
	public static final String ORDER_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/order";
	public static final String ORDER_ITEM_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/order-items";
	public static final String ORDER_ITEM_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/order-item";
	public static final String ITEM_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/items";
	public static final String ITEM_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/item";
	public static final String WATER_TEST_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/water-tests";
	public static final String WATER_TEST_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/water-test";
	public static final String TREATMENT_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/treatments";
	public static final String TREATMENT_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/treatment";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, POOLS_PATH, POOLS);
		sURIMatcher.addURI(AUTHORITY, POOLS_PATH + "/#", POOL_ID);
		sURIMatcher.addURI(AUTHORITY, POOLS_PATH + "/#/" + TASKS_PATH, POOL_ID_TASKS);
		sURIMatcher.addURI(AUTHORITY, POOLS_PATH + "/#/" + WATER_TESTS_PATH, POOL_ID_WATER_TESTS);
		sURIMatcher.addURI(AUTHORITY, POOLS_PATH + "/#/" + TREATMENTS_PATH, POOL_ID_TREATMENTS);
		sURIMatcher.addURI(AUTHORITY, TASKS_PATH, TASKS);
		sURIMatcher.addURI(AUTHORITY, TASKS_PATH + "/#", TASK_ID);
		sURIMatcher.addURI(AUTHORITY, TASKS_PATH + "/" + POOLS_PATH, TASKS_FOR_POOLS);
		sURIMatcher.addURI(AUTHORITY, ADDRESSES_PATH, ADDRESSES);
		sURIMatcher.addURI(AUTHORITY, ADDRESSES_PATH + "/#", ADDRESS_ID);
		sURIMatcher.addURI(AUTHORITY, REMINDERS_PATH, REMINDERS);
		sURIMatcher.addURI(AUTHORITY, REMINDERS_PATH + "/#", REMINDER_ID);
		sURIMatcher.addURI(AUTHORITY, TIMERS_PATH, TIMERS);
		sURIMatcher.addURI(AUTHORITY, TIMERS_PATH + "/#", TIMER_ID);
		sURIMatcher.addURI(AUTHORITY, ORDERS_PATH, ORDERS);
		sURIMatcher.addURI(AUTHORITY, ORDERS_PATH + "/" + FAVORITES_PATH, ORDERS_FAVORITES);
		sURIMatcher.addURI(AUTHORITY, ORDERS_PATH + "/#", ORDER_ID);
		sURIMatcher.addURI(AUTHORITY, ORDERS_PATH + "/#/" + ORDER_ITEMS_PATH, ORDER_ID_ITEMS);
		sURIMatcher.addURI(AUTHORITY, ORDER_ITEMS_PATH, ORDER_ITEMS);
		sURIMatcher.addURI(AUTHORITY, ORDER_ITEMS_PATH + "/#", ORDER_ITEM_ID);
		sURIMatcher.addURI(AUTHORITY, ITEMS_PATH, ITEMS);
		sURIMatcher.addURI(AUTHORITY, ITEMS_PATH + "/#", ITEM_ID);
		sURIMatcher.addURI(AUTHORITY, WATER_TESTS_PATH, WATER_TESTS);
		sURIMatcher.addURI(AUTHORITY, WATER_TESTS_PATH + "/#", WATER_TEST_ID);
		sURIMatcher.addURI(AUTHORITY, TREATMENTS_PATH, TREATMENTS);
		sURIMatcher.addURI(AUTHORITY, TREATMENTS_PATH + "/#", TREATMENT_ID);
	}

	@Override
	public boolean onCreate() {

		database = new PoolPalDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Optional groupBy and having arguments
		String groupBy = null;
		String having = null;

		// Join query variables
		Boolean isRaw = false;
		String rawQuery = "";
		String[] rawSelectionArgs = new String[]{};
		LinkedHashMap<String, String[]> projections = new LinkedHashMap<String, String[]>();
		HashMap<String, String> onArgs = new HashMap<String, String>();

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case POOLS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, PoolTable.columnProjection());

			// Set the table
			queryBuilder.setTables(PoolTable.TABLE);
			break;
		case POOL_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, PoolTable.columnProjection());

			// Set the table
			queryBuilder.setTables(PoolTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(PoolTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			/*
			// Build map of tables to projections and join tables to on statements
			// Pool Table
			projections.put(PoolTable.TABLE, PoolTable.columnProjection());
			// Address Table
			projections.put(AddressTable.TABLE, AddressTable.detailsColumnProjection());
			onArgs.put(AddressTable.TABLE, PoolTable.CONCRETE_ADDRESS + "=" + AddressTable.CONCRETE_ID);

			// Turn on the raw query indicator
			isRaw = true;

			selection = PoolTable.CONCRETE_ID + "=?";

			// Use joinQuery helper to construct a raw query statement
			rawQuery = joinQuery(projections, onArgs, selection, null);

			// Set the pool id as a selection argument for this raw query
			rawSelectionArgs = new String[] {uri.getLastPathSegment()};*/
			break;
		case TASKS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, TaskTable.columnProjection());

			// Set the table
			queryBuilder.setTables(TaskTable.TABLE);
			break;
		case TASK_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, TaskTable.columnProjection());

			// Set the table
			queryBuilder.setTables(TaskTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(TaskTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case POOL_ID_TASKS:
			// Build map of tables to projections and join tables to on statements
			// Pool Table
			projections.put(TaskTable.TABLE, TaskTable.columnProjection());
			// Address Table
			projections.put(AddressTable.TABLE, AddressTable.detailsColumnProjection());
			onArgs.put(AddressTable.TABLE, TaskTable.CONCRETE_ADDRESS + "=" + AddressTable.CONCRETE_ID);
			// Task Table
			projections.put(PoolTable.TABLE, PoolTable.detailsColumnProjection());
			onArgs.put(PoolTable.TABLE, TaskTable.CONCRETE_POOL + "=" + PoolTable.CONCRETE_ID);

			// Turn on the raw query indicator
			isRaw = true;

			selection = PoolTable.CONCRETE_ID + "=?";

			// Use joinQuery helper to construct a raw query statement
			rawQuery = joinQuery(projections, onArgs, selection, sortOrder);

			ArrayList<String> pathSegments = new ArrayList<String>(uri.getPathSegments());

			// Set the pool id as a selection argument for this raw query
			rawSelectionArgs = new String[] {pathSegments.get(1)};
			break;
		case POOL_ID_WATER_TESTS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, WaterTestTable.columnProjection());

			// Set the table
			queryBuilder.setTables(WaterTestTable.TABLE);

			// Add the pool ID to the query
			queryBuilder.appendWhere(WaterTestTable.KEY_POOL + "="
					+ uri.getPathSegments().get(1));
			break;
		case POOL_ID_TREATMENTS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, TreatmentTable.columnProjection());

			// Set the table
			queryBuilder.setTables(TreatmentTable.TABLE);

			// Add the pool ID to the query
			queryBuilder.appendWhere(TreatmentTable.KEY_POOL + "="
					+ uri.getPathSegments().get(1));
			break;
		case TASKS_FOR_POOLS:
			// Build map of tables to projections and join tables to on statements
			// Pool Table
			projections.put(TaskTable.TABLE, TaskTable.columnProjection());
			// Address Table
			projections.put(AddressTable.TABLE, AddressTable.detailsColumnProjection());
			onArgs.put(AddressTable.TABLE, TaskTable.CONCRETE_ADDRESS + "=" + AddressTable.CONCRETE_ID);
			// Task Table
			projections.put(PoolTable.TABLE, PoolTable.detailsColumnProjection());
			onArgs.put(PoolTable.TABLE, TaskTable.CONCRETE_POOL + "=" + PoolTable.CONCRETE_ID);

			// Turn on the raw query indicator
			isRaw = true;

			// Use joinQuery helper to construct a raw query statement
			rawQuery = joinQuery(projections, onArgs, selection, sortOrder);

			// Set the pool id as a selection argument for this raw query
			rawSelectionArgs = selectionArgs;
			break;
		case ADDRESSES:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, AddressTable.columnProjection());

			// Set the table
			queryBuilder.setTables(AddressTable.TABLE);
			break;
		case ADDRESS_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, AddressTable.columnProjection());

			// Set the table
			queryBuilder.setTables(AddressTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(AddressTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case REMINDERS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, ReminderTable.columnProjection());

			// Set the table
			queryBuilder.setTables(ReminderTable.TABLE);
			break;
		case REMINDER_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, ReminderTable.columnProjection());

			// Set the table
			queryBuilder.setTables(ReminderTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(ReminderTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case TIMERS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, TimerTable.columnProjection());

			// Set the table
			queryBuilder.setTables(TimerTable.TABLE);
			break;
		case TIMER_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, TimerTable.columnProjection());

			// Set the table
			queryBuilder.setTables(TimerTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(TimerTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case ORDERS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, OrderTable.columnProjection());

			// Set the table
			queryBuilder.setTables(OrderTable.TABLE);
			break;
		case ORDERS_FAVORITES:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, OrderTable.columnProjection());

			// Set the table
			queryBuilder.setTables(OrderTable.TABLE);

			// Add the pool ID to the query
			queryBuilder.appendWhere(OrderTable.KEY_FAVORITE + ">0");
			break;
		case ORDER_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, OrderTable.columnProjection());

			// Set the table
			queryBuilder.setTables(OrderTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(OrderTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case ORDER_ID_ITEMS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, OrderItemTable.columnProjection());

			// Set the table
			queryBuilder.setTables(OrderItemTable.TABLE);

			// Add the pool ID to the query
			queryBuilder.appendWhere(OrderItemTable.KEY_ORDER + "="
					+ uri.getPathSegments().get(1));
			break;
		case ORDER_ITEMS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, OrderItemTable.columnProjection());

			// Set the table
			queryBuilder.setTables(OrderItemTable.TABLE);
			break;
		case ORDER_ITEM_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, OrderItemTable.columnProjection());

			// Set the table
			queryBuilder.setTables(OrderItemTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(OrderItemTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case ITEMS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, ItemTable.columnProjection());

			// Set the table
			queryBuilder.setTables(ItemTable.TABLE);
			break;
		case ITEM_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, ItemTable.columnProjection());

			// Set the table
			queryBuilder.setTables(ItemTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(ItemTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case WATER_TESTS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, WaterTestTable.columnProjection());

			// Set the table
			queryBuilder.setTables(WaterTestTable.TABLE);
			break;
		case WATER_TEST_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, WaterTestTable.columnProjection());

			// Set the table
			queryBuilder.setTables(WaterTestTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(WaterTestTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		case TREATMENTS:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, TreatmentTable.columnProjection());

			// Set the table
			queryBuilder.setTables(TreatmentTable.TABLE);
			break;
		case TREATMENT_ID:
			// Check if the caller has requested a column which does not exists
			checkColumns(projection, TreatmentTable.columnProjection());

			// Set the table
			queryBuilder.setTables(TreatmentTable.TABLE);

			// Adding the ID to the original query
			queryBuilder.appendWhere(TreatmentTable.KEY_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor;
		if(isRaw) {
			cursor = db.rawQuery(rawQuery, rawSelectionArgs);
		}else{
			cursor = queryBuilder.query(db, projection, selection,
					selectionArgs, groupBy, having, sortOrder);
		}
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		String basePath = "content://" + AUTHORITY + "/";
		String contentPath;

		switch (uriType) {
		case POOLS:
			id = sqlDB.insert(PoolTable.TABLE, null, values);
			contentPath = basePath + POOLS_PATH + "/" + id;
			break;
		case TASKS:
			id = sqlDB.insert(TaskTable.TABLE, null, values);
			contentPath = basePath + TASKS_PATH + "/" + id;
			break;
		case ADDRESSES:
			id = sqlDB.insert(AddressTable.TABLE, null, values);
			contentPath = basePath + ADDRESSES_PATH + "/" + id;
			break;
		case REMINDERS:
			id = sqlDB.insert(ReminderTable.TABLE, null, values);
			contentPath = basePath + REMINDERS_PATH + "/" + id;
			break;
		case TIMERS:
			id = sqlDB.insert(TimerTable.TABLE, null, values);
			contentPath = basePath + TIMERS_PATH + "/" + id;
			break;
		case ORDERS:
			id = sqlDB.insert(OrderTable.TABLE, null, values);
			contentPath = basePath + ORDERS_PATH + "/" + id;
			break;
		case ORDER_ITEMS:
			id = sqlDB.insert(OrderItemTable.TABLE, null, values);
			contentPath = basePath + ORDER_ITEMS_PATH + "/" + id;
			break;
		case ITEMS:
			id = sqlDB.insert(ItemTable.TABLE, null, values);
			contentPath = basePath + ITEMS_PATH + "/" + id;
			break;
		case WATER_TESTS:
			id = sqlDB.insert(WaterTestTable.TABLE, null, values);
			contentPath = basePath + WATER_TESTS_PATH + "/" + id;
			break;
		case TREATMENTS:
			id = sqlDB.insert(TreatmentTable.TABLE, null, values);
			contentPath = basePath + TREATMENTS_PATH + "/" + id;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(contentPath);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		String id;

		switch (uriType) {
		case POOLS:
			rowsDeleted = sqlDB.delete(PoolTable.TABLE, selection,
					selectionArgs);
			break;
		case POOL_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						PoolTable.TABLE,
						PoolTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						PoolTable.TABLE,
						PoolTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case TASKS:
			rowsDeleted = sqlDB.delete(TaskTable.TABLE, selection,
					selectionArgs);
			break;
		case TASK_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						TaskTable.TABLE,
						TaskTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						TaskTable.TABLE,
						TaskTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case ADDRESSES:
			rowsDeleted = sqlDB.delete(AddressTable.TABLE, selection,
					selectionArgs);
			break;
		case ADDRESS_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						AddressTable.TABLE,
						AddressTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						AddressTable.TABLE,
						AddressTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case REMINDERS:
			rowsDeleted = sqlDB.delete(ReminderTable.TABLE, selection,
					selectionArgs);
			break;
		case REMINDER_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						ReminderTable.TABLE,
						ReminderTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						ReminderTable.TABLE,
						ReminderTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case TIMERS:
			rowsDeleted = sqlDB.delete(TimerTable.TABLE, selection,
					selectionArgs);
			break;
		case TIMER_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						TimerTable.TABLE,
						TimerTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						TimerTable.TABLE,
						TimerTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case ORDERS:
			rowsDeleted = sqlDB.delete(OrderTable.TABLE, selection,
					selectionArgs);
			break;
		case ORDER_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						OrderTable.TABLE,
						OrderTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						OrderTable.TABLE,
						OrderTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case ORDER_ITEMS:
			rowsDeleted = sqlDB.delete(OrderItemTable.TABLE, selection,
					selectionArgs);
			break;
		case ORDER_ITEM_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						OrderItemTable.TABLE,
						OrderItemTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						OrderItemTable.TABLE,
						OrderItemTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case ITEMS:
			rowsDeleted = sqlDB.delete(ItemTable.TABLE, selection,
					selectionArgs);
			break;
		case ITEM_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						ItemTable.TABLE,
						ItemTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						ItemTable.TABLE,
						ItemTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case WATER_TESTS:
			rowsDeleted = sqlDB.delete(WaterTestTable.TABLE, selection,
					selectionArgs);
			break;
		case WATER_TEST_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						WaterTestTable.TABLE,
						WaterTestTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						WaterTestTable.TABLE,
						WaterTestTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case TREATMENTS:
			rowsDeleted = sqlDB.delete(TreatmentTable.TABLE, selection,
					selectionArgs);
			break;
		case TREATMENT_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(
						TreatmentTable.TABLE,
						TreatmentTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(
						TreatmentTable.TABLE,
						TreatmentTable.KEY_ID + "=" + id
						+ " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		String id;

		switch (uriType) {
		case POOLS:
			rowsUpdated = sqlDB.update(PoolTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case POOL_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(PoolTable.TABLE,
						values,
						PoolTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(PoolTable.TABLE,
						values,
						PoolTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case TASKS:
			rowsUpdated = sqlDB.update(TaskTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case TASK_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(TaskTable.TABLE,
						values,
						TaskTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(TaskTable.TABLE,
						values,
						TaskTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case ADDRESSES:
			rowsUpdated = sqlDB.update(AddressTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case ADDRESS_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(AddressTable.TABLE,
						values,
						AddressTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(AddressTable.TABLE,
						values,
						AddressTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case REMINDERS:
			rowsUpdated = sqlDB.update(ReminderTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case REMINDER_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(ReminderTable.TABLE,
						values,
						ReminderTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(ReminderTable.TABLE,
						values,
						ReminderTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case TIMERS:
			rowsUpdated = sqlDB.update(TimerTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case TIMER_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(TimerTable.TABLE,
						values,
						TimerTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(TimerTable.TABLE,
						values,
						TimerTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case ORDERS:
			rowsUpdated = sqlDB.update(OrderTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case ORDER_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(OrderTable.TABLE,
						values,
						OrderTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(OrderTable.TABLE,
						values,
						OrderTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case ORDER_ITEMS:
			rowsUpdated = sqlDB.update(OrderItemTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case ORDER_ITEM_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(OrderItemTable.TABLE,
						values,
						OrderItemTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(OrderItemTable.TABLE,
						values,
						OrderItemTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case ITEMS:
			rowsUpdated = sqlDB.update(ItemTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case ITEM_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(ItemTable.TABLE,
						values,
						ItemTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(ItemTable.TABLE,
						values,
						ItemTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case WATER_TESTS:
			rowsUpdated = sqlDB.update(WaterTestTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case WATER_TEST_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(WaterTestTable.TABLE,
						values,
						WaterTestTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(WaterTestTable.TABLE,
						values,
						WaterTestTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		case TREATMENTS:
			rowsUpdated = sqlDB.update(TreatmentTable.TABLE,
					values,
					selection,
					selectionArgs);
			break;
		case TREATMENT_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(TreatmentTable.TABLE,
						values,
						TreatmentTable.KEY_ID + "=" + id,
						null);
			} else {
				rowsUpdated = sqlDB.update(TreatmentTable.TABLE,
						values,
						TreatmentTable.KEY_ID + "=" + id
						+ " and "
						+ selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection, String[] available) {
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

	private String joinQuery(LinkedHashMap<String, String[]> projections,
			HashMap<String, String> onArgs, String selection,
			String sortOrder) {

		StringBuilder sb = new StringBuilder();

		String[] tables = projections.keySet().toArray(new String[]{});
		HashMap<String, String> aliases = new HashMap<String, String>();

		Matcher m;

		// make sure table projections were supplied
		Assert.assertTrue("No projections were supplied to joinQuery call", tables.length > 0);

		sb.append("SELECT");

		// Append each key with table aliases and update projection arrays
		// with the aliased values
		for(int i=0; i<tables.length; i++) {
			aliases.put(tables[i], TABLE_ALIASES[i]);
			String[] projection = projections.get(tables[i]);
			for(int j=0; j<projection.length; j++) {
				projection[j] = TABLE_ALIASES[i] + "." + projection[j];
				if(i > 0 || j > 0) {
					sb.append(",");
				}
				sb.append(" " + projection[j]);
			}
			projections.put(tables[i], projection);
		}

		// Append the base table to select from with its alias
		sb.append(" FROM " + tables[0] + " " + aliases.get(tables[0]));

		// Append the supplied join statements
		for(int i=1; i<tables.length; i++) {
			// make sure the ON statement for this table is included
			Assert.assertTrue("Map of ON statements does not contain "
						+ "statement for the" + tables[i] + " table",
						onArgs.containsKey(tables[i]));

			String on = onArgs.get(tables[i]);

			// make sure on statement is in the correct format
			Assert.assertTrue("ON statement for "
						+ tables[i] + " must be in the format "
						+ "\"table1.column_key=table2.column_key\"",
						on.matches(ON_PATTERN.pattern()));

			m = ON_PATTERN.matcher(on);

			while(m.find()) {
				String table1 = m.group(1);
				String column1 = m.group(2);
				String table2 = m.group(3);
				String column2 = m.group(4);

				// make sure tables referenced in the on statement are actually
				// in this join
				Assert.assertTrue("ON statement for "
						+ tables[i] + " references invalid table ("
						+ table1 + "), which is not included "
						+ "in this join.",
						aliases.containsKey(table1));
				Assert.assertTrue("ON statement for "
						+ tables[i] + " references invalid table ("
						+ table2 + "), which is not included "
						+ "in this join.",
						aliases.containsKey(table2));

				// build the join statement with table aliases
				sb.append(" INNER JOIN " + tables[i] + " " + aliases.get(tables[i])
						+ " ON " + aliases.get(table1) + "." + column1
						+ "=" + aliases.get(table2) + "." + column2);
			}
		}

		// Append the supplied selection args
		if(selection != null) {
			m = QUERY_SELECTION_PATTERN.matcher(selection);

			sb.append(" WHERE");
			while(m.find()) {
				String condition = m.group(1);
				String table = m.group(2);
				String column = m.group(3);

				// make sure the table referenced in this statement is included in
				// this join
				Assert.assertTrue("WHERE statement "
						+ "references invalid table ("
						+ table + "), which is not included "
						+ "in this join.",
						aliases.containsKey(table));

				// append a comma if this is not the first where statement
				if(condition != null) {
					sb.append(" " + condition);
				}

				sb.append(" " + aliases.get(table) + "." + column + "=?");
			}
		}

		// Append order by statement if included
		if(sortOrder != null) {
			m = QUERY_SORT_ORDER_PATTERN.matcher(sortOrder);

			sb.append(" ORDER BY");
			while(m.find()) {
				String comma = m.group(1);
				String table = m.group(2);
				String column = m.group(3);
				String direction = m.group(4);

				// make sure the table referenced in this statement is included in
				// this join
				Assert.assertTrue("ORDER BY statement "
						+ "references invalid table ("
						+ table + "), which is not included "
						+ "in this join.",
						aliases.containsKey(table));

				if(comma != null) {
					sb.append(comma);
				}

				sb.append(" " + aliases.get(table) + "." + column);

				// append direction variable if provided
				if(direction != null) {
					sb.append(" " + direction);
				}
			}

		}

		return sb.toString();
	}

}
