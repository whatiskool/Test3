package net.apptools.test3;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;


import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity  implements android.location.LocationListener {

	LocationManager mLocationManager;

	private GoogleMap mGoogleMap;
	private BusDb mBusDb;
	public Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mBusDb = BusDb.getInstance(getApplicationContext());
		try {
			initilizeMap();
			loadLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	private void cursorClose() {
		if (mCursor != null && !mCursor.isClosed())
			mCursor.close();
	}

	private void initilizeMap() {
		if (mGoogleMap == null) {

			mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

			if (mGoogleMap == null) {
			} else {
				mGoogleMap.setMyLocationEnabled(true);
				mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
				mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
				mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

				cursorClose();
				mGoogleMap.clear();

				mCursor = mBusDb.selectLocation(35.1796F, 129.076F);

				if (mCursor.moveToFirst()) {

					while (mCursor.moveToNext()) {

						double latitude = mCursor.getDouble(8);
						double longitude = mCursor.getDouble(7);

						String id = mCursor.getString(9);
						String name = mCursor.getString(3);

						LatLng latlng = new LatLng(latitude, longitude);

						MarkerOptions marker = new MarkerOptions().position(latlng).title(name).snippet(id);
						marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

						mGoogleMap.addMarker(marker);
					}

					LatLng currentLatlng = new LatLng(35.1796F, 129.076F);

					CameraPosition INIT = new CameraPosition.Builder().target(currentLatlng).zoom(14F).bearing(0F) // orientation
							.tilt(0F) // viewing angle
							.build();
					mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));

					mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

						public void onInfoWindowClick(Marker marker) {
//							Intent intent = new Intent(getApplicationContext(), BusstopDetailActivity.class);
//							intent.putExtra("BusStop", marker.getSnippet());
//							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(intent);

						}
					});
				}


			}
		}

		if ( mGoogleMap != null) {//mTabMode == TAB_LOCATION &&
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.getMyLocation();
		}
	}

	private void loadLocation() {

		if (mGoogleMap == null)
			return;

		GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		Criteria criteria = new Criteria();
		String provider = mLocationManager.getBestProvider(criteria, true);

		if (provider == null) { // ��ġ���� ������ �ȵǾ� ������ �����ϴ� ��Ƽ��Ƽ�� �̵��մϴ�
			DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Location gpsLocation = mLocationManager.getLastKnownLocation("gps");

					if (gpsLocation != null) {
						onLocationChanged(gpsLocation);
					}
				}
			};
			DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
				}
			};

			new AlertDialog.Builder(this).setTitle("��ġ���� ����").setMessage("��ġ���񽺰� �������� �ʾ� ���� ���񽺸� ����Ͻ� �� ����ϴ�. ��ġ���� ������ �Ͻðڽ��ϱ�?").setPositiveButton("����", ok)
					.setNegativeButton("�ݱ�", cancel).show();

		} else { // ��ġ ���� ������ �Ǿ� ������ ������ġ�� �޾ƿɴϴ�.
			mLocationManager.requestLocationUpdates(provider, 1, 1, this);
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.getMyLocation();
		}
	}


	@Override
	public void onLocationChanged(Location location) {


			cursorClose();
			mGoogleMap.clear();

			mCursor = mBusDb.selectLocation(location.getLatitude(), location.getLongitude());

			if (mCursor.moveToFirst()) {

				while (mCursor.moveToNext()) {

					double latitude = mCursor.getDouble(8);
					double longitude = mCursor.getDouble(7);

					String id = mCursor.getString(9);
					String name = mCursor.getString(3);

					LatLng latlng = new LatLng(latitude, longitude);

					MarkerOptions marker = new MarkerOptions().position(latlng).title(name).snippet(id);
					marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

					mGoogleMap.addMarker(marker);
				}

				LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());

				CameraPosition INIT = new CameraPosition.Builder().target(currentLatlng).zoom(16F).bearing(0F) // orientation
						.tilt(0F) // viewing angle
						.build();
				mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));

				mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

					public void onInfoWindowClick(Marker marker) {
//						Intent intent = new Intent(getApplicationContext(), BusstopDetailActivity.class);
//						intent.putExtra("BusStop", marker.getSnippet());
//						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);

					}
				});
			}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
