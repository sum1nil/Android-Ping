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
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.*;
import java.io.*;
import java.util.concurrent.*;
import android.net.*;
import android.net.wifi.*;
import java.math.*;
final class LoadIpsTask extends AsyncTask<String, Integer, List<InetAddress>> { 
		private Context context;
		private ConnectivityManager connManager;
		private NetworkInterface nif;
		private String ip1 = null;
		private String ip2 = null;
		private List<InetAddress> addresses = new ArrayList<InetAddress>();
		private int[] strtIpAddress = new int[4];
		private int[] endIpAddress = new int[4];
		private ProgressDialog progressBar; 
		private int progressBarStatus = 0;
		private int ipsToScan = 0;
		
		public LoadIpsTask(Context context) {
				this.context = context;
				this.connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		
@Override
protected List<InetAddress> doInBackground(String... params) { 
		
		
		ip1 = params[0];
		ip2 = params[1];
		String[] octets1 = null;
		String[] octets2 = null;
		
		if(ip2 != null) {
				// Create corresponding array of entries and load with ip adresses
				octets1 = ip1.split("\\.");
				octets2 = ip2.split("\\.");
				for(int j = 0; j < octets2.length; j++) 
					endIpAddress[j] = Integer.parseInt(octets2[j]);
				for(int j = 0; j < octets1.length; j++) 
					strtIpAddress[j] = Integer.parseInt(octets1[j]);
			}

		if (octets2 != null) { //multiple IPs: ipRange = 172.31.229.240-172.31.229.250 

				int lowerBound = strtIpAddress[3];
				int upperBound = endIpAddress[3];
				ipsToScan = upperBound - lowerBound;

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
		List<IpInfo> ipInfos = new ArrayList<IpInfo>();
		
		for(InetAddress in : addresses) {
						try
						{
								if (in.isReachable(500))
								{ 
										progressBarStatus += 100/ipsToScan;
										publishProgress(progressBarStatus);
										String ipAddress = getIpAddress(in.getAddress());
										ipInfos.add(new IpInfo(ipAddress, in.getCanonicalHostName(),
																					 "Responded OK.", 0, 0));
								}
								else
								{ 
										String ipAddress = getIpAddress(in.getAddress());
										ipInfos.add(new IpInfo(ipAddress, in.getCanonicalHostName(),
																					 "No response: Time out", 0, 0));
								}
						}
						catch (IOException e)
						{}
				
				 
		}
		PingSweepActivity.setIpInfoList(ipInfos);
		return addresses;
		
} 
@Override 
protected void onPostExecute(List<InetAddress> result) { 
		progressBar.dismiss();
		Toast.makeText(context, "IP list size: " + result.size(), Toast.LENGTH_LONG).show();
		Toast.makeText(context, "IP info list size: " + PingSweepActivity.getipInfoList().size(), Toast.LENGTH_LONG).show();
	} 
@Override 
protected void onPreExecute() {
	/*
		NetworkInfo info = connManager.getActiveNetworkInfo(); 
		int netType = info.getType(); 
		int netSubtype = info.getSubtype(); 
		if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_WIMAX) { 
		//no restrictions, do some networking 
				WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				int ipAddress = wifiInfo.getIpAddress();
				byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
				try
				{
						InetAddress addr = InetAddress.getByAddress(bytes);
						nif = NetworkInterface.getByInetAddress(addr);
				}
				catch (UnknownHostException e)
				{}
				catch (SocketException e)
				{}
				Log.e("MyTemp", nif.getDisplayName());
		} */
		
		// prepare for a progress bar dialog 
		progressBar = new ProgressDialog(context); 
		progressBar.setCancelable(true); 
		progressBar.setMessage("Pinging IPs ..."); 
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		progressBar.setProgress(0); 
		progressBar.setMax(100); 
		progressBar.show();
	} 
@Override protected void onProgressUpdate(Integer... values) { 
		progressBar.setProgress(values[0]);
	} 

	
		/** * Convert raw IP address to string. 
		* * @param rawBytes raw IP address. 
		* @return a string representation of the raw ip address. */
		public String getIpAddress(byte[] rawBytes) { 
		int i = 4; 
		String ipAddress = ""; 
		for (byte raw : rawBytes) { 
			ipAddress += (raw & 0xFF); 
			if (--i > 0) { ipAddress += "."; }
		} 
		
		return ipAddress; 
	}
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
				//pingButton.setEnabled(false);
				pingButton.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v) {
									Toast.makeText(getApplication(),"Ping button clicked.", Toast.LENGTH_SHORT).show();
									LoadIpsTask task = new LoadIpsTask(v.getContext());
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
											Toast.makeText(this, ips[0], Toast.LENGTH_SHORT).show();
											//pingButton.setEnabled(true);
											endIpText.requestFocus();
									 		break;
										case R.id.ending_ip_address:
											ips[1] = endIpText.getText().toString();
											Toast.makeText(this, ips[1], Toast.LENGTH_SHORT).show();
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
 
