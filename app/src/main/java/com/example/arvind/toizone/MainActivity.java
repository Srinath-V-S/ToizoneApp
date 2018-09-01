package com.example.arvind.toizone;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.collection.LLRBNode;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeReader;
import com.karan.churi.PermissionManager.PermissionManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import at.markushi.ui.CircleButton;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    private static int GALLERY_INTENT = 2;

    String link;
    int j = 0;
    double ratt,sum;

    TextView ptname;
    Button went;
    TextView phno;
    TextView facildisp;
    TextView ohdisp;
    TextView pricedisp;
    TextView addisp;
    TextView ratdisp;
    TextView ratedisp, ratendisp;
    CircleButton complain;
    String deviceid, phone, gone, spl;
    ImageView coverpic;
    BarChart barChart;
    CheckedTextView splfeatures;
    String name, lat, lng,wentval;
    int count;
    boolean connected = false;
    int ratingcalculator;
    ArrayList<String> rating = new ArrayList<String>(1);
    ArrayList<Double> ratingdouble = new ArrayList<Double>(1);

    Toolbar toolbar;

    private Firebase mref, mref1, mref2;
    private StorageReference mstore;
    Map<String, String> map;
    Map<String, String> map1;
    Map<String, Integer> map2;
    Map<String, String> mapint;
    ArrayList<Integer> arrayList = new ArrayList<Integer>(1);
    int xmin, xmax, xnew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
         toolbar = (Toolbar) findViewById(R.id.toolbar);
    //    setSupportActionBar(toolbar);
     //   if (getSupportActionBar() != null) {
      //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // }

        link = getIntent().getExtras().getString("ptlink");
        //link="Toilet 1";

        final Activity activity = this;

        ptname = (TextView) findViewById(R.id.ptname);
        phno = (TextView) findViewById(R.id.phno);
        facildisp = (TextView) findViewById(R.id.facildisp);
        ohdisp = (TextView) findViewById(R.id.ohdisp);
        pricedisp = (TextView) findViewById(R.id.pricedisp);
        addisp = (TextView) findViewById(R.id.addisp);
        ratdisp = (TextView) findViewById(R.id.ratdisp);
        // ratendisp = (TextView) findViewById(R.id.ratendisp);
        complain = (CircleButton) findViewById(R.id.complain);
        coverpic = (ImageView) findViewById(R.id.coverpic);
        barChart = (BarChart) findViewById(R.id.graph);
        splfeatures = (CheckedTextView) findViewById(R.id.splfeatures);
        went = (Button) findViewById(R.id.went);

        deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        mref = new Firebase("https://ptlocator.firebaseio.com/raipur/" + link + "/details");
        mref1 = new Firebase("https://ptlocator.firebaseio.com/raipur/" + link + "/Reviews");
        mref2 = new Firebase("https://ptlocator.firebaseio.com/raipur/" + link + "/peak");
        mstore = FirebaseStorage.getInstance().getReference();


        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map = dataSnapshot.getValue(Map.class);
                String imgu = map.get("img");
                Picasso.with(MainActivity.this).load(imgu).into(coverpic);
                String facil = map.get("facil");
                facildisp.setText(facil + "\n");
                String oh = map.get("opening");
                ohdisp.setText(oh);
                lat = map.get("lat");
                lng = map.get("lng");
                name = map.get("name");
                toolbar.setTitle(name);
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                ptname.setText(name);
                phone = map.get("phno");
                phno.setText(phone);
                String pri = map.get("price");
                pricedisp.setText(pri);
                String spl = map.get("add");
                if(spl!=null) {
                    splfeatures.setText("☑ " + spl);
                }
                String add = map.get("address");
                addisp.setText(add);
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("raipur").child(link).child("Reviews").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for(com.google.firebase.database.DataSnapshot d:dataSnapshot.getChildren()) {
                    if (d.child("rating").getValue() != null) {
                        rating.add(j, d.child("rating").getValue().toString());
                        // Log.v("TAG","INSIDEFOR:"+rating.get(j));
                        ratingdouble.add(j, Double.parseDouble(rating.get(j)));
                        Log.v("TAG", "INSIDEFOR:" + ratingdouble.get(j));
                        sum += ratingdouble.get(j);
                        j++;
                        count++;
                        Log.v("TAG", "INSIDEFORCOUNT:" + count);
                    }
                }
                setrating();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
       /* for(int i=0;i<ratingdouble.size();i++)
        {
            Log.v("TAG","CHECKINGVALUES"+ratingdouble.get(i));
            sum+=ratingdouble.get(i);
        }*/


        progressDialog.dismiss();




/*
        mref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String srating = map.get("rating");
                rating.add(j,srating);j++;
                for(int i=0;i<rating.size();i++)
                {
                    ratingdouble.add(i,Double.parseDouble(rating.get(i)));
                }
                for(int i=0;i<rating.size();i++)
                {
                   sum+=ratingdouble.get(i);
                }
                double ratt=sum/(j+1);


                //gone = map.get("went");

                if (ratt < 2.0) {
                    ratdisp.setBackgroundColor(Color.parseColor("#F01515"));
                    ratdisp.setTextColor(Color.parseColor("#FFFFFF"));
                    ratdisp.setText(ratt+"");
                } else if (ratt > 2.0 && ratt < 4.0) {
                    ratdisp.setBackgroundColor(Color.parseColor("#FFF700"));
                    ratdisp.setTextColor(Color.parseColor("#FFFFFF"));
                    ratdisp.setText(ratt+"");
                } else {
                    ratdisp.setBackgroundColor(Color.parseColor("#12F312"));
                    ratdisp.setTextColor(Color.parseColor("#FFFFFF"));
                    ratdisp.setText(ratt+"");
                }
                progressDialog.dismiss();

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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
*/
        went.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();

            }
        });









        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map2 = dataSnapshot.getValue(Map.class);
                int num=map2.get("00");
                int num1=map2.get("04");
                int num2=map2.get("06");
                int num3=map2.get("07");
                int num4=map2.get("08");
                int num5=map2.get("09");
                int num6=map2.get("10");
                int num7=map2.get("14");
                int num8=map2.get("18");
                int num9=map2.get("20");
                int num10=map2.get("23");

                arrayList.add(0,num);
                arrayList.add(1,num1);
                arrayList.add(2,num2);
                arrayList.add(3,num3);
                arrayList.add(4,num4);
                arrayList.add(5,num5);
                arrayList.add(6,num6);
                arrayList.add(7,num7);
                arrayList.add(8,num8);
                arrayList.add(9,num9);
                arrayList.add(10,num10);

                xmin=arrayList.get(0);
                xmax=arrayList.get(0);

                for (int i=1;i<arrayList.size();i++)
                {
                    if(xmin>arrayList.get(i))
                    {
                        xmin=arrayList.get(i);
                    }

                }
                Log.v("TAG","MINIS:"+xmin);
                for (int i=1;i<arrayList.size();i++)
                {
                    if(xmax<arrayList.get(i))
                    {
                        xmax=arrayList.get(i);
                    }

                }
                Log.v("TAG","MAXIS:"+xmax);

               // xnew=(num-xmin)/(xmax-num)
                float div=xmax-xmin;
                Log.v("TAG","DIVIS:"+div);
                float dnum,dnum1,dnum2,dnum3,dnum4,dnum5,dnum6,dnum7,dnum8,dnum9,dnum10;
                dnum=((num-xmin)/div)*10;
                dnum1=((num1-xmin)/div)*10;
                Log.v("TAG",dnum+"");
                dnum2=((num2-xmin)/div)*10;
                dnum3=((num3-xmin)/div)*10;
                dnum4=((num4-xmin)/div)*10;
                dnum5=((num5-xmin)/div)*10;
                dnum6=((num6-xmin)/div)*10;
                dnum7=((num7-xmin)/div)*10;
                dnum8=((num8-xmin)/div)*10;
                dnum9=((num9-xmin)/div)*10;
                dnum10=((num10-xmin)/div)*10;



                final ArrayList<BarEntry> barEntries=new ArrayList<>();
                barEntries.add(new BarEntry(((num-xmin)/div)*10,0));
               // Log.v("TAG",(((num7-xmin)/(xmax-xmin))+""));
                barEntries.add(new BarEntry((int)dnum1,1));
                barEntries.add(new BarEntry((int)dnum2,2));
                barEntries.add(new BarEntry((int)dnum3,3));
                barEntries.add(new BarEntry((int)dnum4,4));
                barEntries.add(new BarEntry((int)dnum5,5));
                barEntries.add(new BarEntry((int)dnum6,6));
                barEntries.add(new BarEntry((int)dnum7,7));
              //  Log.v("TAG",((num7-xmin)/(xmax-xmin)+""));
                barEntries.add(new BarEntry((int)dnum8,8));
                barEntries.add(new BarEntry((int)dnum9,9));
                barEntries.add(new BarEntry((int)dnum10,10));
                BarDataSet barDataSet=new BarDataSet(barEntries,"Crowd");

                ArrayList<String>dates=new ArrayList<>();
                dates.add("12AM-04AM");
                dates.add("04AM-06AM");
                dates.add("06AM");
                dates.add("07AM");
                dates.add("08AM");
                dates.add("09AM");
                dates.add("10AM");
                dates.add("10AM-02PM");
                dates.add("02PM-06PM");
                dates.add("06PM-08PM");
                dates.add("08PM-11PM");
                BarData barData=new BarData(dates,barDataSet);
                barChart.setData(barData);
                barChart.isShown();
                barChart.invalidate();
                barChart.setTouchEnabled(true);


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });



        /*
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                map = dataSnapshot.getValue(Map.class);
                String facil = map.get("facil")+"\n";
                facildisp.setText(facil);
                String oh = map.get("opening");
                ohdisp.setText(oh);
                String name=map.get("name");
                ptname.setText(name);
                String phone=map.get("phno");
                phno.setText(phone);


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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/



    }

    public void setrating()
    {
        Log.v("TAG","TOTALSUMVALUEIS"+sum);
        ratt=sum/count;

        if (ratt <= 2.0) {
            //ratdisp.setBackgroundColor(Color.parseColor("#F01515"));
           // ratdisp.setTextColor(Color.parseColor("#000000"));
            ratdisp.setTextColor(Color.parseColor("#F01515"));
            ratdisp.setText("★"+new DecimalFormat("#.0").format(ratt)+"");
        } else if (ratt > 2.0 && ratt < 4.0) {
            //ratdisp.setBackgroundColor(Color.parseColor("#FFF700"));
           // ratdisp.setTextColor(Color.parseColor("#000000"));
            ratdisp.setTextColor(Color.parseColor("#FFC300"));
            ratdisp.setText("★"+new DecimalFormat("#.0").format(ratt)+"");
        } else {
           // ratdisp.setBackgroundColor(Color.parseColor("#12F312"));
            //ratdisp.setTextColor(Color.parseColor("#000000"));
            ratdisp.setTextColor(Color.parseColor("#008000"));
            ratdisp.setText("★"+new DecimalFormat("#.0").format(ratt)+"");
        }
    }



    public void sendcomplain(View view) {
        Intent intent = new Intent(MainActivity.this, Complain.class);
        startActivity(intent);
    }
    public void seerev(View view) {
        Intent intent = new Intent(MainActivity.this, ReviewDisplay.class);
        intent.putExtra("ptlink",link);
        startActivity(intent);
    }
    public void gofunc(View view) {
        String format = "geo:0,0?q=" + lat + "," + lng + "( Location title)";
        Uri uri = Uri.parse(format);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void addReview(View view) {
        Intent intent = new Intent(MainActivity.this, Review.class);
        intent.putExtra("ptlink",link);
        startActivity(intent);
    }
    public void more(View view) {
        Intent intent = new Intent(MainActivity.this, ImageDisplay.class);
        intent.putExtra("ptlink",link);
        startActivity(intent);

    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phone));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }
    public void viewinmap(View view)
    {
      //  Toast.makeText(getApplicationContext(),"inside",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("ptlat",lat);
        intent.putExtra("ptlng",lng);
        intent.putExtra("ptname",name);
        startActivity(intent);

    }


    public void addPhoto(View view){
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            Uri uri=data.getData();
            Random r=new Random();
            int num=0;
            for(int i=0;i<10000;i++)
            {
                num=r.nextInt(10000);
            }
            StorageReference filepath=mstore.child("userpics").child(link).child("file"+num);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String duri =taskSnapshot.getDownloadUrl().toString();

                    mref1=new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/Reviews/"+deviceid);
                    Firebase child=mref1.child("image");
                    child.setValue(duri);

                    Toast.makeText(getApplication(),"completed",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });

        }
   //     else if (requestCode==0 && resultCode==RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                } else {
                    wentval = result.getContents();

                  //  Toast.makeText(getApplicationContext(),result.getContents(),Toast.LENGTH_SHORT).show();

                    if(result.getContents().equals(link)) {

                        mref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Integer> map2;
                                map2 = dataSnapshot.getValue(Map.class);

                                int temp = map2.get("went");
                                // int itemp;
                                temp++;
                                Log.v("TAG", "MSGSISCNT:" + temp);
                                // temp=Integer.parseInt(cnts);

                                mref.child("went").setValue(temp);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        Toast.makeText(getApplication(),"Successfully Registered Your visit",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Please select "+result.getContents()+" marker and update",Toast.LENGTH_LONG).show();

                }
            } else
                super.onActivityResult(requestCode, resultCode, data);

        //}

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
