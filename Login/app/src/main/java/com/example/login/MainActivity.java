package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;
import java.util.Map;
public class MainActivity extends AppCompatActivity {

    private String uid, pwd;
    private static int attempt = 3;
    public static String url = "http://sb2.urdemo.net/android_webservice/login/login_intern";
    private EditText userid, password;
    private TextView attemptview;
    private Button loginButton, cancelButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.loginButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        userid = (EditText) findViewById(R.id.loginid);
        password = (EditText) findViewById(R.id.password);
        attemptview = (TextView) findViewById(R.id.attempt);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                uid = userid.getText().toString();
                pwd = password.getText().toString();
                if (uid.equals("")) {
                    userid.setHint("please enter user id");
                } else if (pwd.equals("")) {
                    password.setHint("password can't be null");
                } else if (pwd.length() < 6) {
                    password.setText("");
                    password.setHint("password should be minimum 6 character");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("log_user", s);
                            String result = null;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(s);
                                result = json.getString("login");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (result.equals("sucessful")) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                attempt--;
                                if (attempt == 0) {
                                    onDestroy();
                                } else {
                                    attemptview.setText("Attempts left  : " + attempt);
                                    userid.setHint("wrong user id");
                                    userid.getCurrentHintTextColor();
                                    password.setHint("wrong password");
                                    password.getCurrentHintTextColor();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map getParams() throws AuthFailureError {
                            Map<String, String> params = new Hashtable<>();
                            params.put("user_name", uid);
                            params.put("password", pwd);
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Login");
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid.setText("");
                password.setText("");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
