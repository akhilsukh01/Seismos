package com.akhilsukh01.earthquakepreparednessproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class message extends Activity {

//    private EditText userName;
//    private EditText userMail;
//    private EditText userPhone;
    private EditText userZip;
//    private EditText userMessage;
//    private Button userSend1;
    private Button userSend2;

    private DatabaseReference mDatabase;
    private DatabaseReference zDatabase;

    private static final String TAG = "PrepInfo";

    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    private ArrayList<String> zipArrayList=new ArrayList<>();
    private TinyDB tinydb;
    private String newToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( message.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);
                zDatabase = FirebaseDatabase.getInstance().getReference().child("Zip").child(newToken).child(currentDateTimeString);

            }
        });

//        final String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + newToken);
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(token).child(currentDateTimeString);
//        zDatabase = FirebaseDatabase.getInstance().getReference().child("Zip").child(newToken).child(currentDateTimeString);


//        userName = (EditText) findViewById(R.id.userName);
//        userMail = (EditText) findViewById(R.id.userMail);
//        userPhone = (EditText) findViewById(R.id.userPhone);
//        userMessage = (EditText) findViewById(R.id.userMessage);
//        userSend1 = (Button) findViewById(R.id.userSend1);
        userZip = findViewById(R.id.userZip);
        userSend2 = findViewById(R.id.userSend2);
        final ListView listView= findViewById(R.id.listView);


//        userSend1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String mName = userName.getText().toString();
//                String mMail = userMail.getText().toString();
//                String mPhone = userPhone.getText().toString();
//                String mMessage = userMessage.getText().toString();
//
//                if(mMail.equals("")|| mMessage.equals("")){
//                    Toast.makeText(message.this, "Please Fill out the Necessary Fields", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    mDatabase.child("Name").setValue(mName);
//                    mDatabase.child("Mail").setValue(mMail);
//                    mDatabase.child("Phone").setValue(mPhone);
//                    mDatabase.child("Message").setValue(mMessage);
//                    mDatabase.child("Token").setValue(token);
//
//                    Toast.makeText(message.this, "Sent", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        tinydb = new TinyDB(getApplicationContext());
        zipArrayList = tinydb.getListString("MyUsers");
        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,R.layout.card_zip,zipArrayList);
        //assign adapter to listview
        listView.setAdapter(arrayAdapter);

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(message.this,"clicked item:"+i+" "+ zipArrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                zipArrayList.remove(i);
                tinydb.putListString("MyUsers", zipArrayList);
                listView.setAdapter(arrayAdapter);
                Toast.makeText(message.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });


        userSend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mZip = userZip.getText().toString();

                if(mZip.length()!=5){
                    Toast.makeText(message.this, "Please check your zip-code. It should be 5 digits long.", Toast.LENGTH_SHORT).show();
                }
                else {
                    zDatabase.child("Zip").setValue(mZip);
                    zDatabase.child("Token").setValue(newToken);

                    zipArrayList.add(mZip);
                    tinydb.putListString("MyUsers", zipArrayList);
                    listView.setAdapter(arrayAdapter);

                    Toast.makeText(message.this, "Added", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(message.this, "You will start receiving Earthquake alerts in your area", Toast.LENGTH_LONG).show();
            }
        });

    }
}
