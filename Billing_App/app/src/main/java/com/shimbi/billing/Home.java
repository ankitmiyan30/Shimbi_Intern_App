package com.shimbi.billing;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.shimbi.library.MyBilling_sessionManager;
import com.shimbi.library.MyBilling_user_function;

public class Home extends Activity {
	//declare objects

	MyBilling_user_function user_function;
	MyBilling_sessionManager sessionManager;
	Login login;
	Intent intent;
	static ArrayList<String> opration_list;
	ArrayList<String> result_list;
	ArrayAdapter<String> opration_adapter;
	ArrayList<HashMap<String, String>> invoiceList;
	Popup popup;
	//declare widget
	TextView tv_inovice_due_result,tv_welcome_use,tv_load_text,tv_total_amount_result,tv_invoice_result,tv_receipts_result,tv_total_amount,tv_invoice,tv_receipt;
	ImageView Iv_logout,Iv_menu,Iv_home,iv_header_logo;
	ListView lv_opration,lv_result;
	ProgressDialog dialog;
	//declare variable
	String id,str_result,role,invoice_client;
	public static String pr_login_name,pr_login_id,pr_role;
	public static boolean click_status;
	int type;
	static 
	{
		 Popup.opration_list=new ArrayList<String>();
		 Popup.opration_list.add("Invoice");
	     Popup.opration_list.add("Receipt");
	     Popup.opration_list.add("Estimate");
	     Popup.opration_list.add("Overdue");
	}
	private SharedPreferences sharedpreferences ;
	private SharedPreferences.Editor editor ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newhome);
		init();
		login = new Login();
		iv_header_logo.setImageBitmap(Login.get_user_logo());
		user_function=new MyBilling_user_function();
		sessionManager=new MyBilling_sessionManager(getApplicationContext());
		result_list=new ArrayList<String>();
		invoiceList= new ArrayList<HashMap<String,String>>();

		sharedpreferences = this.getSharedPreferences("myPref", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();

        String Client_Id = sharedpreferences.getString("Client_Id" , null);

		tv_welcome_use.setText("Wel-Come "+login.Client_Name+" !!"); 	
		sessionManager.insert_into_login_logout_status(true);
		new Home_details(Client_Id).execute();

		Iv_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Display display = getWindowManager().getDefaultDisplay();
				int width=(display.getWidth()/3);
				int height=(display.getHeight());
				popup=new Popup(getApplicationContext(), width, height);
				popup.show(arg0);

			}
		});
	    Iv_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
			        .setTitle("Logout")
			        .setMessage("Are You Sure ?").setPositiveButton("Ok",  new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int which) {
			            	sessionManager.insert_into_login_logout_status(false);
							if(login.check)
							{
				
								Intent i=new Intent(getApplicationContext(), Login.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
								
								finish();
								
							}
							else
							{
								MyBilling_sessionManager.editor_login.clear();
								MyBilling_sessionManager.editor_login.commit();
								Intent i=new Intent(getApplicationContext(), Login.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
								finish();
							}		
			               Home.this.finish();
			            }
			        })

			        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            }
			        });
			       builder.show();
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//finish();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}*/
	public void init()
	{
		//new UI
		Iv_home=(ImageView)findViewById(R.id.newheader_iv_home);
		Iv_home=(ImageView)findViewById(R.id.newheader_iv_home);
		Iv_logout=(ImageView)findViewById(R.id.newheader_iv_loogout);
		iv_header_logo=(ImageView)findViewById(R.id.newheader_iv_logo);
		Iv_menu=(ImageView)findViewById(R.id.newheader_iv_menu);
		Iv_home.setVisibility(View.VISIBLE);
		tv_invoice_result=(TextView)findViewById(R.id.new_home_tv_invoice_result);
		tv_receipts_result=(TextView)findViewById(R.id.new_home_tv_receipts_result);
		tv_total_amount=(TextView)findViewById(R.id.new_home_tv_total_amount);
		tv_invoice=(TextView)findViewById(R.id.new_home_tv_invoice_text);
		tv_receipt=(TextView)findViewById(R.id.new_home_tv_receipts_text);
		tv_welcome_use=(TextView)findViewById(R.id.new_home_tv_welcome_user);
		tv_inovice_due_result=(TextView)findViewById(R.id.new_home_tv_Overdue_result);
	}

	class Home_details extends AsyncTask<Void, Void, Void>
	{
		String clientID, stresult;

		public Home_details(String id) {
			// TODO Auto-generated constructor stub
			clientID = id;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(Home.this);
			dialog.setMessage("Loading Home details please wait..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			str_result = user_function.getHomeDetails(clientID);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String[] res = str_result.split("shimbicol");
    		tv_invoice_result.setText(""+Login.Client_code+" "+res[0]);
    		tv_receipts_result.setText(""+Login.Client_code+" "+res[1]);
    		tv_inovice_due_result.setText(""+Login.Client_code+" "+res[2]);
    		tv_total_amount.setText("Total Amount ("+Login.Client_code+") ");
    		if(dialog != null)
    		dialog.dismiss();
		}
		
	}
	 
	
}// end of login_9 class

//#A3FFFFFF