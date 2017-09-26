package com.shimbi.billing;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Opration_Menu extends Activity {
	ArrayList<String> list;
	ArrayAdapter<String> adapter;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.opration_menu);
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
		list=new ArrayList<String>();
		list.add("INVOICE");
		list.add("ESTIMATE");
		list.add("RECEIPT");
		list.add("Overdue");
		lv=(ListView)findViewById(R.id.opration_menu_lv_opration_list);
		lv.setScrollContainer(false);
		adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,list);
		lv.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.opration__menu, menu);
		return true;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}
	@Override
	public void onAttachedToWindow() {
	    super.onAttachedToWindow();

	    View view = getWindow().getDecorView();
	    WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    lp.gravity = Gravity.LEFT|Gravity.TOP ;
	  
	    lp.x = -100;
	    lp.y =display.getHeight()/10;
	    int h=display.getHeight()/2;
	    int w=display.getWidth()/2;
	    lp.width=w;
	    lp.height=h;
	  
	    getWindowManager().updateViewLayout(view, lp);
	}

}
