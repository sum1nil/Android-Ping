package com.wly.net;
import android.content.*;
import java.util.*;
import java.net.*;
import java.io.*;
import android.os.Environment;
import android.widget.*;
import android.util.*;
 
public class FileManager { 
		private static final String TAG = "FileManager";
		private static final String file = "IpData.ser";
		
		private final static File NET_TOOLS_PATH = new File(Environment.getExternalStorageDirectory() + "/NetTools"); 
		public static File getNetToolsPath() { return NET_TOOLS_PATH; } 
		private final static File TEMP_PATH = new File(NET_TOOLS_PATH + "/Temp"); 
		public static File getTempPath() { return TEMP_PATH; }
		
		
public static void saveData(List<InetAddress> data) { 
		// Serialize data object to a file
		ObjectOutputStream out = null;
		
		try
				{
						out = new ObjectOutputStream(new FileOutputStream(TEMP_PATH.getAbsolutePath()  + "/" + file));
						 
						for (InetAddress address : data) 
								out.writeObject(address);
								
						out.writeObject(null);
						out.close();
				}
				catch (IOException e)
				{e.printStackTrace();}
} 
	
	public static ArrayList<InetAddress> restoreData() { 
				ArrayList<InetAddress> result = new ArrayList<InetAddress>();
				Object o = new Object();
			try{
					FileInputStream fis = new FileInputStream(TEMP_PATH.getAbsolutePath() + "/" + file);
					ObjectInputStream reader = new ObjectInputStream(fis);
					while( (o = reader.readObject()) != null) {
						InetAddress address = (InetAddress)o;
						result.add(address);
					}
					reader.close();
					}
					catch (IOException e)
					{ e.printStackTrace(); }
					catch (ClassNotFoundException e)
					{ e.printStackTrace(); }
					
			return result;
		}
		
		
		public static void manageDirectory(boolean create) {
				Log.i(TAG, NET_TOOLS_PATH.getAbsolutePath());
				boolean result = false;
				if(create) {
						
					if(NET_TOOLS_PATH.exists() == false) {
							Log.i(TAG, "Preparing to make Net Tools directory.");
							result = NET_TOOLS_PATH.mkdir();
							Log.i(TAG, "The result is " + String.valueOf(result));	
					}
					if(result)
						Log.i(TAG, "Net Tools directory created.");
					else
						Log.i(TAG, "Net Tools directory not created.");
						
					if(TEMP_PATH.exists()  == false)
							TEMP_PATH.mkdir(); 
				
				}
				
					if(TEMP_PATH.exists() && create == false) {
						File[] files = TEMP_PATH.listFiles();
						for(File file : files)  {
							Log.i(TAG, "Found " + file.getName() + " in " + TEMP_PATH.getAbsolutePath());
							if(file.isFile()) 
									if(!file.delete())
											Log.i(TAG, "Could not delete " + file.getName());
									else
											Log.i(TAG," Sucessfully deleted " + file.getName());
					}
				}
			}
		}
	

