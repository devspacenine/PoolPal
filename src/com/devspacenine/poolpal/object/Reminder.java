package com.devspacenine.poolpal.object;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.ReminderTable;

public class Reminder implements Parcelable {

	public static String listToString(List<Reminder> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<Reminder> i = l.iterator(); i.hasNext();) {
			Reminder t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<Reminder> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<Reminder> l = new ArrayList<Reminder>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new Reminder(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	public static final int ONE_MINUTE = 0;
	public static final int FIVE_MINUTES = 1;
	public static final int TEN_MINUTES = 2;
	public static final int FIFTEEN_MINUTES = 3;
	public static final int TWENTY_MINUTES = 4;
	public static final int TWENTY_FIVE_MINUTES = 5;
	public static final int THIRTY_MINUTES = 6;
	public static final int FOURTY_FIVE_MINUTES = 7;
	public static final int ONE_HOUR = 8;
	public static final int TWO_HOURS = 9;
	public static final int THREE_HOURS = 10;
	public static final int TWELVE_HOURS = 11;
	public static final int ONE_DAY = 12;
	public static final int TWO_DAYS = 13;
	public static final int THREE_DAYS = 14;
	public static final int ONE_WEEK = 15;

	private long id;
	private Date date;
	private int period;
	private boolean notification;
	private boolean sms;
	private boolean email;

	public Reminder(){
		id = 0;
		date = new Date();
		period = 0;
		notification = true;
		sms = false;
		email = false;
	}

	public Reminder(Context ctx, int period) {
		this();
		this.period = period;
		insert(ctx);
	}

	public Reminder(Context context, long id) throws ParseException {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.REMINDERS_CONTENT_URI, Long.toString(id)),
				ReminderTable.columnProjection(), null, null, null));
	}

	public Reminder(Context context, Cursor cursor) throws ParseException {

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

		if(cols.containsKey(ReminderTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(ReminderTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(ReminderTable.KEY_DATE)) {
			this.date = PoolPal.DATE_FORMAT.parse(cursor.getString(cols.get(ReminderTable.KEY_DATE)));
		}else{
			this.date = new Date();
		}

		if(cols.containsKey(ReminderTable.KEY_PERIOD)) {
			this.period = cursor.getInt(cols.get(ReminderTable.KEY_PERIOD));
		}else{
			this.period = 0;
		}

		if(cols.containsKey(ReminderTable.KEY_NOTIFICATION)) {
			this.notification = cursor.getInt(cols.get(ReminderTable.KEY_NOTIFICATION)) > 0;
		}else{
			this.notification = true;
		}

		if(cols.containsKey(ReminderTable.KEY_SMS)) {
			this.sms = cursor.getInt(cols.get(ReminderTable.KEY_SMS)) > 0;
		}else{
			this.sms = false;
		}

		if(cols.containsKey(ReminderTable.KEY_EMAIL)) {
			this.email = cursor.getInt(cols.get(ReminderTable.KEY_EMAIL)) > 0;
		}else{
			this.email = false;
		}

	}

	public ContentValues getContentValues() {

		ContentValues values = new ContentValues();

		values.put(ReminderTable.KEY_DATE, PoolPal.DATE_FORMAT.format(date));
		values.put(ReminderTable.KEY_PERIOD, period);
		values.put(ReminderTable.KEY_NOTIFICATION, notification ? 1 : 0);
		values.put(ReminderTable.KEY_SMS, sms ? 1 : 0);
		values.put(ReminderTable.KEY_EMAIL, email ? 1 : 0);

		return values;
	}

	public boolean insert(Context ctx) {

		Uri uri = ctx.getContentResolver().insert(PoolPalContent.REMINDERS_CONTENT_URI, getContentValues());
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
	 * @return the period
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * @return the notification
	 */
	public boolean isNotification() {
		return notification;
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	/**
	 * @return the sms
	 */
	public boolean isSms() {
		return sms;
	}

	/**
	 * @param sms the sms to set
	 */
	public void setSms(boolean sms) {
		this.sms = sms;
	}

	/**
	 * @return the email
	 */
	public boolean isEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(boolean email) {
		this.email = email;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeLong(id);
		out.writeString(PoolPal.DATE_FORMAT.format(date));
		out.writeInt(period);
		out.writeBooleanArray(new boolean[]{notification, sms, email});
	}

	private Reminder(Parcel in) throws ParseException {

		this.id = in.readLong();
		this.date = PoolPal.DATE_FORMAT.parse(in.readString());
		this.period = in.readInt();
		boolean[] bools = new boolean[3];
		in.readBooleanArray(bools);
		this.notification = bools[0];
		this.sms = bools[1];
		this.email = bools[2];
	}

	public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {

		public Reminder createFromParcel(Parcel in) {
			try {
				return new Reminder(in);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		public Reminder[] newArray(int size) {
			return new Reminder[size];
		}
	};
}
