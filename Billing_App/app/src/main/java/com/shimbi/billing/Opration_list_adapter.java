package com.shimbi.billing;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class Opration_list_adapter extends BaseAdapter {

	private Context activity;
	private ArrayList<HashMap<String, String>> data;
	private LayoutInflater mInflator = null;
	private String opration_name;
	Login login=new Login();
	public Opration_list_adapter(Context a, ArrayList<HashMap<String, String>> invoice_list,String opration) {

		activity = a;
		data = invoice_list;
		opration_name=opration;
		mInflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView = mInflator.inflate(R.layout.opration_adapter, null);
		
		TextView tv_product_name=(TextView)convertView.findViewById(R.id.opration_adapter_tv_company_name);
		TextView tv_invoice_id=(TextView)convertView.findViewById(R.id.opration_adapter_tv_product_invoice_id);
		TextView tv_receipts_id=(TextView)convertView.findViewById(R.id.opration_adapter_tv_product_receipts_id);
		TextView tv_invoce_date=(TextView)convertView.findViewById(R.id.opration_adapter_tv_invoice_date);
		TextView tv_invoice_amount=(TextView)convertView.findViewById(R.id.opration_adapter_tv_product_price);
		TextView tv_inovice_status=(TextView)convertView.findViewById(R.id.opration_adapter_tv_open_text);
		HashMap<String, String> ticket = new HashMap<String, String>();
		ticket = data.get(position);
		if(opration_name.equalsIgnoreCase("Invoice"))
		{
			tv_receipts_id.setVisibility(View.VISIBLE);
			tv_product_name.setText(ticket.get("company_name"));
			tv_invoce_date.setText(ticket.get("date"));
			tv_invoice_id.setText(ticket.get("invoiceNo"));
			tv_invoice_amount.setText(ticket.get("Amount")+" ("+Login.Client_code+")");
			String status=ticket.get("status");
			//int s=Integer.parseInt(status);
				
				tv_inovice_status.setText("Open");
			
			
		}
		else if(opration_name.equalsIgnoreCase("Receipt"))
		{
			tv_product_name.setText(ticket.get("company_name"));
			tv_inovice_status.setVisibility(View.VISIBLE);
			tv_invoce_date.setText(ticket.get("date"));
			tv_receipts_id.setText(ticket.get("receiptNo"));
			tv_invoice_amount.setText(ticket.get("Amount")+" ("+Login.Client_code+")");
			tv_invoice_id.setText(ticket.get("invoiceNo"));
		}
		else if(opration_name.equalsIgnoreCase("Estimate"))
		{
			tv_product_name.setText(ticket.get("company_name"));
			tv_receipts_id.setVisibility(View.VISIBLE);
			tv_product_name.setText(ticket.get("company_name"));
			tv_invoce_date.setText(ticket.get("date"));
			tv_invoice_amount.setText(ticket.get("Amount")+" ("+Login.Client_code+")");
			tv_invoice_id.setText(ticket.get("invoiceNo"));
			String status=ticket.get("status");
		//	int s=Integer.parseInt(status);
				
				tv_inovice_status.setText("Open");	
		}
		else if(opration_name.equalsIgnoreCase("Overdue"))
		{
			tv_product_name.setText(ticket.get("company_name"));
			tv_receipts_id.setVisibility(View.VISIBLE);
			tv_product_name.setText(ticket.get("company_name"));
			tv_invoce_date.setText(ticket.get("date"));
			tv_invoice_amount.setText(ticket.get("Amount")+" ("+Login.Client_code+")");
			tv_invoice_id.setText(ticket.get("invoiceNo"));
			String status=ticket.get("status");
		//	int s=Integer.parseInt(status);
				
				tv_inovice_status.setText("Open");	
		}
		return convertView;
	}

	
}
