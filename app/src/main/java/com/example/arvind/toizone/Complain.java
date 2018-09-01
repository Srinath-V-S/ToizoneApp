package com.example.arvind.toizone;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Complain extends AppCompatActivity  {

    private EditText TextSubject;
    private EditText TextMessage;
    private EditText username;
    private EditText mailaccount;
    private Button buttonSend;
    boolean connected = false;
    Toolbar toolbar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        checkcon();
        if (connected==false)
        {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "No Internet Connection", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkcon();
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }
        toolbar3 = (Toolbar) findViewById(R.id.toolbar3);
        toolbar3.setTitleTextColor(Color.WHITE);
        toolbar3.setTitle("Complain");
        setSupportActionBar(toolbar3);


        TextSubject = (EditText) findViewById(R.id.editTextSubject);
        TextMessage = (EditText) findViewById(R.id.editTextMessage);
        username=(EditText)findViewById(R.id.Name);
        mailaccount=(EditText)findViewById(R.id.mail);
        buttonSend=(Button)findViewById(R.id.buttonSend);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextSubject.getText().toString().length()==0) {
                    TextSubject.setError("subject is required");
                }

                else if(TextMessage.getText().toString().length()==0) {
                    TextMessage.setError("please give feedback");


                }
                else if(username.getText().toString().length()==0) {
                    username.setError("please enter your name");
                }

                else if(mailaccount.getText().toString().length()==0) {
                    mailaccount.setError("please enter your mail");
                }
                else
                {
                    sendEmail();
                }

            }
        });
    }
    private void sendEmail() {


        String email="bheem.karthik007@gmail.com";
        String subject = TextSubject.getText().toString().trim();
        String message = TextMessage.getText().toString().trim();
        String Name= username.getText().toString().trim();
        String sender= mailaccount.getText().toString().trim();
        if(!isEmailValid(sender))
        {
            mailaccount.setError("Invaild mail Id");
        }
        else {
            SendMail sm = new SendMail(this, email, subject, message, Name, sender);


            sm.execute();
        }
    }
    public void checkcon(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
