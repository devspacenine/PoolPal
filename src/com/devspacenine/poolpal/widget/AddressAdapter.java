package com.devspacenine.poolpal.widget;

import java.util.ArrayList;

import android.location.Address;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devspacenine.poolpal.R;

public class AddressAdapter extends BaseAdapter {

	private ArrayList<Address> mAddresses;
	private FragmentActivity mCtx;

	public AddressAdapter(FragmentActivity context) {

		mCtx = context;
		mAddresses = new ArrayList<Address>();
	}

	public AddressAdapter(FragmentActivity context, ArrayList<Address> addresses) {

		mCtx = context;
		mAddresses = addresses;
	}

	public void add(Address address) {

		mAddresses.add(address);
	}

	public void add(ArrayList<Address> addresses) {

		mAddresses.addAll(addresses);
	}

	public boolean contains(Address address) {
		for(Address a : mAddresses) {
			if(a.getFeatureName().equals(address.getFeatureName())
					&& a.getThoroughfare().equals(address.getThoroughfare())
					&& a.getLocality().equals(address.getLocality())
					&& a.getAdminArea().equals(address.getAdminArea())
					&& a.getPostalCode().equals(address.getPostalCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getCount() {
		return mAddresses.size();
	}

	@Override
	public Object getItem(int position) {
		return mAddresses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if(v == null) {
			v = LayoutInflater.from(mCtx).inflate(R.layout.address_item, parent, false);
		}

		Address address = (Address) getItem(position);
		((TextView)v.findViewById(R.id.line1)).setText(address.getAddressLine(0));
		if(address.getMaxAddressLineIndex() >= 1) {
			((TextView)v.findViewById(R.id.line2)).setText(address.getAddressLine(1));
		}

		return v;
	}

}