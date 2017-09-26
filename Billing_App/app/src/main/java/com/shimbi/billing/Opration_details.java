package com.shimbi.billing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shimbi.library.MyBilling_sessionManager;
import com.shimbi.library.MyBilling_user_function;


public class Opration_details extends Activity {
	Intent i;
	MyBilling_user_function user_function;
	MyBilling_sessionManager sessionManager;
	ArrayList<String> detail_list;
	ArrayAdapter<String> detail_adapter;
	Login login;
	Popup popup;
	//declare widgets
	ListView lv_details;
	ProgressDialog dialog;
	LayoutInflater linflater;
	LinearLayout l_main,l_blank1,l_blank2,l_blank3,l_blank4,l_blank5,l_blank_bottom,l_service_tax_veritcal;
	LinearLayout l_inovice_date,l_amount,l_service_tax,l_payabal_amount,l_color,l_balace, l_shipping, l_additional;
	ImageView Iv_logout,Iv_menu,Iv_home,IV_back,Iv_stamp,iv_header_logo;
	TextView tv_amount_text,tv_payable_amount_text,tv_horizantal, tv_shipping_text, tv_shipping_result, tv_additional_text, tv_additional_result ;
	TextView tv_invoice_date_text_vertical,tv_amount_vertical,tv_service_vertical,tv_payable_vertical,tv_balance_vertical, tv_estimate_shipping_text, tv_estimate_shipping_result, tv_estimate_additional_text, tv_estimate_additional_result;
	TextView tv_opration_name,tv_invoice_id,tv_invoice_date_result,tv_due_result,tv_amount_result,tv_service_tax_text,tv_service_tax_result;
	TextView tv_sales_tax_text,tv_sales_tax_result,tv_paid_amount_text,tv_balance_text,tv_pable_amount_result,tv_paid_amount_result,tv_balance_result,tv_invoice_date,tv_due_date;
	//declare variable
	String invoice_id,str_result,opration;
	int type;
	   static int pos = 0,l_id;
	
	static 
	{
		 Popup.opration_list=new ArrayList<String>();
		 Popup.opration_list.add("Invoice");
	     Popup.opration_list.add("Receipt");
	     Popup.opration_list.add("Estimate");
	     Popup.opration_list.add("Overdue");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recurring_invoice);
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
		init();
		i = getIntent();
		sessionManager=new MyBilling_sessionManager(getApplicationContext());
		user_function=new MyBilling_user_function();
		login=new Login();
		iv_header_logo.setImageBitmap(Login.get_user_logo());
		detail_list=new ArrayList<String>();
		invoice_id = i.getStringExtra("invoice_id");
		type = i.getIntExtra("type",1);
		opration=i.getStringExtra("opration");
		if(opration.equalsIgnoreCase("Invoice"))
		{	
			tv_invoice_id.setText(invoice_id);
			tv_opration_name.setText(opration);
			new LoadDetails(invoice_id,type).execute();
		}
		else if(opration.equalsIgnoreCase("Estimate"))
		{
			tv_invoice_id.setText(invoice_id);
			tv_opration_name.setText(opration);
			tv_due_date.setVisibility(View.VISIBLE);
			tv_due_result.setVisibility(View.VISIBLE);
			
			new LoadDetails(invoice_id,type).execute();
		}
		else if(opration.equalsIgnoreCase("Receipt"))
		{
			tv_invoice_id.setText(invoice_id);
			tv_opration_name.setText(opration);
			
			new LoadDetails(invoice_id,type).execute();
		}
		else if(opration.equalsIgnoreCase("Overdue"))
		{	
			tv_invoice_id.setText(invoice_id);
			tv_opration_name.setText(opration);
			new LoadDetails(invoice_id,type).execute();
		}
		Iv_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(), Home.class);
				overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
				startActivity(i);
				
			}
		});
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
				AlertDialog.Builder builder = new AlertDialog.Builder(Opration_details.this)
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
		               Opration_details.this.finish();  
		               

		            }
		        })

		        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		             
		            }
		        });
		       builder.show();
				
			}
		});
		
		IV_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i=new Intent(getApplicationContext(), Selected_Opration_list.class);
			
				i.putExtra("opration", opration);
				startActivity(i);
				
				
			}
		});
		
	}
	public void init()
	{
		
		tv_invoice_id=(TextView)findViewById(R.id.opration_header_tv__opration_name);
		
		tv_opration_name=(TextView)findViewById(R.id.opration_header_tv__all);
		tv_invoice_date_result=(TextView)findViewById(R.id.new_tv_invoice_details_invoice_date_result);
		tv_due_result=(TextView)findViewById(R.id.tv_due_date_result);
		tv_amount_result=(TextView)findViewById(R.id.new_tv_invoice_details_amount_result);
		tv_pable_amount_result=(TextView)findViewById(R.id.new_tv_invoice_details_payable_amount_result);
		tv_invoice_date=(TextView)findViewById(R.id.new_tv_invoice_details_invoice_date_text);
		tv_due_date=(TextView)findViewById(R.id.tv_due_date);
		tv_paid_amount_result=(TextView)findViewById(R.id.new_tv_invoice_details_paid_amount_result);
		tv_balance_result=(TextView)findViewById(R.id.new_tv_invoice_details_balance_result);
		tv_paid_amount_text=(TextView)findViewById(R.id.new_tv_invoice_details_paid_amount_text);
		tv_amount_text=(TextView)findViewById(R.id.new_tv_invoice_details_amount_text);
		tv_amount_vertical=(TextView)findViewById(R.id.new_tv_invoice_details_amount_text_vertical);
		tv_balance_text=(TextView)findViewById(R.id.new_tv_invoice_details_balance_text);
		tv_balance_vertical=(TextView)findViewById(R.id.new_tv_invoice_details_balance_text);
		tv_payable_amount_text=(TextView)findViewById(R.id.new_tv_invoice_details_payable_amount_text);
		tv_payable_vertical=(TextView)findViewById(R.id.new_tv_invoice_details_payable_amount_text_vertical);
		tv_paid_amount_text=(TextView)findViewById(R.id.new_tv_invoice_details_paid_amount_text);
		
		tv_service_tax_text=(TextView)findViewById(R.id.new_tv_invoice_details_service_tax_text);
		tv_sales_tax_text=(TextView)findViewById(R.id.new_tv_invoice_details_sales_tax_text);
		tv_service_tax_result=(TextView)findViewById(R.id.new_tv_invoice_details_service_tax_result);
		tv_sales_tax_result=(TextView)findViewById(R.id.new_tv_invoice_details_sales_tax_result);
		
		tv_shipping_text = (TextView)findViewById(R.id.new_tv_invoice_details_shipping_text);
		tv_shipping_result = (TextView)findViewById(R.id.new_tv_invoice_details_shipping_result);
		tv_additional_text = (TextView)findViewById(R.id.new_tv_invoice_details_additional_text);
		tv_additional_result = (TextView)findViewById(R.id.new_tv_invoice_details_additional_result);
		
		//header_9
		Iv_home=(ImageView)findViewById(R.id.newheader_iv_home);
		Iv_logout=(ImageView)findViewById(R.id.newheader_iv_loogout);
		Iv_menu=(ImageView)findViewById(R.id.newheader_iv_menu);
		IV_back=(ImageView)findViewById(R.id.opration_header_btn_back);
		Iv_stamp=(ImageView)findViewById(R.id.iv_receipt_stamp);
		iv_header_logo=(ImageView)findViewById(R.id.newheader_iv_logo);
		//layout
		l_amount=(LinearLayout)findViewById(R.id.layout_amount);
		l_balace=(LinearLayout)findViewById(R.id.layout_bal);
		l_blank1=(LinearLayout)findViewById(R.id.layout_blank_1);
		l_blank2=(LinearLayout)findViewById(R.id.layout_blank2);
		l_blank3=(LinearLayout)findViewById(R.id.layout_blank3);
		l_blank4=(LinearLayout)findViewById(R.id.layout_blank4);
		l_blank5=(LinearLayout)findViewById(R.id.layout_blank5);
		l_blank_bottom=(LinearLayout)findViewById(R.id.layout_blank_bottom);
		l_service_tax_veritcal=(LinearLayout)findViewById(R.id.l_vertical_service_tax);
		l_color=(LinearLayout)findViewById(R.id.layout_color);
		l_inovice_date=(LinearLayout)findViewById(R.id.layout_invoice_date);
		l_main=(LinearLayout)findViewById(R.id.layout_main);
		l_payabal_amount=(LinearLayout)findViewById(R.id.layout_payabal_amount);
		l_service_tax=(LinearLayout)findViewById(R.id.layout_service_tax);
		
		l_shipping = (LinearLayout)findViewById(R.id.layout_shipping);
		l_additional = (LinearLayout)findViewById(R.id.layout_additional);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent i=new Intent(getApplicationContext(), Selected_Opration_list.class);
		
		i.putExtra("opration", opration);
		startActivity(i);
		
	}
	
	class LoadDetails extends AsyncTask<Void, Void, Void>
	{
		String id;
		int type;

		public LoadDetails(String invoice_id, int type) {
			// TODO Auto-generated constructor stub
			id=invoice_id;
			this.type=type;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(Opration_details.this);
			dialog.setMessage("Loading details please wait..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			//TODO Auto-generated method stub
			str_result=user_function.getInvoiceDetails(id, this.type);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (MyBilling_user_function.url_error == 0)
			{
		
				switch (this.type) {
				case 1: //Invoice 
						
						try {
									if(str_result.equalsIgnoreCase("-3") ||str_result.equalsIgnoreCase("-2") || str_result.equalsIgnoreCase("-1"))
										{
											Toast.makeText(getApplicationContext(), "No Record found!!!", Toast.LENGTH_LONG).show();
											if(dialog != null)
											dialog.dismiss();
								
										}
								  else
								{	
									  JSONArray  jArray = new JSONArray(str_result);
		
									  for(int i=0;i<jArray.length();i++)
									  {
										  JSONObject jsonObject=jArray.getJSONObject(i);
										  String invoice_date=jsonObject.getString("invoicedate");
										  tv_invoice_date.setText("Invoice Date");
										  tv_invoice_date_result.setText(": "+invoice_date);
										  String duedate=jsonObject.getString("invoice_duedate");
										  tv_due_date.setText("Due Date");
										  tv_due_result.setText(": "+duedate);
										  String total_amt=jsonObject.getString("calulated_invoice_amt");
										  tv_amount_text.setText("Amount"+ " ("+Login.Client_code+")");
										  tv_amount_result.setText(": "+total_amt);
										  String payable=jsonObject.getString("invoice_totalamount");
										  tv_payable_amount_text.setText("Payable Amount"+" ("+Login.Client_code+")");
										  tv_pable_amount_result.setText(": "+payable);
										  String paid_amont=jsonObject.getString("invoice_installmentamount");
										  tv_paid_amount_text.setText("Paid Amount"+" ("+Login.Client_code+")" );
										  tv_paid_amount_result.setText(": "+paid_amont);
										  String balance=jsonObject.getString("invoice_closingbalance");
										  tv_balance_text.setText("Balance"+" ("+Login.Client_code+")");
										  tv_balance_result.setText(": "+balance);
										  
										  String shippingCharges = jsonObject.getString("shipping_charges");
										  String additionalCharges = jsonObject.getString("additional_charges");
										  //System.out.println("shipping charges: "+shippingCharges);
										  //System.out.println("additional charges: "+additionalCharges);
										  
											  tv_shipping_text.setText("Shipping Charges");
											  tv_shipping_result.setText(": "+shippingCharges);
											  tv_additional_text.setText("Additional Charges");
											  tv_additional_result.setText(": "+additionalCharges);
										  
										  JSONArray jtax_array=jsonObject.getJSONArray("tax_result");
										  if(jtax_array.length()>0)
										  {	
											  for(int i1=0;i1<jtax_array.length();i1++)
											  {
												  JSONObject jtax_object=jtax_array.getJSONObject(i1);
												  if(jtax_array.length()==1) {
													 	tv_sales_tax_text.setVisibility(View.GONE);
													 	tv_sales_tax_result.setVisibility(View.GONE);
												}
												  if(i1==0)
												  {		
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													tv_service_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
													tv_service_tax_result.setText(": "+tax);
												  }
												  else if(i1==1)
												  {
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													if(tax==null)
													{
														 tv_sales_tax_text.setVisibility(View.GONE);
														  tv_sales_tax_result.setVisibility(View.GONE);
													}
													else
													{	
														tv_sales_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
														tv_sales_tax_result.setText(": "+tax);
													}	
												  }
												  else
													{
													  	l_id=l_id+1;
														String tax=jtax_object.getString("tax_amt");
														String tax_name=jtax_object.getString("tax_name");
														String tax_percentage=jtax_object.getString("percentage");
														add_text_details(tax_name,tax , tax_percentage,1);
													
													}
											  }
										  }
										  else
										  {	
											  tv_service_tax_text.setText("Service Tax (0.0%)");
											  tv_service_tax_result.setText(": 0.0");
											  tv_sales_tax_text.setText("Sales Tax (0.0%)");
											  tv_sales_tax_result.setText(": 0.0");
										  }
									
									  }// end of for loop		
								if(dialog != null)
								dialog.dismiss();
								}//end of if else
							 }// end of try		
						catch (JSONException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						break;
				case 2:	//Receipt
						
						try {
								if(str_result.equalsIgnoreCase("-3") ||str_result.equalsIgnoreCase("-2") || str_result.equalsIgnoreCase("-1"))
								{
									Toast.makeText(getApplicationContext(), "No Record found!!!", Toast.LENGTH_LONG).show();
									if(dialog != null)
										dialog.dismiss();
								}
								else
								{	
									JSONArray  jArray = new JSONArray(str_result);
									for(int i=0;i<jArray.length();i++)
									{
										Iv_stamp.setVisibility(View.VISIBLE);
										JSONObject jsonObject=jArray.getJSONObject(i);
										tv_invoice_date.setText("Invoice No");
										String invoiceid=jsonObject.getString("invoiceid");
										tv_invoice_date_result.setText(": "+invoiceid);
										tv_due_date.setText("Receipt Date");
										String duedate=jsonObject.getString("receiptdate");
										tv_due_result.setText(": "+duedate);
										String total_amt=jsonObject.getString("calulated_invoice_amt");
										tv_amount_text.setText("Amount"+" ("+Login.Client_code+")");
										tv_amount_result.setText(": "+total_amt);
										String payable=jsonObject.getString("invoice_totalamount");
										tv_payable_amount_text.setText("Payable Amount"+" ("+Login.Client_code+")");
										tv_pable_amount_result.setText(": "+payable);
										String balance=jsonObject.getString("invoice_closingbalance");
										String paid_amont=jsonObject.getString("receipt_amount");
										tv_paid_amount_text.setText("Paid Amount"+" ("+Login.Client_code+")");
										tv_paid_amount_result.setText(": "+paid_amont);
										tv_balance_text.setText("Balance"+" ("+Login.Client_code+")");
										tv_balance_result.setText(": "+balance);
										
										String shippingCharges = jsonObject.getString("shipping_charges");
										String additionalCharges = jsonObject.getString("additional_charges");
										//System.out.println("shipping charges: "+shippingCharges);
										//System.out.println("additional charges: "+additionalCharges);
																				  
										  tv_shipping_text.setText("Shipping Charges");
										  tv_shipping_result.setText(": "+shippingCharges);
										  tv_additional_text.setText("Additional Charges");
										  tv_additional_result.setText(": "+additionalCharges);
										
										  
										JSONArray jtax_array=jsonObject.getJSONArray("tax_result");
										if(jtax_array.length()>0)
										{	
											for(int i1=0;i1<jtax_array.length();i1++)
											{
												JSONObject jtax_object=jtax_array.getJSONObject(i1);
												 if(jtax_array.length()==1) {
													 	tv_sales_tax_text.setVisibility(View.GONE);
													 	tv_sales_tax_result.setVisibility(View.GONE);
												}
												if(i1==0)
												{		
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													tv_service_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
													tv_service_tax_result.setText(": "+tax);
												}
												else if(i1==1)
												{
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													if(tax==null)
													{
														 tv_sales_tax_text.setVisibility(View.GONE);
														  tv_sales_tax_result.setVisibility(View.GONE);
													}
													else
													{	
														tv_sales_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
														tv_sales_tax_result.setText(": "+tax);
													}	
													
												}
												else
												{
													l_id=l_id+1;
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													add_text_details(tax_name,tax , tax_percentage,2);
												}
											}
										}
										else
										{	
											tv_service_tax_text.setText("Service Tax (0.0%)");
											tv_service_tax_result.setText(": 0.0");
											tv_sales_tax_text.setText("Sales Tax (0.0%)");
											tv_sales_tax_result.setText(": 0.0");
										}
								}// end of for loop		
								if(dialog != null)
								dialog.dismiss();
							} // end of if else
						}//end of try	
						catch (JSONException e) 
						{
						// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
				case 3:	 //Estimate
						removelayout();
						inflate_estimate();
						try {
								if(str_result.equalsIgnoreCase("-3") ||str_result.equalsIgnoreCase("-2") || str_result.equalsIgnoreCase("-1"))
								{
									Toast.makeText(getApplicationContext(), "No Record found!!!", Toast.LENGTH_LONG).show();
									if(dialog != null)
										dialog.dismiss();
					
								}
								else
								{	
										JSONArray  jArray = new JSONArray(str_result);
		
										for(int i=0;i<jArray.length();i++)
										{
										
											JSONObject jsonObject=jArray.getJSONObject(i);
											tv_invoice_date.setText("Estimate Date");
											String quotationdate=jsonObject.getString("quotationdate");
											tv_invoice_date_result.setText(": "+quotationdate);
											String total_amt=jsonObject.getString("calulated_quotation_amt");
											tv_amount_text.setText("Amount"+" ("+Login.Client_code+")");
											tv_payable_amount_text.setText("Payable Amount"+" ("+Login.Client_code+")");
											tv_amount_result.setText(": "+total_amt);
											String payable=jsonObject.getString("amount");
											tv_pable_amount_result.setText(": "+payable);
											
											String shippingCharges = jsonObject.getString("shipping_charges");
											String additionalCharges = jsonObject.getString("additional_charges");
											//System.out.println("shipping charges: "+shippingCharges);
											//System.out.println("additional charges: "+additionalCharges);
																					  
											tv_estimate_shipping_text.setText("Shipping Charges");
											tv_estimate_shipping_result.setText(": "+shippingCharges);
											tv_estimate_additional_text.setText("Additional Charges");
											tv_estimate_additional_result.setText(": "+additionalCharges);
											
											  
											JSONArray jtax_array=jsonObject.getJSONArray("tax_result");
											if(jtax_array.length()>0)
											{	
												for(int i1=0;i1<jtax_array.length();i1++)
												{
													JSONObject jtax_object=jtax_array.getJSONObject(i1);
													 if(jtax_array.length()==1) {
														 	tv_sales_tax_text.setVisibility(View.GONE);
														 	tv_sales_tax_result.setVisibility(View.GONE);
													}
													if(i1==0)
													{		
														String tax=jtax_object.getString("tax_amt");
														String tax_name=jtax_object.getString("tax_name");
														String tax_percentage=jtax_object.getString("percentage");
														tv_service_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
														tv_service_tax_result.setText(": "+tax);
													}
													else if(i1==1)
													{
														String tax=jtax_object.getString("tax_amt");
														String tax_name=jtax_object.getString("tax_name");
														String tax_percentage=jtax_object.getString("percentage");
														if(tax==null)
														{
															 tv_sales_tax_text.setVisibility(View.GONE);
															  tv_sales_tax_result.setVisibility(View.GONE);
														}
														else
														{	
															tv_sales_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
															tv_sales_tax_result.setText(": "+tax);
														}	
														
													}
													else
													{
														l_id=l_id+1;
														String tax=jtax_object.getString("tax_amt");
														String tax_name=jtax_object.getString("tax_name");
														String tax_percentage=jtax_object.getString("percentage");
														add_text_details(tax_name,tax , tax_percentage,3);
													}
													
												
												}
											}
											else
											{
												tv_service_tax_text.setText("Service Tax (0.0%)");
												tv_service_tax_result.setText(": 0.0");
												tv_sales_tax_text.setText("Sales Tax (0.0%)");
												tv_sales_tax_result.setText(": 0.0");
											}
							
										}	//end of for loop	
								if(dialog != null)
									dialog.dismiss();
								}//end of if else	
						} // end of try
						catch (JSONException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(dialog != null)
							dialog.dismiss();
						break;	
				case 4 : //OverDue
					try {
								if(str_result.equalsIgnoreCase("-3") ||str_result.equalsIgnoreCase("-2") || str_result.equalsIgnoreCase("-1"))
								{
									Toast.makeText(getApplicationContext(), "No Record found!!!", Toast.LENGTH_LONG).show();
									if(dialog != null)
									dialog.dismiss();
								}
								else
								{	
									JSONArray  jArray = new JSONArray(str_result);
									for(int i=0;i<jArray.length();i++)
									{
										JSONObject jsonObject=jArray.getJSONObject(i);
										String invoice_date=jsonObject.getString("invoicedate");
										tv_invoice_date.setText("Invoice Date");
										tv_invoice_date_result.setText(": "+invoice_date);
										String duedate=jsonObject.getString("invoice_duedate");
										tv_due_date.setText("Due Date");
										tv_due_result.setTextColor(Color.RED);
										tv_due_result.setText(": "+duedate);
										String total_amt=jsonObject.getString("calulated_invoice_amt");
										tv_amount_text.setText("Amount"+ " ("+Login.Client_code+")");
										tv_amount_result.setText(": "+total_amt);
										String payable=jsonObject.getString("invoice_totalamount");
										tv_payable_amount_text.setText("Payable Amount"+" ("+Login.Client_code+")");
										tv_pable_amount_result.setText(": "+payable);
										String paid_amont=jsonObject.getString("invoice_installmentamount");
										tv_paid_amount_text.setText("Paid Amount"+" ("+Login.Client_code+")" );
										tv_paid_amount_result.setText(": "+paid_amont);
										String balance=jsonObject.getString("invoice_closingbalance");
										tv_balance_text.setText("Balance"+" ("+Login.Client_code+")");
										tv_balance_result.setText(": "+balance);
										
										String shippingCharges = jsonObject.getString("shipping_charges");
										String additionalCharges = jsonObject.getString("additional_charges");
										//System.out.println("shipping charges: "+shippingCharges);
										//System.out.println("additional charges: "+additionalCharges);
																				  
										  tv_shipping_text.setText("Shipping Charges");
										  tv_shipping_result.setText(": "+shippingCharges);
										  tv_additional_text.setText("Additional Charges");
										  tv_additional_result.setText(": "+additionalCharges);
										
										JSONArray jtax_array=jsonObject.getJSONArray("tax_result");
										if(jtax_array.length()>0)
										{	
											for(int i1=0;i1<jtax_array.length();i1++)
											{
												JSONObject jtax_object=jtax_array.getJSONObject(i1);
												if(jtax_array.length()==1) 
												{
													tv_sales_tax_text.setVisibility(View.GONE);
													tv_sales_tax_result.setVisibility(View.GONE);
												}
												if(i1==0)
												{		
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													tv_service_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
													tv_service_tax_result.setText(": "+tax);
												}
												else if(i1==1)
												{
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													if(tax==null)
													{
														tv_sales_tax_text.setVisibility(View.GONE);
														tv_sales_tax_result.setVisibility(View.GONE);
													}
													else
													{	
														tv_sales_tax_text.setText(tax_name+" ("+tax_percentage+"%)");
														tv_sales_tax_result.setText(": "+tax);
													}	
												}
												else
												{
													l_id=l_id+1;
													String tax=jtax_object.getString("tax_amt");
													String tax_name=jtax_object.getString("tax_name");
													String tax_percentage=jtax_object.getString("percentage");
													add_text_details(tax_name,tax , tax_percentage,4);
												}
											}
										}
									else
									{	
										tv_service_tax_text.setText("Service Tax (0.0%)");
										tv_service_tax_result.setText(": 0.0");
										tv_sales_tax_text.setText("Sales Tax (0.0%)");
										tv_sales_tax_result.setText(": 0.0");
									}
									}// end of for loop		
									if(dialog != null)
										dialog.dismiss();
								}//end of if else
						}// end of try		
						catch (JSONException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
				default:
						break;
				}//end of switch
			}// end of outer if
		}//end of method on post

		
	}//end of async class
	public void removelayout()
	{
		l_amount.removeAllViews();l_balace.removeAllViews();l_blank1.removeAllViews();l_blank2.removeAllViews();l_blank3.removeAllViews();
		l_blank4.removeAllViews();l_blank5.removeAllViews();l_blank_bottom.removeAllViews();l_inovice_date.removeAllViews();
		l_payabal_amount.removeAllViews();l_service_tax.removeAllViews();
		l_blank1.setVisibility(View.GONE);l_blank2.setVisibility(View.GONE);
		l_blank3.setVisibility(View.GONE);l_blank4.setVisibility(View.GONE);
		l_blank5.setVisibility(View.GONE);
		l_blank_bottom.setVisibility(View.GONE);
		l_color.setVisibility(View.GONE);
		
	}
	public void add_text_details(String tax_name,String tax,String tax_percentage,int option_details)
	{
	
		LinearLayout parentLayout ;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
	
		if(option_details==3)
		{
			parentLayout= (LinearLayout)findViewById(R.id.l_service_main_estimate);
		}
		else
		{	
			parentLayout= (LinearLayout)findViewById(R.id.l_vertical_service_tax);
		}	
		LinearLayout l_inner_veritcal = new LinearLayout(getApplicationContext());
		l_inner_veritcal.setId(l_id);
		l_inner_veritcal.setOrientation(LinearLayout.VERTICAL);
		l_inner_veritcal.setLayoutParams(layoutParams);
		
		LinearLayout L_inner_horizantal=new LinearLayout(getApplicationContext());
		L_inner_horizantal.setId(l_id);
		L_inner_horizantal.setOrientation(LinearLayout.HORIZONTAL);
		L_inner_horizantal.setLayoutParams(layoutParams);
		
		TextView tv_tax=new TextView(getApplicationContext());
		TextView tv_tax_name=new TextView(getApplicationContext());
		//tv_tax_name.setTextAppearance(getApplicationContext(), android.R.attr.textAppearanceLarge);
		tv_tax_name.setTextSize(16);
		tv_tax_name.setText(tax_name+" ("+tax_percentage+"%)");
		tv_tax_name.setEms(6);
		tv_tax_name.setTextColor(Color.BLACK);
		//tv_tax.setTextAppearance(getApplicationContext(), android.R.attr.textAppearanceLarge);
		tv_tax.setTextSize(16);
		tv_tax.setText(": "+tax);
		tv_tax.setEms(8);
		tv_tax.setTextColor(Color.BLACK);
		layoutParams.setMargins(3, 0, 0, 0);
		L_inner_horizantal.addView(tv_tax_name);
		L_inner_horizantal.addView(tv_tax);
		l_inner_veritcal.addView(L_inner_horizantal);
		
		parentLayout.addView(l_inner_veritcal);	
	}
	public void inflate_estimate()
	{
		 linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		 View myView = linflater.inflate(R.layout.estimate, null);
	       myView.setId(pos);

	       pos++;
	       l_main.addView(myView);
	       tv_invoice_date=(TextView)findViewById(R.id.estimate_estimate_date_text);
	       tv_invoice_date.setText("Estimate Date");
	       tv_invoice_date_result=(TextView)findViewById(R.id.estimate_estimate_date_result);
	       tv_amount_text=(TextView)findViewById(R.id.estimate_amount_text);
	       tv_amount_text.setText("Amount (INR)");
	       tv_amount_result=(TextView)findViewById(R.id.estimate_amount_result);
	       tv_payable_amount_text=(TextView)findViewById(R.id.estimate_payabal_amount_text);
	       tv_payable_amount_text.setText("Payabal Amount (INR)");
	       tv_pable_amount_result=(TextView)findViewById(R.id.estimate_payable_amount_result);
	       tv_service_tax_text=(TextView)findViewById(R.id.estimate_service_tax_text);
	       tv_service_tax_result=(TextView)findViewById(R.id.estimate_service_tax_result);
	       tv_sales_tax_text=(TextView)findViewById(R.id.estimate_sales_tax_text);
	       tv_horizantal=(TextView)findViewById(R.id.tv__horizantal);
	       tv_sales_tax_result=(TextView)findViewById(R.id.estimate_sales_tax_sales_tax_result);
	       
	       tv_estimate_shipping_text = (TextView)findViewById(R.id.estimate_shipping_text);
	       tv_estimate_shipping_result = (TextView)findViewById(R.id.estimate_shipping_result);
	       tv_estimate_additional_text = (TextView)findViewById(R.id.estimate_additional_text);
	       tv_estimate_additional_result = (TextView)findViewById(R.id.estimate_additional_result);
	}
	
}
