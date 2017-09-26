package com.example.sakalnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewsActivity extends AppCompatActivity {

    Button login, cancel;
    private String user_id, user_password;
    private EditText login_id, password;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        textView = (TextView) findViewById(R.id.attempt);
        login_id = (EditText) findViewById(R.id.loginid);
        password = (EditText) findViewById(R.id.user_password);
        login = (Button) findViewById(R.id.loginButton);
        cancel = (Button) findViewById(R.id.cancelButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = login_id.getText().toString();
                user_password = password.getText().toString();
                if (user_id.equals("") && user_password.equals("")) {
                    login_id.setHint("user_id_can't_be_null");
                    password.setHint("password_can't_be_null");
                } else if (user_id.equals("")) {
                    login_id.setHint("user_id_can't_be_null");
                } else if (user_password.equals("")) {
                    password.setHint("password_can't_be_null");
                } else if (user_password.length() < 2) {
                    password.setHint("password_should_be_>_8Char");
                } else if (user_id.equals("abc") && user_password.equals("abc")) {
                    Intent intent = new Intent(AddNewsActivity.this, AddDetails.class);
                    startActivity(intent);
                } else {
                    textView.setText("Login Failed Please Try Again");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_id.setText("");
                password.setText("");
            }
        });
    }
}
