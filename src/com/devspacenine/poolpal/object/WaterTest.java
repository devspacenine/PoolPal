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
import com.devspacenine.poolpal.database.WaterTestTable;

public class WaterTest implements Parcelable {

	public static String listToString(List<WaterTest> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<WaterTest> i = l.iterator(); i.hasNext();) {
			WaterTest t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<WaterTest> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<WaterTest> l = new ArrayList<WaterTest>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new WaterTest(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	private long id;
	private Pool pool;
	private Task task;
	private Date date;
	private double chlorine;
	private double bromine;
	private double ph;
	private int alkalinity;
	private int hardness;
	private int stabilizer;
	private double temperature;
	private int tds;
	private int borate;
	private double lsi; // Langelier Saturation Index (pH-pHs): aggressive < -1.5 < 0 < 1.5 < scale forming
	private double rsi; // Ryznar Stability Index (2pHs-pH): scale forming < 4.5 < 6 < 8 < aggressive
	private double psi; // Puckorius Scaling Index (2pHs-pHeq): scale forming < 4.5 < 6 < 8 < aggressive

	public WaterTest() {
		id = 0;
		pool = new Pool();
		task = new Task();
		date = new Date();
		chlorine = -1.00d;
		bromine = -1.00d;
		ph = -1.00d;
		alkalinity = -1;
		hardness = -1;
		stabilizer = -1;
		temperature = -100.00d;
		tds = -1;
		borate = -1;
		lsi = -100.00d;
		rsi = -100.00d;
		psi = -100.00d;
	}

	public WaterTest(Context context, long id) throws ParseException {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.WATER_TESTS_CONTENT_URI, Long.toString(id)),
				WaterTestTable.columnProjection(), null, null, null));
	}

	public WaterTest(Context context, Cursor cursor) throws ParseException {

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

		if(cols.containsKey(WaterTestTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(WaterTestTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(WaterTestTable.KEY_POOL)) {
			this.pool = new Pool(context, cursor.getInt(cols.get(WaterTestTable.KEY_POOL)));
		}else{
			this.pool = new Pool();
		}

		if(cols.containsKey(WaterTestTable.KEY_TASK)) {
			this.task = new Task(context, cursor.getInt(cols.get(WaterTestTable.KEY_TASK)));
		}else{
			this.task = new Task();
		}

		if(cols.containsKey(WaterTestTable.KEY_DATE)) {
			this.date = PoolPal.DATE_FORMAT.parse(cursor.getString(cols.get(WaterTestTable.KEY_DATE)));
		}else{
			this.date = new Date();
		}

		if(cols.containsKey(WaterTestTable.KEY_CHLORINE)) {
			this.chlorine = cursor.getDouble(cols.get(WaterTestTable.KEY_CHLORINE));
		}else{
			this.chlorine = -1.00d;
		}

		if(cols.containsKey(WaterTestTable.KEY_BROMINE)) {
			this.bromine = cursor.getDouble(cols.get(WaterTestTable.KEY_BROMINE));
		}else{
			this.bromine = -1.00d;
		}

		if(cols.containsKey(WaterTestTable.KEY_PH)) {
			this.ph = cursor.getDouble(cols.get(WaterTestTable.KEY_PH));
		}else{
			this.ph = -1.00d;
		}

		if(cols.containsKey(WaterTestTable.KEY_ALKALINITY)) {
			this.alkalinity = cursor.getInt(cols.get(WaterTestTable.KEY_ALKALINITY));
		}else{
			this.alkalinity = -1;
		}

		if(cols.containsKey(WaterTestTable.KEY_HARDNESS)) {
			this.hardness = cursor.getInt(cols.get(WaterTestTable.KEY_HARDNESS));
		}else{
			this.hardness = -1;
		}

		if(cols.containsKey(WaterTestTable.KEY_STABILIZER)) {
			this.stabilizer = cursor.getInt(cols.get(WaterTestTable.KEY_STABILIZER));
		}else{
			this.stabilizer = -1;
		}

		if(cols.containsKey(WaterTestTable.KEY_TEMPERATURE)) {
			this.temperature = cursor.getDouble(cols.get(WaterTestTable.KEY_TEMPERATURE));
		}else{
			this.temperature = -100.00d;
		}

		if(cols.containsKey(WaterTestTable.KEY_TDS)) {
			this.tds = cursor.getInt(cols.get(WaterTestTable.KEY_TDS));
		}else{
			this.tds = -1;
		}

		if(cols.containsKey(WaterTestTable.KEY_BORATE)) {
			this.borate = cursor.getInt(cols.get(WaterTestTable.KEY_BORATE));
		}else{
			this.borate = -1;
		}

		if(cols.containsKey(WaterTestTable.KEY_LSI)) {
			this.lsi = cursor.getDouble(cols.get(WaterTestTable.KEY_LSI));
		}else{
			this.lsi = -100.00d;
		}

		if(cols.containsKey(WaterTestTable.KEY_RSI)) {
			this.rsi = cursor.getDouble(cols.get(WaterTestTable.KEY_RSI));
		}else{
			this.rsi = -100.00d;
		}

		if(cols.containsKey(WaterTestTable.KEY_PSI)) {
			this.psi = cursor.getDouble(cols.get(WaterTestTable.KEY_PSI));
		}else{
			this.psi = -100.00d;
		}

	}

	/**
	 * @return string representation of this task
	 */
	public String toString() {
		return (this.id > 0) ? Long.toString(this.id) : null;
	}

	public double calculateLSI() {

		// Make sure pH value is set
		if(ph < 0) throw new IllegalStateException("pH required to calculate LSI.");

		return ph - saturationPH();
	}

	public double calculateRSI() {

		// Make sure pH value is set
		if(ph < 0) throw new IllegalStateException("pH required to calculate RSI.");

		return (2d * saturationPH()) - ph;
	}

	public double calculatePSI() {

		// Make sure pH value is set
		if(ph < 0) throw new IllegalStateException("pH required to calculate PSI.");

		return (2d * saturationPH()) - equilibriumPH();
	}

	public double saturationPH() {

		// Make sure TDS, Temp, Hardness, and Alkalinity values are set
		if(tds < 0) throw new IllegalStateException("TDS required to calculate saturation pH.");
		if(temperature < 0) throw new IllegalStateException("Temperature required to calculate saturation pH.");
		if(hardness < 0) throw new IllegalStateException("Hardness required to calculate saturation pH.");
		if(alkalinity < 0) throw new IllegalStateException("Alkalinity required to calculate saturation pH.");

		double logKs = -171.9065d - (0.077993d * temperature) + (2839.319d / temperature)
				+ (71.595d * Math.log10(temperature));
		double logK2 = -107.8871d - (0.032528d * temperature) + (5151.79d / temperature)
				+ (38.92561d * Math.log10(temperature)) - (563713.9d / Math.pow(temperature, 2d));
		double mu = 0.000025d * ((double)tds);
		double eps = (60954d / (temperature + 116d)) - 68.937d;
		double logyCa = -(1825000d * (Math.pow((eps * temperature), -1.5d)))
				* 4d * ((Math.sqrt(mu) / (1d + Math.sqrt(mu))) - (0.3d * mu));
		double logyHCO3 = -(1825000d * (Math.pow((eps * temperature), -1.5d)))
				* ((Math.sqrt(mu) / (1d + Math.sqrt(mu))) - (0.3d * mu));
		double c = logK2 - logKs - 9.7d + logyCa + logyHCO3;
		double tc = 0.0155d * kelvinToCelsius(temperature);

		return -(Math.log10((double)hardness) + Math.log10((double)alkalinity) + tc + c);
	}

	public double equilibriumPH() {

		// Make sure Alkalinity value is set
		if(alkalinity < 0) throw new IllegalStateException("Alkalinity required to calculate saturation pH.");

		return (1.465d * Math.log10((double)alkalinity)) + 4.54d;
	}

	/**
	 * Convert degrees fahrenheit to degrees celsius
	 */
	public double fahrenheitToCelsius(double Tf) {

		return (5d / 9d) * (Tf - 32d);
	}

	/**
	 * Convert degrees fahrenheit to degrees kelvin
	 */
	public double fahrenheitToKelvin(double Tf) {

		return celsiusToKelvin(fahrenheitToCelsius(Tf));
	}

	/**
	 * Convert degrees celsius to degrees fahrenheit
	 */
	public double celsiusToFahrenheit(double Tc) {

		return ((9d / 5d) * Tc) + 32d;
	}

	/**
	 * Convert degrees celsius to degrees kelvin
	 */
	public double celsiusToKelvin(double Tc) {

		return Tc + 273d;
	}

	/**
	 * Convert degrees kelvin to degrees celsius
	 */
	public double kelvinToCelsius(double Tk) {

		return Tk - 273d;
	}

	/**
	 * Convert degrees kelvin to degrees fahrenheit
	 */
	public double kelvinToFahrenheit(double Tk) {

		return celsiusToFahrenheit(kelvinToCelsius(Tk));
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
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
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
	 * @return the chlorine
	 */
	public double getChlorine() {
		return chlorine;
	}

	/**
	 * @param chlorine the chlorine to set
	 */
	public void setChlorine(double chlorine) {
		this.chlorine = chlorine;
	}

	/**
	 * @return the bromine
	 */
	public double getBromine() {
		return bromine;
	}

	/**
	 * @param bromine the bromine to set
	 */
	public void setBromine(double bromine) {
		this.bromine = bromine;
	}

	/**
	 * @return the ph
	 */
	public double getPh() {
		return ph;
	}

	/**
	 * @param ph the ph to set
	 */
	public void setPh(double ph) {
		this.ph = ph;
	}

	/**
	 * @return the alkalinity
	 */
	public int getAlkalinity() {
		return alkalinity;
	}

	/**
	 * @param alkalinity the alkalinity to set
	 */
	public void setAlkalinity(int alkalinity) {
		this.alkalinity = alkalinity;
	}

	/**
	 * @return the hardness
	 */
	public int getHardness() {
		return hardness;
	}

	/**
	 * @param hardness the hardness to set
	 */
	public void setHardness(int hardness) {
		this.hardness = hardness;
	}

	/**
	 * @return the stabilizer
	 */
	public int getStabilizer() {
		return stabilizer;
	}

	/**
	 * @param stabilizer the stabilizer to set
	 */
	public void setStabilizer(int stabilizer) {
		this.stabilizer = stabilizer;
	}

	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the tds
	 */
	public int getTds() {
		return tds;
	}

	/**
	 * @param tds the tds to set
	 */
	public void setTds(int tds) {
		this.tds = tds;
	}

	/**
	 * @return the lsi
	 */
	public double getLsi() {
		return lsi;
	}

	/**
	 * @param lsi the lsi to set
	 */
	public void setLsi(double lsi) {
		this.lsi = lsi;
	}

	/**
	 * @return the borate
	 */
	public int getBorate() {
		return borate;
	}

	/**
	 * @param borate the borate to set
	 */
	public void setBorate(int borate) {
		this.borate = borate;
	}

	/**
	 * @return the rsi
	 */
	public double getRsi() {
		return rsi;
	}

	/**
	 * @param rsi the rsi to set
	 */
	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	/**
	 * @return the psi
	 */
	public double getPsi() {
		return psi;
	}

	/**
	 * @param psi the psi to set
	 */
	public void setPsi(double psi) {
		this.psi = psi;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeParcelable(pool, flags);
		out.writeParcelable(task, flags);
		out.writeLong(id);
		out.writeString(PoolPal.DATE_FORMAT.format(date));
		out.writeDouble(chlorine);
		out.writeDouble(bromine);
		out.writeDouble(ph);
		out.writeInt(alkalinity);
		out.writeInt(hardness);
		out.writeInt(stabilizer);
		out.writeDouble(temperature);
		out.writeInt(tds);
		out.writeInt(borate);
		out.writeDouble(lsi);
		out.writeDouble(rsi);
		out.writeDouble(psi);
	}

	private WaterTest(Parcel in) throws ParseException {

		this.pool = in.readParcelable(Pool.class.getClassLoader());
		this.task = in.readParcelable(Task.class.getClassLoader());
		this.id = in.readLong();
		this.date = PoolPal.DATE_FORMAT.parse(in.readString());
		this.chlorine = in.readDouble();
		this.bromine = in.readDouble();
		this.ph = in.readDouble();
		this.alkalinity = in.readInt();
		this.hardness = in.readInt();
		this.stabilizer = in.readInt();
		this.temperature = in.readDouble();
		this.tds = in.readInt();
		this.borate = in.readInt();
		this.lsi = in.readDouble();
		this.rsi = in.readDouble();
		this.psi = in.readDouble();
	}

	public static final Parcelable.Creator<WaterTest> CREATOR = new Parcelable.Creator<WaterTest>() {

		public WaterTest createFromParcel(Parcel in) {
			try {
				return new WaterTest(in);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		public WaterTest[] newArray(int size) {
			return new WaterTest[size];
		}
	};
}
