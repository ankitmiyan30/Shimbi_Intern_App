package com.example.sliderdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private String[] menus;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.menu);


        menus = new String[]{"Primary", "Social", "Send", "Important", "Outbox"};
        drawerLayout = (DrawerLayout) findViewById(R.id.d_layout);
        listView = (ListView) findViewById(R.id.left_slider);

        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, menus);
        listView.setAdapter(arrayAdapter);
        listView.setSelector(android.R.color.holo_blue_bright);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                drawerLayout.closeDrawers();
                Bundle bundle = new Bundle();
                bundle.putString("menus", menus[position]);

                Fragment details = new DetailsFragment();
                details.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, details).commit();
            }
        });
    }
}

