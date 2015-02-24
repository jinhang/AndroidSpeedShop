package com.tg.yc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetFileDescriptor;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.util.CharacterSetToolkit;
import com.mc.util.HttpUtil;
import com.mc.util.Util;
import com.tg.util.ActivityContral;
import com.zijunlin.Zxing.Demo.CaptureActivity;

//import android.content.DialogInterface.*;

public class ShoppingCarActivity extends Activity {

	ImageView backButton;
	Button payButton;
	Button scanButton;
	TextView sumprice;// 合计
	ListView lv;
	List<Map<String, Object>> listItem;
	String userName = "18291994584";// 账户
	double sum;// 合计
	String name;// 要删除的商品名字
	String item_no;// 商品的item_no
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_shop_car, null);
		setContentView(view);
		context = ShoppingCarActivity.this;
		lv = (ListView) findViewById(R.id.list);
		sumprice = (TextView) findViewById(R.id.sumprice);
		setView();
		/*
		 * GwcListAsytask gwcListAsytask = new GwcListAsytask();
		 * gwcListAsytask.execute();
		 */
		// listItem = new ArrayList<Map<String,Object>>();
		backButton = (ImageView) findViewById(R.id.back);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		payButton = (Button) findViewById(R.id.okpay);
		payButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (userName == null || userName.equals("")) {
					Toast.makeText(getApplicationContext(), "请先注册", 1000)
							.show();
				}
				if (!Util.isNetworkConnected(getApplicationContext())) {
					Toast.makeText(getApplicationContext(), "请检查网络连接", 1000)
							.show();
				} else {
					ActivityContral.startActivity(ShoppingCarActivity.this,
							PayingActivity.class, 0);
				}
			}
		});
		scanButton = (Button) findViewById(R.id.scan);
		scanButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (userName == null || userName.equals("")) {
					Toast.makeText(getApplicationContext(), "请先注册", 1000)
							.show();
				}
				if (!Util.isNetworkConnected(getApplicationContext())) {
					Toast.makeText(getApplicationContext(), "请检查网络连接", 1000)
							.show();
				} else {
					Intent intent = new Intent();
					intent.setClass(ShoppingCarActivity.this,
							CaptureActivity.class);
					startActivityForResult(intent, 0);
				}
				// ActivityContral.startActivity(ShoppingCarActivity.this,
				// ScanActivity.class, 0);
			}
		});

		/**
		 * 长恩列表
		 */
		// 添加长按点击，查看详情
		lv.setOnCreateContextMenuListener(myListener2);
	}

	private OnCreateContextMenuListener myListener2 = new OnCreateContextMenuListener() {

		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "减少");
			menu.add(0, 1, 1, "增加");
			menu.add(0, 2, 2, "删除");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo info = item.getMenuInfo();
		AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
		// 获取选中行位置
		int position = contextMenuInfo.position;
		HashMap<String, Object> itemd = (HashMap<String, Object>) listItem
				.get(position);
		if (item.getItemId() == 0) {// 减少
			name = (String) itemd.get("ItemName");// 货物名字
			System.out.println("item_no" + name);
			dialog("减少");
		}
		if (item.getItemId() == 1) {// 增加
			item_no = (String) itemd.get("item_no");// 货物名字
			System.out.println("item_no" + item_no);
			dialog("增加");
		}
		if (item.getItemId() == 2) {// 清除
			name = (String) itemd.get("ItemName");// 货物名字
			System.out.println("item_no" + name);
			dialog("删除");
		}
		return super.onContextItemSelected(item);
	}
	// 删除
	protected void dialog(final String string) {
		Builder builder = new Builder(ShoppingCarActivity.this);
		builder.setMessage("确认" + string + "吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("删除成功");
				if (string.equals("减少")) {
					deleteGoods dGoods = new deleteGoods();
					dGoods.execute("false");
				}
				if (string.equals("增加")) {
					QingQiuAsyntask qingQiuAsyntask = new QingQiuAsyntask();
					qingQiuAsyntask.execute(item_no);
				}
				if (string.equals("删除")) {
					deleteGoods dGoods = new deleteGoods();
					dGoods.execute("true");
				}

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 删除该物品
	 * 
	 * @author mc 2013-11-9
	 */
	class deleteGoods extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "";
			String result = "";
			try {
				url = HttpUtil.BASE_URL
						+ "DeleteServlet?username="
						+ URLEncoder.encode(
								URLEncoder.encode(userName, "UTF-8"), "UTF-8")
						+ "&is_all="
						+ params[0]
						+ "&goods="
						+ URLEncoder.encode(URLEncoder.encode(
								CharacterSetToolkit.toUnicode(name, true),
								"UTF-8"), "UTF-8");
				System.out.println("删除成功" + url);
				result = HttpUtil.queryStringForPost(url);
				System.out.println("结果" + result);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result.equals("0")) {
				Toast.makeText(context, "成功", 1000).show();
				setView();
			} else {
				Toast.makeText(context, "服务器错误", 1000).show();
			}
		}

	}

	private void redirectTo() {
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		System.out.println("成功" + requestCode + resultCode);

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				if (intent.getStringExtra("result").equals("ok")) {
					// 添加成功 实例化 非静态内部类
					GwcListAsytask gwcListAsytask = new GwcListAsytask();
					gwcListAsytask.execute("false");
				} else {
					Toast.makeText(getApplicationContext(), "添加购物车失败", 1000)
							.show();
				}
			} else if (resultCode == RESULT_CANCELED) {
			}
		} else {
			return;
		}
	}

	class GwcListAsytask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			String url;
			try {
				url = HttpUtil.BASE_URL
						+ "GwcListServlet?userName="
						+ URLEncoder.encode(
								URLEncoder.encode(userName, "UTF-8"), "UTF-8")
						+ "&is_sale="
						+ URLEncoder.encode(
								URLEncoder.encode(params[0], "UTF-8"), "UTF-8");
				;
				result = HttpUtil.queryStringForPost(url);
				System.out.println("result:" + result);
				return result;
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				listItem = new ArrayList<Map<String, Object>>();
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = (JSONArray) jsonObject.get("itemCarsDAO");
				sum = 0;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject o = (JSONObject) jsonArray.get(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("ItemName", CharacterSetToolkit.fromUnicode(o
							.getString("goods").toCharArray(), 0,
							o.getString("goods").length(), o.getString("goods")
									.toCharArray()));
					map.put("Sale_price",
							String.valueOf(Double.parseDouble(o
									.getString("price")))
									+ "x"
									+ o.getString("count"));
					map.put("item_no", o.getString("item_no"));
					sum += Double.parseDouble(o.getString("price"))
							* Integer.parseInt(o.getString("count"));
					listItem.add(map);
				}
				setListView();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	private void setView() {
		// lv = (ListView) findViewById(R.id.list);

		// listItem = getDatas();
		/*
		 * Intent intent = getIntent(); listItem = (List<Map<String, Object>>)
		 * intent .getSerializableExtra("list"); sum =
		 * intent.getDoubleExtra("sum", 0.0);
		 */
		GwcListAsytask gwcListAsytask = new GwcListAsytask();
		gwcListAsytask.execute("false");// 未购买的
		/* setListView(); */
	}

	private void setListView() {
		sumprice.setText("合计：" + String.valueOf(sum) + "元");
		SimpleAdapter adapter = new SimpleAdapter(this, listItem,
				R.layout.item, new String[] { "ItemName", "Sale_price" },
				new int[] { R.id.name, R.id.howmany });
		lv.setAdapter(adapter);
	}

	/**
	 * 将物品加入购物车
	 * 
	 * @author mc 2013-10-27
	 */
	class QingQiuAsyntask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url;
			try {
				url = HttpUtil.BASE_URL
						+ "AddGwcListServlet?userName="
						+ URLEncoder.encode(
								URLEncoder.encode(userName, "UTF-8"), "UTF-8")
						+ "&item_no="
						+ URLEncoder.encode(
								URLEncoder.encode(params[0], "UTF-8"), "UTF-8")
						+ "&count=1";
				String result = HttpUtil.queryStringForPost(url);
				System.out.println("result:" + result);
				return result;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("result:" + result);
			if (result.equals("ok")) {
				setView();
				Toast.makeText(getApplicationContext(), "添加成功", 1000).show();
			} else {
				Toast.makeText(getApplicationContext(), "服务器问题", 1000).show();
			}
		}

	}

}
