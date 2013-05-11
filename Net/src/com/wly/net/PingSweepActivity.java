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

public class PingSweepActivity extends FragmentActivity implements OnEditorActionListener
{
		private final int range = 255;
	  private final  String TAG = "PingActivity";
    private EditText strtIpText = null;
    private EditText endIpText = null;
		private int[] strtIpAddress = new int[4];
		private int[] endIpAddress = new int[4];
		private static List<InetAddress> ipList = new ArrayList<InetAddress>();
	  public static List<InetAddress> getIpList() {
				return ipList;
		}
		
    @Override
    public void onCreate(Bundle savedInstanceState)
		{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dual_pane);

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

		// Check whole ip address:
		private boolean validateIp(final String ip) { 
				Log.i(TAG, " Validating: " + ip);
				boolean result = false;
				result = Patterns.IP_ADDRESS.matcher(ip).matches();
			  return result;
		}



		public void processIp(final String ip, int id)
		{
			// To Do: Place the 4 tuple numbers into an array. 
				if(validateIp(ip)) {
						String[] values = ip.split("\\.");
						if (strtIpText.getId() == id) {
								Log.i(TAG, ip + " is a valid ip.");
								for(int j = 0; j < values.length; j++) 
										strtIpAddress[j] = Integer.parseInt(values[j]);
						}
						if (endIpText.getId() == id) {
								for(int j = 0; j < values.length; j++) 
										endIpAddress[j] = Integer.parseInt(values[j]);
						}
						
				}
				else { 
						// It is not IP
						Log.i(TAG, ip + " is not a valid ip ");
			}
		}

		private static List<InetAddress> parseIpRange(int[] strtIp, int[] endIp) throws UnknownHostException {
				List<InetAddress> addresses = new ArrayList<InetAddress>();
        if (endIp.length != 0) { //multiple IPs: ipRange = 172.31.229.240-172.31.229.250 
            
            int lowerBound = strtIp[3];
            int upperBound = endIp[3];

            for (int i = lowerBound; i <= upperBound; i++) {
                String ip = strtIp[0] + "." + strtIp[1] + "." + strtIp[2] + "." + i;
                addresses.add(InetAddress.getByName(ip));
            }
        } else { //single ip: ipRange = 172.31.229.240
						String ip = strtIp[0] + "." + strtIp[1] + strtIp[2] + "." + strtIp[3];
            addresses.add(InetAddress.getByName(ip));
        }
        return addresses;
    }
		@Override 
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
				if (actionId == EditorInfo.IME_ACTION_NEXT ) { 
								// the user is done typing. 
								int viewId =  v.getId();
								switch (viewId) {
										case R.id.starting_ip_address:
											endIpText.requestFocus();
									 		break;
										case R.id.ending_ip_address:
											
										break;
									}
								processIp(((EditText)v).getText().toString(),viewId);
						try
						{
								parseIpRange(strtIpAddress, endIpAddress);
						}
						catch (UnknownHostException e)
						{}
						return true; // consume
						} 
			 return false; // pass on to other listeners. 
		}

}
 
