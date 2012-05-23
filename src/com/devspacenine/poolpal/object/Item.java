package com.devspacenine.poolpal.object;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.ItemTable;


public class Item implements Parcelable {

	public static String listToString(List<Item> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<Item> i = l.iterator(); i.hasNext();) {
			Item t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<Item> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<Item> l = new ArrayList<Item>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new Item(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	private long id;
	private String ASIN;
	private String EAN;
	private String ISBN;
	private String EISBN;
	private String MPN;
	private String SKU;
	private String title;
	private String productGroup;
	private String manufacturer;
	private String brand;
	private String model;
	private double listPrice;
	private String formattedListPrice;
	private double price;
	private String formattedPrice;
	private String currencyCode;
	private double rating;
	private String url;

	public Item() {
		id = 0;
		ASIN = "";
		EAN = "";
		ISBN = "";
		EISBN = "";
		MPN = "";
		SKU = "";
		title = "";
		productGroup = "";
		manufacturer = "";
		brand = "";
		model = "";
		listPrice = -1.00d;
		formattedListPrice = "";
		price = -1.00d;
		formattedPrice = "";
		currencyCode = "";
		rating = -1.00d;
		url = "";
	}

	public Item(Context context, long id) {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.ITEMS_CONTENT_URI, Long.toString(id)),
				ItemTable.columnProjection(), null, null, null));
	}

	public Item(Context context, Cursor cursor) {

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

		if(cols.containsKey(ItemTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(ItemTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(ItemTable.KEY_ASIN)) {
			this.ASIN = cursor.getString(cols.get(ItemTable.KEY_ASIN));
		}else{
			this.ASIN = "";
		}

		if(cols.containsKey(ItemTable.KEY_EAN)) {
			this.EAN = cursor.getString(cols.get(ItemTable.KEY_EAN));
		}else{
			this.EAN = "";
		}

		if(cols.containsKey(ItemTable.KEY_ISBN)) {
			this.ISBN = cursor.getString(cols.get(ItemTable.KEY_ISBN));
		}else{
			this.ISBN = "";
		}

		if(cols.containsKey(ItemTable.KEY_EISBN)) {
			this.EISBN = cursor.getString(cols.get(ItemTable.KEY_EISBN));
		}else{
			this.EISBN = "";
		}

		if(cols.containsKey(ItemTable.KEY_MPN)) {
			this.MPN = cursor.getString(cols.get(ItemTable.KEY_MPN));
		}else{
			this.MPN = "";
		}

		if(cols.containsKey(ItemTable.KEY_SKU)) {
			this.SKU = cursor.getString(cols.get(ItemTable.KEY_SKU));
		}else{
			this.SKU = "";
		}

		if(cols.containsKey(ItemTable.KEY_TITLE)) {
			this.title = cursor.getString(cols.get(ItemTable.KEY_TITLE));
		}else{
			this.title = "";
		}

		if(cols.containsKey(ItemTable.KEY_PRODUCT_GROUP)) {
			this.productGroup = cursor.getString(cols.get(ItemTable.KEY_PRODUCT_GROUP));
		}else{
			this.productGroup = "";
		}

		if(cols.containsKey(ItemTable.KEY_MANUFACTURER)) {
			this.manufacturer = cursor.getString(cols.get(ItemTable.KEY_MANUFACTURER));
		}else{
			this.manufacturer = "";
		}

		if(cols.containsKey(ItemTable.KEY_BRAND)) {
			this.brand = cursor.getString(cols.get(ItemTable.KEY_BRAND));
		}else{
			this.brand = "";
		}

		if(cols.containsKey(ItemTable.KEY_MODEL)) {
			this.model = cursor.getString(cols.get(ItemTable.KEY_MODEL));
		}else{
			this.model = "";
		}

		if(cols.containsKey(ItemTable.KEY_LIST_PRICE)) {
			this.listPrice = cursor.getDouble(cols.get(ItemTable.KEY_LIST_PRICE));
		}else{
			this.listPrice = -1.00d;
		}

		if(cols.containsKey(ItemTable.KEY_FORMATTED_LIST_PRICE)) {
			this.formattedListPrice = cursor.getString(cols.get(ItemTable.KEY_FORMATTED_LIST_PRICE));
		}else{
			this.formattedListPrice = "";
		}

		if(cols.containsKey(ItemTable.KEY_PRICE)) {
			this.price = cursor.getDouble(cols.get(ItemTable.KEY_PRICE));
		}else{
			this.price = -1.00d;
		}

		if(cols.containsKey(ItemTable.KEY_FORMATTED_PRICE)) {
			this.formattedPrice = cursor.getString(cols.get(ItemTable.KEY_FORMATTED_PRICE));
		}else{
			this.formattedPrice = "";
		}

		if(cols.containsKey(ItemTable.KEY_CURRENCY_CODE)) {
			this.currencyCode = cursor.getString(cols.get(ItemTable.KEY_CURRENCY_CODE));
		}else{
			this.currencyCode = "";
		}

		if(cols.containsKey(ItemTable.KEY_RATING)) {
			this.rating = cursor.getDouble(cols.get(ItemTable.KEY_RATING));
		}else{
			this.rating = -1.00d;
		}

		if(cols.containsKey(ItemTable.KEY_URL)) {
			this.url = cursor.getString(cols.get(ItemTable.KEY_URL));
		}else{
			this.url = "";
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
	 * @return the aSIN
	 */
	public String getASIN() {
		return ASIN;
	}

	/**
	 * @param aSIN the aSIN to set
	 */
	public void setASIN(String aSIN) {
		ASIN = aSIN;
	}

	/**
	 * @return the eAN
	 */
	public String getEAN() {
		return EAN;
	}

	/**
	 * @param eAN the eAN to set
	 */
	public void setEAN(String eAN) {
		EAN = eAN;
	}

	/**
	 * @return the iSBN
	 */
	public String getISBN() {
		return ISBN;
	}

	/**
	 * @param iSBN the iSBN to set
	 */
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	/**
	 * @return the eISBN
	 */
	public String getEISBN() {
		return EISBN;
	}

	/**
	 * @param eISBN the eISBN to set
	 */
	public void setEISBN(String eISBN) {
		EISBN = eISBN;
	}

	/**
	 * @return the mPN
	 */
	public String getMPN() {
		return MPN;
	}

	/**
	 * @param mPN the mPN to set
	 */
	public void setMPN(String mPN) {
		MPN = mPN;
	}

	/**
	 * @return the sKU
	 */
	public String getSKU() {
		return SKU;
	}

	/**
	 * @param sKU the sKU to set
	 */
	public void setSKU(String sKU) {
		SKU = sKU;
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
	 * @return the productGroup
	 */
	public String getProductGroup() {
		return productGroup;
	}

	/**
	 * @param productGroup the productGroup to set
	 */
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the listPrice
	 */
	public double getListPrice() {
		return listPrice;
	}

	/**
	 * @param listPrice the listPrice to set
	 */
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	/**
	 * @return the formattedListPrice
	 */
	public String getFormattedListPrice() {
		return formattedListPrice;
	}

	/**
	 * @param formattedListPrice the formattedListPrice to set
	 */
	public void setFormattedListPrice(String formattedListPrice) {
		this.formattedListPrice = formattedListPrice;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the formattedPrice
	 */
	public String getFormattedPrice() {
		return formattedPrice;
	}

	/**
	 * @param formattedPrice the formattedPrice to set
	 */
	public void setFormattedPrice(String formattedPrice) {
		this.formattedPrice = formattedPrice;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeLong(id);
		out.writeString(ASIN);
		out.writeString(EAN);
		out.writeString(ISBN);
		out.writeString(EISBN);
		out.writeString(MPN);
		out.writeString(SKU);
		out.writeString(title);
		out.writeString(productGroup);
		out.writeString(manufacturer);
		out.writeString(brand);
		out.writeString(model);
		out.writeDouble(listPrice);
		out.writeString(formattedListPrice);
		out.writeDouble(price);
		out.writeString(formattedPrice);
		out.writeString(currencyCode);
		out.writeDouble(rating);
		out.writeString(url);
	}

	private Item(Parcel in) {

		this.id = in.readLong();
		this.ASIN = in.readString();
		this.EAN = in.readString();
		this.ISBN = in.readString();
		this.EISBN = in.readString();
		this.MPN = in.readString();
		this.SKU = in.readString();
		this.title = in.readString();
		this.productGroup = in.readString();
		this.manufacturer = in.readString();
		this.brand = in.readString();
		this.model = in.readString();
		this.listPrice = in.readDouble();
		this.formattedListPrice = in.readString();
		this.price = in.readDouble();
		this.formattedPrice = in.readString();
		this.currencyCode = in.readString();
		this.rating = in.readDouble();
		this.url = in.readString();
	}

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		public Item[] newArray(int size) {
			return new Item[size];
		}
	};
}
