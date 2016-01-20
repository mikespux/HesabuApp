package com.hesabu;

/**
 * @author Michael N.Orenge
 * 
 */

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.hesabuapp.R;

public class Activity_Advise extends Activity {

private SQLiteDatabase newDB;
DBHelper dbHelper;
String partyName;
Spinner spinner;
private EditText et;

ArrayList<String> dbdata=new ArrayList<String>();
ArrayAdapter<String> adapter;

private ArrayList<String> array_sort= new ArrayList<String>();

int textlength=0;

protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_sales);

et = (EditText) findViewById(R.id.editText1);
spinner=(Spinner)findViewById(R.id.spinner1);
DataFromDB();//method to get data from sqlite database
CheckFromSpinner();//method to check data from spinner
//SelectPartyName();

}
private void DataFromDB() {
dbHelper=new DBHelper(Activity_Advise.this);
newDB=dbHelper.getWritableDatabase();
Cursor c=newDB.rawQuery("select item from stock", null);
if(c!=null)
{
if(c.moveToFirst())
{
do{
partyName=c.getString(c.getColumnIndex("item"));
dbdata.add(partyName);

}while(c.moveToNext());
}
}

adapter=new ArrayAdapter<String>(Activity_Advise.this,android.R.layout.simple_list_item_1,dbdata);


adapter=new ArrayAdapter<String>(Activity_Advise.this,android.R.layout.simple_list_item_1,dbdata);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spinner.setAdapter(adapter);
}

private void CheckFromSpinner() {
et.addTextChangedListener(new TextWatcher()
{
public void afterTextChanged(Editable s)
{
// Abstract Method of TextWatcher Interface.
}
public void beforeTextChanged(CharSequence s,
int start, int count, int after)
{
// Abstract Method of TextWatcher Interface.
}
public void onTextChanged(CharSequence s,
int start, int before, int count)
{
textlength = et.getText().length();
array_sort.clear();
for (int i = 0; i < dbdata.size(); i++)
{
if (textlength <= dbdata.get(i).length())
{
if(et.getText().toString().equalsIgnoreCase(
(String)
dbdata.get(i).subSequence(0,
textlength)))
{
array_sort.add(dbdata.get(i));
}
}
}

spinner.setAdapter(new ArrayAdapter<String>(Activity_Advise.this, android.R.layout.simple_spinner_dropdown_item,array_sort));

}



});

}
}