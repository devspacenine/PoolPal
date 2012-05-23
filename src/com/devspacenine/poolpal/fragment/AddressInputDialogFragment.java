package com.devspacenine.poolpal.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.object.PoolAddress;
import com.devspacenine.poolpal.util.MockLocationUtil;
import com.devspacenine.poolpal.widget.AddressAdapter;
import com.devspacenine.poolpal.widget.PoolDataAdapter;

public class AddressInputDialogFragment extends InputDialogFragment
	implements LocationListener {
	// Request codes
	public static final int ENABLE_GPS = 1;

	// Thread Message Handler Codes
	public static final int MESSAGE_GPS_ADDRESS_SUCCESS = 1;
	public static final int MESSAGE_STOP = 2;
	public static final int MESSAGE_ERROR = 3;

	// Tags
	public static final String TAG_NORMAL = "normal";
	public static final String TAG_GPS = "gps";

	// Location variables
	protected LocationManager mLocationManager;
	protected boolean waiting_for_gps;
	protected LocationListener mLocationListener;
	private HashMap<Integer, GetAddressThread> mGetAddressThreads;

	// View references
	protected EditText mLineOne;
	protected EditText mCity;
	protected EditText mState;
	protected EditText mZip;
	protected ImageView mGPSButton;
	protected ViewGroup mProgressView;
	protected ViewGroup mChoiceView;
	protected ListView mChoiceList;
	protected AddressAdapter mAdapter;
	private Address mInitAddress;
	protected boolean mGeocoded;

	protected boolean mTest;
	protected String mLocationProvider;

	public static AddressInputDialogFragment newInstance(Bundle args) {

		AddressInputDialogFragment frag = new AddressInputDialogFragment();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedState) {

		super.onCreate(savedState);

		mLocationListener = this;

		// Get the device's default location manager
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mGetAddressThreads = new HashMap<Integer, GetAddressThread>();

        if(PoolAddress.isBlank((Address)mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS))) {
			Log.d("DSN Debug", "Initial Address is blank");
			mInitAddress = PoolAddress.blankAddress();
		}else{
			mInitAddress = PoolAddress.clone((Address)mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS));
		}

        try{
	        ApplicationInfo ai = mCtx.getPackageManager().getApplicationInfo(
	        		mCtx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle b = ai.metaData;
			mTest = b.getBoolean("com.devspacenine.poolpal.TEST", true);
        }catch(NameNotFoundException e) {
        	mTest = true;
        	e.printStackTrace();
        }

        if(mTest) {
        	mLocationProvider = MockLocationUtil.PROVIDER_NAME;
        	MockLocationUtil.createMockLocationProvider(mLocationManager, mLocationProvider);
		}else{
			mLocationProvider = LocationManager.GPS_PROVIDER;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {

		final Resources res = getResources();
		Bundle args = getArguments();

		mRequestCode = args.getInt(REQUEST_CODE);

		View v;
		v = inflater.inflate(R.layout.input_dialog, container, false);

		// Set the layout of the view stub
		ViewStub stub = (ViewStub) v.findViewById(R.id.stub);
		stub.setLayoutResource(args.getInt(LAYOUT));
		mInputView = (ViewGroup) stub.inflate();

		final ViewStub progressStub = (ViewStub) v.findViewById(R.id.progress_stub);
		final ViewStub choiceStub = (ViewStub) v.findViewById(R.id.choice_stub);

		// Set the title
		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText(args.getString(TITLE));

		// Set the prompt
		mPrompt = (TextView) v.findViewById(R.id.prompt);
		mPrompt.setVisibility(View.VISIBLE);
		mPrompt.setText(args.getString(DETAILS));

		if(!(mValues.containsKey(PoolDataAdapter.VALUE))
				|| mValues.getString(PoolDataAdapter.VALUE) == null) {
			mValues.putString(PoolDataAdapter.VALUE, "");
		}

		mCancelButton = (TextView) v.findViewById(R.id.cancel);
		mConfirmButton = (TextView) v.findViewById(R.id.confirm);

		// Initialize the type tag of the cancel button
		mCancelButton.setTag(R.id.key_type, TAG_NORMAL);
		mConfirmButton.setTag(R.id.key_type, TAG_NORMAL);

		mLineOne = (EditText) mInputView.findViewById(R.id.line1);
		mCity = (EditText) mInputView.findViewById(R.id.city);
		mState = (EditText) mInputView.findViewById(R.id.state);
		mZip = (EditText) mInputView.findViewById(R.id.zip);

		Address a = mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS);
		mLineOne.setText(a.getAddressLine(0));
		mCity.setText(a.getLocality());
		mState.setText(a.getAdminArea());
		mZip.setText(a.getPostalCode());

		if(!mValues.containsKey(PoolDataAdapter.VALUE_GEOCODED)) {
			mValues.putBoolean(PoolDataAdapter.VALUE_GEOCODED, false);
			mGeocoded = false;
		}else{
			mGeocoded = mValues.getBoolean(PoolDataAdapter.VALUE_GEOCODED);
		}

		// Use TextWatchers to update the value if any edittexts change
		mLineOne.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				Address a = (Address) mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS);
				String newVal = s.toString().trim().replaceAll(" +", " ");
				if(newVal.equals(a.getAddressLine(0))) {
					return;
				}
				a.setAddressLine(0, newVal);
				a = PoolAddress.setLines(PoolAddress.parseLineOne(a));
				mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, a);
				mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(a));
				if(!newVal.equals(mInitAddress.getAddressLine(0))) {
					mGeocoded = false;
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		mLineOne.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if((actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
						|| actionId == EditorInfo.IME_ACTION_NEXT) {
					mCity.requestFocus();
					return true;
				}
				return false;
			}
		});

		mCity.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				Address a = (Address) mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS);
				String newVal = s.toString().trim().replaceAll(" +", " ");
				if(newVal.equals(a.getLocality())) {
					return;
				}
				a.setLocality(newVal);
				mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, PoolAddress.setLines(a));
				mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(a));
				if(!newVal.equals(mInitAddress.getLocality())) {
					mGeocoded = false;
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		mCity.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if((actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
						|| actionId == EditorInfo.IME_ACTION_NEXT) {
					mState.requestFocus();
					return true;
				}
				return false;
			}
		});

		mState.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				Address a = (Address) mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS);
				String newVal = s.toString().trim().replaceAll(" +", " ");
				if(newVal.equals(a.getAdminArea())) {
					return;
				}
				a.setAdminArea(newVal);
				mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, PoolAddress.setLines(a));
				mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(a));
				if(!newVal.equals(mInitAddress.getAdminArea())) {
					mGeocoded = false;
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		mState.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if((actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
						|| actionId == EditorInfo.IME_ACTION_NEXT) {
					mZip.requestFocus();
					return true;
				}
				return false;
			}
		});

		mZip.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				Address a = (Address) mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS);
				String newVal = s.toString().trim().replaceAll(" +", " ");
				if(newVal.equals(a.getPostalCode())) {
					return;
				}
				a.setPostalCode(newVal);
				mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, PoolAddress.setLines(a));
				mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(a));
				if(!newVal.equals(mInitAddress.getPostalCode())) {
					mGeocoded = false;
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		mZip.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if((actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN)
						|| actionId == EditorInfo.IME_ACTION_DONE) {
					saveAddress((Address)mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS));
					return true;
				}
				return false;
			}
		});

		mGPSButton = (ImageView) mInputView.findViewById(R.id.gps);
		mGPSButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Make sure the location provider is enabled
				if(mLocationManager.isProviderEnabled(mLocationProvider)){
					// Create the progress view if it hasn't been already
					if(mProgressView == null) {
						progressStub.setLayoutResource(R.layout.progress_stub);
						mProgressView = (ViewGroup) progressStub.inflate();
						((TextView)mProgressView.findViewById(R.id.progress_text)).setText(
								res.getString(R.string.waiting_for_gps));
						ImageView graphic = (ImageView) mProgressView.findViewById(R.id.progress_graphic);
						Animation rotateAnim = AnimationUtils.loadAnimation(mCtx, R.anim.progress_indeterminate_animation);
						graphic.startAnimation(rotateAnim);
					}

					// Create the choice view if it hasn't been already
					if(mChoiceView == null) {
						choiceStub.setLayoutResource(R.layout.spinner_input);
						mChoiceView = (ViewGroup) choiceStub.inflate();
						mChoiceList = (ListView) mChoiceView.findViewById(R.id.list);
						mChoiceView.setVisibility(View.GONE);

						// Set the choices adapter as a blank list of addresses
						mAdapter = new AddressAdapter(mCtx);
						mChoiceList.setAdapter(mAdapter);
						// Set the onItemClick listener for the choices list
						mChoiceList.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Address a = (Address) mAdapter.getItem(position);

								// Update the values
		    					mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, a);
		    					mValues.putBoolean(PoolDataAdapter.VALUE_GEOCODED, true);
		    					mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(a));

								// Update the text inputs
		    					mLineOne.setText(a.getAddressLine(0));
		    					mCity.setText(a.getLocality());
		    					mZip.setText(a.getPostalCode());
		    					mState.setText(a.getAdminArea());

								// Stop listening for location updates
		    					mHandler.removeCallbacks(mLocationUpdatesTimer);
								mLocationManager.removeUpdates(mLocationListener);
			    				waiting_for_gps = false;

			    				// Return to the starting layout
								mInputView.setVisibility(View.VISIBLE);
								mChoiceView.setVisibility(View.GONE);
								mProgressView.setVisibility(View.GONE);
								mPrompt.setText(R.string.details_address);
								mCancelButton.setTag(R.id.key_type, TAG_NORMAL);
								mConfirmButton.setTag(R.id.key_type, TAG_NORMAL);
								mConfirmButton.setEnabled(true);
								mConfirmButton.setText(android.R.string.ok);
							}
						});
					}
					startGPSSearch();
				}else{
        			// GPS is disabled, send out an intent to turn it on
					Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(intent, ENABLE_GPS);
        		}
			}
		});

		// make the confirm button save the address
		mConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.isEnabled()) {
					if(v.getTag(R.id.key_type).toString().equals(TAG_GPS)) {
						startGPSSearch();
						return;
					}else{
						Address a = (Address)mValues.getParcelable(PoolDataAdapter.VALUE_ADDRESS);
						if(!PoolAddress.equals(a, mInitAddress)) {
							mValues.putBoolean(PoolDataAdapter.VALUE_GEOCODED, mGeocoded);
						}
						saveAddress(a);
						return;
					}
				}
			}
		});

		// Set click listeners for the cancel button
		mCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getTag(R.id.key_type).toString().equals(TAG_GPS)) {
					mHandler.removeCallbacks(mLocationUpdatesTimer);
					mLocationManager.removeUpdates(mLocationListener);
					stopGetAddressThreads();
					waiting_for_gps = false;
					mProgressView.setVisibility(View.GONE);
					mChoiceView.setVisibility(View.GONE);
					mInputView.setVisibility(View.VISIBLE);
					mCancelButton.setTag(R.id.key_type, TAG_NORMAL);
					mConfirmButton.setTag(R.id.key_type, TAG_NORMAL);
					mConfirmButton.setEnabled(true);
					mConfirmButton.setText(android.R.string.ok);
				}else{
					negativeDecision();
					return;
				}
			}
		});

		return v;
	}

	public void startGPSSearch() {
		// Make sure the location provider is enabled
		if(mLocationManager.isProviderEnabled(mLocationProvider)){
			// Start getting the current location in a asynchronous thread
			if(mTest) {
				MockLocationUtil.publishMockLocation(32.72395, -97.41373, mCtx, mLocationManager,
						mLocationListener, mLocationProvider);
			}else{
				mLocationManager.requestLocationUpdates(mLocationProvider, 100, 0,
						mLocationListener);
			}
			waiting_for_gps = true;

			// Hide the text input view
			mInputView.setVisibility(View.GONE);

			mProgressView.setVisibility(View.VISIBLE);

			// Disable the confirm button and make the cancel button just cancel
			// the gps feature
			mConfirmButton.setTag(R.id.key_type, TAG_GPS);
			mConfirmButton.setEnabled(false);
			mConfirmButton.setText(R.string.search_again);
			mCancelButton.setTag(R.id.key_type, TAG_GPS);

			// Stop the location updates after 60 seconds
			mHandler.postDelayed(mLocationUpdatesTimer, 1000*60);

			// Try to get the last read location
			Location loc = mLocationManager.getLastKnownLocation(mLocationProvider);
			if(loc != null) {
				startGetAddressThread(loc);
			}
		}else{
			// GPS is disabled, send out an intent to turn it on
			Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, ENABLE_GPS);
		}
	}

	public void saveAddress(Address a) {

		Address geocodedAddress = null;

		// Skip this step if this address is equal to the initial address
		if(PoolAddress.equals(a, mInitAddress)) {
			Log.d("DSN Debug", "New address same as initial address.\nNew: ("+a+")\nInit: ("+mInitAddress+")");
			negativeDecision();
			return;
		}

		// Get coordinates for this address if it isn't geocoded
		if(!mValues.getBoolean(PoolDataAdapter.VALUE_GEOCODED, false)) {
			Log.d("DSN Debug", "not geocoded");
			Geocoder gc = new Geocoder(mCtx);
			if(Geocoder.isPresent()) {
				List<Address> locs = new ArrayList<Address>();
				try {
					locs = gc.getFromLocationName(a.getAddressLine(0) + " "
							+ a.getAddressLine(1) + " " + a.getAddressLine(2), 5);
				} catch (IOException e) {
					e.printStackTrace();
					Log.d("DSN Debug", "Geocoder IOException");
					negativeDecision();
					return;
				}

				for(Iterator<Address> iter = locs.iterator(); iter.hasNext();) {
					Address _a = iter.next();
					if(PoolAddress.isGeocoded(_a)) {
						Log.d("DSN Debug", "geocoded valid address");
						geocodedAddress = PoolAddress.setLines(_a);
						break;
					}
					if(!iter.hasNext()) {
						Log.d("DSN Debug", "Could not geocode a complete address");
						if(a.getFeatureName().equals("") || a.getThoroughfare().equals("")) {
							a = PoolAddress.parseLineOne(a);
						}
						a = PoolAddress.setLines(a);
					}
				}
			}else{
				Log.d("DSN Debug", "No Geocoder service available. Continue without coordinates");
				a.clearLatitude();
				a.clearLongitude();
				mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, PoolAddress.setLines(a));
				mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(a));
				positiveDecision();
				return;
			}
		}

		if(PoolAddress.isComplete(geocodedAddress)) {
			mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, geocodedAddress);
			mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(a));
			positiveDecision();
			return;
		}else{
			Log.d("DSN Debug", "Could not save address because it is incomplete.");
			negativeDecision();
			return;
		}
	}

	@Override
	public void negativeDecision() {

		mLocationManager.removeUpdates(mLocationListener);
		stopGetAddressThreads();
		mValues.putParcelable(PoolDataAdapter.VALUE_ADDRESS, mInitAddress);
		mValues.putBoolean(PoolDataAdapter.VALUE_GEOCODED, PoolAddress.isGeocoded(mInitAddress));
		mValues.putString(PoolDataAdapter.VALUE, PoolAddress.toString(mInitAddress));
		super.negativeDecision();
	}

	@Override
	public void positiveDecision() {

		mLocationManager.removeUpdates(mLocationListener);
		stopGetAddressThreads();
		super.positiveDecision();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch(requestCode) {
		case ENABLE_GPS:
			if(resultCode == Activity.RESULT_OK) {
				if(mLocationManager.isProviderEnabled(mLocationProvider)) {

				}else{
					Toast.makeText(getActivity(), "GPS Provider failed to start.", Toast.LENGTH_SHORT).show();
        		}
			}
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	public void startGetAddressThread(Location location) {
		int index = mGetAddressThreads.size();
    	GetAddressThread new_thread = new GetAddressThread(location.getLatitude(),
    			location.getLongitude(), 10, mHandler, index);
    	new_thread.startThread();
    	mGetAddressThreads.put(index, new_thread);
	}

	public void stopGetAddressThreads() {
		for(int key : mGetAddressThreads.keySet()) {
			mGetAddressThreads.get(key).stopThread();
		}
		mGetAddressThreads.clear();
	}

	/**
     * Called when the gps location has been updated
     */
    public void onLocationChanged(Location location) {
    	startGetAddressThread(location);
	}

    /**
     * Called when the status of the provider changes
     */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if(status == LocationProvider.OUT_OF_SERVICE) {
			mHandler.removeCallbacks(mLocationUpdatesTimer);
			mLocationManager.removeUpdates(mLocationListener);
			stopGetAddressThreads();
			waiting_for_gps = false;
			Toast.makeText(getActivity(), "Could not get an accurate GPS location", Toast.LENGTH_SHORT).show();
			mProgressView.setVisibility(View.GONE);
			mChoiceView.setVisibility(View.GONE);
			mInputView.setVisibility(View.VISIBLE);
			mCancelButton.setTag(R.id.key_type, TAG_NORMAL);
			mConfirmButton.setTag(R.id.key_type, TAG_NORMAL);
			mConfirmButton.setEnabled(true);
			mConfirmButton.setText(android.R.string.ok);
		}
	}

	/**
	 * Called when the provider is enabled
	 */
	public void onProviderEnabled(String provider) {}

	/**
	 * Called when the provider is disabled
	 */
	public void onProviderDisabled(String provider) {
		Toast.makeText(getActivity(), "GPS was just disabled. Canceling address search", Toast.LENGTH_SHORT).show();
		mHandler.removeCallbacks(mLocationUpdatesTimer);
		mLocationManager.removeUpdates(mLocationListener);
		stopGetAddressThreads();
		waiting_for_gps = false;
		mProgressView.setVisibility(View.GONE);
		mChoiceView.setVisibility(View.GONE);
		mInputView.setVisibility(View.VISIBLE);
		mCancelButton.setTag(R.id.key_type, TAG_NORMAL);
		mConfirmButton.setTag(R.id.key_type, TAG_NORMAL);
		mConfirmButton.setEnabled(true);
		mConfirmButton.setText(android.R.string.ok);
	}

	// A thread to try and search for the device's current location by address
	private class GetAddressThread extends Thread {
		private double mmLatitude;
		private double mmLongitude;
		private int mmMaxResults;
		private ArrayList<Address> mmAddressResult;
		private final Handler mmHandler;
		private volatile boolean mmRunning;
		private int mmIndex;

		public GetAddressThread(double latitude, double longitude, int maxResults, Handler handler, int index) {
			mmLatitude = latitude;
			mmLongitude = longitude;
			mmMaxResults = maxResults;
			mmHandler = handler;
			mmIndex = index;
			mmRunning = false;
		}

		public void run() {
			Geocoder geocoder = new Geocoder(getActivity());
			Bundle data = new Bundle();
			data.putInt(PoolPal.EXTRA_DATA, mmIndex);
			try{
				mmAddressResult = (ArrayList<Address>) geocoder.getFromLocation(mmLatitude, mmLongitude, mmMaxResults);
				if(mmRunning) {
					data.putParcelableArrayList(PoolPal.EXTRA_ADDRESSES, mmAddressResult);
					mmHandler.obtainMessage(MESSAGE_GPS_ADDRESS_SUCCESS, data).sendToTarget();
				}else{
					mmHandler.obtainMessage(MESSAGE_STOP, data).sendToTarget();
				}
			}catch(IOException e){
				if(mmRunning) {
					e.printStackTrace();
					mmHandler.obtainMessage(MESSAGE_ERROR, data).sendToTarget();
				}
			}
		}

		public synchronized void startThread() {
			if(!mmRunning) {
				mmRunning = true;
				start();
			}
		}

		public synchronized void stopThread() {
			if(mmRunning) {
				mmRunning = false;
				interrupt();
			}
		}
	}

	protected final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Get the data
			Bundle data = (Bundle) msg.obj;

			switch(msg.what) {
			// Message sent by a GetLocationThread when it successfully finds an address
			case MESSAGE_GPS_ADDRESS_SUCCESS:
				if(waiting_for_gps) {
					final ArrayList<Address> address_list = new ArrayList<Address>();

					// Remove any incomplete or duplicate addresses from the list
					for(Parcelable a : (ArrayList<Parcelable>)data.getParcelableArrayList(PoolPal.EXTRA_ADDRESSES)) {
						if(PoolAddress.isGeocoded((Address)a)) {
							if(!mAdapter.contains((Address)a)) {
								address_list.add(PoolAddress.setLines((Address)a));
							}
						}
					}

					// Check if any valid addresses were found
    				if(!address_list.isEmpty()) {
    					// Make sure progress text is updated
    					((TextView)mProgressView.findViewById(R.id.progress_text)).setText(
    							R.string.waiting_for_more_gps);
    					mProgressView.findViewById(R.id.divider).setVisibility(View.VISIBLE);

    					// Make sure the progress graphic is small
    					ImageView graphic = (ImageView)mProgressView.findViewById(R.id.progress_graphic);
    					graphic.setImageResource(
    							R.drawable.spinner_48_inner);
    					Animation rotateAnim = AnimationUtils.loadAnimation(mCtx, R.anim.progress_indeterminate_animation);
						graphic.startAnimation(rotateAnim);

    					// Make sure the choice view is visible
    					mChoiceView.setVisibility(View.VISIBLE);
    					mPrompt.setText(R.string.choose_address);

    					mAdapter.add(address_list);
    					mAdapter.notifyDataSetChanged();
    					if(mAdapter.getCount() > 4) {
    						removeCallbacks(mLocationUpdatesTimer);
    						mLocationManager.removeUpdates(mLocationListener);
    	    				waiting_for_gps = false;
    	    				mProgressView.setVisibility(View.GONE);
    					}
            		}
				}
				mGetAddressThreads.remove(data.getInt(PoolPal.EXTRA_DATA));
				break;
			// Message sent by a GetLocationThread when it has been stopped prematurely
			case MESSAGE_STOP:
			// Message sent by a GetLocationThread when it encounters an error
				mGetAddressThreads.remove(data.getInt(PoolPal.EXTRA_DATA));
			case MESSAGE_ERROR:
				if(waiting_for_gps) {
					Toast.makeText(mCtx, "Could not get an address because of network errors", Toast.LENGTH_SHORT).show();
					removeCallbacks(mLocationUpdatesTimer);
					mLocationManager.removeUpdates(mLocationListener);
					stopGetAddressThreads();
    				waiting_for_gps = false;
    				mProgressView.setVisibility(View.GONE);
    				mChoiceView.setVisibility(View.GONE);
					mInputView.setVisibility(View.VISIBLE);
					mCancelButton.setTag(R.id.key_type, TAG_NORMAL);
					mConfirmButton.setTag(R.id.key_type, TAG_NORMAL);
					mConfirmButton.setEnabled(true);
					mConfirmButton.setText(android.R.string.ok);
				}
				break;
			default:
				Log.d("DSN Debug", "Message type \"" + msg.what + "\" not supported");
			}
		}
	};

	private Runnable mLocationUpdatesTimer = new Runnable() {

		public void run() {
			mLocationManager.removeUpdates(mLocationListener);
			stopGetAddressThreads();
			waiting_for_gps = false;
			mProgressView.setVisibility(View.GONE);
			mConfirmButton.setEnabled(true);
			// If no addresses were found, show an error message and return to the manual
			// inputs
			if(mChoiceList.getAdapter().getCount() == 0) {
				Toast.makeText(mCtx, "Could not get an accurate GPS location", Toast.LENGTH_SHORT).show();
				mChoiceView.setVisibility(View.GONE);
				mInputView.setVisibility(View.VISIBLE);
				mCancelButton.setTag(R.id.key_type, TAG_NORMAL);
				mConfirmButton.setTag(R.id.key_type, TAG_NORMAL);
				mConfirmButton.setText(android.R.string.ok);
			}else{
				// Set the confirm button to start watching location updates again
				mConfirmButton.setTag(R.id.key_type, TAG_GPS);
			}
		}
	};
}
