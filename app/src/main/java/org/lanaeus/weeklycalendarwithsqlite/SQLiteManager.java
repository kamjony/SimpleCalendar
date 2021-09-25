package org.lanaeus.weeklycalendarwithsqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "calenderDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "event";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DATE_FIELD = "date";
    private static final String TIME_FIELD = "time";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null){
            sqLiteManager = new SQLiteManager(context);
        }
        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DATE_FIELD)
                .append(" TEXT, ")
                .append(TIME_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addEventToDatabase(Event event){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, event.getId());
        contentValues.put(TITLE_FIELD, event.getName());
        contentValues.put(DATE_FIELD, getStringFromLocaleDate(event.getDate()));
        contentValues.put(TIME_FIELD, getStringFromTime(event.getTime()));
        contentValues.put(DELETED_FIELD, getStringFromDate(event.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateEventListArray(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)){
            if (result.getCount() != 0){
                while (result.moveToNext()){
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String stringDate = result.getString(3);
                    String stringTime = result.getString(4);
                    String stringDeleted = result.getString(5);
                    LocalDate date = getLocalDateFromString(stringDate);
                    LocalTime time = getLocalTimeFromString(stringTime);
                    Date deleted = getDateFromString(stringDeleted);

                    Event event = new Event(id, title, date, time, deleted);
                    Event.eventsList.add(event);
                }
            }
        }
    }

    public void updateEventInDatabase(Event event){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, event.getId());
        contentValues.put(TITLE_FIELD, event.getName());
        contentValues.put(DATE_FIELD, getStringFromLocaleDate(event.getDate()));
        contentValues.put(TIME_FIELD, getStringFromTime(event.getTime()));
        contentValues.put(DELETED_FIELD, getStringFromDate(event.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(event.getId())});

    }

    private LocalTime getLocalTimeFromString(String string){
        try {
            return LocalTime.parse(string, timeFormatter);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private LocalDate getLocalDateFromString(String string){
        try {
            return LocalDate.parse(string, formatter);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Date getDateFromString(String string)
    {
        try
        {
            return dateFormat.parse(string);
        }
        catch (ParseException | NullPointerException e)
        {
            return null;
        }
    }

    private String getStringFromDate(Date date)
    {
        if(date == null)
            return null;
        return dateFormat.format(date);
    }

    private String getStringFromLocaleDate(LocalDate date) {
        if(date == null)
            return null;
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String getStringFromTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
    }
}
