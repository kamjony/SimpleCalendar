package org.lanaeus.weeklycalendarwithsqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.lanaeus.weeklycalendarwithsqlite.CalendarUtils.daysInWeekArray;
import static org.lanaeus.weeklycalendarwithsqlite.CalendarUtils.monthYearFromDate;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearTV;
    private RecyclerView calendarRV;
    private ListView eventLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        calendarRV = findViewById(R.id.rvCalendar);
        monthYearTV = findViewById(R.id.tvMonthYear);
        eventLV = findViewById(R.id.lvEvent);
        setWeekView();
        loadFromDBToMemory();
        setOnListClickListener();

    }

    private void setOnListClickListener() {
        eventLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Event selectedEvent = (Event) eventLV.getItemAtPosition(position);
                Intent editEventIntent = new Intent(getApplicationContext(), EventEditActivity.class);
                editEventIntent.putExtra(Event.EVENT_EDIT_EXTRA, selectedEvent.getId());
                startActivity(editEventIntent);
            }
        });
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateEventListArray();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setWeekView() {
        monthYearTV.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRV.setLayoutManager(layoutManager);
        calendarRV.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventLV.setAdapter(eventAdapter);

    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    public void createEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    @Override
    public void onItemClick(int adapterPosition, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
}