package com.tg.yc;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.mc.model.TodaySalesPromotionResult;
import com.mc.util.HttpUtil;
import com.tg.yc.ShoppingCarActivity.QingQiuAsyntask;
import com.tg.yc.ShoppingCarActivity.deleteGoods;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ClipData.Item;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TodayCheepActivity extends Activity /*implements OnScrollListener*/ {

	String sizes = "30";// ���մ���ÿ��������ٸ�����
	private ImageView backButton;
	private ListView listView;
	private List<Map<String, Object>> listItem;
	private String currentPage;// ��ǰҳ��
	private String page;// ��ҳ��
	private View moreView; // ���ظ���ҳ��
	private int lastItem;
	private int count;
	private int size;
	TodaySalesPromotionResult todaySalesPromotionResult = null;// Ϊ�˽���JSON
	SimpleAdapter adapter;
	String userName = "18291994584";
 String item_no = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.today_cheep_activity,
				null);
		setContentView(view);
		backButton = (ImageView) findViewById(R.id.back);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		setView();
		

	}

	private void setView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.show);
		Intent intent = getIntent();
		listItem = (List<Map<String, Object>>) intent
				.getSerializableExtra("list");
		count = listItem.size();
		currentPage = intent.getStringExtra("currentPage");
		page = intent.getStringExtra("page");
		size = intent.getIntExtra("size", 0);
		/*System.out.println("ֵ��"+currentPage+" " + page +" "+size);
		moreView = getLayoutInflater().inflate(R.layout.listload, null);
		listView.addFooterView(moreView);// ��ӵײ�view��һ��Ҫ��setAdapter֮ǰ��ӣ�����ᱨ��
*/		setListView();
		
	}

	private void setListView() {
		adapter = new SimpleAdapter(this, listItem, R.layout.cheep_item,
				new String[] { "ItemName", "Sale_price","nowprice" }, new int[] {
						R.id.name, R.id.howmany,R.id.tejia });
		listView.setAdapter(adapter);
//		listView.setOnScrollListener(TodayCheepActivity.this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				// ��ȡѡ����λ��
				HashMap<String, Object> itemd = (HashMap<String, Object>) listItem
						.get(position);
				item_no = (String)itemd.get("item_no");
				dialog("���");
			}
		});
	}
	// ɾ��
		protected void dialog(final String string) {
			Builder builder = new Builder(TodayCheepActivity.this);
			builder.setMessage("ȷ��" + string + "��");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
                    //���
					QingQiuAsyntask qingQiuAsyntask = new QingQiuAsyntask();
					qingQiuAsyntask.execute(item_no);
				}
			});
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
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
					System.out.println("yyyy"+url);
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
	private void redirectTo() {

		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
		finish();
	}

	/*@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1; // ��1����Ϊ������˸�addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		Log.i("TodayCheepActivity", "scrollState=" + scrollState);
		// �����������ǣ������һ��item�����������ݵ�����ʱ�����и���
System.out.println("��ʾ:" +(lastItem == count && scrollState == this.SCROLL_STATE_IDLE));
		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
			Log.i("TodayCheepActivity", "������ײ�");
			moreView.setVisibility(view.VISIBLE);

			// mHandler.sendEmptyMessage(0);
			if (Integer.parseInt(currentPage) < Integer.parseInt(page)) {
				Toast.makeText(getApplicationContext(), "����", 1000).show();
				
				TodaySalesPromotionAsytask todaySalesPromotionAsytask = new TodaySalesPromotionAsytask();
				todaySalesPromotionAsytask.execute("1",
						String.valueOf(Integer.parseInt(currentPage) + 1),
						sizes);
			} else {
				listView.removeFooterView(moreView); // �Ƴ��ײ���ͼ
			}

		}
	}*/

	// �첽����
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
			currentPage = todaySalesPromotionResult.getCurrentPage();// ��ǰҳ��
			// todaySaleListItem = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < todaySalesPromotionResult
					.getTodaySalesPromotions().size(); i++) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("ItemName", todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getGoods());
				hashMap.put("Sale_price", todaySalesPromotionResult
						.getTodaySalesPromotions().get(i).getPrice());
				listItem.add(hashMap);
			}
			/*count = listItem.size();
			adapter.notifyDataSetChanged();
			moreView.setVisibility(View.GONE);
			if (count == size) {// û��������
				Toast.makeText(TodayCheepActivity.this, "ľ�и������ݣ�", 3000).show();
				listView.removeFooterView(moreView); // �Ƴ��ײ���ͼ
			}*/
		}

	}

}
