package com.tg.yc;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MapActivity extends Activity {
	BMapManager mBMapMan = null;
	MapView mMapView = null;
	Button enter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan=new BMapManager(getApplication());
		mBMapMan.init("1eb71b6de5dbb8e694afe8a7ca111a7a", null);  
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.activity_map);
		mMapView=(MapView)findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		//设置启用内置的缩放控件
		MapController mMapController=mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);//设置地图中心点
		mMapController.setZoom(12);//设置地图zoom级别
		enter=(Button) findViewById(R.id.okpay);
		enter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				redirectTo();  				
			}
		});
	}
	
	   private void redirectTo() {
	    	
			Intent intent = new Intent(this,FuntionNavActivity.class);
			startActivity(intent);
			finish();
		}
	
	@Override
	protected void onDestroy(){
	        mMapView.destroy();
	        if(mBMapMan!=null){
	                mBMapMan.destroy();
	                mBMapMan=null;
	        }
	        super.onDestroy();
	}
	@Override
	protected void onPause(){
	        mMapView.onPause();
	        if(mBMapMan!=null){
                mBMapMan.stop();
	        }
	        super.onPause();
	}
	@Override
	protected void onResume(){
	        mMapView.onResume();
	        if(mBMapMan!=null){
	                mBMapMan.start();
	        }
        super.onResume();
	}
}