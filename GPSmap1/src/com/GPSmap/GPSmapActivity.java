package com.GPSmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class GPSmapActivity extends Activity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private Context context;

	// ��λ���
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	// �Զ��嶨λͼ��
	private BitmapDescriptor mIconLocation;

	// ���������
	private BitmapDescriptor mMarker;
		

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		getWindow().setFlags(0x08000000, 0x08000000);
		SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.main);
        this.context = this;

		initView();
		// ��ʼ����λ
		initLocation();
		initMarker();
		addOverlays();
		
		
		 //�����������Ӧ�¼�
	       mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				public boolean onMarkerClick(Marker arg0) {
					//showWindow();
					showNewActivity();
					return true;
				}  
	        }); 
		
		//��ʱ����
		/************************************************************/
		//Intent intent = new Intent();
		//intent.setClass(GPSmapActivity.this, OnDrawActivity.class);
		//startActivity(intent);
		/************************************************************/
    }
    
  //��������������һ��activity
	private void showNewActivity()
	{
		Intent intent = new Intent();
		intent.setClass(GPSmapActivity.this, OnDrawActivity.class);
		startActivity(intent);
		
	}
	//��������ﵯ��
	private void showWindow()
	{

		//����InfoWindowչʾ��view  
		Button button = new Button(getApplicationContext());  
		button.setBackgroundResource(R.drawable.popup);  
		//����������ʾ��InfoWindow�������  
		LatLng pt = new LatLng(34.239705,108.969155);  
		//����InfoWindow , ���� view�� �������꣬ y ��ƫ���� 
		InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);  
		//��ʾInfoWindow  
		mBaiduMap.showInfoWindow(mInfoWindow);
		
	}

	private void initMarker()
	{
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
		
	}

	private void initLocation()
	{

		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		
	}

	private void initView()
	{
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//���Ŵ�С����
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		// ������λ
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
	
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		// ֹͣ��λ
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.id_map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;

		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;

		case R.id.id_map_traffic:
			if (mBaiduMap.isTrafficEnabled())
			{
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("ʵʱ��ͨ(off)");
			} else
			{
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("ʵʱ��ͨ(on)");
			}
			break;
		case R.id.id_map_location:
			centerToMyLocation();
			break;		
		//case R.id.id_add_overlay:
			//addOverlays(Info.infos);
			//addOverlays();
		//	break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��Ӹ�����
	 * 
	 * @param infos
	 */
	//���maker
	private void addOverlays()
	{
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions options;
		
		latLng = new LatLng(34.239705,108.969155);
			// ͼ��
		options = new MarkerOptions().position(latLng).icon(mMarker)
					.zIndex(5);
		mBaiduMap.addOverlay(options);
		
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
		/*
		//������������ʾ�������  
		LatLng llText = new LatLng(34.289705,108.869155);  
		//��������Option���������ڵ�ͼ���������  
		OverlayOptions textOption = new TextOptions()  
		    .bgColor(0xAAFFFF00)  
		    .fontSize(50)  
		    .fontColor(0xFFFF00FF)  
		    .text("�ٶȵ�ͼSDKbalabalabaaldfjoaewufo ewu")  
		    .rotate(-30)  
		    .position(llText); 
		    */

	}
	
	

	/**
	 * ��λ���ҵ�λ��
	 */
	private void centerToMyLocation()
	{
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	private class MyLocationListener implements BDLocationListener
	{
		public void onReceiveLocation(BDLocation location)
		{
			MyLocationData data = new MyLocationData.Builder()//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();
			mBaiduMap.setMyLocationData(data);
			
			// ���¾�γ��
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();

			if (isFirstIn)
			{
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;

				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
			}

		}
	}
  }
    
    