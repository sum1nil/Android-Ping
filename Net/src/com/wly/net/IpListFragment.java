package com.wly.net;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.*;
import java.net.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;;  

public class IpListFragment extends ListFragment
{
		OnIpSelectedListener mCallback;
		
		ListView lv = null;
		static IpListAdapter la = null;		
		int num = 1;
		
				// The container Activity must implement this interface so the frag can deliver messages
				public interface OnIpSelectedListener {
						/** Called by HeadlinesFragment when a list item is selected */
						public void onIpInfoSelected(int position);
				}

				@Override
				public void onCreate(Bundle savedInstanceState) {
						super.onCreate(savedInstanceState);
						System.out.println("IpListFragment.onCreate");

						// We need to use a different list item layout for devices older than Honeycomb
						int layout = R.layout.ip_list_view;
            // Initially there is no data
            //setEmptyText("No Data Here");

            // Create an empty adapter we will use to display the loaded data.
						// An array adapter for the list view
						la = new IpListAdapter(getActivity(), layout);//, (ArrayList<InetAddress>)PingSweepActivity.getIpList());
						setListAdapter(la);

            // Start out with a progress indicator.
            //setListShown(false);
						
					//	Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
             //   android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

				}
				
				
				@Override
				public void onStart()  {
						super.onStart();

						// When in two-pane layout, set the listview to highlight the selected list item
						// (We do this during onStart because at the point the listview is available.)
						if (getFragmentManager().findFragmentById(R.id.ip_info_fragment) != null) {
								getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
						}
				}

				@Override
				public void onAttach(Activity activity) {
						super.onAttach(activity);

						// This makes sure that the container activity has implemented
						// the callback interface. If not, it throws an exception.
						try {
								mCallback = (OnIpSelectedListener) activity;
						} catch (ClassCastException e) {
								throw new ClassCastException(activity.toString()
																						 + " must implement OnHeadlineSelectedListener");
						}
				}

				@Override
				public void onListItemClick(ListView l, View v, int position, long id) {
						// Notify the parent activity of selected item
						mCallback.onIpInfoSelected(position);

						// Set the item as checked to be highlighted when in two-pane layout
						getListView().setItemChecked(position, true);
				}
				
				public static void setAdapterData(List<InetAddress> data) {
						la.setData((ArrayList<InetAddress>)data);
				}
}
