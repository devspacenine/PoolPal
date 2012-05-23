package com.devspacenine.poolpal.object;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.AddressTable;

public class PoolAddress extends Address implements Parcelable {

	private long id;

	public static String listToString(List<PoolAddress> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<PoolAddress> i = l.iterator(); i.hasNext();) {
			PoolAddress t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<PoolAddress> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<PoolAddress> l = new ArrayList<PoolAddress>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new PoolAddress(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	public static boolean equals(Address a, Address b) {
		return PoolAddress.toString(PoolAddress.setLines(a)).equals(
				PoolAddress.toString(PoolAddress.setLines(b)));
	}

	public static boolean isComplete(Address a) {
		return a != null && (a.getFeatureName() != null && a.getThoroughfare() != null
				&& a.getLocality() != null && a.getAdminArea() != null
				&& a.getPostalCode() != null);
	}

	public static boolean isGeocoded(Address a) {
		return (PoolAddress.isComplete(a) && a.hasLatitude() && a.hasLongitude());
	}

	public static String toString(Address a) {
		return String.format("%1$s%2$s%3$s",
				(a.getMaxAddressLineIndex() >= 0) ? a.getAddressLine(0) : "",
				(a.getMaxAddressLineIndex() >= 1) ? "\n"+a.getAddressLine(1) : "",
				(a.getMaxAddressLineIndex() >= 2) ? "\n"+a.getAddressLine(2) : "");
	}

	public static Address setLines(Address a) {

		// Line 1
		StringBuilder sb = new StringBuilder();
		sb.append((a.getFeatureName() != null) ? a.getFeatureName() : "");
		sb.append((a.getThoroughfare() != null) ? " " + a.getThoroughfare() : "");
		a.setAddressLine(0, sb.toString());

		// Line 2
		sb = new StringBuilder();
		sb.append((a.getLocality() != null) ? a.getLocality() : "");
		sb.append((a.getAdminArea() != null) ? " " + a.getAdminArea() : "");
		sb.append((a.getAdminArea() != null && a.getPostalCode() != null)
				? ", " + a.getPostalCode()
						: (a.getPostalCode() != null) ? " " + a.getPostalCode() : "");
		a.setAddressLine(1, sb.toString());

		// Line 3
		sb = new StringBuilder();
		sb.append((a.getCountryCode() != null) ? a.getCountryCode()
				: (a.getCountryName() != null) ? a.getCountryName() : "");
		a.setAddressLine(2, sb.toString());

		// Line 4
		sb = new StringBuilder();
		sb.append(a.hasLatitude() ? "Lat: " + a.getLatitude() : "");
		a.setAddressLine(3, sb.toString());

		// Line 5
		sb = new StringBuilder();
		sb.append(a.hasLongitude() ? "Long: " + a.getLongitude() : "");
		a.setAddressLine(4, sb.toString());

		return a;
	}

	public static Address blankAddress() {
		Address a = new Address(Locale.getDefault());
		a.setAddressLine(0, "");
		a.setAddressLine(1, "");
		a.setAddressLine(2, "");
		a.setFeatureName("");
		a.setAdminArea("");
		a.setLocality("");
		a.setThoroughfare("");
		a.setCountryCode("");
		a.setCountryName("");
		a.setPostalCode("");
		return PoolAddress.setLines(a);
	}

	public static boolean isBlank(Address a) {
		String val = String.format("%1$s%2$s%3$s%4$s%5$s",
				(a.getMaxAddressLineIndex() >= 0) ? a.getAddressLine(0) : "",
				(a.getMaxAddressLineIndex() >= 1) ? a.getAddressLine(1) : "",
				(a.getMaxAddressLineIndex() >= 2) ? a.getAddressLine(2) : "",
				(a.getMaxAddressLineIndex() >= 3) ? a.getAddressLine(3) : "",
				(a.getMaxAddressLineIndex() >= 4) ? a.getAddressLine(4) : "");
		return val.equals("");
	}

	public static Address parseLineOne(Address a) {
		String[] pieces = a.getAddressLine(0).trim().replaceAll(" +", " ").split(" ", 2);
		switch(pieces.length) {
		case 2:
			a.setFeatureName(pieces[0]);
			a.setThoroughfare(pieces[1]);
			break;
		case 1:
			if(pieces[0].matches("^[0-9\\-]+$")) {
				a.setFeatureName(pieces[0]);
				a.setThoroughfare("");
			}else{
				a.setFeatureName("");
				a.setThoroughfare(pieces[0]);
			}
			break;
		default:
			a.setFeatureName("");
			a.setThoroughfare("");
			break;
		}
		return a;
	}

	public static Address clone(Address a) {
		Address clone = new Address(Locale.getDefault());
		for(int i=0; i<=a.getMaxAddressLineIndex(); i++) {
			clone.setAddressLine(i, (a.getAddressLine(i) != null) ? a.getAddressLine(i) : "");
		}
		clone.setFeatureName(a.getFeatureName() != null ? a.getFeatureName() : "");
		clone.setAdminArea(a.getAdminArea() != null ? a.getAdminArea() : "");
		clone.setLocality(a.getLocality() != null ? a.getLocality() : "");
		clone.setThoroughfare(a.getThoroughfare() != null ? a.getThoroughfare() : "");
		clone.setCountryCode(a.getCountryCode() != null ? a.getCountryCode() : "");
		clone.setCountryName(a.getCountryName() != null ? a.getCountryName() : "");
		if(a.hasLatitude()) {
			clone.setPhone(a.getPhone() != null ? a.getPhone() : "");
			clone.setLatitude(a.getLatitude());
		}
		if(a.hasLongitude()) {
			clone.setLongitude(a.getLongitude());
		}
		clone.setPostalCode(a.getPostalCode() != null ? a.getPostalCode() : "");
		return clone;
	}

	public PoolAddress() {

		super(Locale.getDefault());

		id = 0;
		setAddressLine(0, "");
		setAddressLine(1, "");
		setAddressLine(2, "");
		setFeatureName("");
		setAdminArea("");
		setLocality("");
		setThoroughfare("");
		setCountryCode("");
		setCountryName("");
		setPostalCode("");
	}

	public PoolAddress(Context context, long id) {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.ADDRESSES_CONTENT_URI, Long.toString(id)),
				AddressTable.columnProjection(), null, null, null));
	}

	public PoolAddress(Address address) {

		super(Locale.getDefault());

		id = 0;

		for(int i=0; i<=address.getMaxAddressLineIndex(); i++) {
			setAddressLine(i, (address.getAddressLine(i) != null) ? address.getAddressLine(i) : "");
		}
		setFeatureName(address.getFeatureName() != null ? address.getFeatureName() : "");
		setAdminArea(address.getAdminArea() != null ? address.getAdminArea() : "");
		setLocality(address.getLocality() != null ? address.getLocality() : "");
		setThoroughfare(address.getThoroughfare() != null ? address.getThoroughfare() : "");
		setCountryCode(address.getCountryCode() != null ? address.getCountryCode() : "");
		setCountryName(address.getCountryName() != null ? address.getCountryName() : "");
		if(address.hasLatitude()) {
			setPhone(address.getPhone() != null ? address.getPhone() : "");
			setLatitude(address.getLatitude());
		}
		if(address.hasLongitude()) {
			setLongitude(address.getLongitude());
		}
		setPostalCode(address.getPostalCode() != null ? address.getPostalCode() : "");
	}

	public PoolAddress(Context context, Cursor cursor) {

		super(Locale.getDefault());

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

		if(cols.containsKey(AddressTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(AddressTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(AddressTable.KEY_LINE_ONE)) {
			setAddressLine(0, cursor.getString(cols.get(AddressTable.KEY_LINE_ONE)));
		}else{
			setAddressLine(0, "");
		}

		if(cols.containsKey(AddressTable.KEY_LINE_TWO)) {
			setAddressLine(1, cursor.getString(cols.get(AddressTable.KEY_LINE_TWO)));
		}else{
			setAddressLine(1, "");
		}

		if(cols.containsKey(AddressTable.KEY_LINE_THREE)) {
			setAddressLine(2, cursor.getString(cols.get(AddressTable.KEY_LINE_THREE)));
		}else{
			setAddressLine(2, "");
		}

		if(cols.containsKey(AddressTable.KEY_FEATURE)) {
			setFeatureName(cursor.getString(cols.get(AddressTable.KEY_FEATURE)));
		}else{
			setFeatureName("");
		}

		if(cols.containsKey(AddressTable.KEY_ADMIN)) {
			setAdminArea(cursor.getString(cols.get(AddressTable.KEY_ADMIN)));
		}else{
			setAdminArea("");
		}

		if(cols.containsKey(AddressTable.KEY_LOCALITY)) {
			setLocality(cursor.getString(cols.get(AddressTable.KEY_LOCALITY)));
		}else{
			setLocality("");
		}

		if(cols.containsKey(AddressTable.KEY_THOROUGHFARE)) {
			setThoroughfare(cursor.getString(cols.get(AddressTable.KEY_THOROUGHFARE)));
		}else{
			setThoroughfare("");
		}

		if(cols.containsKey(AddressTable.KEY_COUNTRY_CODE)) {
			setCountryCode(cursor.getString(cols.get(AddressTable.KEY_COUNTRY_CODE)));
		}else{
			setCountryCode("");
		}

		if(cols.containsKey(AddressTable.KEY_COUNTRY_NAME)) {
			setCountryName(cursor.getString(cols.get(AddressTable.KEY_COUNTRY_NAME)));
		}else{
			setCountryName("");
		}

		if(cols.containsKey(AddressTable.KEY_LATITUDE)) {
			setLatitude(cursor.getDouble(cols.get(AddressTable.KEY_LATITUDE)));
		}else{
			clearLatitude();
		}

		if(cols.containsKey(AddressTable.KEY_LONGITUDE)) {
			setLongitude(cursor.getDouble(cols.get(AddressTable.KEY_LONGITUDE)));
		}else{
			clearLongitude();
		}

		if(cols.containsKey(AddressTable.KEY_POSTAL_CODE)) {
			setPostalCode(cursor.getString(cols.get(AddressTable.KEY_POSTAL_CODE)));
		}else{
			setPostalCode("");
		}
	}

	public ContentValues getContentValues() {

		ContentValues values = new ContentValues();
		values.put(AddressTable.KEY_LINE_ONE, getAddressLine(0));
		values.put(AddressTable.KEY_LINE_TWO, getAddressLine(1));
		values.put(AddressTable.KEY_LINE_THREE, getAddressLine(2));
		values.put(AddressTable.KEY_FEATURE, getFeatureName());
		values.put(AddressTable.KEY_ADMIN, getAdminArea());
		values.put(AddressTable.KEY_THOROUGHFARE, getThoroughfare());
		values.put(AddressTable.KEY_LOCALITY, getLocality());
		values.put(AddressTable.KEY_COUNTRY_CODE, getCountryCode());
		values.put(AddressTable.KEY_COUNTRY_NAME, getCountryName());
		values.put(AddressTable.KEY_POSTAL_CODE, getPostalCode());
		if(hasLatitude()) {
			values.put(AddressTable.KEY_LATITUDE, getLatitude());
		}
		if(hasLongitude()) {
			values.put(AddressTable.KEY_LONGITUDE, getLongitude());
		}

		return values;
	}

	public boolean insert(Context ctx) {

		Uri uri = ctx.getContentResolver().insert(PoolPalContent.ADDRESSES_CONTENT_URI, getContentValues());
		long new_id = Long.parseLong(uri.getLastPathSegment());
		if(new_id > 0) {
			id = new_id;
			return true;
		}else{
			id = 0;
			return false;
		}
	}

	private PoolAddress(Parcel in) {

		super(Locale.getDefault());

		this.id = in.readLong();
		setAddressLine(0, in.readString());
		setAddressLine(1, in.readString());
		setAddressLine(2, in.readString());
		setFeatureName(in.readString());
		setAdminArea(in.readString());
		setLocality(in.readString());
		setThoroughfare(in.readString());
		setCountryCode(in.readString());
		setCountryName(in.readString());
		Double latitude = in.readDouble();
		Double longitude = in.readDouble();
		if(latitude != -99999d) {
			setLatitude(latitude);
		}else{
			clearLatitude();
		}
		if(longitude != -99999d) {
			setLongitude(longitude);
		}else{
			clearLongitude();
		}
		setPostalCode(in.readString());
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {

		out.writeLong(id);
		out.writeString(getAddressLine(0));
		out.writeString(getAddressLine(1));
		out.writeString(getAddressLine(2));
		out.writeString(getFeatureName());
		out.writeString(getAdminArea());
		out.writeString(getLocality());
		out.writeString(getThoroughfare());
		out.writeString(getCountryCode());
		out.writeString(getCountryName());
		out.writeDouble((hasLatitude()) ? getLatitude() : -99999d);
		out.writeDouble((hasLongitude()) ? getLongitude() : -99999d);
		out.writeString(getPostalCode());
	}

	public static final Parcelable.Creator<PoolAddress> CREATOR = new Parcelable.Creator<PoolAddress>() {

		public PoolAddress createFromParcel(Parcel in) {
			return new PoolAddress(in);
		}

		public PoolAddress[] newArray(int size) {
			return new PoolAddress[size];
		}
	};

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
	 * @return boolean indicating if this addresses id is valid
	 */
	public boolean hasId() {
		return this.id > 0;
	}

	/**
	 * @return string representation of the address
	 */
	public String toString() {
		return String.format("%1$s%2$s%3$s",
				(getMaxAddressLineIndex() >= 0) ? getAddressLine(0) : "",
				(getMaxAddressLineIndex() >= 1) ? "\n" + getAddressLine(1) : "",
				(getMaxAddressLineIndex() >= 2) ? "\n" + getAddressLine(2) : "");
	}

	/**
	 * @return boolean indicating if this address is blank
	 */
	public boolean isBlank() {
		return (getFeatureName().equals("")
				&& getThoroughfare().equals("") && getLocality().equals("")
				&& getAdminArea().equals("") && getPostalCode().equals(""));
	}
}
