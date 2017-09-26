//Copyright � 2014 All Rights Reserved With ShimBi Computing Laboratories Pvt. Ltd.
/*
 * this class is implement  flash screen which appears for few seconds  

 */

package com.shimbi.billing;



import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.shimbi.library.CheckConnectivity;
import com.shimbi.library.MyBilling_sessionManager;
import com.shimbi.library.MyBilling_user_function;

public class Splash_screen extends Activity implements AnimationListener{
	  private static int SPLASH_TIME_OUT = 3000;
	  CheckConnectivity connectivity;
	  MyBilling_sessionManager sessionManager;
	  MyBilling_user_function user_function;
	  Login login;
	  Opration_details opration_details;
	  String user_id,password;
	  boolean ch,stay_sign_in, logout_status;
	  ProgressBar progressBar;
	  ImageView imageView;
	  Animation animZoomIn;
	  int error;
	  String stresult,str_user_id,str_pwd,Client_Id,Client_Name;
	  private String[] errorlist ={ 
	 			"Unable to connect Server",
	 			"Unable to connect DB",
	 			"Authentication Failed"
				};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        progressBar=(ProgressBar)findViewById(R.id.Splash_progressBar1);
         connectivity = new CheckConnectivity();
         opration_details = new Opration_details();
         user_function = new MyBilling_user_function();
         imageView =(ImageView)findViewById(R.id.imageView1_splash_screen_logo);
         //animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
         imageView.setVisibility(View.VISIBLE);
         //animZoomIn.setAnimationListener(this);
         //imageView.startAnimation(animZoomIn);
         login = new Login();

		/*new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
		*/
         sessionManager=new MyBilling_sessionManager(getApplicationContext());
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		  ch = connectivity.checkNow(this.getApplicationContext());
	        if(ch==true)
	  	  {
	        		stay_sign_in=sessionManager.get_Stay_sign_status();
	        	
	        		if(stay_sign_in)
	        		{
	        		    logout_status=sessionManager.get_Logout_status();
	   
	        			if(logout_status)
	        			{
	        				HashMap<String, String>  session_data = sessionManager.getUserDetails();
	        	        	user_id = session_data.get(MyBilling_sessionManager.KEY_email_id);
	        	        	password = session_data.get(MyBilling_sessionManager.KEY_password);
	        	       	
	        	        	new Checklogin(true, true).execute();
	        			}
	        			else
	        			{
	        			 	new Checklogin(true,false).execute();
	        			}	
	        		}
	        		else {
	        		 	new Checklogin(false, false).execute();
	        			
					}
	  	 }
	    	  else
	    	  {
	    		  Toast.makeText(getApplicationContext(),"Check Internet Connection !!!",Toast.LENGTH_LONG).show();
	    		  finish();
	    	  }
	  }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
        return true;
    }
    
    private class Checklogin extends AsyncTask<Void, Integer, Void>
	{
    	boolean stay_sign,log_status;
    	int progress=0;
		public Checklogin(boolean stay_sign_in, boolean logout_status) {
			// TODO Auto-generated constructor stub
			stay_sign=stay_sign_in;
			log_status=logout_status;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		        progressBar.setIndeterminate(false);
		        progressBar.setMax(300);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			progressBar.setProgress(values[0]);
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(stay_sign)
			{
				if(log_status)
				{	
					stresult=user_function.validateLogin(user_id, password);
					int len=stresult.length();
					int total=1;
						for(int i=0;i<len;i++)
						{	
							total=total+1;
							publishProgress((int)((total*100)/len));
						}	
						
					if(stresult.equals("") || stresult == null)
					{
						error = -1;
					}
					else if(stresult.equals("-1") || stresult.equals("-2") || stresult.equals("-3") )
					{
						error = Integer.parseInt(stresult);
					}
					else 
					{
						try {
								JSONArray jArray = new JSONArray(stresult);
								JSONObject jsobj = jArray.getJSONObject(0);
								String role=login.User_Role=jsobj.getString("Role");
								if(role.equalsIgnoreCase("C"))
								{	
									login.Client_Id= jsobj.getString("ClientID");
								
									login.Client_Name = jsobj.getString("ClientName");
									login.Client_code=jsobj.getString("Code");
									login.Client_image_path=jsobj.getString("Image_path");
									Login.user_logo=user_function.DownloadImageFromPath(login.Client_image_path);
								}
								else
								{
									login.Client_Id= jsobj.getString("AdminID");
									login.Client_Name = jsobj.getString("AdminName");
								}
							} 
						catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							}
					}
				}// end of log_status if
				else
				{
					int len=5000;
					int total=1;
						for(int i=0;i<len;i++)
						{	
							total=total+1;
							publishProgress((int)((total*50)/len));
						}
				}//end of else of logout status
			}// end of stay_sign if	
			else
			{
				int len=5000;
				int total=1;
					for(int i=0;i<len;i++)
					{	
						total=total+1;
						publishProgress((int)((total*50)/len));
					}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(stay_sign)
			{
				if(log_status)
				{	
						if( error < 0 )
						{
							error = (error*(-1))-1;
							Toast.makeText(getApplicationContext(),errorlist[error],Toast.LENGTH_LONG).show();
							Intent i  = new Intent(getApplicationContext(), Login.class);
							i.putExtra("Stay_sign_in", true);
							startActivity(i);
						}
						else
						{
							Intent i=new Intent(getApplicationContext(), Home.class);
							i.putExtra("Stay_sign_in", true);
							startActivity(i);
						}
				}//end of log status if	
				else
				{
					Intent i=new Intent(getApplicationContext(),Login.class);
					i.putExtra("Stay_sign_in", true);
					startActivity(i);
				}
			}//end of stay sign in status
			else
			{
				Intent i=new Intent(getApplicationContext(), Login.class);
				i.putExtra("Stay_sign_in", false);
				startActivity(i);
			}
		}// end of method
	} // end of async task class

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == animZoomIn) {	
			// imageView.startAnimation(aimZoomOut);
		}
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}
}//end of class


