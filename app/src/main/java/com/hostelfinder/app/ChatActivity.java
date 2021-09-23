package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hostelfinder.app.adapter.MessageListAdapter;
import com.hostelfinder.app.model.Chat;
import com.hostelfinder.app.model.Conversation;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private RecyclerView rvChat;
    private MessageListAdapter messageListAdapter;
    private List<Chat> chats;
    private FirebaseFirestore db;
    private String me, them, documentId, hostelDocumentId;

    private EditText etChat;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Toolbar toolbar =  findViewById(R.id.toolbar_gchannel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        //Retrieves the user ids from intents and compares them with current user id
        //if the retrieved id matches with id of the user that id is assigned to "me"
        //else it is assigned to "them"
        if (getIntent().getStringExtra("userOne").equalsIgnoreCase(user.getUid())){
            me = getIntent().getStringExtra("userOne");
            them = getIntent().getStringExtra("userTwo");
        }else{
            them = getIntent().getStringExtra("userOne");
            me = getIntent().getStringExtra("userTwo");
        }

        //Retrieves conversation id from intent, this id will be used to add chat messages
        documentId = getIntent().getStringExtra("conversationId");
        etChat = findViewById(R.id.etChat);
        chats = new ArrayList<>();
        rvChat = findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        messageListAdapter = new MessageListAdapter(this, chats, user.getUid());
        rvChat.setAdapter(messageListAdapter);

        //Listens to realtime updates for the conversation id
        db.collection("conversations").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                chats.clear();
                Conversation conversation = value.toObject(Conversation.class);
                chats.addAll(conversation.chat);
                hostelDocumentId = conversation.hostelId;
                messageListAdapter.notifyDataSetChanged();
                rvChat.smoothScrollToPosition(chats.size() - 1);
            }
        });


        findViewById(R.id.btnSend).setOnClickListener(v->{
            if (etChat.getText().toString().isEmpty()){
                etChat.setError("Please enter a message");
                return;
            }
            sendMessage(etChat.getText().toString());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = day;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        /**
         textView.setText("Year: " + myYear + "\n" +
         "Month: " + myMonth + "\n" +
         "Day: " + myday + "\n" +
         "Hour: " + myHour + "\n" +
         "Minute: " + myMinute);

         **/
        String dateTime = myYear+"/"+myMonth+"/"+myday+" "+myHour+":"+myMinute;
        setAppointment(dateTime);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_appointment) {
            new AlertDialog.Builder(this)
                    .setTitle("Appointment")
                    .setMessage("Would you like to make an appointment to come in for a visit?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pickDateTime();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("No", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pickDateTime(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ChatActivity.this, this,year, month,day);
        datePickerDialog.show();
    }
    /**
     * Sends message to document id
     * @param messageText
     */
    private void sendMessage(String messageText){
        DocumentReference conversationId = db.collection("conversations").document(documentId);

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> message = new HashMap<>();

        message.put("messages", messageText);
        message.put("sender", me);
        message.put("receiver", them);
        data.put("chat",  FieldValue.arrayUnion(message));
        conversationId.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    etChat.setText("");
                }
            }
        });

    }

    private void setAppointment(String dateTime){
        DocumentReference conversationId = db.collection("hostel").document(hostelDocumentId);

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> message = new HashMap<>();

        message.put("dateTime", dateTime);
        message.put("sender", me);
        message.put("senderName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        data.put("appointments",  FieldValue.arrayUnion(message));
        conversationId.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Appointment has been set", Toast.LENGTH_SHORT).show();
//                    etChat.setText("");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}