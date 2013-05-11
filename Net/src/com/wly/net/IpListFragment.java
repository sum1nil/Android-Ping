package com.wly.net;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.*;
import java.net.*;;  

public class IpListFragment extends ListFragment
{
		OnIpSelectedListener mCallback;
		IpListAdapter la = null;		

				// The container Activity must implement this interface so the frag can deliver messages
				public interface OnIpSelectedListener {
						/** Called by HeadlinesFragment when a list item is selected */
						public void onIpInfoSelected(int position);
				}

				@Override
				public void onCreate(Bundle savedInstanceState) {
						super.onCreate(savedInstanceState);

						// We need to use a different list item layout for devices older than Honeycomb
						int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

						// Create an array adapter for the list view, using the Ipsum headlines array
						la = new IpListAdapter(this.getView().getContext(), layout, android.R.id.text1, (ArrayList<InetAddress>)PingSweepActivity.getIpList());
						setListAdapter(la);
				}

				@Override
				public void onStart() {
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
}
