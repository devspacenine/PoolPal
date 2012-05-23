package com.devspacenine.poolpal.object;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.PoolTable;
import com.devspacenine.poolpal.widget.DataAdapter;
import com.devspacenine.poolpal.widget.PoolDataAdapter;


public class Pool implements Parcelable {

	public static String listToString(List<Pool> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<Pool> i = l.iterator(); i.hasNext();) {
			Pool t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<Pool> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<Pool> l = new ArrayList<Pool>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new Pool(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	private long id;
	private String image;
	private PoolAddress address;
	private String addressString;
	private String name;
	private double volume;
	private String poolLocale;
	private String finish;
	private String pumpBrand;
	private String pumpModel;
	private String sanitizer;
	private String filter;
	private String cleanerBrand;
	private String cleanerModel;
	private String traffic;
	private double minDepth;
	private double maxDepth;
	private boolean tiling;
	private boolean cover;
	private boolean attachedSpa;
	private boolean heater;
	private boolean divingBoard;
	private boolean slide;
	private boolean ladder;
	private boolean fountains;
	private boolean rockWaterfall;
	private boolean lights;
	private boolean infinity;
	private boolean sportingEquipment;
	private boolean beachEntry;
	private boolean sand;
	private boolean weatherNotifications;
	private boolean waterTestReminders;
	private boolean filterReminders;
	private boolean safetyNotifications;
	private boolean maintenanceReminders;
	private boolean customNotifications;
	private boolean couponNotifications;

	public Pool() {
		id = 0;
		image = "";
		address = new PoolAddress(PoolAddress.blankAddress());
		name = "";
		volume = -1.00d;
		poolLocale = "";
		finish = "";
		pumpBrand = "";
		pumpModel = "";
		sanitizer = "";
		filter = "";
		cleanerBrand = "";
		cleanerModel = "";
		traffic = "";
		minDepth = 3.00d;
		maxDepth = 9.00d;
		tiling = false;
		cover = false;
		attachedSpa = false;
		heater = false;
		divingBoard = false;
		slide = false;
		ladder = false;
		fountains = false;
		rockWaterfall = false;
		lights = false;
		infinity = false;
		sportingEquipment = false;
		beachEntry = false;
		sand = false;
		weatherNotifications = true;
		waterTestReminders = true;
		filterReminders = true;
		safetyNotifications = true;
		maintenanceReminders = true;
		customNotifications = true;
		couponNotifications = true;
	}

	public Pool(Context context, long id) {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI, Long.toString(id)),
				PoolTable.columnProjection(), null, null, null));
	}

	public Pool(Context context, Cursor cursor) {

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

		if(cols.containsKey(PoolTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(PoolTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(PoolTable.KEY_IMAGE)) {
			this.image = cursor.getString(cols.get(PoolTable.KEY_IMAGE));
		}else{
			this.image = "";
		}

		if(cols.containsKey(PoolTable.KEY_ADDRESS)) {
			this.address = new PoolAddress(context,
					cursor.getLong(cols.get(PoolTable.KEY_ADDRESS)));
		}else{
			this.address = new PoolAddress(PoolAddress.blankAddress());
		}

		if(cols.containsKey(PoolTable.KEY_NAME)) {
			this.name = cursor.getString(cols.get(PoolTable.KEY_NAME));
		}else{
			this.name = "";
		}

		if(cols.containsKey(PoolTable.KEY_VOLUME)) {
			this.volume = cursor.getDouble(cols.get(PoolTable.KEY_VOLUME));
		}else{
			this.volume = -1.00d;
		}

		if(cols.containsKey(PoolTable.KEY_POOL_LOCALE)) {
			this.poolLocale = cursor.getString(cols.get(PoolTable.KEY_POOL_LOCALE));
		}else{
			this.poolLocale = "";
		}

		if(cols.containsKey(PoolTable.KEY_FINISH)) {
			this.finish = cursor.getString(cols.get(PoolTable.KEY_FINISH));
		}else{
			this.finish = "";
		}

		if(cols.containsKey(PoolTable.KEY_SANITIZER)) {
			this.sanitizer = cursor.getString(cols.get(PoolTable.KEY_SANITIZER));
		}else{
			this.sanitizer = "";
		}

		if(cols.containsKey(PoolTable.KEY_PUMP_BRAND)) {
			this.pumpBrand = cursor.getString(cols.get(PoolTable.KEY_PUMP_BRAND));
		}else{
			this.pumpBrand = "";
		}

		if(cols.containsKey(PoolTable.KEY_PUMP_MODEL)) {
			this.pumpModel = cursor.getString(cols.get(PoolTable.KEY_PUMP_MODEL));
		}else{
			this.pumpModel = "";
		}

		if(cols.containsKey(PoolTable.KEY_FILTER)) {
			this.filter = cursor.getString(cols.get(PoolTable.KEY_FILTER));
		}else{
			this.filter = "";
		}

		if(cols.containsKey(PoolTable.KEY_CLEANER_BRAND)) {
			this.cleanerBrand = cursor.getString(cols.get(PoolTable.KEY_CLEANER_BRAND));
		}else{
			this.cleanerBrand = "";
		}

		if(cols.containsKey(PoolTable.KEY_CLEANER_MODEL)) {
			this.cleanerModel = cursor.getString(cols.get(PoolTable.KEY_CLEANER_MODEL));
		}else{
			this.cleanerModel = "";
		}

		if(cols.containsKey(PoolTable.KEY_TRAFFIC)) {
			this.traffic = cursor.getString(cols.get(PoolTable.KEY_TRAFFIC));
		}else{
			this.traffic = "";
		}

		if(cols.containsKey(PoolTable.KEY_MIN_DEPTH)) {
			this.minDepth = cursor.getDouble(cols.get(PoolTable.KEY_MIN_DEPTH));
		}else{
			this.minDepth = -1.00d;
		}

		if(cols.containsKey(PoolTable.KEY_MAX_DEPTH)) {
			this.maxDepth = cursor.getDouble(cols.get(PoolTable.KEY_MAX_DEPTH));
		}else{
			this.maxDepth = -1.00d;
		}

		if(cols.containsKey(PoolTable.KEY_TILING)) {
			this.tiling = cursor.getInt(cols.get(PoolTable.KEY_TILING)) > 0;
		}else{
			this.tiling = false;
		}

		if(cols.containsKey(PoolTable.KEY_COVER)) {
			this.cover = cursor.getInt(cols.get(PoolTable.KEY_COVER)) > 0;
		}else{
			this.cover = false;
		}

		if(cols.containsKey(PoolTable.KEY_ATTACHED_SPA)) {
			this.attachedSpa = cursor.getInt(cols.get(PoolTable.KEY_ATTACHED_SPA)) > 0;
		}else{
			this.attachedSpa = false;
		}

		if(cols.containsKey(PoolTable.KEY_HEATER)) {
			this.heater = cursor.getInt(cols.get(PoolTable.KEY_HEATER)) > 0;
		}else{
			this.heater = false;
		}

		if(cols.containsKey(PoolTable.KEY_DIVING_BOARD)) {
			this.divingBoard = cursor.getInt(cols.get(PoolTable.KEY_DIVING_BOARD)) > 0;
		}else{
			this.divingBoard = false;
		}

		if(cols.containsKey(PoolTable.KEY_SLIDE)) {
			this.slide = cursor.getInt(cols.get(PoolTable.KEY_SLIDE)) > 0;
		}else{
			this.slide = false;
		}

		if(cols.containsKey(PoolTable.KEY_LADDER)) {
			this.ladder = cursor.getInt(cols.get(PoolTable.KEY_LADDER)) > 0;
		}else{
			this.ladder = false;
		}

		if(cols.containsKey(PoolTable.KEY_FOUNTAINS)) {
			this.fountains = cursor.getInt(cols.get(PoolTable.KEY_FOUNTAINS)) > 0;
		}else{
			this.fountains = false;
		}

		if(cols.containsKey(PoolTable.KEY_ROCK_WATERFALL)) {
			this.rockWaterfall = cursor.getInt(cols.get(PoolTable.KEY_ROCK_WATERFALL)) > 0;
		}else{
			this.rockWaterfall = false;
		}

		if(cols.containsKey(PoolTable.KEY_LIGHTS)) {
			this.lights = cursor.getInt(cols.get(PoolTable.KEY_LIGHTS)) > 0;
		}else{
			this.lights = false;
		}

		if(cols.containsKey(PoolTable.KEY_INFINITY)) {
			this.infinity = cursor.getInt(cols.get(PoolTable.KEY_INFINITY)) > 0;
		}else{
			this.infinity = false;
		}

		if(cols.containsKey(PoolTable.KEY_SPORTING_EQUIPMENT)) {
			this.sportingEquipment = cursor.getInt(cols.get(PoolTable.KEY_SPORTING_EQUIPMENT)) > 0;
		}else{
			this.sportingEquipment = false;
		}

		if(cols.containsKey(PoolTable.KEY_BEACH_ENTRY)) {
			this.beachEntry = cursor.getInt(cols.get(PoolTable.KEY_BEACH_ENTRY)) > 0;
		}else{
			this.beachEntry = false;
		}

		if(cols.containsKey(PoolTable.KEY_SAND)) {
			this.sand = cursor.getInt(cols.get(PoolTable.KEY_SAND)) > 0;
		}else{
			this.sand = false;
		}

		if(cols.containsKey(PoolTable.KEY_WEATHER_NOTIFICATIONS)) {
			this.weatherNotifications = cursor.getInt(cols.get(PoolTable.KEY_WEATHER_NOTIFICATIONS)) > 0;
		}else{
			this.weatherNotifications = true;
		}

		if(cols.containsKey(PoolTable.KEY_WATER_TEST_REMINDERS)) {
			this.waterTestReminders = cursor.getInt(cols.get(PoolTable.KEY_WATER_TEST_REMINDERS)) > 0;
		}else{
			this.waterTestReminders = true;
		}

		if(cols.containsKey(PoolTable.KEY_FILTER_REMINDERS)) {
			this.filterReminders = cursor.getInt(cols.get(PoolTable.KEY_FILTER_REMINDERS)) > 0;
		}else{
			this.filterReminders = true;
		}

		if(cols.containsKey(PoolTable.KEY_SAFETY_NOTIFICATIONS)) {
			this.safetyNotifications = cursor.getInt(cols.get(PoolTable.KEY_SAFETY_NOTIFICATIONS)) > 0;
		}else{
			this.safetyNotifications = true;
		}

		if(cols.containsKey(PoolTable.KEY_MAINTENANCE_REMINDERS)) {
			this.maintenanceReminders = cursor.getInt(cols.get(PoolTable.KEY_MAINTENANCE_REMINDERS)) > 0;
		}else{
			this.maintenanceReminders = true;
		}

		if(cols.containsKey(PoolTable.KEY_CUSTOM_NOTIFICATIONS)) {
			this.customNotifications = cursor.getInt(cols.get(PoolTable.KEY_CUSTOM_NOTIFICATIONS)) > 0;
		}else{
			this.customNotifications = true;
		}

		if(cols.containsKey(PoolTable.KEY_COUPON_NOTIFICATIONS)) {
			this.couponNotifications = cursor.getInt(cols.get(PoolTable.KEY_COUPON_NOTIFICATIONS)) > 0;
		}else{
			this.couponNotifications = true;
		}
	}

	public ContentValues getContentValues() {

		ContentValues values = new ContentValues();
		values.put(PoolTable.KEY_ADDRESS, getAddress().getId());
		values.put(PoolTable.KEY_NAME, name);
		values.put(PoolTable.KEY_IMAGE, image.equals("") ? null : image);
		values.put(PoolTable.KEY_VOLUME, volume);
		values.put(PoolTable.KEY_POOL_LOCALE, poolLocale.equals("") ? null : poolLocale);
		values.put(PoolTable.KEY_FINISH, finish.equals("") ? null : finish);
		values.put(PoolTable.KEY_PUMP_BRAND, pumpBrand.equals("") ? null : pumpBrand);
		values.put(PoolTable.KEY_PUMP_MODEL, pumpModel.equals("") ? null : pumpModel);
		values.put(PoolTable.KEY_SANITIZER, sanitizer.equals("") ? null : sanitizer);
		values.put(PoolTable.KEY_FILTER, filter.equals("") ? null : filter);
		values.put(PoolTable.KEY_CLEANER_BRAND, cleanerBrand.equals("") ? null : cleanerBrand);
		values.put(PoolTable.KEY_CLEANER_MODEL, cleanerModel.equals("") ? null : cleanerModel);
		values.put(PoolTable.KEY_TRAFFIC, traffic);
		values.put(PoolTable.KEY_MIN_DEPTH, minDepth);
		values.put(PoolTable.KEY_MAX_DEPTH, maxDepth);
		values.put(PoolTable.KEY_TILING, tiling ? 1 : 0);
		values.put(PoolTable.KEY_COVER, cover ? 1 : 0);
		values.put(PoolTable.KEY_ATTACHED_SPA, attachedSpa ? 1 : 0);
		values.put(PoolTable.KEY_HEATER, heater ? 1 : 0);
		values.put(PoolTable.KEY_DIVING_BOARD, divingBoard ? 1 : 0);
		values.put(PoolTable.KEY_SLIDE, slide ? 1 : 0);
		values.put(PoolTable.KEY_LADDER, ladder ? 1 : 0);
		values.put(PoolTable.KEY_FOUNTAINS, fountains ? 1 : 0);
		values.put(PoolTable.KEY_ROCK_WATERFALL, rockWaterfall ? 1 : 0);
		values.put(PoolTable.KEY_LIGHTS, lights ? 1 : 0);
		values.put(PoolTable.KEY_INFINITY, infinity ? 1 : 0);
		values.put(PoolTable.KEY_SPORTING_EQUIPMENT, sportingEquipment ? 1 : 0);
		values.put(PoolTable.KEY_BEACH_ENTRY, beachEntry ? 1 : 0);
		values.put(PoolTable.KEY_SAND, sand ? 1 : 0);
		values.put(PoolTable.KEY_WEATHER_NOTIFICATIONS, weatherNotifications ? 1 : 0);
		values.put(PoolTable.KEY_WATER_TEST_REMINDERS, waterTestReminders ? 1 : 0);
		values.put(PoolTable.KEY_FILTER_REMINDERS, filterReminders ? 1 : 0);
		values.put(PoolTable.KEY_SAFETY_NOTIFICATIONS, safetyNotifications ? 1 : 0);
		values.put(PoolTable.KEY_MAINTENANCE_REMINDERS, maintenanceReminders ? 1 : 0);
		values.put(PoolTable.KEY_CUSTOM_NOTIFICATIONS, customNotifications ? 1 : 0);
		values.put(PoolTable.KEY_COUPON_NOTIFICATIONS, couponNotifications ? 1 : 0);

		return values;
	}

	public boolean insert(Context ctx) {

		Uri uri = ctx.getContentResolver().insert(PoolPalContent.POOLS_CONTENT_URI, getContentValues());
		long new_id = Long.parseLong(uri.getLastPathSegment());
		if(new_id > 0) {
			// successfully inserted new row
			id = new_id;
			// create default tasks
			new Task(ctx, this, Task.GENERAL_MAINTENANCE);
			new Task(ctx, this, Task.WATER_TESTS);
			new Task(ctx, this, Task.FILTER_MAINTENANCE);
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

	public Boolean getAccessory(int id) {

		switch(id) {
		case 0:
			return hasTiling();

		case 1:
			return hasCover();

		case 2:
			return hasAttachedSpa();

		case 3:
			return hasHeater();

		case 4:
			return hasDivingBoard();

		case 5:
			return hasSlide();

		case 6:
			return hasLadder();

		case 7:
			return hasFountains();

		case 8:
			return hasRockWaterfall();

		case 9:
			return hasLights();

		case 10:
			return hasInfinity();

		case 11:
			return hasSportingEquipment();

		case 12:
			return hasBeachEntry();

		case 13:
			return hasSand();

		default:
			return false;
		}
	}

	public void setAccessory(int pos, boolean value) {

		switch(pos) {
		case 0:
			setTiling(value);
			break;

		case 1:
			setCover(value);
			break;

		case 2:
			setAttachedSpa(value);
			break;

		case 3:
			setHeater(value);
			break;

		case 4:
			setDivingBoard(value);
			break;

		case 5:
			setSlide(value);
			break;

		case 6:
			setLadder(value);
			break;

		case 7:
			setFountains(value);
			break;

		case 8:
			setRockWaterfall(value);
			break;

		case 9:
			setLights(value);
			break;

		case 10:
			setInfinity(value);
			break;

		case 11:
			setSportingEquipment(value);
			break;

		case 12:
			setBeachEntry(value);
			break;

		case 13:
			setSand(value);
			break;

		default:
			break;
		}
	}

	public boolean[] getAccessories() {
		return new boolean[] {tiling, cover, attachedSpa, heater, divingBoard, slide, ladder, fountains,
				rockWaterfall, lights, infinity, sportingEquipment, beachEntry, sand};
	}

	public Bundle getAccessoryBundle(int id) {
		Bundle b = new Bundle();
		b.putBoolean(DataAdapter.VALUE, getAccessory(id));
		return b;
	}

	public Bundle getAccessoriesBundle() {
		Bundle b = new Bundle();
		b.putBooleanArray(PoolDataAdapter.VALUE, getAccessories());
		return b;
	}

	/**
	 *
	 * @param requestCode id of attribute bundle to get
	 * @return bundle of values for attribute
	 */
	public Bundle getValuesBundle(Context context, int requestCode) {

		switch(requestCode) {
		case R.id.pool_name:
			return getNameBundle();

		case R.id.pool_image:
			return getImageBundle();

		case R.id.pool_dimensions:
			return getDimensionsBundle();

		case R.id.pool_traffic:
			return getTrafficBundle();

		case R.id.pool_locale:
			return getPoolLocaleBundle();

		case R.id.set_address:
			return getAddressBundle();

		case R.id.pool_finish:
			return getFinishBundle();

		case R.id.pool_sanitizer:
			return getSanitizerBundle();

		case R.id.pool_pump:
			return getPumpBundle();

		case R.id.pool_filter:
			return getFilterBundle();

		case R.id.pool_cleaner:
			return getCleanerBundle();

		case R.id.pool_accessories:
			return getAccessoriesBundle();

		case R.id.pool_tiling:
			return getAccessoryBundle(requestCode);

		case R.id.pool_cover:
			return getAccessoryBundle(requestCode);

		case R.id.pool_attached_spa:
			return getAccessoryBundle(requestCode);

		case R.id.pool_heater:
			return getAccessoryBundle(requestCode);

		case R.id.pool_diving_board:
			return getAccessoryBundle(requestCode);

		case R.id.pool_slide:
			return getAccessoryBundle(requestCode);

		case R.id.pool_ladder:
			return getAccessoryBundle(requestCode);

		case R.id.pool_fountains:
			return getAccessoryBundle(requestCode);

		case R.id.pool_rock_waterfall:
			return getAccessoryBundle(requestCode);

		case R.id.pool_lights:
			return getAccessoryBundle(requestCode);

		case R.id.pool_infinity:
			return getAccessoryBundle(requestCode);

		case R.id.pool_sporting_equipment:
			return getAccessoryBundle(requestCode);

		case R.id.pool_beach_entry:
			return getAccessoryBundle(requestCode);

		case R.id.pool_sand:
			return getAccessoryBundle(requestCode);

		case R.id.pool_weather_notifications:
			return getReminderBundle(requestCode);

		case R.id.pool_water_test_reminders:
			return getReminderBundle(requestCode);

		case R.id.pool_filter_reminders:
			return getReminderBundle(requestCode);

		case R.id.pool_safety_notifications:
			return getReminderBundle(requestCode);

		case R.id.pool_maintenance_reminders:
			return getReminderBundle(requestCode);

		case R.id.pool_custom_notifications:
			return getReminderBundle(requestCode);

		case R.id.pool_coupon_notifications:
			return getReminderBundle(requestCode);

		default:
			return null;
		}
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
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasImage() {
		return !image.equals("");
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getImageBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_IMAGE, image);
		b.putString(PoolDataAdapter.VALUE, image);
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
		if(address.hasId()) {
			this.address = address;
		}else{
			PoolAddress a = new PoolAddress(address);
			a.setId(this.address.getId());
			this.address = a;
		}
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		PoolAddress a = new PoolAddress(address);
		a.setId(this.address.getId());
		this.address = a;
	}

	/**
	 * @param id to give the address
	 */
	public void setAddressId(long id) {
		this.address.setId(id);
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getAddressBundle() {

		Bundle b = new Bundle();
		b.putParcelable(PoolDataAdapter.VALUE_ADDRESS,
				(hasAddress()) ? PoolAddress.setLines(address) : PoolAddress.blankAddress());
		b.putBoolean(PoolDataAdapter.VALUE_GEOCODED, PoolAddress.isGeocoded(address));
		b.putString(PoolDataAdapter.VALUE, (hasAddress()) ? getAddressString() : "");
		return b;
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
	 * @return String short string representation of location
	 */
	public String getLocationText() {

		return String.format("%1$s, %2$s", address.getLocality(), address.getAdminArea());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getNameBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_NAME, name);
		b.putString(PoolDataAdapter.VALUE, name);
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasName() {
		return !name.equals("");
	}

	/**
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	public String getVolumeString() {
		if(volume > 0) {
			return String.format("%1$,.0f g", volume);
		}else{
			return "";
		}
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getDimensionsBundle() {

		Bundle b = new Bundle();
		b.putDouble(PoolDataAdapter.VALUE_MIN_DEPTH, minDepth);
		b.putDouble(PoolDataAdapter.VALUE_MAX_DEPTH, maxDepth);
		b.putDouble(PoolDataAdapter.VALUE_VOLUME, volume);
		b.putString(PoolDataAdapter.VALUE, !hasDimensions() ? "" : String.format("%1$,.0f ft - %2$,.0f ft\n%3$s",
				minDepth,
				maxDepth,
				getVolumeString()));
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasDimensions() {
		return (minDepth > 0 && maxDepth > 0 && volume > 0);
	}

	/**
	 * @return the poolLocale
	 */
	public String getPoolLocale() {
		return poolLocale;
	}

	/**
	 * @param poolLocale the poolLocale to set
	 */
	public void setPoolLocale(String poolLocale) {
		this.poolLocale = poolLocale;
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getPoolLocaleBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_CHOICE, poolLocale);
		b.putString(PoolDataAdapter.VALUE, poolLocale);
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasPoolLocale() {
		return !poolLocale.equals("");
	}

	/**
	 * @return the finish
	 */
	public String getFinish() {
		return finish;
	}

	/**
	 * @param finish the finish to set
	 */
	public void setFinish(String finish) {
		this.finish = finish;
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getFinishBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_CHOICE, finish);
		b.putString(PoolDataAdapter.VALUE, finish);
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasFinish() {
		return !finish.equals("");
	}

	/**
	 * @return the pumpBrand
	 */
	public String getPumpBrand() {
		return pumpBrand;
	}

	/**
	 * @param pumpBrand the pumpBrand to set
	 */
	public void setPumpBrand(String pumpBrand) {
		this.pumpBrand = pumpBrand;
	}

	/**
	 * @return the pumpModel
	 */
	public String getPumpModel() {
		return pumpModel;
	}

	/**
	 * @param pumpModel the pumpModel to set
	 */
	public void setPumpModel(String pumpModel) {
		this.pumpModel = pumpModel;
	}

	public String getPumpString() {
		if(pumpBrand.equals("") && pumpModel.equals("")) {
			return "";
		}else{
			return String.format("%1$s - %2$s", pumpBrand, pumpModel);
		}
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getPumpBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_PUMP_BRAND, pumpBrand);
		b.putString(PoolDataAdapter.VALUE_PUMP_MODEL, pumpModel);
		b.putString(PoolDataAdapter.VALUE, getPumpString());
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasPump() {
		return (!pumpBrand.equals("") && !pumpModel.equals(""));
	}

	/**
	 * @return the sanitizer
	 */
	public String getSanitizer() {
		return sanitizer;
	}

	/**
	 * @param sanitizer the sanitizer to set
	 */
	public void setSanitizer(String sanitizer) {
		this.sanitizer = sanitizer;
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getSanitizerBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_CHOICE, sanitizer);
		b.putString(PoolDataAdapter.VALUE, sanitizer);
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasSanitizer() {
		return !sanitizer.equals("");
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getFilterBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_CHOICE, filter);
		b.putString(PoolDataAdapter.VALUE, filter);
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasFilter() {
		return !filter.equals("");
	}

	/**
	 * @return the cleanerBrand
	 */
	public String getCleanerBrand() {
		return cleanerBrand;
	}

	/**
	 * @param cleanerBrand the cleanerBrand to set
	 */
	public void setCleanerBrand(String cleanerBrand) {
		this.cleanerBrand = cleanerBrand;
	}

	/**
	 * @return the cleanerModel
	 */
	public String getCleanerModel() {
		return cleanerModel;
	}

	/**
	 * @param cleanerModel the cleanerModel to set
	 */
	public void setCleanerModel(String cleanerModel) {
		this.cleanerModel = cleanerModel;
	}

	public String getCleanerString() {
		if(cleanerBrand.equals("") && cleanerModel.equals("")) {
			return "";
		}else{
			return String.format("%1$s - %2$s", cleanerBrand, cleanerModel);
		}
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getCleanerBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_CLEANER_BRAND, cleanerBrand);
		b.putString(PoolDataAdapter.VALUE_CLEANER_MODEL, cleanerModel);
		b.putString(PoolDataAdapter.VALUE, getCleanerString());
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasCleaner() {
		return (!cleanerBrand.equals("") && !cleanerModel.equals(""));
	}

	/**
	 * @return the traffic
	 */
	public String getTraffic() {
		return traffic;
	}

	/**
	 * @param traffic the traffic to set
	 */
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}

	/**
	 * @return bundle representation of attribute
	 */
	public Bundle getTrafficBundle() {

		Bundle b = new Bundle();
		b.putString(PoolDataAdapter.VALUE_CHOICE, traffic);
		b.putString(PoolDataAdapter.VALUE, traffic);
		return b;
	}

	/**
	 * @return boolean indicating whether this attribute is set
	 */
	public boolean hasTraffic() {
		return !traffic.equals("");
	}

	/**
	 * @return the minDepth
	 */
	public double getMinDepth() {
		return minDepth;
	}

	/**
	 * @param minDepth the minDepth to set
	 */
	public void setMinDepth(double minDepth) {
		this.minDepth = minDepth;
	}

	/**
	 * @return the maxDepth
	 */
	public double getMaxDepth() {
		return maxDepth;
	}

	/**
	 * @param maxDepth the maxDepth to set
	 */
	public void setMaxDepth(double maxDepth) {
		this.maxDepth = maxDepth;
	}

	public String getDepth() {
		if(minDepth < 0 || maxDepth < 0) {
			return "";
		}else{
			return String.format("%1$d ft - %2$d ft", minDepth, maxDepth);
		}
	}

	/**
	 * @return the tiling
	 */
	public boolean hasTiling() {
		return tiling;
	}

	/**
	 * @param tiling the tiling to set
	 */
	public void setTiling(boolean tiling) {
		this.tiling = tiling;
	}

	/**
	 * @return the cover
	 */
	public boolean hasCover() {
		return cover;
	}

	/**
	 * @param cover the cover to set
	 */
	public void setCover(boolean cover) {
		this.cover = cover;
	}

	/**
	 * @return the attachedSpa
	 */
	public boolean hasAttachedSpa() {
		return attachedSpa;
	}

	/**
	 * @param attachedSpa the attachedSpa to set
	 */
	public void setAttachedSpa(boolean attachedSpa) {
		this.attachedSpa = attachedSpa;
	}

	/**
	 * @return the heater
	 */
	public boolean hasHeater() {
		return heater;
	}

	/**
	 * @param heater the heater to set
	 */
	public void setHeater(boolean heater) {
		this.heater = heater;
	}

	/**
	 * @return the divingBoard
	 */
	public boolean hasDivingBoard() {
		return divingBoard;
	}

	/**
	 * @param divingBoard the divingBoard to set
	 */
	public void setDivingBoard(boolean divingBoard) {
		this.divingBoard = divingBoard;
	}

	/**
	 * @return the slide
	 */
	public boolean hasSlide() {
		return slide;
	}

	/**
	 * @param slide the slide to set
	 */
	public void setSlide(boolean slide) {
		this.slide = slide;
	}

	/**
	 * @return the ladder
	 */
	public boolean hasLadder() {
		return ladder;
	}

	/**
	 * @param ladder the ladder to set
	 */
	public void setLadder(boolean ladder) {
		this.ladder = ladder;
	}

	/**
	 * @return the fountains
	 */
	public boolean hasFountains() {
		return fountains;
	}

	/**
	 * @param fountains the fountains to set
	 */
	public void setFountains(boolean fountains) {
		this.fountains = fountains;
	}

	/**
	 * @return the rockWaterfall
	 */
	public boolean hasRockWaterfall() {
		return rockWaterfall;
	}

	/**
	 * @param rockWaterfall the rockWaterfall to set
	 */
	public void setRockWaterfall(boolean rockWaterfall) {
		this.rockWaterfall = rockWaterfall;
	}

	/**
	 * @return the lights
	 */
	public boolean hasLights() {
		return lights;
	}

	/**
	 * @param lights the lights to set
	 */
	public void setLights(boolean lights) {
		this.lights = lights;
	}

	/**
	 * @return the infinity
	 */
	public boolean hasInfinity() {
		return infinity;
	}

	/**
	 * @param infinity the infinity to set
	 */
	public void setInfinity(boolean infinity) {
		this.infinity = infinity;
	}

	/**
	 * @return the sportingEquipment
	 */
	public boolean hasSportingEquipment() {
		return sportingEquipment;
	}

	/**
	 * @param sportingEquipment the sportingEquipment to set
	 */
	public void setSportingEquipment(boolean sportingEquipment) {
		this.sportingEquipment = sportingEquipment;
	}

	/**
	 * @return the beachEntry
	 */
	public boolean hasBeachEntry() {
		return beachEntry;
	}

	/**
	 * @param beachEntry the beachEntry to set
	 */
	public void setBeachEntry(boolean beachEntry) {
		this.beachEntry = beachEntry;
	}

	/**
	 * @return the sand
	 */
	public boolean hasSand() {
		return sand;
	}

	/**
	 * @param sand the sand to set
	 */
	public void setSand(boolean sand) {
		this.sand = sand;
	}

	/**
	 * @param requestCode of the target setting
	 */
	public void toggle(int requestCode) {

		switch(requestCode) {
		case PoolDataAdapter.SET_WEATHER_NOTIFICATIONS:
			this.weatherNotifications = !(this.weatherNotifications);
			break;

		case PoolDataAdapter.SET_WATER_TEST_REMINDERS:
			this.waterTestReminders = !(this.waterTestReminders);
			break;

		case PoolDataAdapter.SET_FILTER_REMINDERS:
			this.filterReminders = !(this.filterReminders);
			break;

		case PoolDataAdapter.SET_SAFETY_NOTIFICATIONS:
			this.safetyNotifications = !(this.safetyNotifications);
			break;

		case PoolDataAdapter.SET_MAINTENANCE_REMINDERS:
			this.maintenanceReminders = !(this.maintenanceReminders);
			break;

		case PoolDataAdapter.SET_CUSTOM_NOTIFICATIONS:
			this.customNotifications = !(this.customNotifications);
			break;

		case PoolDataAdapter.SET_COUPON_NOTIFICATIONS:
			this.couponNotifications = !(this.couponNotifications);
			break;

		default:
			break;
		}
	}

	/**
	 * @return bundle of values for data adapters
	 */
	public Bundle getReminderBundle(int id) {
		Bundle b = new Bundle();
		b.putBoolean(DataAdapter.VALUE, getReminder(id));
		return b;
	}

	/**
	 * @return the boolean value of a reminder
	 */
	public boolean getReminder(int id) {

		switch(id) {
		case R.id.pool_weather_notifications:
			return hasWeatherNotifications();

		case R.id.pool_water_test_reminders:
			return hasWaterTestReminders();

		case R.id.pool_filter_reminders:
			return hasFilterReminders();

		case R.id.pool_safety_notifications:
			return hasSafetyNotifications();

		case R.id.pool_maintenance_reminders:
			return hasMaintenanceReminders();

		case R.id.pool_custom_notifications:
			return hasCustomNotifications();

		case R.id.pool_coupon_notifications:
			return hasCouponNotifications();

		default:
			return false;
		}
	}

	/**
	 * @return the weatherNotifications
	 */
	public boolean hasWeatherNotifications() {
		return weatherNotifications;
	}

	/**
	 * @param weatherNotifications the weatherNotifications to set
	 */
	public void setWeatherNotifications(boolean weatherNotifications) {
		this.weatherNotifications = weatherNotifications;
	}

	/**
	 * @return the waterTestReminders
	 */
	public boolean hasWaterTestReminders() {
		return waterTestReminders;
	}

	/**
	 * @param waterTestReminders the waterTestReminders to set
	 */
	public void setWaterTestReminders(boolean waterTestReminders) {
		this.waterTestReminders = waterTestReminders;
	}

	/**
	 * @return the filterReminders
	 */
	public boolean hasFilterReminders() {
		return filterReminders;
	}

	/**
	 * @param filterReminders the filterReminders to set
	 */
	public void setFilterReminders(boolean filterReminders) {
		this.filterReminders = filterReminders;
	}

	/**
	 * @return the safetyNotifications
	 */
	public boolean hasSafetyNotifications() {
		return safetyNotifications;
	}

	/**
	 * @param safetyNotifications the safetyNotifications to set
	 */
	public void setSafetyNotifications(boolean safetyNotifications) {
		this.safetyNotifications = safetyNotifications;
	}

	/**
	 * @return the cleaningReminders
	 */
	public boolean hasMaintenanceReminders() {
		return maintenanceReminders;
	}

	/**
	 * @param cleaningReminders the cleaningReminders to set
	 */
	public void setMaintenanceReminders(boolean maintenanceReminders) {
		this.maintenanceReminders = maintenanceReminders;
	}

	/**
	 * @return the customNotifications
	 */
	public boolean hasCustomNotifications() {
		return customNotifications;
	}

	/**
	 * @param customNotifications the customNotifications to set
	 */
	public void setCustomNotifications(boolean customNotifications) {
		this.customNotifications = customNotifications;
	}

	/**
	 * @return the couponNotifications
	 */
	public boolean hasCouponNotifications() {
		return couponNotifications;
	}

	/**
	 * @param couponNotifications the couponNotifications to set
	 */
	public void setCouponNotifications(boolean couponNotifications) {
		this.couponNotifications = couponNotifications;
	}

	private Pool(Parcel in) {

		this.address = in.readParcelable(PoolAddress.class.getClassLoader());
		this.id = in.readLong();
		this.image = in.readString();
		this.name = in.readString();
		this.volume = in.readDouble();
		this.poolLocale = in.readString();
		this.finish = in.readString();
		this.pumpBrand = in.readString();
		this.pumpModel = in.readString();
		this.sanitizer = in.readString();
		this.filter = in.readString();
		this.cleanerBrand = in.readString();
		this.cleanerModel = in.readString();
		this.traffic = in.readString();
		this.minDepth = in.readDouble();
		this.maxDepth = in.readDouble();
		boolean[] bools = new boolean[21];
		in.readBooleanArray(bools);
		this.tiling = bools[0];
		this.cover = bools[1];
		this.attachedSpa = bools[2];
		this.heater = bools[3];
		this.divingBoard = bools[4];
		this.slide = bools[5];
		this.ladder = bools[6];
		this.fountains = bools[7];
		this.rockWaterfall = bools[8];
		this.lights = bools[9];
		this.infinity = bools[10];
		this.sportingEquipment = bools[11];
		this.beachEntry = bools[12];
		this.sand = bools[13];
		this.weatherNotifications = bools[14];
		this.waterTestReminders = bools[15];
		this.filterReminders = bools[16];
		this.safetyNotifications = bools[17];
		this.maintenanceReminders = bools[18];
		this.customNotifications = bools[19];
		this.couponNotifications = bools[20];
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {

		out.writeParcelable(address, flags);
		out.writeLong(id);
		out.writeString(image);
		out.writeString(name);
		out.writeDouble(volume);
		out.writeString(poolLocale);
		out.writeString(finish);
		out.writeString(pumpBrand);
		out.writeString(pumpModel);
		out.writeString(sanitizer);
		out.writeString(filter);
		out.writeString(cleanerBrand);
		out.writeString(cleanerModel);
		out.writeString(traffic);
		out.writeDouble(minDepth);
		out.writeDouble(maxDepth);
		out.writeBooleanArray(new boolean[] {tiling, cover, attachedSpa, heater, divingBoard,
				slide, ladder, fountains, rockWaterfall, lights, infinity, sportingEquipment,
				beachEntry, sand, weatherNotifications, waterTestReminders, filterReminders,
				safetyNotifications, maintenanceReminders, customNotifications, couponNotifications});
	}

	public static final Parcelable.Creator<Pool> CREATOR = new Parcelable.Creator<Pool>() {

		public Pool createFromParcel(Parcel in) {
			return new Pool(in);
		}

		public Pool[] newArray(int size) {
			return new Pool[size];
		}
	};
}
