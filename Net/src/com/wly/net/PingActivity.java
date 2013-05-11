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

public class PingActivity extends Activity implements OnEditorActionListener
{
		private final int range = 255;
	  private final  String TAG = "PingActivity";
    private EditText strtIpText = null;
    private EditText endIpText = null;
		private int[] strtIpAddress = new int[4];
		private int[] endIpAddress = new int[4];
		
    @Override
    public void onCreate(Bundle savedInstanceState)
		{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_pane);

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
						Log.i(TAG, ip + " is not a valid ip."); 

				}
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
								
								return true; // consume
						} 
			 return false; // pass on to other listeners. 
		}

}
 
