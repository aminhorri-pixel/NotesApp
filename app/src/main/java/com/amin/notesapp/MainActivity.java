package com.amin.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    EditText edtTitle,edtDesc;
    TextView txtTime,txtDate;
    Button btnSave;
    Button btnShowNotes;
    MyDatabase myDatabase;
    ImageView imgShowNotesToolbar;
    AppCompatCheckBox checkBox;
    public int RequestCode = 1001;
    AlarmManager alarmManager;
    ImageView imgOkToolbar;
    public boolean isPm = false;
    ArrayList<PendingIntent>pendingIntents;
    ImageView imgTime,imgDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();

    }

    private void setUpView() {
        changeStatusBarColor("#2c3e50");
        pendingIntents = new ArrayList<>();
        edtTitle = (EditText)findViewById(R.id.edt_main_title);
        edtDesc = (EditText)findViewById(R.id.edt_main_desc);
        txtTime = (TextView)findViewById(R.id.txt_main_texttime);
        imgTime = (ImageView)findViewById(R.id.img_main_time);
        imgShowNotesToolbar = (ImageView)findViewById(R.id.img_toolbar_showNote);
        myDatabase = new MyDatabase(getApplicationContext());
        btnShowNotes = (Button) findViewById(R.id.btn_main_showNote);
        imgDate = (ImageView)findViewById(R.id.img_main_date);
        checkBox = (AppCompatCheckBox)findViewById(R.id.cb_main_checkbax);
        imgOkToolbar = (ImageView)findViewById(R.id.img_toolbar_ok);
        btnSave = (Button)findViewById(R.id.btn_main_save);
        txtDate = (TextView)findViewById(R.id.txt_main_textdate);

        btnSave.setOnClickListener(this);
        imgOkToolbar.setOnClickListener(this);
        imgShowNotesToolbar.setOnClickListener(this);
        btnShowNotes.setOnClickListener(this);

        imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(MainActivity.this,
                        MainActivity.this,12,35,true);
                dialog.show();
            }

        });

        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtDate.setText(i+"/"+i1+"/"+i2);
                    }
                },2020,1,25);
                dialog.show();
            }
        });
    }

    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        txtTime.setText(i+":"+i1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.img_toolbar_ok || view.getId()==R.id.btn_main_save){



            if (checkBox.isChecked()){
                if (edtTitle.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Title should not be empty !!", Toast.LENGTH_SHORT).show();
                }else if (edtDesc.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Description should not be empty !!", Toast.LENGTH_SHORT).show();
                }else if (txtTime.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Time should not be empty !!", Toast.LENGTH_SHORT).show();
                }else if (txtDate.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Date should not be empty !!", Toast.LENGTH_SHORT).show();
                }else {
                    long id = myDatabase.AddNotes(edtTitle.getText().toString(),edtDesc.getText().toString(),
                            txtTime.getText().toString(),txtDate.getText().toString(),getCurrentTime(),1);
                    Toast.makeText(this, id+"", Toast.LENGTH_SHORT).show();
                    setAlarm();
                }
            }else {
                if (edtTitle.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Title should not be empty !!", Toast.LENGTH_SHORT).show();
                }else if (edtDesc.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Description should not be empty !!", Toast.LENGTH_SHORT).show();
                }else {
                    long id = myDatabase.AddNotes(edtTitle.getText().toString(),edtDesc.getText().toString(),
                            txtTime.getText().toString(),txtDate.getText().toString(),getCurrentTime(),0);
                    Toast.makeText(this, id+"", Toast.LENGTH_SHORT).show();
                }
            }


        }else if (view.getId()==R.id.btn_main_showNote || view.getId()==R.id.img_toolbar_showNote){
            startActivity(new Intent(MainActivity.this,ShowNoteActivity.class));
        }

    }

    public String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    public void setAlarm(){
        Intent intent  = new Intent(MainActivity.this,AlarmActivity.class);
        intent.putExtra("title",edtTitle.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,RequestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        String time = txtTime.getText().toString();
        String date = txtDate.getText().toString();

        String[]times = time.split(":");
        String[] dates = date.split("/");


        if (Integer.parseInt(times[0])>12){
            isPm = true;
        }
        Log.i("LOG", "time :"+times[0]+"--"+times[1]+" date :"+dates[0]+"--"+dates[1]+"--"+dates[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
        calendar.set(Calendar.HOUR,isPm?Integer.parseInt(times[0])-12:Integer.parseInt(times[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.AM_PM,isPm?1:0);


        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        pendingIntents.add(pendingIntent);
        isPm = false;
        RequestCode++;
    }

    private void addAutoStartup() {

        /*try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }*/

        String manufacturer = "xiaomi";
        if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            //this will open auto start screen where user can enable permission for your app
            Intent intent1 = new Intent();
            intent1.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent1);
        }

    }
}
