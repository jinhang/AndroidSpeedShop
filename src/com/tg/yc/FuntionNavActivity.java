package com.tg.yc;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mc.model.TodaySalesPromotionResult;
import com.mc.util.CharacterSetToolkit;
import com.mc.util.HttpUtil;
import com.mc.util.Util;
import com.tg.util.ActivityContral;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class FuntionNavActivity extends Activity {
	static boolean is_shownotification = true;
	String shop_id = "1";// 超市号， 本来是从服务器获取
	String pages = "1";// 查询起始页数
	String size = "30";// 今日促销每次请求多少个数据
	TodaySalesPromotionResult todaySalesPromotionResult = null;// 为了解析JSON
	Button accountButton;
	Button discountButton;
	Button shopButton;
	Button historyButton;
	List<Map<String, Object>> listItem;
	List<Map<String, Object>> todaySaleListItem;
	String userName = "18291994584";// 账户
	Context context;
	static int i = 0;
	// 设置状态栏
	NotificationManager notificationManager = null;
	Notification notification = null;
	PendingIntent pendingIntent = null;// 即将发送的事件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.activity_function_nav,
				null);
		setContentView(view);
		init();
		accountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityContral.startActivityWait(FuntionNavActivity.this,
						AccountActivity.class, 0);
			}
		});
		discountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*ActivityContral.startActivityWait(FuntionNavActivity.this,
						TodayCheepActivity.class, 0);*/
				Intent intent = new Intent(context, TodayCheepActivity.class);
				intent.putExtra("list", (Serializable) todaySaleListItem);
				intent.putExtra("currentPage",
						todaySalesPromotionResult.getCurrentPage());// 当前页数
				intent.putExtra("page", todaySalesPromotionResult.getPages());// 总页数
				intent.putExtra("size", todaySalesPromotionResult.getSize());// 总个数
				startActivity(intent);

			}
		});
		shopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i = 0;
				// 请求得到 服务器上面的购物车
				/*if (userName == null || userName.equals("")) {
					Toast.makeText(context, "请先注册", 1000).show();
				}
				if (!Util.isNetworkConnected(context)) {
					Toast.makeText(context, "请检查网络连接", 1000).show();
				} else {
					GwcListAsytask gwcListAsytask = new GwcListAsytask();
					gwcListAsytask.execute("false");
				}*/
				 ActivityContral.startActivityWait(FuntionNavActivity.this,
				 ShoppingCarActivity.class, 0);

			}
		});
		historyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i = 1;
				/*if (userName == null || userName.equals("")) {
					Toast.makeText(context, "请先注册", 1000).show();
				}
				if (!Util.isNetworkConnected(context)) {
					Toast.makeText(context, "请检查网络连接", 1000).show();
				} else {
					GwcListAsytask gwcListAsytask = new GwcListAsytask();
					gwcListAsytask.execute("true");
				}*/
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		context = getApplicationContext();
		listItem = new ArrayList<Map<String, Object>>();
		accountButton = (Button) findViewById(R.id.account);
		discountButton = (Button) findViewById(R.id.discount);
		shopButton = (Button) findViewById(R.id.shopping);
		historyButton = (Button) findViewById(R.id.scan);
		// 显示通知栏
		if (Util.isNetworkConnected(getApplicationContext())) {
			TodaySalesPromotionAsytask todaySalesPromotionAsytask = new TodaySalesPromotionAsytask();
			todaySalesPromotionAsytask.execute(shop_id, pages, size);
			// System.out.println(todaySalesPromotionResult.getAmount());
		}
	}

	/**
	 * 今日促销
	 */
	class TodaySalesPromotionAsytask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			String url = "";
			try {
				url = HttpUtil.BASE_URL + "TodaySalesPromotionServlet?shop_id="
						+ params[0] + "&page=" + params[1] + "&size"
						+ params[2];
			} catch (Exception e) {
				// TODO: handle exception
			}
			result = HttpUtil.queryStringForPost(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println(result);

			Gson gson = new Gson();
			todaySalesPromotionResult = gson.fromJson(result,
					TodaySalesPromotionResult.class);

			todaySaleListItem = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < todaySalesPromotionResult
					.getTodaySalesPromotions().size(); i++) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("ItemName", todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getGoods());
				hashMap.put("Sale_price", "原价:"+todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getPrice());
				hashMap.put("item_no", todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getItem_no());
				hashMap.put("nowprice", "特价:"+todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getNowPrice());
				todaySaleListItem.add(hashMap);
			}
			/*
			 * JSONObject jsonObject = new JSONObject(result); JSONArray
			 * jsonArray = (JSONArray) jsonObject.get("itemCarsDAO"); String
			 * cuurrectPage = (JSONObject)
			 */
			if (is_shownotification)
				showNotification();
		}

	}

	public void showNotification() {
		if (todaySalesPromotionResult != null
				&& !todaySalesPromotionResult.getAmount().equals("0")) {

			notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notification = new Notification();
			// notification.defaults = Notification.DEFAULT_ALL;// 所有为默认设置

			// notification.flags |= Notification.FLAG_ONGOING_EVENT; //
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			// notification.flags |= Notification.FLAG_NO_CLEAR; //
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			// DEFAULT_ALL 使用所有默认值，比如声音，震动，闪屏等等
			// DEFAULT_LIGHTS 使用默认闪光提示
			// DEFAULT_SOUNDS 使用默认提示声音
			// DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission
			// android:name="android.permission.VIBRATE" />权限
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			notification.defaults = Notification.DEFAULT_ALL
					| Notification.DEFAULT_VIBRATE;
			// 叠加效果常量
			// notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
			notification.ledARGB = Color.BLUE;
			notification.ledOnMS = 5000; // 闪光时间，毫秒
			notification.icon = R.drawable.kuai;
			// 设置事件
			Intent intent = new Intent(context, TodayCheepActivity.class);
			intent.putExtra("list", (Serializable) todaySaleListItem);
			intent.putExtra("currentPage",
					todaySalesPromotionResult.getCurrentPage());// 当前页数
			intent.putExtra("page", todaySalesPromotionResult.getPages());// 总页数
			System.out.println("页数"+todaySalesPromotionResult.getPages());
			intent.putExtra("size", todaySalesPromotionResult.getAmount());// 总个数
			pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);// 跳转到今日促销
			CharSequence tittle = "快购宝今日促销:";
			CharSequence content = "今日促销商品有"
					+ todaySalesPromotionResult.getAmount() + "件";
			notification.setLatestEventInfo(getApplicationContext(), tittle,
					content, pendingIntent);
			notificationManager.notify(0, notification);
		}
	}

	

}
