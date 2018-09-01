package com.example.arvind.toizone;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class ReviewDisplay extends AppCompatActivity {

    String link;
    boolean connected = false;

    private RecyclerView recyclerView;
    private DatabaseReference myref;
    Toolbar toolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display);
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
        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setTitleTextColor(Color.WHITE);
        toolbar2.setTitle("Reviews");
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        link=getIntent().getExtras().getString("ptlink");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myref = FirebaseDatabase.getInstance().getReference().child("raipur").child(link).child("/Reviews");

        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String temp = map.get("name");
                Log.v("TAG", "MSGISinside:" + temp);
                String temp1 = map.get("rev");
                Log.v("TAG", "MSGISinside:" + temp1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.individual_row,
                BlogViewHolder.class,
                myref){

            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setRevi(model.getRev());
                viewHolder.setStar(model.getRating());
                viewHolder.setDateval(model.getDate());
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

            }
        };

        recyclerView.setAdapter(adapter);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        TextView text,revi,date11;
        RatingBar stardisp;
        public BlogViewHolder(View itemview1){
            super(itemview1);
            text=(TextView)itemview1.findViewById(R.id.name11);
            revi=(TextView)itemview1.findViewById(R.id.rev11);
            date11=(TextView)itemview1.findViewById(R.id.date11);
            stardisp=(RatingBar)itemview1.findViewById(R.id.stardisp);
        }
        public void setName(String name)
        {
            if(name!=null) {
                text.setText(name + "");
            }
            else {
                text.setText("N/A");
            }
        }
        public void setRevi(String rev)
        {
            if(rev!=null) {
            revi.setText(rev+"");
            }
            else {
                revi.setText("N/A");
            }
        }
        public void setDateval(String dateval)
        {
            if(dateval!=null) {
            date11.setText(dateval+"");}
            else {
                date11.setText("N/A");
            }
        }
        public void setStar(String rating)
        {
            if(rating!=null) {
                Float temp = Float.parseFloat(rating);
                stardisp.setRating(temp);
                stardisp.setEnabled(false);
            }
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



}
