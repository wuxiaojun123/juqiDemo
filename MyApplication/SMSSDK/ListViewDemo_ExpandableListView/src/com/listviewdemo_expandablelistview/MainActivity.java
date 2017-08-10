package com.listviewdemo_expandablelistview;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class MainActivity extends Activity {
	private ExpandableListView listView;
	private List<String> group;
	private List<List<String>> child;
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ExpandableListView) findViewById(R.id.expandableListView);
		/**
		 * ��ʼ������ 
		 */
		initData();
		adapter = new MyAdapter(this,group,child);
		listView.setAdapter(adapter);
		
	}

	private void initData() {
		group = new ArrayList<String>();
		child = new ArrayList<List<String>>();
		addInfo("����",new String[]{"����","����","������","������"});
		addInfo("�ӱ�", new String[]{"����","ʯ��ׯ","��̨"});
		addInfo("�㶫", new String[]{"����","����","�麣"});
	}
	
	/**
	 * ���������Ϣ
	 * @param g
	 * @param c
	 */
	private void addInfo(String g,String[] c) {
		group.add(g);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			list.add(c[i]);
		}
		child.add(list);
	}

}
