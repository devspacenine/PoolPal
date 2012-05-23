package com.devspacenine.poolpal.object;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.OrderItemTable;

public class OrderItem {

	public static String listToString(List<OrderItem> l) {

		if(l.size() == 0) return null;

		StringBuilder sb = new StringBuilder();
		for(Iterator<OrderItem> i = l.iterator(); i.hasNext();) {
			OrderItem t = i.next();
			if(t.getId() > 0) sb.append(Long.toString(t.getId()));
			if(i.hasNext()) sb.append(",");
		}
		return sb.toString();
	}

	public static ArrayList<OrderItem> listFromString(Context ctx, String s) throws ParseException {
		ArrayList<OrderItem> l = new ArrayList<OrderItem>();
		String[] _ids = s.split(",");
		for(String _id : _ids) {
			l.add(new OrderItem(ctx, Long.parseLong(_id)));
		}
		return l;
	}

	private long id;
	private Item item;
	private Order order;
	private String cartItemId;
	private String ASIN;
	private String offerListingId;
	private String sellerNickname;
	private int quantity;
	private String title;
	private String productGroup;
	private double price;
	private double total;
	private String formattedPrice;
	private String formattedTotal;
	private String currencyCode;

	public OrderItem() {
	}

	public OrderItem(Context context, long id) throws ParseException {
		this(context, context.getContentResolver().query(
				Uri.withAppendedPath(PoolPalContent.ORDER_ITEMS_CONTENT_URI, Long.toString(id)),
				OrderItemTable.columnProjection(), null, null, null));
	}

	public OrderItem(Context context, Cursor cursor) throws ParseException {

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

		if(cols.containsKey(OrderItemTable.KEY_ID)) {
			this.id = cursor.getLong(cols.get(OrderItemTable.KEY_ID));
		}else{
			this.id = 0;
		}

		if(cols.containsKey(OrderItemTable.KEY_ITEM)) {
			this.item = new Item(context, cursor.getLong(cols.get(OrderItemTable.KEY_ITEM)));
		}else{
			this.item = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_ORDER)) {
			this.order = new Order(context, cursor.getLong(cols.get(OrderItemTable.KEY_ORDER)));
		}else{
			this.order = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_CART_ITEM_ID)) {
			this.cartItemId = cursor.getString(cols.get(OrderItemTable.KEY_CART_ITEM_ID));
		}else{
			this.cartItemId = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_ASIN)) {
			this.ASIN = cursor.getString(cols.get(OrderItemTable.KEY_ASIN));
		}else{
			this.ASIN = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_OFFER_LISTING_ID)) {
			this.offerListingId = cursor.getString(cols.get(OrderItemTable.KEY_OFFER_LISTING_ID));
		}else{
			this.offerListingId = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_SELLER_NICKNAME)) {
			this.sellerNickname = cursor.getString(cols.get(OrderItemTable.KEY_SELLER_NICKNAME));
		}else{
			this.sellerNickname = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_QUANTITY)) {
			this.quantity = cursor.getInt(cols.get(OrderItemTable.KEY_QUANTITY));
		}else{
			this.quantity = 0;
		}

		if(cols.containsKey(OrderItemTable.KEY_TITLE)) {
			this.title = cursor.getString(cols.get(OrderItemTable.KEY_TITLE));
		}else{
			this.title = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_PRODUCT_GROUP)) {
			this.productGroup = cursor.getString(cols.get(OrderItemTable.KEY_PRODUCT_GROUP));
		}else{
			this.productGroup = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_PRICE)) {
			this.price = cursor.getDouble(cols.get(OrderItemTable.KEY_PRICE));
		}else{
			this.price = -1.00d;
		}

		if(cols.containsKey(OrderItemTable.KEY_TOTAL)) {
			this.total = cursor.getDouble(cols.get(OrderItemTable.KEY_TOTAL));
		}else{
			this.total = -1.00d;
		}

		if(cols.containsKey(OrderItemTable.KEY_FORMATTED_PRICE)) {
			this.formattedPrice = cursor.getString(cols.get(OrderItemTable.KEY_FORMATTED_PRICE));
		}else{
			this.formattedPrice = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_FORMATTED_TOTAL)) {
			this.formattedTotal = cursor.getString(cols.get(OrderItemTable.KEY_FORMATTED_TOTAL));
		}else{
			this.formattedTotal = null;
		}

		if(cols.containsKey(OrderItemTable.KEY_CURRENCY_CODE)) {
			this.currencyCode = cursor.getString(cols.get(OrderItemTable.KEY_CURRENCY_CODE));
		}else{
			this.currencyCode = null;
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
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @return the cartItemId
	 */
	public String getCartItemId() {
		return cartItemId;
	}

	/**
	 * @param cartItemId the cartItemId to set
	 */
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
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
	 * @return the offerListingId
	 */
	public String getOfferListingId() {
		return offerListingId;
	}

	/**
	 * @param offerListingId the offerListingId to set
	 */
	public void setOfferListingId(String offerListingId) {
		this.offerListingId = offerListingId;
	}

	/**
	 * @return the sellerNickname
	 */
	public String getSellerNickname() {
		return sellerNickname;
	}

	/**
	 * @param sellerNickname the sellerNickname to set
	 */
	public void setSellerNickname(String sellerNickname) {
		this.sellerNickname = sellerNickname;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
}
