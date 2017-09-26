package com.example.jsonparshing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String TAG=MainActivity.class.getSimpleName();
    private ListView tv;
    private List<ArrayList> list=new ArrayList<>();
    private ArrayList<String> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (ListView) findViewById(R.id.display_data);
        contactList=new ArrayList<>();
        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void , Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url=" private ProgressBar progress;\n" +
                    "    private TextView text;";
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            JSONArray jsonArray =null;
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String page=jsonObj.getString("total_pages");
                    String no_of_record=jsonObj.getString("no_of_record");
                    String syn_date=jsonObj.getString("syncdate");

                    // Getting JSON Array node
                    jsonArray = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);
                        String id = jsonArrayJSONObject.getString("event_id");
                        String desc = jsonArrayJSONObject.getString("description");
                        String name = jsonArrayJSONObject.getString("username");

                        /*HashMap<String, String> data = new HashMap<>();
                        // adding each child node to HashMap key => value
                        data.put("id", id);
                        data.put("name", name);
                        data.put("desc", desc);
                        // adding contact to contact list*/

                        contactList.add(id);
                        contactList.add(name);
                        contactList.add(desc);
                        list.add(contactList);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter arrayAdapter;
            for(int i=0;i<list.size();i++) {
                arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_item, R.id.id, list);
                tv.setAdapter(arrayAdapter);
            }
        }
    }
}













