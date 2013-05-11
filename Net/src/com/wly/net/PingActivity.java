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

public class PingActivity extends Activity implements OnEditorActionListener
{
		private final int range = 255;
	  private final  String TAG = "PingActivity";
    private EditText strtIpText = null;
    private EditText endIpText = null;
		//private OnEditorActionListener editListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
		{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twopanes);

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
														if (Integer.valueOf(splits[i]) > 255) {
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

		// Check whole ip addy:
		private boolean validateIp(final String ip) { 
				Log.i(TAG, " Validating: " + ip);
				boolean result = false;
				result = Patterns.IP_ADDRESS.matcher(ip).matches();
			  return result;
		}



		public void processIp(final String ip)
		{
				if(validateIp(ip)) {	
						// It is IP
						Log.i(TAG, ip + " is a valid ip.");
				}
				else { 
						// It is not IP
						Log.i(TAG, ip + " is not a valid ip."); 

				}
		}

		@Override 
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
				if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE 
						|| event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) { 
						if (!event.isShiftPressed()) { 
								// the user is done typing. 
								processIp(((EditText)v).getText().toString());
								return true; // consume.
						} 
				} return false; // pass on to other listeners. 
		}


}

