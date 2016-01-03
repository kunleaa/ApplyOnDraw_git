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

	// 定位相关
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	// 自定义定位图标
	private BitmapDescriptor mIconLocation;

	// 覆盖物相关
	private BitmapDescriptor mMarker;
		

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		getWindow().setFlags(0x08000000, 0x08000000);
		SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.main);
        this.context = this;

		initView();
		// 初始化定位
		initLocation();
		initMarker();
		addOverlays();
		
		
		 //点击覆盖物响应事件
	       mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				public boolean onMarkerClick(Marker arg0) {
					//showWindow();
					showNewActivity();
					return true;
				}  
	        }); 
		
		//临时放置
		/************************************************************/
		//Intent intent = new Intent();
		//intent.setClass(GPSmapActivity.this, OnDrawActivity.class);
		//startActivity(intent);
		/************************************************************/
    }
    
  //点击覆盖物调到另一个activity
	private void showNewActivity()
	{
		Intent intent = new Intent();
		intent.setClass(GPSmapActivity.this, OnDrawActivity.class);
		startActivity(intent);
		
	}
	//点击覆盖物弹窗
	private void showWindow()
	{

		//创建InfoWindow展示的view  
		Button button = new Button(getApplicationContext());  
		button.setBackgroundResource(R.drawable.popup);  
		//定义用于显示该InfoWindow的坐标点  
		LatLng pt = new LatLng(34.239705,108.969155);  
		//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
		InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);  
		//显示InfoWindow  
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
		//缩放大小设置
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		// 开启定位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
	
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		// 停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
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
				item.setTitle("实时交通(off)");
			} else
			{
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(on)");
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
	 * 添加覆盖物
	 * 
	 * @param infos
	 */
	//添加maker
	private void addOverlays()
	{
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions options;
		
		latLng = new LatLng(34.239705,108.969155);
			// 图标
		options = new MarkerOptions().position(latLng).icon(mMarker)
					.zIndex(5);
		mBaiduMap.addOverlay(options);
		
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
		/*
		//定义文字所显示的坐标点  
		LatLng llText = new LatLng(34.289705,108.869155);  
		//构建文字Option对象，用于在地图上添加文字  
		OverlayOptions textOption = new TextOptions()  
		    .bgColor(0xAAFFFF00)  
		    .fontSize(50)  
		    .fontColor(0xFFFF00FF)  
		    .text("百度地图SDKbalabalabaaldfjoaewufo ewu")  
		    .rotate(-30)  
		    .position(llText); 
		    */

	}
	
	

	/**
	 * 定位到我的位置
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
			
			// 更新经纬度
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
    
    