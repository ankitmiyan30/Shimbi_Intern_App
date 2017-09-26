//Copyright � 2014 All Rights Reserved With ShimBi Computing Laboratories Pvt. Ltd.
/*
 * this class is implement to handle session of client 
 * In which  sharedPreference are used:
 *  login_9 preference : four key are used :  KEY_email_id,KEY_password,KEY_stay_sign_in,KEY_logout_status; according to algorithm
 *  method are used in format : 1.insert_into_name of shared Preference_name of key or name of operation
 *  							eg: insert_into_login_ stay_sign_in;
 *  							2.get_nameofkey
 *   							eg: get_stay_sign_in_status
 */

package com.shimbi.library;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyBilling_sessionManager {
	// Shared Preferences
		 public static SharedPreferences application_id_Preferce,login_perference,link_preference ;
		 public static Editor editor_appication_id_Preferce,editor_login,editor_link;
		// Context
		Context _context;
		// Shared pref mode
		int PRIVATE_MODE = 0;
		//Key to acess id
		public static final String KEY_Application_id = "app_id";
		public static final String KEY_email_id = "email_id";
		public static final String KEY_password = "password";
		public static final String KEY_stay_sign_in = "stay_sign_in";
		public static final String KEY_logout_status = "logout_status";
		public static final String KEY_Admin_link = "Admin";
		public static final String Preferce_Name_Application_id = "Application_id";
		public static final String Preferce_Name_Login = "Login";
		public static final String Preferce_Name_Link = "Link";
		static Database domain_db;
		
		// Constructor
		public  MyBilling_sessionManager(Context context){
			// TODO Auto-generated constructor stub
		
			this._context = context;
			domain_db=new Database(_context,"MYDOMAIN",null,1);
			application_id_Preferce = _context.getSharedPreferences(Preferce_Name_Application_id, PRIVATE_MODE);
			editor_appication_id_Preferce=application_id_Preferce.edit();
			login_perference= _context.getSharedPreferences(Preferce_Name_Login, PRIVATE_MODE);
			editor_login=login_perference.edit();
			link_preference= _context.getSharedPreferences(Preferce_Name_Link, PRIVATE_MODE);
			editor_link=link_preference.edit();
			
		}
		public void insert_into_link(String admin_name)
		{
			domain_db.insert(admin_name);
		}
		public void insert_into_application_id(String application_id)
		{
			editor_appication_id_Preferce.putString(KEY_Application_id, application_id);
			editor_appication_id_Preferce.commit();
		}
		public void insert_into_login(String email,String password)
		{
			editor_login.putString(KEY_email_id, email);
			editor_login.putString(KEY_password, password);
			editor_login.commit();
		}
		public void insert_into_login_stay_sign(boolean stay_sign_in)
		{
			editor_login.putBoolean(KEY_stay_sign_in, stay_sign_in);
			editor_login.commit();
		}
		public void insert_into_login_logout_status(boolean logout_status)
		{
			editor_login.putBoolean(KEY_logout_status, logout_status);
			editor_login.commit();
		}
		public HashMap<String, String> getUserDetails(){
			HashMap<String, String> user = new HashMap<String, String>();

			// user email id
			user.put(KEY_email_id, login_perference.getString(KEY_email_id, null));
			// user paswword
			user.put(KEY_password, login_perference.getString(KEY_password, null));
			//is remember me checked

			return user;
		}
		public  static  String get_Admin_link()
		{
			String result;
		
			result=domain_db.getname();
			domain_db.close();
			return result;
		}
		public boolean get_Stay_sign_status()
		{
			boolean result;
			result=login_perference.getBoolean(KEY_stay_sign_in, false);
			return result;
		}
		public boolean get_Logout_status()
		{
			boolean result;
			result=login_perference.getBoolean(KEY_logout_status, true);
			return result;
		}
		
		 
}
