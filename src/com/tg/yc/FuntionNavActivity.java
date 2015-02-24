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
	String shop_id = "1";// ���кţ� �����Ǵӷ�������ȡ
	String pages = "1";// ��ѯ��ʼҳ��
	String size = "30";// ���մ���ÿ��������ٸ�����
	TodaySalesPromotionResult todaySalesPromotionResult = null;// Ϊ�˽���JSON
	Button accountButton;
	Button discountButton;
	Button shopButton;
	Button historyButton;
	List<Map<String, Object>> listItem;
	List<Map<String, Object>> todaySaleListItem;
	String userName = "18291994584";// �˻�
	Context context;
	static int i = 0;
	// ����״̬��
	NotificationManager notificationManager = null;
	Notification notification = null;
	PendingIntent pendingIntent = null;// �������͵��¼�

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
						todaySalesPromotionResult.getCurrentPage());// ��ǰҳ��
				intent.putExtra("page", todaySalesPromotionResult.getPages());// ��ҳ��
				intent.putExtra("size", todaySalesPromotionResult.getSize());// �ܸ���
				startActivity(intent);

			}
		});
		shopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				i = 0;
				// ����õ� ����������Ĺ��ﳵ
				/*if (userName == null || userName.equals("")) {
					Toast.makeText(context, "����ע��", 1000).show();
				}
				if (!Util.isNetworkConnected(context)) {
					Toast.makeText(context, "������������", 1000).show();
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
					Toast.makeText(context, "����ע��", 1000).show();
				}
				if (!Util.isNetworkConnected(context)) {
					Toast.makeText(context, "������������", 1000).show();
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
		// ��ʾ֪ͨ��
		if (Util.isNetworkConnected(getApplicationContext())) {
			TodaySalesPromotionAsytask todaySalesPromotionAsytask = new TodaySalesPromotionAsytask();
			todaySalesPromotionAsytask.execute(shop_id, pages, size);
			// System.out.println(todaySalesPromotionResult.getAmount());
		}
	}

	/**
	 * ���մ���
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
				hashMap.put("Sale_price", "ԭ��:"+todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getPrice());
				hashMap.put("item_no", todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getItem_no());
				hashMap.put("nowprice", "�ؼ�:"+todaySalesPromotionResult
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
			// notification.defaults = Notification.DEFAULT_ALL;// ����ΪĬ������

			// notification.flags |= Notification.FLAG_ONGOING_EVENT; //
			// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			// notification.flags |= Notification.FLAG_NO_CLEAR; //
			// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			// DEFAULT_ALL ʹ������Ĭ��ֵ�������������𶯣������ȵ�
			// DEFAULT_LIGHTS ʹ��Ĭ��������ʾ
			// DEFAULT_SOUNDS ʹ��Ĭ����ʾ����
			// DEFAULT_VIBRATE ʹ��Ĭ���ֻ��𶯣������<uses-permission
			// android:name="android.permission.VIBRATE" />Ȩ��
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			notification.defaults = Notification.DEFAULT_ALL
					| Notification.DEFAULT_VIBRATE;
			// ����Ч������
			// notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
			notification.ledARGB = Color.BLUE;
			notification.ledOnMS = 5000; // ����ʱ�䣬����
			notification.icon = R.drawable.kuai;
			// �����¼�
			Intent intent = new Intent(context, TodayCheepActivity.class);
			intent.putExtra("list", (Serializable) todaySaleListItem);
			intent.putExtra("currentPage",
					todaySalesPromotionResult.getCurrentPage());// ��ǰҳ��
			intent.putExtra("page", todaySalesPromotionResult.getPages());// ��ҳ��
			System.out.println("ҳ��"+todaySalesPromotionResult.getPages());
			intent.putExtra("size", todaySalesPromotionResult.getAmount());// �ܸ���
			pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);// ��ת�����մ���
			CharSequence tittle = "�칺�����մ���:";
			CharSequence content = "���մ�����Ʒ��"
					+ todaySalesPromotionResult.getAmount() + "��";
			notification.setLatestEventInfo(getApplicationContext(), tittle,
					content, pendingIntent);
			notificationManager.notify(0, notification);
		}
	}

	

}
