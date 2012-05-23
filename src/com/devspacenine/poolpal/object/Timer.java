package com.devspacenine.poolpal.object;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.TimerTable;

public class Timer implements Parcelable {

	public static String listToString(List<Timer> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<Timer> i = l.iterator(); i.hasNext();) {
			Timer t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<Timer> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<Timer> l = new ArrayList<Timer>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new Timer(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	private long id;
	private String title;
	private String message;
	private Date startDate;
	private long duration;
	private Date endDate;
	private boolean notification;
	private boolean sms;
	private boolean email;

	public Timer() {
		id = 0;
		title = "";
		message = "";
		startDate = new Date();
		duration = 0;
		endDate = new Date();
		notification = true;
		sms = false;
		email = false;
	}

	public Timer(Context context, long id) throws ParseException {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.TIMERS_CONTENT_URI, Long.toString(id)),
				TimerTable.columnProjection(), null, null, null));
	}

	public Timer(Context context, Cursor cursor) throws ParseException {

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

		if(cols.containsKey(TimerTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(TimerTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(TimerTable.KEY_TITLE)) {
			this.title = cursor.getString(cols.get(TimerTable.KEY_TITLE));
		}else{
			this.title = "";
		}

		if(cols.containsKey(TimerTable.KEY_MESSAGE)) {
			this.message = cursor.getString(cols.get(TimerTable.KEY_MESSAGE));
		}else{
			this.message = "";
		}

		if(cols.containsKey(TimerTable.KEY_START_DATE)) {
			this.startDate = PoolPal.DATE_FORMAT.parse(cursor.getString(cols.get(TimerTable.KEY_START_DATE)));
		}else{
			this.startDate = new Date();
		}

		if(cols.containsKey(TimerTable.KEY_DURATION)) {
			this.duration = cursor.getLong(cols.get(TimerTable.KEY_DURATION));
		}else{
			this.duration = 0;
		}

		if(cols.containsKey(TimerTable.KEY_END_DATE)) {
			this.endDate = PoolPal.DATE_FORMAT.parse(cursor.getString(cols.get(TimerTable.KEY_END_DATE)));
		}else{
			this.endDate = new Date();
		}

		if(cols.containsKey(TimerTable.KEY_NOTIFICATION)) {
			this.notification = cursor.getInt(cols.get(TimerTable.KEY_NOTIFICATION)) > 0;
		}else{
			this.notification = true;
		}

		if(cols.containsKey(TimerTable.KEY_SMS)) {
			this.sms = cursor.getInt(cols.get(TimerTable.KEY_SMS)) > 0;
		}else{
			this.sms = false;
		}

		if(cols.containsKey(TimerTable.KEY_EMAIL)) {
			this.email = cursor.getInt(cols.get(TimerTable.KEY_EMAIL)) > 0;
		}else{
			this.email = false;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the notification
	 */
	public boolean hasNotification() {
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
	public boolean hasSms() {
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
	public boolean hasEmail() {
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
		out.writeString(title);
		out.writeString(message);
		out.writeString(PoolPal.DATE_FORMAT.format(startDate));
		out.writeLong(duration);
		out.writeString(PoolPal.DATE_FORMAT.format(endDate));
		out.writeBooleanArray(new boolean[]{notification, sms, email});
	}

	private Timer(Parcel in) throws ParseException {

		this.id = in.readLong();
		this.title = in.readString();
		this.message = in.readString();
		this.startDate = PoolPal.DATE_FORMAT.parse(in.readString());
		this.duration = in.readLong();
		this.endDate = PoolPal.DATE_FORMAT.parse(in.readString());
		boolean[] bools = new boolean[3];
		in.readBooleanArray(bools);
		this.notification = bools[0];
		this.sms = bools[1];
		this.email = bools[2];
	}

	public static final Parcelable.Creator<Timer> CREATOR = new Parcelable.Creator<Timer>() {

		public Timer createFromParcel(Parcel in) {
			try {
				return new Timer(in);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		public Timer[] newArray(int size) {
			return new Timer[size];
		}
	};
}
