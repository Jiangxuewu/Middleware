package com.j.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.j.R;

public class TextAdapter extends BaseAdapter {
	Context context;
	List<String> list;

	public TextAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
	}

	public void update(List<String> list) {
		if (null != this.list) {
			this.list = null;
		}
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return null == list ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return null == list ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_text, null);
			viewHolder = new ViewHolder();
			viewHolder.tView = (TextView) convertView
					.findViewById(R.id.textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tView.setText(list.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView tView;
	}

}
