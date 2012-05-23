package com.devspacenine.poolpal.object;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.TaskTable;
import com.devspacenine.poolpal.widget.TaskDataAdapter;

public class Task implements Parcelable {

	public static String listToString(List<Task> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<Task> i = l.iterator(); i.hasNext();) {
			Task t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}

		return sb.toString();
	}

	public static ArrayList<Task> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<Task> l = new ArrayList<Task>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new Task(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	public static final int CUSTOM = 0;
	public static final int GENERAL_MAINTENANCE = 100;
	public static final int CLEANING = 101;
	public static final int SHOCK_TREATMENT = 102;
	public static final int PH_UP_TREATMENT = 103;
	public static final int PH_DOWN_TREATMENT = 104;
	public static final int ALKALINITY_UP_TREATMENT = 105;
	public static final int ALKALINITY_DOWN_TREATMENT = 106;
	public static final int STABILIZER_UP_TREATMENT = 107;
	public static final int HARDNESS_UP_TREATMENT = 108;
	public static final int ALGAECIDE_TREATMENT = 109;
	public static final int METAL_TREATMENT = 110;
	public static final int CLARIFIER_TREATMENT = 111;
	public static final int WATER_TESTS = 20;
	public static final int BASIC_WATER_TESTS = 21;
	public static final int COMMON_WATER_TESTS = 22;
	public static final int LONG_TERM_WATER_TESTS = 23;
	public static final int FILTER_MAINTENANCE = 30;
	public static final int FILTER_CLEAN = 31;
	public static final int FILTER_REPLACEMENT = 32;

	public static final int SCALE_MINUTE = 0;
	public static final int SCALE_HOUR = 1;
	public static final int SCALE_DAY = 2;
	public static final int SCALE_WEEK = 3;
	public static final int SCALE_MONTH = 4;
	public static final int SCALE_YEAR = 5;

	private long id;
	private String title;
	private Pool pool;
	private Date date;
	private String message;
	private int repitition;
	private int repititionScale;
	private boolean nextOnCompletion;
	private ArrayList<Reminder> reminders;
	private boolean lateReminders;
	private Timer timer;
	private Item item;
	private Order favorite;
	private boolean completed;
	private boolean save;
	private PoolAddress address;
	private int taskType;
	private ArrayList<Task> tasks;

	public Task() {
		id = 0;
		title = "";
		pool = new Pool();
		date = new Date();
		message = "";
		repitition = 0;
		repititionScale = 0;
		nextOnCompletion = true;
		reminders = new ArrayList<Reminder>();
		lateReminders = true;
		timer = new Timer();
		item = new Item();
		favorite = new Order();
		completed = false;
		save = false;
		address = new PoolAddress(PoolAddress.blankAddress());
		taskType = 0;
		tasks = new ArrayList<Task>();
	}

	public Task(Context ctx, Pool pool, int type) {
		this();
		this.pool = pool;
		this.taskType = type;
		switch(type) {
		case GENERAL_MAINTENANCE:
			title = ctx.getString(R.string.title_general_maintenance);
			message = ctx.getString(R.string.message_general_maintenance);
			reminders.add(new Reminder(ctx, Reminder.THIRTY_MINUTES));
			tasks.add(new Task(ctx, pool, CLEANING));
			tasks.add(new Task(ctx, pool, SHOCK_TREATMENT));
			break;

		case CLEANING:
			title = ctx.getString(R.string.title_cleaning);
			message = ctx.getString(R.string.message_cleaning);
			repitition = 1;
			repititionScale = SCALE_WEEK;
			break;

		case SHOCK_TREATMENT:
			title = ctx.getString(R.string.title_shock_treatment);
			message = ctx.getString(R.string.message_shock_treatment);
			repitition = 2;
			repititionScale = SCALE_WEEK;
			break;

		case PH_UP_TREATMENT:
			title = ctx.getString(R.string.title_ph_up_treatment);
			message = ctx.getString(R.string.message_ph_up_treatment);
			break;

		case PH_DOWN_TREATMENT:
			title = ctx.getString(R.string.title_ph_down_treatment);
			message = ctx.getString(R.string.message_ph_down_treatment);
			break;

		case ALKALINITY_UP_TREATMENT:
			title = ctx.getString(R.string.title_alkalinity_up_treatment);
			message = ctx.getString(R.string.message_alkalinity_up_treatment);
			break;

		case ALKALINITY_DOWN_TREATMENT:
			title = ctx.getString(R.string.title_alkalinity_down_treatment);
			message = ctx.getString(R.string.message_alkalinity_down_treatment);
			break;

		case STABILIZER_UP_TREATMENT:
			title = ctx.getString(R.string.title_stabilizer_up_treatment);
			message = ctx.getString(R.string.message_stabilizer_up_treatment);
			break;

		case HARDNESS_UP_TREATMENT:
			title = ctx.getString(R.string.title_hardness_up_treatment);
			message = ctx.getString(R.string.message_hardness_up_treatment);
			break;

		case ALGAECIDE_TREATMENT:
			title = ctx.getString(R.string.title_algaecide_treatment);
			message = ctx.getString(R.string.message_algaecide_treatment);
			break;

		case METAL_TREATMENT:
			title = ctx.getString(R.string.title_metal_treatment);
			message = ctx.getString(R.string.message_metal_treatment);
			break;

		case CLARIFIER_TREATMENT:
			title = ctx.getString(R.string.title_clarifier_treatment);
			message = ctx.getString(R.string.message_clarifier_treatment);
			break;

		case WATER_TESTS:
			title = ctx.getString(R.string.title_water_tests);
			message = ctx.getString(R.string.message_water_tests);
			tasks.add(new Task(ctx, pool, BASIC_WATER_TESTS));
			tasks.add(new Task(ctx, pool, COMMON_WATER_TESTS));
			tasks.add(new Task(ctx, pool, LONG_TERM_WATER_TESTS));
			reminders.add(new Reminder(ctx, Reminder.THIRTY_MINUTES));
			break;

		case BASIC_WATER_TESTS:
			title = ctx.getString(R.string.title_basic_water_tests);
			message = ctx.getString(R.string.message_basic_water_tests);
			repitition = 2;
			repititionScale = SCALE_DAY;
			nextOnCompletion = false;
			break;

		case COMMON_WATER_TESTS:
			title = ctx.getString(R.string.title_common_water_tests);
			message = ctx.getString(R.string.message_common_water_tests);
			repitition = 1;
			repititionScale = SCALE_WEEK;
			nextOnCompletion = false;
			break;

		case LONG_TERM_WATER_TESTS:
			title = ctx.getString(R.string.title_long_term_water_tests);
			message = ctx.getString(R.string.message_long_term_water_tests);
			repitition = 1;
			repititionScale = SCALE_MONTH;
			nextOnCompletion = false;
			break;

		case FILTER_MAINTENANCE:
			title = ctx.getString(R.string.title_filter_maintenance);
			message = ctx.getString(R.string.message_filter_maintenance);
			tasks.add(new Task(ctx, pool, FILTER_CLEAN));
			tasks.add(new Task(ctx, pool, FILTER_REPLACEMENT));
			reminders.add(new Reminder(ctx, Reminder.THIRTY_MINUTES));
			reminders.add(new Reminder(ctx, Reminder.ONE_DAY));
			break;

		case FILTER_CLEAN:
			title = ctx.getString(R.string.title_filter_clean);
			message = ctx.getString(R.string.message_filter_clean);
			repitition = 3;
			repititionScale = SCALE_MONTH;
			nextOnCompletion = false;
			break;

		case FILTER_REPLACEMENT:
			title = ctx.getString(R.string.title_filter_replacement);
			message = ctx.getString(R.string.message_filter_replacement);
			repitition = 1;
			repititionScale = SCALE_YEAR;
			reminders.add(new Reminder(ctx, Reminder.ONE_WEEK));
			nextOnCompletion = false;
			break;

		default:
			break;
		}

		insert(ctx);
	}

	public Task(Context ctx, long id) throws ParseException {
		this(ctx, ctx.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.TASKS_CONTENT_URI, Long.toString(id)),
				TaskTable.columnProjection(), null, null, null));
	}

	/**
	 * Constructs a Task using a positioned cursor.
	 * @param cursor
	 * @throws ParseException
	 */
	public Task(Context ctx, Cursor cursor) throws ParseException {

		if(cursor.isBeforeFirst()) {
			cursor.moveToFirst();
		}

		// Map of keys and indexes in this cursor
		HashMap<String, Integer> cols = new HashMap<String, Integer>();
		String[] names = cursor.getColumnNames();

		// Populate map with non null columns only
		for(String name : names) {
			int index = cursor.getColumnIndex(name);
			if(!cursor.isNull(index)) {
				cols.put(name, index);
			}
		}

		if(cols.containsKey(TaskTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(TaskTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(TaskTable.KEY_TITLE)) {
			this.title = cursor.getString(cols.get(TaskTable.KEY_TITLE));
		}else{
			this.title = "";
		}

		if(cols.containsKey(TaskTable.KEY_POOL)) {
			this.pool = new Pool(ctx, cursor.getLong(cols.get(TaskTable.KEY_POOL)));
		}else{
			this.pool = new Pool();
		}

		if(cols.containsKey(TaskTable.KEY_DATE)) {
			this.date = PoolPal.DATE_FORMAT.parse(cursor.getString(cols.get(TaskTable.KEY_DATE)));
		}else{
			this.date = new Date();
		}

		if(cols.containsKey(TaskTable.KEY_MESSAGE)) {
			this.message = cursor.getString(cols.get(TaskTable.KEY_MESSAGE));
		}else{
			this.message = "";
		}

		if(cols.containsKey(TaskTable.KEY_REPITITION)) {
			this.repitition = cursor.getInt(cols.get(TaskTable.KEY_REPITITION));
		}else{
			this.repitition = 0;
		}

		if(cols.containsKey(TaskTable.KEY_REPITITION_SCALE)) {
			this.repititionScale = cursor.getInt(cols.get(TaskTable.KEY_REPITITION_SCALE));
		}else{
			this.repititionScale = 0;
		}

		if(cols.containsKey(TaskTable.KEY_NEXT_ON_COMPLETION)) {
			this.nextOnCompletion = cursor.getInt(cols.get(TaskTable.KEY_NEXT_ON_COMPLETION)) > 0;
		}else{
			this.nextOnCompletion = true;
		}

		if(cols.containsKey(TaskTable.KEY_REMINDERS)) {
			this.reminders = Reminder.listFromString(ctx, cursor.getString(cols.get(TaskTable.KEY_REMINDERS)));
		}else{
			this.reminders = new ArrayList<Reminder>();
		}

		if(cols.containsKey(TaskTable.KEY_LATE_REMINDERS)) {
			this.lateReminders = cursor.getInt(cols.get(TaskTable.KEY_LATE_REMINDERS)) > 0;
		}else{
			this.lateReminders = true;
		}

		if(cols.containsKey(TaskTable.KEY_TIMER)) {
			this.timer = new Timer(ctx, cursor.getLong(cols.get(TaskTable.KEY_TIMER)));
		}else{
			this.timer = new Timer();
		}

		if(cols.containsKey(TaskTable.KEY_ITEM)) {
			this.item = new Item(ctx, cursor.getLong(cols.get(TaskTable.KEY_ITEM)));
		}else{
			this.item = new Item();
		}

		if(cols.containsKey(TaskTable.KEY_FAVORITE)) {
			this.favorite = new Order(ctx, cursor.getLong(cols.get(TaskTable.KEY_FAVORITE)));
		}else{
			this.favorite = new Order();
		}

		if(cols.containsKey(TaskTable.KEY_COMPLETED)) {
			this.completed = cursor.getInt(cols.get(TaskTable.KEY_COMPLETED)) > 0;
		}else{
			this.completed = false;
		}

		if(cols.containsKey(TaskTable.KEY_SAVE)) {
			this.save = cursor.getInt(cols.get(TaskTable.KEY_SAVE)) > 0;
		}else{
			this.save = false;
		}

		if(cols.containsKey(TaskTable.KEY_ADDRESS)) {
			this.address = new PoolAddress(ctx, cursor.getLong(cols.get(TaskTable.KEY_ADDRESS)));
		}else{
			this.address = new PoolAddress(PoolAddress.blankAddress());
		}

		if(cols.containsKey(TaskTable.KEY_TASK_TYPE)) {
			this.taskType = cursor.getInt(cols.get(TaskTable.KEY_TASK_TYPE));
		}else{
			this.taskType = 0;
		}

		if(cols.containsKey(TaskTable.KEY_TASKS)) {
			this.tasks = Task.listFromString(ctx, cursor.getString(cols.get(TaskTable.KEY_TASKS)));
		}else{
			this.tasks = new ArrayList<Task>();
		}
	}

	public ContentValues getContentValues() {

		ContentValues values = new ContentValues();

		values.put(TaskTable.KEY_TITLE, title);
		values.put(TaskTable.KEY_MESSAGE, message);
		values.put(TaskTable.KEY_TASK_TYPE, taskType);
		values.put(TaskTable.KEY_DATE, PoolPal.DATE_FORMAT.format(date));
		values.put(TaskTable.KEY_REPITITION, repitition);
		values.put(TaskTable.KEY_REPITITION_SCALE, repititionScale);
		values.put(TaskTable.KEY_POOL, pool.getId() > 0 ? pool.getId() : null);
		values.put(TaskTable.KEY_TIMER, timer.getId() > 0 ? timer.getId() : null);
		values.put(TaskTable.KEY_ITEM, item.getId() > 0 ? item.getId() : null);
		values.put(TaskTable.KEY_FAVORITE, favorite.getId() > 0 ? favorite.getId() : null);
		values.put(TaskTable.KEY_ADDRESS, address.getId() > 0 ? address.getId() : null);
		values.put(TaskTable.KEY_REMINDERS, Reminder.listToString(reminders));
		values.put(TaskTable.KEY_TASKS, Task.listToString(tasks));
		values.put(TaskTable.KEY_NEXT_ON_COMPLETION, nextOnCompletion ? 1 : 0);
		values.put(TaskTable.KEY_LATE_REMINDERS, lateReminders ? 1 : 0);
		values.put(TaskTable.KEY_COMPLETED, completed ? 1 : 0);
		values.put(TaskTable.KEY_SAVE, save ? 1 : 0);

		return values;
	}

	public boolean insert(Context ctx) {

		Uri uri = ctx.getContentResolver().insert(PoolPalContent.TASKS_CONTENT_URI, getContentValues());
		long new_id = Long.parseLong(uri.getLastPathSegment());
		if(new_id > 0) {
			// successfully inserted new row
			id = new_id;
			return true;
		}else{
			// failed to insert new row
			id = 0;
			return false;
		}
	}

	/**
	 *
	 * @param requestCode id of attribute bundle to get
	 * @return bundle of values for attribute
	 */
	public Bundle getValuesBundle(Context context, int requestCode) {

		switch(requestCode) {
		case R.id.task_title:
			return getTitleBundle();

		case R.id.task_message:
			return getMessageBundle();

		case R.id.task_date:
			return getDateBundle();

		case R.id.task_repitition:
			return getRepititionBundle(context);

		case R.id.task_next_on_completion:
			return getNextOnCompletionBundle();

		case R.id.task_reminders:
		case R.id.set_reminder:
			return getRemindersBundle(context);

		case R.id.task_late_reminders:
			return getLateRemindersBundle();

		case R.id.set_timer:
		case R.id.task_timer_duration:
		case R.id.task_timer_notification:
		case R.id.task_timer_message:
			return getTimerBundle();

		case R.id.set_item:
			return getItemBundle();

		case R.id.set_favorite:
			return getFavoriteBundle();

		case R.id.set_address:
			return getAddressBundle();

		default:
			return null;
		}
	}

	/**
	 * @return string representation of this task
	 */
	public String toString() {
		return (this.id > 0) ? Long.toString(this.id) : null;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return bundle of values for this attribute
	 */
	public Bundle getTitleBundle() {
		Bundle b = new Bundle();
		b.putString(TaskDataAdapter.VALUE, title);
		b.putString(TaskDataAdapter.VALUE_TITLE, title);
		return b;
	}

	/**
	 * @return the pool
	 */
	public Pool getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(Pool pool) {
		this.pool = pool;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return bundle of values for this attribute
	 */
	public Bundle getDateBundle() {
		Bundle b = new Bundle();
		b.putString(TaskDataAdapter.VALUE, PoolPal.DATE_FORMAT.format(date));
		b.putString(TaskDataAdapter.VALUE_DATE, PoolPal.DATE_FORMAT.format(date));
		return b;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the description to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return bundle of values for this attribute
	 */
	public Bundle getMessageBundle() {
		Bundle b = new Bundle();
		b.putString(TaskDataAdapter.VALUE, message);
		b.putString(TaskDataAdapter.VALUE_MESSAGE, message);
		return b;
	}

	/**
	 * @return the repitition
	 */
	public int getRepitition() {
		return repitition;
	}

	/**
	 * @param repitition the period to set
	 */
	public void setRepitition(int repitition) {
		this.repitition = repitition;
	}

	/**
	 * @return the repititionScale
	 */
	public int getRepititionScale() {
		return repititionScale;
	}

	/**
	 * @param repititionScale the repititionScale to set
	 */
	public void setRepititionScale(int repititionScale) {
		this.repititionScale = repititionScale;
	}

	/**
	 * @param pos pos of repitition value in resource string array
	 * @return string representation of repitition value at given position
	 */
	public String getRepititionString(Context context) {

		int plural_res;
		switch(getRepititionScale()) {
		case 1:
			plural_res = R.plurals.plural_hours;
			break;

		case 2:
			plural_res = R.plurals.plural_days;
			break;

		case 3:
			plural_res = R.plurals.plural_weeks;
			break;

		case 4:
			plural_res = R.plurals.plural_months;
			break;

		case 5:
			plural_res = R.plurals.plural_years;
			break;

		default:
			return "One-time Event";
		}
		return String.format("Every %1$s @ %2$s",
				context.getResources().getQuantityString(plural_res, getRepitition()),
				PoolPal.TIME_FORMAT.format(getDate()));
	}

	/**
	 * @return bundle of values for this attribute
	 */
	public Bundle getRepititionBundle(Context context) {
		Bundle b = new Bundle();
		b.putString(TaskDataAdapter.VALUE, getRepititionString(context));
		b.putInt(TaskDataAdapter.VALUE_REPITITION, repitition);
		b.putInt(TaskDataAdapter.VALUE_REPITITION_SCALE, repititionScale);
		return b;
	}

	/**
	 * @return the nextOnCompletion
	 */
	public boolean hasNextOnCompletion() {
		return nextOnCompletion;
	}

	/**
	 * @param nextOnCompletion the nextOnCompletion to set
	 */
	public void setNextOnCompletion(boolean nextOnCompletion) {
		this.nextOnCompletion = nextOnCompletion;
	}

	/**
	 * @return bundle of values for this attribute
	 */
	public Bundle getNextOnCompletionBundle() {
		Bundle b = new Bundle();
		b.putBoolean(TaskDataAdapter.VALUE, nextOnCompletion);
		return b;
	}

	/**
	 * @return the reminders
	 */
	public ArrayList<Reminder> getReminders() {
		return reminders;
	}

	/**
	 * @param reminders the reminders to set
	 */
	public void setReminders(ArrayList<Reminder> reminders) {
		this.reminders = reminders;
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getRemindersBundle(Context context) {

		Bundle b = new Bundle();
		b.putParcelableArrayList(TaskDataAdapter.VALUE_REMINDERS, getReminders());
		b.putString(TaskDataAdapter.VALUE, context.getResources().getString(R.string.details_task_reminders));
		return b;
	}

	/**
	 * @return the lateReminders
	 */
	public boolean hasLateReminders() {
		return lateReminders;
	}

	/**
	 * @param lateReminders the lateReminders to set
	 */
	public void setLateReminders(boolean lateReminders) {
		this.lateReminders = lateReminders;
	}

	/**
	 * @return bundle of values for this attribute
	 */
	public Bundle getLateRemindersBundle() {
		Bundle b = new Bundle();
		b.putBoolean(TaskDataAdapter.VALUE, lateReminders);
		return b;
	}

	/**
	 * @return the timer
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasTimer() {
		return (timer.getId() > 0);
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getTimerBundle() {

		Bundle b = new Bundle();
		b.putParcelable(TaskDataAdapter.VALUE_TIMER, timer);
		b.putLong(TaskDataAdapter.VALUE_TIMER_DURATION, timer.getDuration());
		b.putString(TaskDataAdapter.VALUE_TIMER_MESSAGE, timer.getMessage());
		b.putBoolean(TaskDataAdapter.VALUE_TIMER_NOTIFICATION, timer.hasNotification());
		b.putString(TaskDataAdapter.VALUE, (hasTimer()) ? timer.toString() : "");
		return b;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasItem() {
		return (item.getId() > 0);
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getItemBundle() {

		Bundle b = new Bundle();
		b.putParcelable(TaskDataAdapter.VALUE_ITEM, item);
		b.putString(TaskDataAdapter.VALUE, (hasItem()) ? item.toString() : "");
		return b;
	}

	/**
	 * @return the favorite
	 */
	public Order getFavorite() {
		return favorite;
	}

	/**
	 * @param favorite the favorite to set
	 */
	public void setFavorite(Order favorite) {
		this.favorite = favorite;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasFavorite() {
		return (favorite.getId() > 0);
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getFavoriteBundle() {

		Bundle b = new Bundle();
		b.putParcelable(TaskDataAdapter.VALUE_FAVORITE, favorite);
		b.putString(TaskDataAdapter.VALUE, (hasFavorite()) ? favorite.toString() : "");
		return b;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the save
	 */
	public boolean isSave() {
		return save;
	}

	/**
	 * @param save the save to set
	 */
	public void setSave(boolean save) {
		this.save = save;
	}

	/**
	 * @return bundle of values for this attribute
	 */
	public Bundle getSaveBundle() {
		Bundle b = new Bundle();
		b.putBoolean(TaskDataAdapter.VALUE, save);
		return b;
	}

	/**
	 * @return the address
	 */
	public PoolAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(PoolAddress address) {
		this.address = address;
	}

	/**
	 * @param id to give the address
	 */
	public void setAddressId(long id) {
		this.address.setId(id);
	}

	/**
	 * @return String string representation of address
	 */
	public String getAddressString() {
		return address.toString();
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasAddress() {
		return (!address.isBlank());
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getAddressBundle() {

		Bundle b = new Bundle();
		b.putParcelable(TaskDataAdapter.VALUE_ADDRESS,
				(hasAddress()) ? PoolAddress.setLines(address) : PoolAddress.blankAddress());
		b.putBoolean(TaskDataAdapter.VALUE_GEOCODED, PoolAddress.isGeocoded(address));
		b.putString(TaskDataAdapter.VALUE, (hasAddress()) ? getAddressString() : "");
		return b;
	}

	/**
	 * @return the taskType
	 */
	public int getTaskType() {
		return taskType;
	}

	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeParcelable(pool, flags);
		out.writeParcelableArray(reminders.toArray(new Reminder[]{}), flags);
		out.writeParcelable(timer, flags);
		out.writeParcelable(item, flags);
		out.writeParcelable(favorite, flags);
		out.writeParcelable(address, flags);
		out.writeLong(id);
		out.writeString(title);
		out.writeString(PoolPal.DATE_FORMAT.format(date));
		out.writeString(message);
		out.writeInt(repitition);
		out.writeInt(repititionScale);
		out.writeInt(taskType);
		out.writeBooleanArray(new boolean[]{nextOnCompletion, lateReminders, completed, save});
	}

	private Task(Parcel in) throws ParseException {

		this.pool = in.readParcelable(Pool.class.getClassLoader());
		this.reminders = new ArrayList<Reminder>(
				Arrays.asList(
						(Reminder[])in.readParcelableArray(
								Reminder.class.getClassLoader())));
		this.timer = in.readParcelable(Timer.class.getClassLoader());
		this.item = in.readParcelable(Item.class.getClassLoader());
		this.favorite = in.readParcelable(Order.class.getClassLoader());
		this.address = in.readParcelable(PoolAddress.class.getClassLoader());
		this.id = in.readLong();
		this.title = in.readString();
		this.date = PoolPal.DATE_FORMAT.parse(in.readString());
		this.message = in.readString();
		this.repitition = in.readInt();
		this.repititionScale = in.readInt();
		this.taskType = in.readInt();
		boolean[] bools = new boolean[4];
		in.readBooleanArray(bools);
		this.nextOnCompletion = bools[0];
		this.lateReminders = bools[1];
		this.completed = bools[2];
		this.save = bools[3];
	}

	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

		public Task createFromParcel(Parcel in) {
			try {
				return new Task(in);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
}
