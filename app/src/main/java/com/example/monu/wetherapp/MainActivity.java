/*
package com.example.monu.wetherapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Context context;
    ArrayList<Event> events = new ArrayList<>();
  */
/*  FirebaseDatabase firebaseDatabase;
    DatabaseReference myref;*//*

    EventAdapter adapter;
    GridView eventGridView;
    //FirebaseAuth auth;
    // FirebaseUser user;
    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    private int year, month, day;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        editor = preferences.edit();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myref = firebaseDatabase.getReference("eventInfo");
        myref.keepSynced(true);

//        user = auth.getCurrentUser();

        eventGridView = findViewById(R.id.eventGridView);
        toolbar = findViewById(R.id.toolbar);

        adapter = new EventAdapter(context,events);
        eventGridView.setAdapter(adapter);

        eventGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = events.get(i);
                Intent intent = new Intent(EventsActivity.this,ExpenseActivity.class);
                intent.putExtra("event",event);
                startActivity(intent);
            }
        });
        eventGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                PopupMenu popup = new PopupMenu(context, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.update_or_delete_menu, popup.getMenu());
                popup.setGravity(Gravity.RIGHT);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.edit_item:
                                updateEvent(i);
                                Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete_item:
                                myref.child(events.get(i).getNodeKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                            adapter.notifyDataSetChanged();
                                        }else {
                                            Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });

        auth = FirebaseAuth.getInstance();

        getDataFromDatabase();

        setSupportActionBar(toolbar);
        othersInitialization();

    }
    private void updateEvent(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.event_creation, null);
        dialogBuilder.setView(dialogView);

        final EditText et_eventName =  dialogView.findViewById(R.id.et_eventName);
        final EditText et_eventDate = dialogView.findViewById(R.id.et_eventDate);
        final EditText et_eventBudget = dialogView.findViewById(R.id.et_eventBudget);
        Button btn_createEvent = dialogView.findViewById(R.id.btn_createEvent);

        et_eventName.setText(events.get(position).getEventName());
        et_eventDate.setText(events.get(position).getEventDate());
        et_eventBudget.setText(events.get(position).getEventBudget());

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    private void othersInitialization(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEventCreationDialogue();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tv_user_name);
        navUsername.setText("Hi "+auth.getCurrentUser().getDisplayName()+" !");
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void showEventCreationDialogue() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.event_creation, null);
        dialogBuilder.setView(dialogView);

        final EditText et_eventName =  dialogView.findViewById(R.id.et_eventName);
        final EditText et_eventDate = dialogView.findViewById(R.id.et_eventDate);
        final EditText et_eventBudget = dialogView.findViewById(R.id.et_eventBudget);
        Button btn_createEvent = dialogView.findViewById(R.id.btn_createEvent);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        et_eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EventsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd, MMM, yyyy");
                        calendar.set(year,month,dayOfMonth);
                        String finalDate = sdf.format(calendar.getTime());
                        et_eventDate.setText(finalDate);
                    }
                }, year, month, day
                );
                datePickerDialog.show();
            }
        });

        btn_createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = et_eventName.getText().toString();
                String eventDate = et_eventDate.getText().toString();
                String eventBudget = et_eventBudget.getText().toString();

                DatabaseReference reference = firebaseDatabase.getReference("eventInfo");

                String key = reference.push().getKey();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Event event = new Event(eventName,eventDate,eventBudget,key,user.getUid());

                reference.child(key).setValue(event).addOnCompleteListener(EventsActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context,"Event Added",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(context,"Event not Added",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                getDataFromDatabase();
                alertDialog.dismiss();
            }
        });
    }
    public void getDataFromDatabase(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Query query = reference.child("eventInfo").orderByChild("user_id").equalTo(currentUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for (DataSnapshot items : dataSnapshot.getChildren()){

                    Event event = items.getValue(Event.class);
                    events.add(event);
                    adapter.notifyDataSetChanged();

                    if (events.size()>=0){
                        ((TextView) findViewById(R.id.tv_dummyText)).setVisibility(View.GONE);
                    }else {
                        Toast.makeText(EventsActivity.this, "No event record.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(context,"Lo",Toast.LENGTH_LONG).show();

            }
        });
    }
}
*/
