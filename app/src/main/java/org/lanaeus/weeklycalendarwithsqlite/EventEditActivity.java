package org.lanaeus.weeklycalendarwithsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalTime;
import java.util.Date;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private Button deleteBtn;

    private LocalTime time;
    private Event selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        eventNameET = findViewById(R.id.etEventName);
        eventDateTV = findViewById(R.id.tvEventDate);
        eventTimeTV = findViewById(R.id.tvEventTime);
        deleteBtn = findViewById(R.id.btnEventDelete);

        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));

        checkForEditEvent();
    }

    private void checkForEditEvent(){
        Intent previousIntent = getIntent();

        int passedEventID = previousIntent.getIntExtra(Event.EVENT_EDIT_EXTRA, -1);
        selectedEvent = Event.getEventForID(passedEventID);

        if (selectedEvent!=null){
            eventNameET.setText(selectedEvent.getName());
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void saveEventAction(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String eventName = eventNameET.getText().toString();

        if(selectedEvent == null) {
            int id = Event.eventsList.size();
            Event newEvent = new Event(id, eventName, CalendarUtils.selectedDate, time);
            Event.eventsList.add(newEvent);
            sqLiteManager.addEventToDatabase(newEvent);
        } else {
            selectedEvent.setName(eventName);
            sqLiteManager.updateEventInDatabase(selectedEvent);
        }

        finish();
    }

    public void deleteEventAction(View view) {
        selectedEvent.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateEventInDatabase(selectedEvent);
        finish();
    }
}