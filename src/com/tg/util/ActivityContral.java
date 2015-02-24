package com.tg.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ActivityContral {
	
	public static void startActivity(Activity activity1,Class<?> activity2,int type){
		Intent intent=new Intent();
		intent.setClass(activity1,activity2);
		activity1.startActivity(intent);
		activity1.finish();
	}
	
	
	public static void startActivityWait(Activity activity1,Class<?> activity2,int type){
		Intent intent=new Intent();
		intent.setClass(activity1,activity2);
		//
		activity1.startActivity(intent);
	}
	
	public static void startActivityWait(Activity activity1,Class<?> activity2,Bundle bundle){
		Intent intent=new Intent();
		intent.putExtras(bundle);
		intent.setClass(activity1,activity2);
		activity1.startActivity(intent);
	}
	
	
	 public static void cancelLoading(View v,int id) {
		    View viewLayout =v.findViewById(id);
	    	viewLayout.setVisibility(View.GONE);
		}

	/* public static void cancelLoading(Context context) {
		 RelativeLayout layout=(RelativeLayout)((Activity) context).findViewById(R.id.layout_loading);
			layout.setVisibility(View.GONE);
		}
	 public static void showLoading(Context context) {
		 RelativeLayout layout=(RelativeLayout)((Activity) context).findViewById(R.id.layout_loading);
		 layout.setVisibility(View.VISIBLE);
	 }*/

}
