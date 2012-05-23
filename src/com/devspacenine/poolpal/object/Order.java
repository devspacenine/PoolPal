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
import com.devspacenine.poolpal.database.OrderTable;

public class Order implements Parcelable {

	public static String listToString(List<Order> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<Order> i = l.iterator(); i.hasNext();) {
			Order t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<Order> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<Order> l = new ArrayList<Order>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new Order(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	private long id;
	private String orderNumber;
	private Date date;
	private double subtotal;
	private double tax;
	private double shipping;
	private double savings;
	private double total;
	private String formattedSubtotal;
	private String formattedTax;
	private String formattedShipping;
	private String formattedSavings;
	private String formattedTotal;
	private String cartId;
	private String HMAC;
	private String purchaseUrl;
	private String currencyCode;
	private boolean favorite;
	private String title;

	public Order() {
		id = 0;
		orderNumber = "";
		date = new Date();
		subtotal = -1.00d;
		tax = -1.00d;
		shipping = -1.00d;
		savings = -1.00d;
		total = -1.00d;
		formattedSubtotal = "";
		formattedTax = "";
		formattedShipping = "";
		formattedSavings = "";
		formattedTotal = "";
		cartId = "";
		HMAC = "";
		purchaseUrl = "";
		currencyCode = "";
		favorite = false;
		title = "";
	}

	public Order(Context context, long id) throws ParseException {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.ORDERS_CONTENT_URI, Long.toString(id)),
				OrderTable.columnProjection(), null, null, null));
	}

	public Order(Context context, Cursor cursor) throws ParseException {

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

		if(cols.containsKey(OrderTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(OrderTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(OrderTable.KEY_ORDER_NUMBER)) {
			this.orderNumber = cursor.getString(cols.get(OrderTable.KEY_ORDER_NUMBER));
		}else{
			this.orderNumber = "";
		}

		if(cols.containsKey(OrderTable.KEY_DATE)) {
			this.date = PoolPal.DATE_FORMAT.parse(cursor.getString(cols.get(OrderTable.KEY_DATE)));
		}else{
			this.date = new Date();
		}

		if(cols.containsKey(OrderTable.KEY_SUBTOTAL)) {
			this.subtotal = cursor.getDouble(cols.get(OrderTable.KEY_SUBTOTAL));
		}else{
			this.subtotal = -1.00d;
		}

		if(cols.containsKey(OrderTable.KEY_TAX)) {
			this.tax = cursor.getDouble(cols.get(OrderTable.KEY_TAX));
		}else{
			this.tax = -1.00d;
		}

		if(cols.containsKey(OrderTable.KEY_SHIPPING)) {
			this.shipping = cursor.getDouble(cols.get(OrderTable.KEY_SHIPPING));
		}else{
			this.shipping = -1.00d;
		}

		if(cols.containsKey(OrderTable.KEY_SAVINGS)) {
			this.savings = cursor.getDouble(cols.get(OrderTable.KEY_SAVINGS));
		}else{
			this.savings = -1.00d;
		}

		if(cols.containsKey(OrderTable.KEY_TOTAL)) {
			this.total = cursor.getDouble(cols.get(OrderTable.KEY_TOTAL));
		}else{
			this.total = -1.00d;
		}

		if(cols.containsKey(OrderTable.KEY_FORMATTED_SUBTOTAL)) {
			this.formattedSubtotal = cursor.getString(cols.get(OrderTable.KEY_FORMATTED_SUBTOTAL));
		}else{
			this.formattedSubtotal = "";
		}

		if(cols.containsKey(OrderTable.KEY_FORMATTED_TAX)) {
			this.formattedTax = cursor.getString(cols.get(OrderTable.KEY_FORMATTED_TAX));
		}else{
			this.formattedTax = "";
		}

		if(cols.containsKey(OrderTable.KEY_FORMATTED_SHIPPING)) {
			this.formattedShipping = cursor.getString(cols.get(OrderTable.KEY_FORMATTED_SHIPPING));
		}else{
			this.formattedShipping = "";
		}

		if(cols.containsKey(OrderTable.KEY_FORMATTED_SAVINGS)) {
			this.formattedSavings = cursor.getString(cols.get(OrderTable.KEY_FORMATTED_SAVINGS));
		}else{
			this.formattedSavings = "";
		}

		if(cols.containsKey(OrderTable.KEY_FORMATTED_TOTAL)) {
			this.formattedTotal = cursor.getString(cols.get(OrderTable.KEY_FORMATTED_TOTAL));
		}else{
			this.formattedTotal = "";
		}

		if(cols.containsKey(OrderTable.KEY_CART_ID)) {
			this.cartId = cursor.getString(cols.get(OrderTable.KEY_CART_ID));
		}else{
			this.cartId = "";
		}

		if(cols.containsKey(OrderTable.KEY_HMAC)) {
			this.HMAC = cursor.getString(cols.get(OrderTable.KEY_HMAC));
		}else{
			this.HMAC = "";
		}

		if(cols.containsKey(OrderTable.KEY_PURCHASE_URL)) {
			this.purchaseUrl = cursor.getString(cols.get(OrderTable.KEY_PURCHASE_URL));
		}else{
			this.purchaseUrl = "";
		}

		if(cols.containsKey(OrderTable.KEY_CURRENCY_CODE)) {
			this.currencyCode = cursor.getString(cols.get(OrderTable.KEY_CURRENCY_CODE));
		}else{
			this.currencyCode = "";
		}

		if(cols.containsKey(OrderTable.KEY_FAVORITE)) {
			this.favorite = cursor.getInt(cols.get(OrderTable.KEY_FAVORITE)) > 0;
		}else{
			this.favorite = false;
		}

		if(cols.containsKey(OrderTable.KEY_TITLE)) {
			this.title = cursor.getString(cols.get(OrderTable.KEY_TITLE));
		}else{
			this.title = "";
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
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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
	 * @return the subtotal
	 */
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @return the tax
	 */
	public double getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(double tax) {
		this.tax = tax;
	}

	/**
	 * @return the shipping
	 */
	public double getShipping() {
		return shipping;
	}

	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(double shipping) {
		this.shipping = shipping;
	}

	/**
	 * @return the savings
	 */
	public double getSavings() {
		return savings;
	}

	/**
	 * @param savings the savings to set
	 */
	public void setSavings(double savings) {
		this.savings = savings;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the formattedSubtotal
	 */
	public String getFormattedSubtotal() {
		return formattedSubtotal;
	}

	/**
	 * @param formattedSubtotal the formattedSubtotal to set
	 */
	public void setFormattedSubtotal(String formattedSubtotal) {
		this.formattedSubtotal = formattedSubtotal;
	}

	/**
	 * @return the formattedTax
	 */
	public String getFormattedTax() {
		return formattedTax;
	}

	/**
	 * @param formattedTax the formattedTax to set
	 */
	public void setFormattedTax(String formattedTax) {
		this.formattedTax = formattedTax;
	}

	/**
	 * @return the formattedShipping
	 */
	public String getFormattedShipping() {
		return formattedShipping;
	}

	/**
	 * @param formattedShipping the formattedShipping to set
	 */
	public void setFormattedShipping(String formattedShipping) {
		this.formattedShipping = formattedShipping;
	}

	/**
	 * @return the formattedSavings
	 */
	public String getFormattedSavings() {
		return formattedSavings;
	}

	/**
	 * @param formattedSavings the formattedSavings to set
	 */
	public void setFormattedSavings(String formattedSavings) {
		this.formattedSavings = formattedSavings;
	}

	/**
	 * @return the formattedTotal
	 */
	public String getFormattedTotal() {
		return formattedTotal;
	}

	/**
	 * @param formattedTotal the formattedTotal to set
	 */
	public void setFormattedTotal(String formattedTotal) {
		this.formattedTotal = formattedTotal;
	}

	/**
	 * @return the cartId
	 */
	public String getCartId() {
		return cartId;
	}

	/**
	 * @param cartId the cartId to set
	 */
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	/**
	 * @return the hMAC
	 */
	public String getHMAC() {
		return HMAC;
	}

	/**
	 * @param hMAC the hMAC to set
	 */
	public void setHMAC(String hMAC) {
		HMAC = hMAC;
	}

	/**
	 * @return the purchaseUrl
	 */
	public String getPurchaseUrl() {
		return purchaseUrl;
	}

	/**
	 * @param purchaseUrl the purchaseUrl to set
	 */
	public void setPurchaseUrl(String purchaseUrl) {
		this.purchaseUrl = purchaseUrl;
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
	 * @return the favorite
	 */
	public boolean isFavorite() {
		return favorite;
	}

	/**
	 * @param favorite the favorite to set
	 */
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeLong(id);
		out.writeString(orderNumber);
		out.writeString(PoolPal.DATE_FORMAT.format(date));
		out.writeDouble(subtotal);
		out.writeDouble(tax);
		out.writeDouble(shipping);
		out.writeDouble(savings);
		out.writeDouble(total);
		out.writeString(formattedSubtotal);
		out.writeString(formattedTax);
		out.writeString(formattedShipping);
		out.writeString(formattedSavings);
		out.writeString(formattedTotal);
		out.writeString(cartId);
		out.writeString(HMAC);
		out.writeString(purchaseUrl);
		out.writeString(currencyCode);
		out.writeString(title);
		out.writeBooleanArray(new boolean[]{favorite});
	}

	private Order(Parcel in) throws ParseException {

		this.id = in.readLong();
		this.orderNumber = in.readString();
		this.date = PoolPal.DATE_FORMAT.parse(in.readString());
		this.subtotal = in.readDouble();
		this.tax = in.readDouble();
		this.shipping = in.readDouble();
		this.savings = in.readDouble();
		this.total = in.readDouble();
		this.formattedSubtotal = in.readString();
		this.formattedTax = in.readString();
		this.formattedShipping = in.readString();
		this.formattedSavings = in.readString();
		this.formattedTotal = in.readString();
		this.cartId = in.readString();
		this.HMAC = in.readString();
		this.purchaseUrl = in.readString();
		this.currencyCode = in.readString();
		this.title = in.readString();
		boolean[] bools = new boolean[1];
		in.readBooleanArray(bools);
		this.favorite = bools[0];
	}

	public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {

		public Order createFromParcel(Parcel in) {
			try {
				return new Order(in);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		public Order[] newArray(int size) {
			return new Order[size];
		}
	};
}
