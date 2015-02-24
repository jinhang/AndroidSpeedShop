package com.mc.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListViewAdapter extends SimpleAdapter {

	private Context context;                        //运行上下文   
    private List<Map<String, Object>> listItems;    //商品信息集合   
    private LayoutInflater listContainer;           //视图容器   
    public final class ListItemView{                //自定义控件集合     
        public TextView name;     //商品名
        public TextView count;   //个数
 }     
	public ListViewAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}

}
