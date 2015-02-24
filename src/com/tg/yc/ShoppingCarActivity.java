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
	TextView sumprice;// �ϼ�
	ListView lv;
	List<Map<String, Object>> listItem;
	String userName = "18291994584";// �˻�
	double sum;// �ϼ�
	String name;// Ҫɾ������Ʒ����
	String item_no;// ��Ʒ��item_no
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
					Toast.makeText(getApplicationContext(), "����ע��", 1000)
							.show();
				}
				if (!Util.isNetworkConnected(getApplicationContext())) {
					Toast.makeText(getApplicationContext(), "������������", 1000)
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
					Toast.makeText(getApplicationContext(), "����ע��", 1000)
							.show();
				}
				if (!Util.isNetworkConnected(getApplicationContext())) {
					Toast.makeText(getApplicationContext(), "������������", 1000)
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
		 * �����б�
		 */
		// ��ӳ���������鿴����
		lv.setOnCreateContextMenuListener(myListener2);
	}

	private OnCreateContextMenuListener myListener2 = new OnCreateContextMenuListener() {

		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "����");
			menu.add(0, 1, 1, "����");
			menu.add(0, 2, 2, "ɾ��");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ContextMenuInfo info = item.getMenuInfo();
		AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
		// ��ȡѡ����λ��
		int position = contextMenuInfo.position;
		HashMap<String, Object> itemd = (HashMap<String, Object>) listItem
				.get(position);
		if (item.getItemId() == 0) {// ����
			name = (String) itemd.get("ItemName");// ��������
			System.out.println("item_no" + name);
			dialog("����");
		}
		if (item.getItemId() == 1) {// ����
			item_no = (String) itemd.get("item_no");// ��������
			System.out.println("item_no" + item_no);
			dialog("����");
		}
		if (item.getItemId() == 2) {// ���
			name = (String) itemd.get("ItemName");// ��������
			System.out.println("item_no" + name);
			dialog("ɾ��");
		}
		return super.onContextItemSelected(item);
	}
	// ɾ��
	protected void dialog(final String string) {
		Builder builder = new Builder(ShoppingCarActivity.this);
		builder.setMessage("ȷ��" + string + "��");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("ɾ���ɹ�");
				if (string.equals("����")) {
					deleteGoods dGoods = new deleteGoods();
					dGoods.execute("false");
				}
				if (string.equals("����")) {
					QingQiuAsyntask qingQiuAsyntask = new QingQiuAsyntask();
					qingQiuAsyntask.execute(item_no);
				}
				if (string.equals("ɾ��")) {
					deleteGoods dGoods = new deleteGoods();
					dGoods.execute("true");
				}

			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * ɾ������Ʒ
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
				System.out.println("ɾ���ɹ�" + url);
				result = HttpUtil.queryStringForPost(url);
				System.out.println("���" + result);
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
				Toast.makeText(context, "�ɹ�", 1000).show();
				setView();
			} else {
				Toast.makeText(context, "����������", 1000).show();
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
		System.out.println("�ɹ�" + requestCode + resultCode);

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				if (intent.getStringExtra("result").equals("ok")) {
					// ��ӳɹ� ʵ���� �Ǿ�̬�ڲ���
					GwcListAsytask gwcListAsytask = new GwcListAsytask();
					gwcListAsytask.execute("false");
				} else {
					Toast.makeText(getApplicationContext(), "��ӹ��ﳵʧ��", 1000)
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
		gwcListAsytask.execute("false");// δ�����
		/* setListView(); */
	}

	private void setListView() {
		sumprice.setText("�ϼƣ�" + String.valueOf(sum) + "Ԫ");
		SimpleAdapter adapter = new SimpleAdapter(this, listItem,
				R.layout.item, new String[] { "ItemName", "Sale_price" },
				new int[] { R.id.name, R.id.howmany });
		lv.setAdapter(adapter);
	}

	/**
	 * ����Ʒ���빺�ﳵ
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
				Toast.makeText(getApplicationContext(), "��ӳɹ�", 1000).show();
			} else {
				Toast.makeText(getApplicationContext(), "����������", 1000).show();
			}
		}

	}

}
