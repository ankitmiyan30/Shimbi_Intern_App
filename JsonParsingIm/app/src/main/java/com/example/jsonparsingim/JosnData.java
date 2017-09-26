package com.example.jsonparsingim;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class JosnData extends AppCompatActivity {

    private ListView list;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_josn_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url="http://sb2.urdemo.net/android_webservice/assignment/assignment_list";
        StringRequest  stringRequest=new StringRequest(url,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(JosnData.this,"onResponse",Toast.LENGTH_LONG).show();
                String []data=parseDataArray(response);
                populatedListView(data);
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(JosnData.this,"onErrorMessage",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.json_parsing));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

    }

    private void populatedListView(String[] data) {
        list= (ListView) findViewById(R.id.json_data);
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        list.setAdapter(stringArrayAdapter);
    }

    private String[] parseDataArray(String response)  {
        JSONArray jsonArray=null;
        String []emails=null;
        try
        {
            JSONObject jsonObject=new JSONObject(response);
            String page=jsonObject.getString("total_pages");
            String no_of_record=jsonObject.getString("no_of_record");
            String syn_date=jsonObject.getString("syncdate");

            jsonArray=jsonObject.getJSONArray("result");

            emails=new String[jsonArray.length()];
            for(int i=0;i<jsonArray.length();i++)
            {
                    JSONObject object=jsonArray.getJSONObject(i);
                    String event_id=object.getString("event_id");
                    String no_reply=object.getString("no_reply");
                    String username=object.getString("username");
                    String description=object.getString("description");
                    String added_date=object.getString("added_date");
                    //Stroning in array
                    emails[i]="Page No : "+page+"\nNo of Record: "+no_of_record+"\nSync Date : "+syn_date+"\nEvent Id : "+event_id+"\nUser Name : "+username+"\nNo Reply : "+
                            no_reply+"\nDescription : "+description+"\nAdded Data : "+added_date+"\n\n\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emails;
    }
}
