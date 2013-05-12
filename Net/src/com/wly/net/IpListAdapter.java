package com.wly.net;

import java.util.ArrayList;
import android.content.Context; 
import android.graphics.Color; 
import android.util.Log; 
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup; 
import android.widget.ArrayAdapter; 
import android.widget.TextView; 
import android.widget.Toast;
import java.net.*;

/** * @author sum1nil * */

public class IpListAdapter extends ArrayAdapter<InetAddress>{ 
private Context context; 
private int layoutResourceId; 
private ArrayList<InetAddress> listData = null;

		@Override public void notifyDataSetChanged() { 
		Toast.makeText(this.getContext(), "data set changed", Toast.LENGTH_SHORT).show();

		}

		public IpListAdapter(Context context, int resId, int text1, ArrayList<InetAddress> items) { 
		super(context, resId, text1, items); 
		this.context = context; 
		this.layoutResourceId = resId; 
		this.listData = items; 
		setNotifyOnChange(true);

		}

		@Override public View getView(int position, View convertView, ViewGroup parent) { 
		super.getView(position, convertView, parent);
		Log.v("ConvertView", String.valueOf(position)); 
		View row = convertView; ListHolder holder = null;

				if(row == null) {
						LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						row = li.inflate(layoutResourceId, null, false); 
						holder = new ListHolder(); 
						holder.address= (TextView) row.findViewById(android.R.id.text1);
						holder.address.setText(listData.get(position).getHostAddress());

						row.setTag(holder);

				} else { 
				holder = (ListHolder)row.getTag(); 
				/*
				holder.address = (TextView) row.findViewById(android.R.id.text1); 
				holder.address.setText(listData.get(position).name);
*/
				}

				return row;

		}

		@Override 
		public int getCount() { 
		// TODO Auto-generated method stub 
		return listData.size(); }

		@Override 
		public long getItemId(int index) { 
		// TODO Auto-generated method stub 
		return listData.get(index).hashCode(); }

		public class ListHolder { 
		public ListHolder() { 
		// TODO Auto-generated constructor stub
		}
		TextView address;
	}
}

