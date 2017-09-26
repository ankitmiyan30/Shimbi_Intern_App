package com.shimbi.billing;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import android.widget.AdapterView.OnItemClickListener;

public class Popup extends PopupWindow {  
	  
Context context;  
ArrayAdapter<String> opration_adapter;
ListView lv_opration;
static ArrayList<String> opration_list;
  
private int dx;  
private int dy;  
int width,height;
   
public Popup(Context ctx,int pop_width,int pop_height) 
{  	
					super(ctx);  
					context = ctx;  
					setContentView(LayoutInflater.from(context).inflate(R.layout.opration_menu, null));
					width=pop_width;
					height=pop_height;
					setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);  
					setWidth(pop_width);  
					View popupView = getContentView();  
					setFocusable(true);  
					setOutsideTouchable(true);
					setTouchable(true);
					lv_opration=(ListView)popupView.findViewById(R.id.opration_menu_lv_opration_list);
					opration_adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,opration_list);
					lv_opration.setAdapter(opration_adapter);
					lv_opration.setScrollContainer(false);
					
					
					lv_opration.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
						{
								// TODO Auto-generated method stub
							String opration=opration_list.get(arg2);
							//Toast.makeText(getApplicationContext(), opration, Toast.LENGTH_LONG).show();
							Intent i=new Intent(context, Selected_Opration_list.class);
							i.putExtra("opration",opration);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(i);
							dismiss();
						}
					});
					lv_opration.setOnTouchListener(new OnTouchListener() {

					    public boolean onTouch(View v, MotionEvent event) {
					        if (event.getAction() == MotionEvent.ACTION_MOVE) {
					            return true; // Indicates that this has been handled by you and will not be forwarded further.
					        }
					        return false;
					    }
					});
 
  
// setOnTouchListener is to add drag and drop the popup window.  
// If you didn't want, you can remove it.  
		/*popupView.setOnTouchListener(new OnTouchListener() {  
			
			@Override
 		public boolean onTouch(View arg0, MotionEvent motionEvent) {
	 			// TODO Auto-generated method stub
	 			switch (motionEvent.getAction()) 
	 			{  
	 			case MotionEvent.ACTION_DOWN:  dx = (int) motionEvent.getRawX();  
	 										   dy = (int) motionEvent.getRawY();  
	 										   break;  
	 			case MotionEvent.ACTION_MOVE:  int x = (int) motionEvent.getRawX();  
	 										   int y = (int) motionEvent.getRawY();  
	 										   int left = (x - dx);  
	 										   int top = (y - dy);  
	 										   update(left, top, -1, -1);  
	 										   break;  
	 			}  
	return true;
			}  
		});  */
}  
  
		public void show(View v) 
		{  
			showAsDropDown(v,-5,0);  
		}
}
