package com.mc.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListViewAdapter extends SimpleAdapter {

	private Context context;                        //����������   
    private List<Map<String, Object>> listItems;    //��Ʒ��Ϣ����   
    private LayoutInflater listContainer;           //��ͼ����   
    public final class ListItemView{                //�Զ���ؼ�����     
        public TextView name;     //��Ʒ��
        public TextView count;   //����
 }     
	public ListViewAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}

}
