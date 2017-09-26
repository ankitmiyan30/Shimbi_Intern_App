//Copyright � 2014 All Rights Reserved With ShimBi Computing Laboratories Pvt. Ltd.
//for checking Internet connection
package com.shimbi.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class CheckConnectivity
{
	ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    
    public Boolean checkNow(Context con){
    	
    	try{
    	ConnectivityManager connectivity = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivity != null) 
		  {
			  NetworkInfo[] info = connectivity.getAllNetworkInfo();
			  if (info != null) 
				  for (int i = 0; i < info.length; i++) 
					  if (info[i].getState() == NetworkInfo.State.CONNECTED)
					  {
						  return true;
					  }
		  }
    	}
	        catch(Exception e){
	        //  Log.v("ChcekConnectivity","CheckConnectivity Exception !!! " + e.getMessage());
	        }
		  return false;
    }   
}