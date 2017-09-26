package com.shimbi.billing;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.shimbi.library.MyBilling_sessionManager;
import com.shimbi.library.MyBilling_user_function;

public class Selected_Opration_list extends Activity {
	//widgets
	ProgressDialog dialog;
	ListView lv_result;
	Opration_list_adapter adapter;
	TextView tv_opration_name;
	ImageView Iv_home,Iv_logout,Iv_menu,IV_back,iv_header_logo;
	ArrayAdapter<String> opration_adapter;
	ListView lv_opration;
	boolean checkLoadMore = true;
	static ArrayList<String> opration_list;
	//Button btnLoadMore;
	ProgressBar progressBar;
	//objects
	MyBilling_user_function user_function;
	MyBilling_sessionManager sessionManager;
	Popup popup;
	Login login;
	//variable
	int type, page_no, total_count, total_pages;
	static int prev_page;
	String str_result,id,invoice_client,opration, lastID;
	ArrayList<HashMap<String, String>> invoiceList;
	public static boolean count;
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
		setContentView(R.layout.selected_opration_list);
		
		init();
		opration=getIntent().getStringExtra("opration");
		user_function=new MyBilling_user_function();
		login=new Login();
		iv_header_logo.setImageBitmap(Login.get_user_logo());

		sessionManager=new MyBilling_sessionManager(getApplicationContext());
		tv_opration_name.setText(opration);
		invoiceList= new ArrayList<HashMap<String,String>>();
		id=Login.Client_Id;
		
		if(opration.equalsIgnoreCase("Invoice"))
		{
	
			new Get_result_list(opration).execute();
			
		}
		else if(opration.equalsIgnoreCase("Receipt"))
		{
	
			new Get_result_list(opration).execute();
			
		}
		else if(opration.equalsIgnoreCase("Estimate"))
		{
	
			new Get_result_list(opration).execute();
			
		}
		else if(opration.equalsIgnoreCase("Overdue"))
		{
			new Get_result_list(opration).execute();
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
		IV_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(), Home.class);
				
				startActivity(i);
				
				
			}
		});
		Iv_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(Selected_Opration_list.this)
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
		               Selected_Opration_list.this.finish();  
		               

		            }
		        })

		        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		             
		            }
		        });
		       builder.show();
				

			}
		});
		Iv_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Display display = getWindowManager().getDefaultDisplay();
				int width=(display.getWidth()/3);
				int height=(display.getHeight());
				popup=new Popup(getApplicationContext(), width, height);
				popup.show(v);
				
			}
		});
		
		
		
		
		
		//btnLoadMore = new Button(getApplicationContext());
		//btnLoadMore.setText("Load More");
		//lv_result.addFooterView(btnLoadMore);
		
		/*btnLoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int currentPosition = lv_result.getFirstVisiblePosition();
				int currentLastPosition = lv_result.getLastVisiblePosition();
				lv_result.setSelectionFromTop(currentLastPosition, currentPosition + 1);
				if(invoiceList.size() < total_count )
				{
					new LoadMore().execute();
				}
				
				
			}
		});*/
		
		
		
		/*lv_result.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
				final int lastitem = firstVisibleItem + visibleItemCount;
				System.out.println("Last Item "+ lastitem);
				System.out.println("First Visible Item " + firstVisibleItem);
				System.out.println("Visible Item Count " + visibleItemCount);
				System.out.println("Total Item Count " + totalItemCount);
				
				
				if(totalItemCount > 0 && lastitem == totalItemCount)
				{
					if(invoiceList.size() < total_count )
					{
						new LoadMore().execute();
					}
					
				}
				else
				{
					checkLoadMore = false;
				}
				
			}
		});*/
		
		
		
		this.lv_result.setOnScrollListener(new OnScrollListener()
        {
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				// TODO Auto-generated method stub
			}

			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount)
			{
				// TODO Auto-generated method stub
				//NewsHome.isInternetPresent = NewsHome.cd.isConnectingToInternet();

			//	if( NewsHome.isInternetPresent )
			//	{
				
				
					final int lastItem = firstVisibleItem + visibleItemCount;
					
		            if(totalItemCount>0 && lastItem == totalItemCount && checkLoadMore == true)
					{
		            	if( page_no < total_pages)
						{	//lv_result.scrollBy(0, 8);
		            		if(prev_page == page_no-1)
		            		{
		            			checkLoadMore = false;		            			
		            			progressBar.setVisibility(View.VISIBLE);
								//ld = new LoadMore();
								//ld.execute(appURL + id +"&page_no="+ page_no);
		            			//System.out.println("Page No When scrolled "+ page_no);
		            			new LoadMore().execute();
		            		}
						}
						else
						{
							checkLoadMore=false;
						}
				    }
			//	}
			/*	else
				{
					error_msg = (TextView)findViewById(R.id.error_msg);
					error_msg = (TextView)findViewById(R.id.error_msg);
		        	error_msg.setText("Data connection failed. Try again later");
		        	error_msg.setVisibility(View.VISIBLE);

		        	heading.setVisibility(View.INVISIBLE);
		        	lv.setVisibility(View.INVISIBLE);
		        	pb1.setVisibility(View.GONE);
				} */			
		            
			}
		});
		
		
	}
	public void init()
	{
		lv_result=(ListView)findViewById(R.id.select_opration_lv_opration);
		tv_opration_name=(TextView)findViewById(R.id.opration_header_tv__opration_name);
		Iv_home=(ImageView)findViewById(R.id.newheader_iv_home);
		Iv_logout=(ImageView)findViewById(R.id.newheader_iv_loogout);
		Iv_menu=(ImageView)findViewById(R.id.newheader_iv_menu);
		IV_back=(ImageView)findViewById(R.id.opration_header_btn_back);
		iv_header_logo=(ImageView)findViewById(R.id.newheader_iv_logo);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
	}
	 public void showInvoiceDetail(String invoice_id)
	    {
		    // 	Toast.makeText(getApplicationContext(), invoice_id, Toast.LENGTH_LONG).show();
		 		Intent intent = new Intent(getApplicationContext(),Opration_details.class);
		 		intent.putExtra("invoice_id",invoice_id);
		 		intent.putExtra("opration", opration);
		 		intent.putExtra("type",type);
		 		startActivity(intent);
		 	
	    }
	 @Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
			Intent i=new Intent(getApplicationContext(), Home.class);
			
			startActivity(i);
			
			
		}
		private class Get_result_list extends AsyncTask<Void, Void, Void>
	{
		String request;

		public Get_result_list(String opration) {
			// TODO Auto-generated constructor stub
			request=opration;
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(Selected_Opration_list.this);
			dialog.setMessage("Loading details please wait..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
			
			page_no = 0;
			prev_page = -1;
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(request.equalsIgnoreCase("Invoice"))
			{
				type=1;
				str_result=user_function.getRecordsList(id,type);
			}
			else if(request.equalsIgnoreCase("Receipt"))
			{
				type=2;
				str_result=user_function.getRecordsList(id,type);
			}
			else if(request.equalsIgnoreCase("Estimate"))
			{
				type=3;
				str_result=user_function.getRecordsList(id,type);
			}
			else if(request.equalsIgnoreCase("OverDue"))
			{
				type=4;
				str_result=user_function.getRecordsList(id,type);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			invoiceList.clear();
			if (MyBilling_user_function.url_error == 0)
			{
				if (str_result.equals(null) || str_result.equals("")) 
				{
				
					Toast.makeText(getApplicationContext(),"No Record found!!!",Toast.LENGTH_LONG).show();				
						if(dialog != null)
						dialog.dismiss();
						invoiceList.clear();
						adapter = new Opration_list_adapter(getApplicationContext(),invoiceList,opration);
						lv_result.setAdapter(adapter);
				}//end of if
				else {
					
			
					if(str_result.equalsIgnoreCase("-3") ||str_result.equalsIgnoreCase("-2") || str_result.equalsIgnoreCase("-1"))
					{
						Toast.makeText(getApplicationContext(), "No Record found!!!", Toast.LENGTH_LONG).show();

						if(dialog != null)
						dialog.dismiss();
						invoiceList.clear();
						adapter = new Opration_list_adapter(getApplicationContext(),invoiceList,opration);
						lv_result.setAdapter(adapter);
					}
					else
					{
				
						if(request.equalsIgnoreCase("Invoice"))
						{
							String[] invoice_list = str_result.split("shimbirow");
							for (int i = 0; i < invoice_list.length; i++) 
							{
								final String[] invoice_record = invoice_list[i].split("shimbicol");
								HashMap<String, String> map = new HashMap<String, String>();	
									map.put("date", invoice_record[0]);
									map.put("invoiceNo", invoice_record[1]);
									map.put("Amount", invoice_record[2]);
									map.put("company_name", invoice_record[3]);
									map.put("status", invoice_record[4]);
									invoiceList.add(map);
									
									lastID = invoice_record[1];
									
									//System.out.println("Last ID "+lastID);
									
									//page_no = Integer.parseInt(invoice_record[5]);
									//System.out.println("Page No "+ page_no);
									
									total_count = Integer.parseInt(invoice_record[6]);
									//System.out.println("Total Count "+ total_count);
									
									total_pages = (total_count / 10);
									
									int tmp = total_count % 10;

				    				if(tmp == 0)
				    				{
				    					total_pages = total_pages - 1;
				    				}									
									
									//System.out.println("Total Pages "+ total_pages);
				    				/*int tmp = total_count % 10;

				    				if(tmp > 0)
				    				{
				    					total_pages = total_pages + 1;
				    				}*/
							}
						}
						else if(request.equalsIgnoreCase("Receipt"))
						{
				
							String[] invoice_list = str_result.split("shimbirow");
							for (int i = 0; i < invoice_list.length; i++) 
							{
								final String[] invoice_record = invoice_list[i].split("shimbicol");
								HashMap<String, String> map = new HashMap<String, String>();	
									map.put("date", invoice_record[0]);
									map.put("invoiceNo", invoice_record[1]);
									map.put("Amount", invoice_record[2]);
									map.put("receiptNo",invoice_record[3]);
									map.put("company_name", invoice_record[4]);
									invoiceList.add(map);
									
									lastID = invoice_record[1];
									//System.out.println("Last ID "+lastID);
									total_count = Integer.parseInt(invoice_record[6]);
									//System.out.println("Total Count "+total_count);
									total_pages = (total_count / 10);
									
									int tmp = total_count % 10;

				    				if(tmp == 0)
				    				{
				    					total_pages = total_pages - 1;
				    				}
									
									//System.out.println("Total Pages "+ total_pages);
							}
						}
						else if(request.equalsIgnoreCase("Estimate"))
						{
							String[] invoice_list = str_result.split("shimbirow");
							for (int i = 0; i < invoice_list.length; i++) 
							{
								final String[] invoice_record = invoice_list[i].split("shimbicol");
								HashMap<String, String> map = new HashMap<String, String>();	
									map.put("invoiceNo", invoice_record[0]);
									map.put("Amount", invoice_record[1]);
									map.put("date", invoice_record[2]);
									map.put("status",invoice_record[3]);
									map.put("company_name", invoice_record[4]);
									invoiceList.add(map);
									
									lastID = invoice_record[1];
									//System.out.println("Last ID "+lastID);
									total_count = Integer.parseInt(invoice_record[6]);
									//System.out.println("Total Count "+total_count);
									total_pages = (total_count / 10);
									
									int tmp = total_count % 10;

				    				if(tmp == 0)
				    				{
				    					total_pages = total_pages - 1;
				    				}
									
									//System.out.println("Total Pages "+ total_pages);
							}
						}
						else if(request.equalsIgnoreCase("Overdue"))
						{
							String[] invoice_list = str_result.split("shimbirow");
							for (int i = 0; i < invoice_list.length; i++) 
							{
								final String[] invoice_record = invoice_list[i].split("shimbicol");
								HashMap<String, String> map = new HashMap<String, String>();	
									map.put("date", invoice_record[0]);
									map.put("invoiceNo", invoice_record[1]);
									map.put("Amount", invoice_record[2]);
									map.put("company_name", invoice_record[3]);
									map.put("status", invoice_record[4]);
									invoiceList.add(map);
									
									lastID = invoice_record[1];
									//System.out.println("Last ID "+lastID);
									total_count = Integer.parseInt(invoice_record[7]);
									//System.out.println("Total Count "+total_count);
									total_pages = (total_count / 10);
									
									int tmp = total_count % 10;

				    				if(tmp == 0)
				    				{
				    					total_pages = total_pages - 1;
				    				}
									
									//System.out.println("Total Pages "+ total_pages);
							}
						}
						
						/*Button btnLoadMore = new Button(getApplicationContext());
						btnLoadMore.setText("Load More");
						lv_result.addFooterView(btnLoadMore);
						
						btnLoadMore.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								int currentPosition = lv_result.getFirstVisiblePosition();
								lv_result.setSelectionFromTop(currentPosition + 1, 4);
							}
						});*/
						
						
						
						
						
						adapter = new Opration_list_adapter(getApplicationContext(),invoiceList,opration);
						lv_result.setAdapter(adapter);
						if(dialog != null)
							dialog.dismiss();
						lv_result.setOnItemClickListener(new OnItemClickListener() 
						{
							@Override
							public void onItemClick(AdapterView<?> view, View arg1,int position, long id) 
							{
							// TODO Auto-generated method stub
								showInvoiceDetail(invoiceList.get(position).get("invoiceNo"));			
							}
						}); //end of list listener....	
					}	 //end of else -3
				}// end of else
			}// end of outer if		
		  }	//end of method
	}// end of  async class
		
		
		
		
		
		
		
		private class LoadMore extends AsyncTask<Void, Void, Void>
		{
			


			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				/*dialog = new ProgressDialog(Selected_Opration_list.this);
				dialog.setMessage("Loading details please wait..");
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.show();*/
				
				//lv_result.removeFooterView(btnLoadMore);
				
				//progressBar = new ProgressBar(getApplicationContext());
				//lv_result.addFooterView(progressBar);
				
				prev_page = page_no;
				page_no = page_no+1;
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				
					//page_no=page_no+1;
					//System.out.println("Page number before calling "+page_no);
					//System.out.println("Prev Page before calling "+ prev_page);
					if( prev_page == page_no-1 )
					{
						str_result=user_function.LoadMore(id,type,page_no);
					}
					else
					{
						checkLoadMore = false;
					}
					
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//dialog.dismiss();
				
				//lv_result.removeFooterView(progressBar);
				//lv_result.addFooterView(btnLoadMore);
				checkLoadMore = true;
				ArrayList<HashMap<String, String>> invoiceList_temp = new ArrayList<HashMap<String,String>>();
				
				//invoiceList.clear();
				if (MyBilling_user_function.url_error == 0)
				{
					if (str_result.equals(null) || str_result.equals("")) 
					{
					
						Toast.makeText(getApplicationContext(),"No Record found!!!",Toast.LENGTH_LONG).show();				
							if(dialog != null)
							dialog.dismiss();
							invoiceList.clear();
							adapter = new Opration_list_adapter(getApplicationContext(),invoiceList,opration);
							lv_result.setAdapter(adapter);
					}//end of if
					else {
						
				
						if(str_result.equalsIgnoreCase("-3") ||str_result.equalsIgnoreCase("-2") || str_result.equalsIgnoreCase("-1"))
						{
							Toast.makeText(getApplicationContext(), "No Record found!!!", Toast.LENGTH_LONG).show();

							if(dialog != null)
							dialog.dismiss();
							invoiceList.clear();
							adapter = new Opration_list_adapter(getApplicationContext(),invoiceList,opration);
							lv_result.setAdapter(adapter);
						}
						else
						{
					
							
								// For INVOICE
								if(type == 1)
								{
									String[] invoice_list = str_result.split("shimbirow");
									for (int i = 0; i < invoice_list.length; i++) 
									{
										final String[] invoice_record = invoice_list[i].split("shimbicol");
										HashMap<String, String> map = new HashMap<String, String>();	
											map.put("date", invoice_record[0]);
											map.put("invoiceNo", invoice_record[1]);
											map.put("Amount", invoice_record[2]);
											map.put("company_name", invoice_record[3]);
											map.put("status", invoice_record[4]);
											//invoiceList.add(map);
											invoiceList_temp.add(map);
											
											lastID = invoice_record[1];
											
											//System.out.println("Last ID "+lastID);
											
											//page_no = Integer.parseInt(invoice_record[5]);
											//System.out.println("Page No "+ page_no);
											
									}
								}
								
								// For RECEIPT
								else if(type == 2)
								{
						
									String[] invoice_list = str_result.split("shimbirow");
									for (int i = 0; i < invoice_list.length; i++) 
									{
										final String[] invoice_record = invoice_list[i].split("shimbicol");
										HashMap<String, String> map = new HashMap<String, String>();	
											map.put("date", invoice_record[0]);
											map.put("invoiceNo", invoice_record[1]);
											map.put("Amount", invoice_record[2]);
											map.put("receiptNo",invoice_record[3]);
											map.put("company_name", invoice_record[4]);
											//invoiceList.add(map);
											invoiceList_temp.add(map);
											
											lastID = invoice_record[1];
											//System.out.println("Last ID "+lastID);
									}
								}
								
								// For ESTIMATE
								else if(type == 3)
								{
									String[] invoice_list = str_result.split("shimbirow");
									for (int i = 0; i < invoice_list.length; i++) 
									{
										final String[] invoice_record = invoice_list[i].split("shimbicol");
										HashMap<String, String> map = new HashMap<String, String>();	
											map.put("invoiceNo", invoice_record[0]);
											map.put("Amount", invoice_record[1]);
											map.put("date", invoice_record[2]);
											map.put("status",invoice_record[3]);
											map.put("company_name", invoice_record[4]);
											//invoiceList.add(map);
											invoiceList_temp.add(map);
											
											lastID = invoice_record[1];
											//System.out.println("Last ID "+lastID);
											
									}
								}
								
								// For OVERDUE
								else if(type == 4)
								{
									String[] invoice_list = str_result.split("shimbirow");
									for (int i = 0; i < invoice_list.length; i++) 
									{
										final String[] invoice_record = invoice_list[i].split("shimbicol");
										HashMap<String, String> map = new HashMap<String, String>();	
											map.put("date", invoice_record[0]);
											map.put("invoiceNo", invoice_record[1]);
											map.put("Amount", invoice_record[2]);
											map.put("company_name", invoice_record[3]);
											map.put("status", invoice_record[4]);
											//invoiceList.add(map);
											invoiceList_temp.add(map);
											
											lastID = invoice_record[1];
											//System.out.println("Last ID "+lastID);
									}
								}
								
								
						
							
							invoiceList.addAll(invoiceList_temp);
							progressBar.setVisibility(View.GONE);
							
							
							adapter.notifyDataSetChanged();
							
							//adapter = new Opration_list_adapter(getApplicationContext(),invoiceList,opration);
							//lv_result.setAdapter(adapter);
								
							if(dialog != null)
								dialog.dismiss();
							lv_result.setOnItemClickListener(new OnItemClickListener() 
							{
								@Override
								public void onItemClick(AdapterView<?> view, View arg1,int position, long id) 
								{
								// TODO Auto-generated method stub
									showInvoiceDetail(invoiceList.get(position).get("invoiceNo"));			
								}
							}); //end of list listener....	
						}	 //end of else -3
					}// end of else
				}// end of outer if	
				
				
			  }	//end of method
		}// end of  async class
		
		
		
}





