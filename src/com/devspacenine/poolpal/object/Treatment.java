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
import com.devspacenine.poolpal.database.TreatmentTable;

public class Treatment implements Parcelable {

	public static String listToString(List<Treatment> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<Treatment> i = l.iterator(); i.hasNext();) {
			Treatment t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<Treatment> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<Treatment> l = new ArrayList<Treatment>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new Treatment(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	private long id;
	private Pool pool;
	private Task task;
	private Date date;
	private int chlorineTabs;
	private int bromineTabs;
	private String sanitizerSize;
	private double chlorineLiquid;
	private double chlorineGranules;
	private double bromineGranules;
	private double salt;
	private double chlorineShock;
	private double nonChlorineShock;
	private double quatAlgaecide;
	private double polyquatAlgaecide;
	private double metalAlgaecide;
	private double otherAlgaecide;
	private double phosphateRemover;
	private double sodiumCarbonate; // Baking Soda, pH & Alkalinity Increaser
	private double borax; // pH Increaser
	private double boricAcid;
	private double otherPHIncreaser;
	private double sodiumBisulfate; // pH Reducer
	private double acid; // pH and Alkalinity Reducer
	private double otherPHReducer;
	private double hardnessIncreaser;
	private double stabilizer;
	private double diatomaceousEarth;
	private boolean netRaked;
	private boolean skimmerCleaned;
	private boolean pumpBasketCleaned;
	private boolean cleanerEmptied;
	private boolean vacuumed;
	private boolean brushed;
	private boolean backwashed;
	private boolean filterWashed;
	private boolean drained;
	private boolean waterAdded;

	public Treatment() {
		id = 0;
		pool = new Pool();
		task = new Task();
		date = new Date();
		chlorineTabs = 0;
		bromineTabs = 0;
		sanitizerSize = "";
		chlorineLiquid = -1.00d;
		chlorineGranules = -1.00d;
		bromineGranules = -1.00d;
		salt = -1.00d;
		chlorineShock = -1.00d;
		nonChlorineShock = -1.00d;
		quatAlgaecide = -1.00d;
		polyquatAlgaecide = -1.00d;
		metalAlgaecide = -1.00d;
		otherAlgaecide = -1.00d;
		phosphateRemover = -1.00d;
		sodiumCarbonate = -1.00d;
		borax = -1.00d;
		boricAcid = -1.00d;
		otherPHIncreaser = -1.00d;
		sodiumBisulfate = -1.00d;
		acid = -1.00d;
		otherPHReducer = -1.00d;
		hardnessIncreaser = -1.00d;
		stabilizer = -1.00d;
		diatomaceousEarth = -1.00d;
		netRaked = false;
		skimmerCleaned = false;
		pumpBasketCleaned = false;
		cleanerEmptied = false;
		vacuumed = false;
		brushed = false;
		backwashed = false;
		filterWashed = false;
		drained = false;
		waterAdded = false;
	}

	public Treatment(Context context, long id) throws ParseException {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.TREATMENTS_CONTENT_URI, Long.toString(id)),
				TreatmentTable.columnProjection(), null, null, null));
	}

	public Treatment(Context context, Cursor cursor) throws ParseException {

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

		if(cols.containsKey(TreatmentTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(TreatmentTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(TreatmentTable.KEY_POOL)) {
			this.pool = new Pool(context, cursor.getLong(cols.get(TreatmentTable.KEY_POOL)));
		}else{
			this.pool = new Pool();
		}

		if(cols.containsKey(TreatmentTable.KEY_TASK)) {
			this.task = new Task(context, cursor.getInt(cols.get(TreatmentTable.KEY_TASK)));
		}else{
			this.task = new Task();
		}

		if(cols.containsKey(TreatmentTable.KEY_DATE)) {
			this.date = PoolPal.DATE_FORMAT.parse(cursor.getString(cols.get(TreatmentTable.KEY_DATE)));
		}else{
			this.date = new Date();
		}

		if(cols.containsKey(TreatmentTable.KEY_CHLORINE_TABS)) {
			this.chlorineTabs = cursor.getInt(cols.get(TreatmentTable.KEY_CHLORINE_TABS));
		}else{
			this.chlorineTabs = 0;
		}

		if(cols.containsKey(TreatmentTable.KEY_BROMINE_TABS)) {
			this.bromineTabs = cursor.getInt(cols.get(TreatmentTable.KEY_BROMINE_TABS));
		}else{
			this.bromineTabs = 0;
		}

		if(cols.containsKey(TreatmentTable.KEY_SANITIZER_SIZE)) {
			this.sanitizerSize = cursor.getString(cols.get(TreatmentTable.KEY_SANITIZER_SIZE));
		}else{
			this.sanitizerSize = "";
		}

		if(cols.containsKey(TreatmentTable.KEY_CHLORINE_LIQUID)) {
			this.chlorineLiquid = cursor.getDouble(cols.get(TreatmentTable.KEY_CHLORINE_LIQUID));
		}else{
			this.chlorineLiquid = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_CHLORINE_GRANULES)) {
			this.chlorineGranules = cursor.getDouble(cols.get(TreatmentTable.KEY_CHLORINE_GRANULES));
		}else{
			this.chlorineGranules = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_BROMINE_GRANULES)) {
			this.bromineGranules = cursor.getDouble(cols.get(TreatmentTable.KEY_BROMINE_GRANULES));
		}else{
			this.bromineGranules = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_SALT)) {
			this.salt = cursor.getDouble(cols.get(TreatmentTable.KEY_SALT));
		}else{
			this.salt = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_CHLORINE_SHOCK)) {
			this.chlorineShock = cursor.getDouble(cols.get(TreatmentTable.KEY_CHLORINE_SHOCK));
		}else{
			this.chlorineShock = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_NON_CHLORINE_SHOCK)) {
			this.nonChlorineShock = cursor.getDouble(cols.get(TreatmentTable.KEY_NON_CHLORINE_SHOCK));
		}else{
			this.nonChlorineShock = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_QUAT_ALGAECIDE)) {
			this.quatAlgaecide = cursor.getDouble(cols.get(TreatmentTable.KEY_QUAT_ALGAECIDE));
		}else{
			this.quatAlgaecide = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_POLYQUAT_ALGAECIDE)) {
			this.polyquatAlgaecide = cursor.getDouble(cols.get(TreatmentTable.KEY_POLYQUAT_ALGAECIDE));
		}else{
			this.polyquatAlgaecide = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_METAL_ALGAECIDE)) {
			this.metalAlgaecide = cursor.getDouble(cols.get(TreatmentTable.KEY_METAL_ALGAECIDE));
		}else{
			this.metalAlgaecide = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_OTHER_ALGAECIDE)) {
			this.otherAlgaecide = cursor.getDouble(cols.get(TreatmentTable.KEY_OTHER_ALGAECIDE));
		}else{
			this.otherAlgaecide = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_PHOSPHATE_REMOVER)) {
			this.phosphateRemover = cursor.getDouble(cols.get(TreatmentTable.KEY_PHOSPHATE_REMOVER));
		}else{
			this.phosphateRemover = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_SODIUM_CARBONATE)) {
			this.sodiumCarbonate = cursor.getDouble(cols.get(TreatmentTable.KEY_SODIUM_CARBONATE));
		}else{
			this.sodiumCarbonate = -1.00d;
		}
 // Baking Soda, pH & Alkalinity Increaser
		if(cols.containsKey(TreatmentTable.KEY_BORAX)) {
			this.borax = cursor.getDouble(cols.get(TreatmentTable.KEY_BORAX));
		}else{
			this.borax = -1.00d;
		}
 // pH Increaser
		if(cols.containsKey(TreatmentTable.KEY_BORIC_ACID)) {
			this.boricAcid = cursor.getDouble(cols.get(TreatmentTable.KEY_BORIC_ACID));
		}else{
			this.boricAcid = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_OTHER_PH_INCREASER)) {
			this.otherPHIncreaser = cursor.getDouble(cols.get(TreatmentTable.KEY_OTHER_PH_INCREASER));
		}else{
			this.otherPHIncreaser = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_SODIUM_BISULFATE)) {
			this.sodiumBisulfate = cursor.getDouble(cols.get(TreatmentTable.KEY_SODIUM_BISULFATE));
		}else{
			this.sodiumBisulfate = -1.00d;
		}
 // pH Reducer
		if(cols.containsKey(TreatmentTable.KEY_ACID)) {
			this.acid = cursor.getDouble(cols.get(TreatmentTable.KEY_ACID));
		}else{
			this.acid = -1.00d;
		}
 // pH and Alkalinity Reducer
		if(cols.containsKey(TreatmentTable.KEY_OTHER_PH_REDUCER)) {
			this.otherPHReducer = cursor.getDouble(cols.get(TreatmentTable.KEY_OTHER_PH_REDUCER));
		}else{
			this.otherPHReducer = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_HARDNESS_INCREASER)) {
			this.hardnessIncreaser = cursor.getDouble(cols.get(TreatmentTable.KEY_HARDNESS_INCREASER));
		}else{
			this.hardnessIncreaser = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_STABILIZER)) {
			this.stabilizer = cursor.getDouble(cols.get(TreatmentTable.KEY_STABILIZER));
		}else{
			this.stabilizer = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_DIATOMACEOUS_EARTH)) {
			this.diatomaceousEarth = cursor.getDouble(cols.get(TreatmentTable.KEY_DIATOMACEOUS_EARTH));
		}else{
			this.diatomaceousEarth = -1.00d;
		}

		if(cols.containsKey(TreatmentTable.KEY_NET_RAKED)) {
			this.netRaked = cursor.getInt(cols.get(TreatmentTable.KEY_NET_RAKED)) > 0;
		}else{
			this.netRaked = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_SKIMMER_CLEANED)) {
			this.skimmerCleaned = cursor.getInt(cols.get(TreatmentTable.KEY_SKIMMER_CLEANED)) > 0;
		}else{
			this.skimmerCleaned = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_PUMP_BASKET_CLEANED)) {
			this.pumpBasketCleaned = cursor.getInt(cols.get(TreatmentTable.KEY_PUMP_BASKET_CLEANED)) > 0;
		}else{
			this.pumpBasketCleaned = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_CLEANER_EMPTIED)) {
			this.cleanerEmptied = cursor.getInt(cols.get(TreatmentTable.KEY_CLEANER_EMPTIED)) > 0;
		}else{
			this.cleanerEmptied = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_VACUUMED)) {
			this.vacuumed = cursor.getInt(cols.get(TreatmentTable.KEY_VACUUMED)) > 0;
		}else{
			this.vacuumed = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_BRUSHED)) {
			this.brushed = cursor.getInt(cols.get(TreatmentTable.KEY_BRUSHED)) > 0;
		}else{
			this.brushed = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_BACKWASHED)) {
			this.backwashed = cursor.getInt(cols.get(TreatmentTable.KEY_BACKWASHED)) > 0;
		}else{
			this.backwashed = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_FILTER_WASHED)) {
			this.filterWashed = cursor.getInt(cols.get(TreatmentTable.KEY_FILTER_WASHED)) > 0;
		}else{
			this.filterWashed = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_DRAINED)) {
			this.drained = cursor.getInt(cols.get(TreatmentTable.KEY_DRAINED)) > 0;
		}else{
			this.drained = false;
		}

		if(cols.containsKey(TreatmentTable.KEY_WATER_ADDED)) {
			this.waterAdded = cursor.getInt(cols.get(TreatmentTable.KEY_WATER_ADDED)) > 0;
		}else{
			this.waterAdded = false;
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
	 * @return the chlorineTabs
	 */
	public int getChlorineTabs() {
		return chlorineTabs;
	}

	/**
	 * @param chlorineTabs the chlorineTabs to set
	 */
	public void setChlorineTabs(int chlorineTabs) {
		this.chlorineTabs = chlorineTabs;
	}

	/**
	 * @return the bromineTabs
	 */
	public int getBromineTabs() {
		return bromineTabs;
	}

	/**
	 * @param bromineTabs the bromineTabs to set
	 */
	public void setBromineTabs(int bromineTabs) {
		this.bromineTabs = bromineTabs;
	}

	/**
	 * @return the sanitizerSize
	 */
	public String getSanitizerSize() {
		return sanitizerSize;
	}

	/**
	 * @param sanitizerSize the sanitizerSize to set
	 */
	public void setSanitizerSize(String sanitizerSize) {
		this.sanitizerSize = sanitizerSize;
	}

	/**
	 * @return the chlorineLiquid
	 */
	public double getChlorineLiquid() {
		return chlorineLiquid;
	}

	/**
	 * @param chlorineLiquid the chlorineLiquid to set
	 */
	public void setChlorineLiquid(double chlorineLiquid) {
		this.chlorineLiquid = chlorineLiquid;
	}

	/**
	 * @return the chlorineGranules
	 */
	public double getChlorineGranules() {
		return chlorineGranules;
	}

	/**
	 * @param chlorineGranules the chlorineGranules to set
	 */
	public void setChlorineGranules(double chlorineGranules) {
		this.chlorineGranules = chlorineGranules;
	}

	/**
	 * @return the bromineGranules
	 */
	public double getBromineGranules() {
		return bromineGranules;
	}

	/**
	 * @param bromineGranules the bromineGranules to set
	 */
	public void setBromineGranules(double bromineGranules) {
		this.bromineGranules = bromineGranules;
	}

	/**
	 * @return the salt
	 */
	public double getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(double salt) {
		this.salt = salt;
	}

	/**
	 * @return the chlorineShock
	 */
	public double getChlorineShock() {
		return chlorineShock;
	}

	/**
	 * @param chlorineShock the chlorineShock to set
	 */
	public void setChlorineShock(double chlorineShock) {
		this.chlorineShock = chlorineShock;
	}

	/**
	 * @return the nonChlorineShock
	 */
	public double getNonChlorineShock() {
		return nonChlorineShock;
	}

	/**
	 * @param nonChlorineShock the nonChlorineShock to set
	 */
	public void setNonChlorineShock(double nonChlorineShock) {
		this.nonChlorineShock = nonChlorineShock;
	}

	/**
	 * @return the quatAlgaecide
	 */
	public double getQuatAlgaecide() {
		return quatAlgaecide;
	}

	/**
	 * @param quatAlgaecide the quatAlgaecide to set
	 */
	public void setQuatAlgaecide(double quatAlgaecide) {
		this.quatAlgaecide = quatAlgaecide;
	}

	/**
	 * @return the polyquatAlgaecide
	 */
	public double getPolyquatAlgaecide() {
		return polyquatAlgaecide;
	}

	/**
	 * @param polyquatAlgaecide the polyquatAlgaecide to set
	 */
	public void setPolyquatAlgaecide(double polyquatAlgaecide) {
		this.polyquatAlgaecide = polyquatAlgaecide;
	}

	/**
	 * @return the metalAlgaecide
	 */
	public double getMetalAlgaecide() {
		return metalAlgaecide;
	}

	/**
	 * @param metalAlgaecide the metalAlgaecide to set
	 */
	public void setMetalAlgaecide(double metalAlgaecide) {
		this.metalAlgaecide = metalAlgaecide;
	}

	/**
	 * @return the otherAlgaecide
	 */
	public double getOtherAlgaecide() {
		return otherAlgaecide;
	}

	/**
	 * @param otherAlgaecide the otherAlgaecide to set
	 */
	public void setOtherAlgaecide(double otherAlgaecide) {
		this.otherAlgaecide = otherAlgaecide;
	}

	/**
	 * @return the phosphateRemover
	 */
	public double getPhosphateRemover() {
		return phosphateRemover;
	}

	/**
	 * @param phosphateRemover the phosphateRemover to set
	 */
	public void setPhosphateRemover(double phosphateRemover) {
		this.phosphateRemover = phosphateRemover;
	}

	/**
	 * @return the sodiumCarbonate
	 */
	public double getSodiumCarbonate() {
		return sodiumCarbonate;
	}

	/**
	 * @param sodiumCarbonate the sodiumCarbonate to set
	 */
	public void setSodiumCarbonate(double sodiumCarbonate) {
		this.sodiumCarbonate = sodiumCarbonate;
	}

	/**
	 * @return the borax
	 */
	public double getBorax() {
		return borax;
	}

	/**
	 * @param borax the borax to set
	 */
	public void setBorax(double borax) {
		this.borax = borax;
	}

	/**
	 * @return the boricAcid
	 */
	public double getBoricAcid() {
		return boricAcid;
	}

	/**
	 * @param boricAcid the boricAcid to set
	 */
	public void setBoricAcid(double boricAcid) {
		this.boricAcid = boricAcid;
	}

	/**
	 * @return the otherPHIncreaser
	 */
	public double getOtherPHIncreaser() {
		return otherPHIncreaser;
	}

	/**
	 * @param otherPHIncreaser the otherPHIncreaser to set
	 */
	public void setOtherPHIncreaser(double otherPHIncreaser) {
		this.otherPHIncreaser = otherPHIncreaser;
	}

	/**
	 * @return the sodiumBisulfate
	 */
	public double getSodiumBisulfate() {
		return sodiumBisulfate;
	}

	/**
	 * @param sodiumBisulfate the sodiumBisulfate to set
	 */
	public void setSodiumBisulfate(double sodiumBisulfate) {
		this.sodiumBisulfate = sodiumBisulfate;
	}

	/**
	 * @return the acid
	 */
	public double getAcid() {
		return acid;
	}

	/**
	 * @param acid the acid to set
	 */
	public void setAcid(double acid) {
		this.acid = acid;
	}

	/**
	 * @return the otherPHReducer
	 */
	public double getOtherPHReducer() {
		return otherPHReducer;
	}

	/**
	 * @param otherPHReducer the otherPHReducer to set
	 */
	public void setOtherPHReducer(double otherPHReducer) {
		this.otherPHReducer = otherPHReducer;
	}

	/**
	 * @return the hardnessIncreaser
	 */
	public double getHardnessIncreaser() {
		return hardnessIncreaser;
	}

	/**
	 * @param hardnessIncreaser the hardnessIncreaser to set
	 */
	public void setHardnessIncreaser(double hardnessIncreaser) {
		this.hardnessIncreaser = hardnessIncreaser;
	}

	/**
	 * @return the stabilizer
	 */
	public double getStabilizer() {
		return stabilizer;
	}

	/**
	 * @param stabilizer the stabilizer to set
	 */
	public void setStabilizer(double stabilizer) {
		this.stabilizer = stabilizer;
	}

	/**
	 * @return the diatomaceousEarth
	 */
	public double getDiatomaceousEarth() {
		return diatomaceousEarth;
	}

	/**
	 * @param diatomaceousEarth the diatomaceousEarth to set
	 */
	public void setDiatomaceousEarth(double diatomaceousEarth) {
		this.diatomaceousEarth = diatomaceousEarth;
	}

	/**
	 * @return the netRaked
	 */
	public boolean isNetRaked() {
		return netRaked;
	}

	/**
	 * @param netRaked the netRaked to set
	 */
	public void setNetRaked(boolean netRaked) {
		this.netRaked = netRaked;
	}

	/**
	 * @return the skimmerCleaned
	 */
	public boolean isSkimmerCleaned() {
		return skimmerCleaned;
	}

	/**
	 * @param skimmerCleaned the skimmerCleaned to set
	 */
	public void setSkimmerCleaned(boolean skimmerCleaned) {
		this.skimmerCleaned = skimmerCleaned;
	}

	/**
	 * @return the pumpBasketCleaned
	 */
	public boolean isPumpBasketCleaned() {
		return pumpBasketCleaned;
	}

	/**
	 * @param pumpBasketCleaned the pumpBasketCleaned to set
	 */
	public void setPumpBasketCleaned(boolean pumpBasketCleaned) {
		this.pumpBasketCleaned = pumpBasketCleaned;
	}

	/**
	 * @return the cleanerEmptied
	 */
	public boolean isCleanerEmptied() {
		return cleanerEmptied;
	}

	/**
	 * @param cleanerEmptied the cleanerEmptied to set
	 */
	public void setCleanerEmptied(boolean cleanerEmptied) {
		this.cleanerEmptied = cleanerEmptied;
	}

	/**
	 * @return the vacuumed
	 */
	public boolean isVacuumed() {
		return vacuumed;
	}

	/**
	 * @param vacuumed the vacuumed to set
	 */
	public void setVacuumed(boolean vacuumed) {
		this.vacuumed = vacuumed;
	}

	/**
	 * @return the brushed
	 */
	public boolean isBrushed() {
		return brushed;
	}

	/**
	 * @param brushed the brushed to set
	 */
	public void setBrushed(boolean brushed) {
		this.brushed = brushed;
	}

	/**
	 * @return the backwashed
	 */
	public boolean isBackwashed() {
		return backwashed;
	}

	/**
	 * @param backwashed the backwashed to set
	 */
	public void setBackwashed(boolean backwashed) {
		this.backwashed = backwashed;
	}

	/**
	 * @return the filterWashed
	 */
	public boolean isFilterWashed() {
		return filterWashed;
	}

	/**
	 * @param filterWashed the filterWashed to set
	 */
	public void setFilterWashed(boolean filterWashed) {
		this.filterWashed = filterWashed;
	}

	/**
	 * @return the drained
	 */
	public boolean isDrained() {
		return drained;
	}

	/**
	 * @param drained the drained to set
	 */
	public void setDrained(boolean drained) {
		this.drained = drained;
	}

	/**
	 * @return the waterAdded
	 */
	public boolean isWaterAdded() {
		return waterAdded;
	}

	/**
	 * @param waterAdded the waterAdded to set
	 */
	public void setWaterAdded(boolean waterAdded) {
		this.waterAdded = waterAdded;
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
		out.writeInt(chlorineTabs);
		out.writeInt(bromineTabs);
		out.writeString(sanitizerSize);
		out.writeDouble(chlorineLiquid);
		out.writeDouble(chlorineGranules);
		out.writeDouble(bromineGranules);
		out.writeDouble(salt);
		out.writeDouble(chlorineShock);
		out.writeDouble(nonChlorineShock);
		out.writeDouble(quatAlgaecide);
		out.writeDouble(polyquatAlgaecide);
		out.writeDouble(metalAlgaecide);
		out.writeDouble(otherAlgaecide);
		out.writeDouble(phosphateRemover);
		out.writeDouble(sodiumCarbonate);
		out.writeDouble(borax);
		out.writeDouble(boricAcid);
		out.writeDouble(otherPHIncreaser);
		out.writeDouble(sodiumBisulfate);
		out.writeDouble(acid);
		out.writeDouble(otherPHReducer);
		out.writeDouble(hardnessIncreaser);
		out.writeDouble(stabilizer);
		out.writeDouble(diatomaceousEarth);
		out.writeBooleanArray(new boolean[]{netRaked, skimmerCleaned, pumpBasketCleaned,
				cleanerEmptied, vacuumed, brushed, backwashed, filterWashed, drained,
				waterAdded});
	}

	private Treatment(Parcel in) throws ParseException {

		this.pool = in.readParcelable(Pool.class.getClassLoader());
		this.task = in.readParcelable(Task.class.getClassLoader());
		this.id = in.readLong();
		this.date = PoolPal.DATE_FORMAT.parse(in.readString());
		this.chlorineTabs = in.readInt();
		this.bromineTabs = in.readInt();
		this.sanitizerSize = in.readString();
		this.chlorineLiquid = in.readDouble();
		this.chlorineGranules = in.readDouble();
		this.bromineGranules = in.readDouble();
		this.salt = in.readDouble();
		this.chlorineShock = in.readDouble();
		this.nonChlorineShock = in.readDouble();
		this.quatAlgaecide = in.readDouble();
		this.polyquatAlgaecide = in.readDouble();
		this.metalAlgaecide = in.readDouble();
		this.otherAlgaecide = in.readDouble();
		this.phosphateRemover = in.readDouble();
		this.sodiumCarbonate = in.readDouble();
		this.borax = in.readDouble();
		this.boricAcid = in.readDouble();
		this.otherPHIncreaser = in.readDouble();
		this.sodiumBisulfate = in.readDouble();
		this.acid = in.readDouble();
		this.otherPHReducer = in.readDouble();
		this.hardnessIncreaser = in.readDouble();
		this.stabilizer = in.readDouble();
		this.diatomaceousEarth = in.readDouble();
		boolean[] bools = new boolean[10];
		in.readBooleanArray(bools);
		this.netRaked = bools[0];
		this.skimmerCleaned = bools[1];
		this.pumpBasketCleaned = bools[2];
		this.cleanerEmptied = bools[3];
		this.vacuumed = bools[4];
		this.brushed = bools[5];
		this.backwashed = bools[6];
		this.filterWashed = bools[7];
		this.drained = bools[8];
		this.waterAdded = bools[9];
	}

	public static final Parcelable.Creator<Treatment> CREATOR = new Parcelable.Creator<Treatment>() {

		public Treatment createFromParcel(Parcel in) {
			try {
				return new Treatment(in);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		public Treatment[] newArray(int size) {
			return new Treatment[size];
		}
	};
}
