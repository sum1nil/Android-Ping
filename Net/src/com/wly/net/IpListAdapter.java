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
import android.text.style.*;

/** * @author sum1nil * */

public class IpListAdapter extends ArrayAdapter<InetAddress>{ 
private Context context; 
private int layoutResourceId; 
//private ArrayList<InetAddress> listData = null;

		@Override 
		public void notifyDataSetChanged() { 
			super.notifyDataSetChanged();
			Toast.makeText(this.getContext(), "Data set has changed!", Toast.LENGTH_SHORT).show();
			
			}

		public void setData(ArrayList<InetAddress> data) {
        clear();
        if (data != null) {
            for (InetAddress address : data) {
                add(address);
            }
        }
    }
		public IpListAdapter(Context context, int resId, ArrayList<InetAddress> items) { 
		super(context, resId, items); 
		this.context = context; 
		this.layoutResourceId = resId;
//		this.listData = items; 
		setNotifyOnChange(true);
		}

		public IpListAdapter(Context context, int layoutResId) {
			super(context, layoutResId); 
			this.context = context;
			this.layoutResourceId = layoutResId;
//			this.listData = new ArrayList<InetAddress>();
			setNotifyOnChange(true);
		}
		
		@Override 
		public View getView(int position, View convertView, ViewGroup parent) { 
		super.getView(position, convertView, parent);
		Log.v("ConvertView  #", String.valueOf(position)); 
		View row = convertView; 
		ListHolder holder = null;

				if(row == null) {
						LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						row = li.inflate(layoutResourceId, parent, false); 
						holder = new ListHolder(); 
						holder.address= (TextView) row.findViewById(R.id.ip_address);
						holder.address.setText(getItem(position).getHostAddress());
						holder.host = (TextView) row.findViewById(R.id.host_name);
						holder.host.setText(getItem(position).getHostName());

						row.setTag(holder);

				} else { 
				holder = (ListHolder)row.getTag(); 
				/*
				holder.address = (TextView) row.findViewById(android.R.id.text1); 
				holder.address.setText(listData.get(position).name);
					*/
				}
				Toast.makeText(context, row.toString(), Toast.LENGTH_SHORT).show();
				return row;

		}

		@Override 
		public int getCount() { 
		// TODO Auto-generated method stub 
		
		// Toast.makeText(context,"Adapter count is " + listData.size(), Toast.LENGTH_SHORT).show();
		return super.getCount(); }

		@Override 
		public long getItemId(int index) { 
		// TODO Auto-generated method stub 
		byte[] ba = getItem(index).getAddress();
		StringBuilder sb = new StringBuilder();
		for(byte b : ba)
			sb.append(Byte.toString(b));
			
		return	Long.getLong(sb.toString());
		
		}

		public class ListHolder { 
		public ListHolder() { 
		// TODO Auto-generated constructor stub
		}
		TextView address;
		TextView host;
	}
}

