package com.shimbi.billing;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shimbi.library.MyBilling_sessionManager;
import com.shimbi.library.MyBilling_user_function;

public class Login extends Activity {

	// declare objects
	MyBilling_user_function user_function;
	MyBilling_sessionManager sessionManager;
	Intent intent;
	//declare Widgets
	EditText ed_userid,ed_password,ed_admin_link;
	CheckBox ch_remember_me;
	Button btn_log_in,btn_cancel;
	TextView tv_admin_link;
	ImageView iv_header_logo,tv_qestion_mark;
	static Bitmap user_logo;
	ProgressDialog dialog;
	//declare variables
	int error;
	String stresult,str_user_id,str_pwd;
	public static String Client_Id,Client_Name,User_Role,Client_code,admin_link,Client_image_path;
	Boolean stay_sign_in;
	private String[] errorlist ={ 
 			"Unable to connect Server",
 			"Unable to connect DB",
 			"Authentication Failed"
			};
	public static boolean  check;
	private SharedPreferences sharedpreferences ;
	private SharedPreferences.Editor editor ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		 sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
		 editor = sharedpreferences.edit();

		// defining widgets 
		init_component();

		// defining objects
		user_function=new MyBilling_user_function();
		sessionManager=new MyBilling_sessionManager(getApplicationContext());
		intent=getIntent();

		// coding start here
		stay_sign_in=intent.getBooleanExtra("Stay_sign_in", false);
		admin_link=sessionManager.get_Admin_link();

		set_admin_field();
		if(stay_sign_in)
		{
			HashMap<String, String>  session_data = sessionManager.getUserDetails();
        	String email = session_data.get(MyBilling_sessionManager.KEY_email_id);
        	String passwd = session_data.get(MyBilling_sessionManager.KEY_password);

        	ed_userid.setText(email);
        	ed_password.setText(passwd);
		}
		tv_qestion_mark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String first = "http://mybilling.biz/";
				String next = "<font color='#00FF00'>abhi</font>";
				 AlertDialog.Builder builder = new AlertDialog.Builder(Login.this).setTitle("Domain Name : abhi").setMessage(Html.fromHtml(first + next))
						.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int which) {
			            	      

			            }
			        });
				 builder.show();
				}
		});
		btn_log_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String admin_name;
				str_user_id=ed_userid.getText().toString();
				str_pwd=ed_password.getText().toString();
				
				str_user_id = str_user_id.trim();
				str_pwd = str_pwd.trim();
			
				if(str_user_id.equals("") && str_pwd.equals("") )
				{
					if(admin_link==null)
					{
							admin_name=ed_admin_link.getText().toString();
							admin_name = admin_name.trim();

							if(admin_name.equals(""))
							{
								Toast.makeText(getApplicationContext(),"Please Enter User Id and Password and Domain Name", Toast.LENGTH_LONG).show();
							}
					    }
					else
					{
						Toast.makeText(getApplicationContext(), "Please Enter User Id and Password ", Toast.LENGTH_SHORT).show();
					}
				}
				else if(str_user_id.equals(""))
				{	
					if(admin_link==null)
					{
							admin_name=ed_admin_link.getText().toString();
							admin_name = admin_name.trim();
							if(admin_name.equals(""))
							{
								Toast.makeText(getApplicationContext(),"Please Enter User Id and Domain Name", Toast.LENGTH_LONG).show();
							}
					}
					else
					{	
						Toast.makeText(getApplicationContext(), "Please Enter User ID", Toast.LENGTH_SHORT).show();
					}	
				}
				else if(str_pwd.equals(""))
				{
					if(admin_link==null)
					{
							admin_name=ed_admin_link.getText().toString();
							admin_name = admin_name.trim();
							if(admin_name.equals(""))
							{
								Toast.makeText(getApplicationContext(),"Please Enter Password and Domain Name", Toast.LENGTH_LONG).show();
							}
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{	
					if(admin_link == null)
					{
							admin_name=ed_admin_link.getText().toString();
							admin_name = admin_name.trim();
							if(admin_name.equals(""))
							{
								Toast.makeText(getApplicationContext(),"Please Enter Domain Name", Toast.LENGTH_LONG).show();
						
							}
							else
							{
							
								sessionManager.insert_into_link(admin_name);
							}
					}
					
					
					if(ch_remember_me.isChecked())
					{
						check=true;
						sessionManager.insert_into_login(str_user_id,str_pwd);
						sessionManager.insert_into_login_stay_sign(true);	
					}
					else
					{	
						check=false;
						sessionManager.insert_into_login_stay_sign(false);
					}
					new Checklogin(str_user_id,str_pwd).execute();
				}

			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public void set_admin_field() {
		// TODO Auto-generated method stub
		if(admin_link==null)
		{
			tv_admin_link.setVisibility(View.VISIBLE);
			ed_admin_link.setVisibility(View.VISIBLE);
			tv_qestion_mark.setVisibility(View.VISIBLE);
		}
		else
		{
			tv_admin_link.setVisibility(View.GONE);
			ed_admin_link.setVisibility(View.GONE);
			tv_qestion_mark.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//Intent i=new Intent();
		 finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}*/
	public void init_component() {
		// TODO Auto-generated method stub
		ed_userid=(EditText)findViewById(R.id.login_ed_userid);
		ed_password=(EditText)findViewById(R.id.Login_ed_password);
		ch_remember_me=(CheckBox)findViewById(R.id.login_ch__remeber_me);
		tv_admin_link=(TextView)findViewById(R.id.login_tv_admin_Link);
		ed_admin_link=(EditText)findViewById(R.id.Login_ed_admin_link);
		btn_cancel=(Button)findViewById(R.id.login_btn_cancel);
		btn_log_in=(Button)findViewById(R.id.Login_btn_login);
		iv_header_logo=(ImageView)findViewById(R.id.newheader_iv_logo);
		tv_qestion_mark=(ImageView)findViewById(R.id.login_tv_question_mark);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	  intent.putExtra("EXIT", false);
	}
	return super.onKeyDown(keyCode, event);

	}

	private class Checklogin extends AsyncTask<Void, Void, Void>
	{
		String user_id,password;
		
		public Checklogin(String str_user_id, String str_pwd) {
			// TODO Auto-generated constructor stub
			user_id=str_user_id;
			password=str_pwd;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(Login.this);
			dialog.setMessage("Loading please wait..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			stresult = user_function.validateLogin(user_id, password);

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

					User_Role=jsobj.getString("Role");

					if(User_Role.equalsIgnoreCase("C"))
					 {
						 Client_Id= jsobj.getString("ClientID");
						 Client_Name = jsobj.getString("ClientName");
						 Client_code=jsobj.getString("Code");
						 Client_image_path=jsobj.getString("Image_path");
						 user_logo=user_function.DownloadImageFromPath(Client_image_path);
						 editor.putString("Client_Id", Client_Id);
						 editor.commit();
					}
					else
					{
						Client_Id = jsobj.getString("AdminID");
						Client_Name = jsobj.getString("AdminName");
						editor.putString("Client_Id", Client_Id);
						editor.commit();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if( error < 0 )
			{
				error = (error*(-1))-1;
				if(dialog != null)
		    	dialog.dismiss();
				Toast.makeText(getApplicationContext(),errorlist[error],Toast.LENGTH_LONG).show();	
			}
			else
			{

				Intent i=new Intent(getApplicationContext(), Home.class);
				startActivity(i);
				if(dialog != null)
			    	dialog.dismiss();
			}	
			
		}

	}
	public static Bitmap get_user_logo()
	{
		return user_logo;
	}
}















