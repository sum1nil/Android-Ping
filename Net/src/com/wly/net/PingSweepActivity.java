package com.wly.net;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.regex.*;
import android.text.*;
import android.util.*;
import android.view.inputmethod.*;
import android.widget.TextView.*;
import java.util.*;
import java.net.*;
import android.support.v4.app.*;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.*;
import java.io.*;
import java.util.concurrent.*;
final class LoadIpsTask extends AsyncTask<String, Void, List<InetAddress>> { 

		private String ip1 = null;
		private String ip2 = null;
		private List<InetAddress> addresses = new ArrayList<InetAddress>();
		private int[] strtIpAddress = new int[4];
		private int[] endIpAddress = new int[4];
		
@Override
protected List<InetAddress> doInBackground(String... params) { 
		ip1 = params[0];
		ip2 = params[1];
		// Create corresponding array of entries and load with ip adresses
		String[] octets1 = {"0","0","0","0"};
		String[] octets2 = {"0","0","0","0"};
		octets1 = ip1.split("\\.");
		if(ip2 != null) {
			octets2 = ip2.split("\\.");
			for(int j = 0; j < octets2.length; j++) 
				endIpAddress[j] = Integer.parseInt(octets2[j]);
		}
				
		for(int j = 0; j < octets1.length; j++) 
		strtIpAddress[j] = Integer.parseInt(octets1[j]);
	

		if (octets2.length != 0) { //multiple IPs: ipRange = 172.31.229.240-172.31.229.250 

				int lowerBound = strtIpAddress[3];
				int upperBound = endIpAddress[3];

				for (int i = lowerBound; i <= upperBound; i++) {
						String ip = strtIpAddress[0] + "." + strtIpAddress[1] + "." + strtIpAddress[2] + "." + i;
						try
						{
								addresses.add(InetAddress.getByName(ip));
						}
						catch (UnknownHostException e)
						{ e.printStackTrace(); }
				}
		}
		else { //single ip: ipRange = 172.31.229.240
			 String ip = strtIpAddress[0] + "." + strtIpAddress[1] + strtIpAddress[2] + "." + strtIpAddress[3];
				try
				{
						addresses.add(InetAddress.getByName(ip));
				}
				catch (UnknownHostException e)
				{ e.printStackTrace(); }
		}
		return addresses;
		
} 
@Override 
protected void onPostExecute(List<InetAddress> result) { 
			List<IpInfo> ipInfos = new ArrayList<IpInfo>();
			int num = 0;
			StringBuilder sb = new StringBuilder();
			for(InetAddress in : result) {
					try
					{
							if (in.isReachable(5000))
							{ 
									byte[] ip = in.getAddress();
									for(int i = 0; i < ip.length; i++) {
										sb.append(Byte.toString(ip[i]));
										sb.append(".");
										}
									ipInfos.add(new IpInfo(sb.substring(0, sb.length() - 1), in.getCanonicalHostName(),
									"Responded OK.", 0,0));
							}
							else
							{ 
									byte[] ip = in.getAddress();
									for(int i = 0; i < ip.length; i++) {
											sb.append(Byte.toString(ip[i]));
											sb.append(".");
									}
									ipInfos.add(new IpInfo(sb.substring(0, sb.length() - 1), in.getCanonicalHostName(),
																				 "No response: Time out", 0,0));
							}
					}
					catch (IOException e)
					{  e.printStackTrace(); } 
			}
	} 
@Override 
protected void onPreExecute() { } 
@Override protected void onProgressUpdate(Void... values) { } 
}
		
public class PingSweepActivity extends FragmentActivity implements OnEditorActionListener,  IpListFragment.OnIpSelectedListener
{
		private final int range = 255;
	  private final  String TAG = "PingSweepActivity";
    private EditText strtIpText = null;
    private EditText endIpText = null;
		private  Button pingButton = null;
	  private String[] ips = new String[2];
		private static List<InetAddress> ipList = new ArrayList<InetAddress>();
	  public static List<InetAddress> getIpList() { return ipList;	}
		private static List<IpInfo> ipInfoList = new ArrayList<IpInfo>();
		public static void setIpInfoList(List<IpInfo> info) {ipInfoList = info;}
		public static List<IpInfo> getipInfoList() { return ipInfoList; }
		
    @Override
    public void onCreate(Bundle savedInstanceState)
		{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_main);
				
				pingButton = (Button) findViewById(R.id.ping_button);
				pingButton.setEnabled(false);
				pingButton.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v) {
									Toast.makeText(getApplication(),"Ping button clicked.", Toast.LENGTH_SHORT).show();
									LoadIpsTask task = new LoadIpsTask();
									task.execute(ips[0],ips[1]);
								try
								{
										ipList = task.get();
								}
								catch (ExecutionException e)
								{ e.printStackTrace(); }
								catch (InterruptedException e)
								{ e.printStackTrace(); }
								IpListFragment.setAdapterData(ipList);
								updateAdapter();
						}
				});

				strtIpText = (EditText) findViewById(R.id.starting_ip_address);
				endIpText = (EditText) findViewById(R.id.ending_ip_address); 

				InputFilter[] filters = new InputFilter[1];
				filters[0] = new InputFilter() { 
						public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
								if (end > start) {
										String destTxt = dest.toString();
										String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
										if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) { 
												return "";
										} else {
												String[] splits = resultingTxt.split("\\.");
												for (int i=0; i<splits.length; i++) {
														if (Integer.valueOf(splits[i]) > range) {
																return "";
														}
													
												}
										}
								}
								return null;
						}
				};
				strtIpText.setFilters(filters);
				strtIpText.setOnEditorActionListener(this);
				endIpText.setFilters(filters);
				endIpText.setOnEditorActionListener(this);

				// Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            IpListFragment firstFragment = new IpListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
						.add(R.id.fragment_container, firstFragment).commit();
				}

		}
    	@Override
    	public void onResume() {
    		super.onResume();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(strtIpText, InputMethodManager.SHOW_IMPLICIT);
    		
    	}
		// Check whole ip address:
		private boolean validateIp(final String ip) { 
				Log.i(TAG, " Validating: " + ip);
				boolean result = false;
				result = Patterns.IP_ADDRESS.matcher(ip).matches();
			  return result;
		}

		public void onIpInfoSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        IpInfoFragment ipInfoFrag = (IpInfoFragment)
						getSupportFragmentManager().findFragmentById(R.id.ip_info_fragment);

        if (ipInfoFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            ipInfoFrag.updateIpInfoView((position));

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            IpInfoFragment newFragment = new IpInfoFragment();
            Bundle args = new Bundle();
            args.putInt(IpInfoFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
						// Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            // Commit the transaction
            getSupportFragmentManager().beginTransaction().
						replace(R.id.fragment_container, newFragment).addToBackStack(null).commit();

            
            
        }
		}

		@Override 
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
				if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) { 
								// the user is done typing. 
								if(validateIp(((EditText)v).getText().toString())) {
								int viewId =  v.getId();
								switch (viewId) {
										case R.id.starting_ip_address:
										  ips[0] = strtIpText.getText().toString();
											pingButton.setEnabled(true);
											endIpText.requestFocus();
									 		break;
										case R.id.ending_ip_address:
											ips[1] = endIpText.getText().toString();
										break;
									}
									return true; // consume
						} 
			 }
				return false; // pass on to other listeners. 
		}
		
		private void updateAdapter() { 
		IpListAdapter la = (IpListAdapter) ((IpListFragment) getSupportFragmentManager().findFragmentById(R.id.ip_list_fragment)).getListAdapter(); 
		la.notifyDataSetInvalidated();
		la.notifyDataSetChanged(); }

}
 
