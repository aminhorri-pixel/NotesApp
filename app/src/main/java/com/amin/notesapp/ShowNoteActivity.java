package com.amin.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowNoteActivity extends AppCompatActivity {

    ImageView ShowListToolbar, OkToolbar;
    MyDatabase myDatabase;
    private List<NoteModels> NoteDate;
    RecyclerView recyclerShowNote;
    TextView txtDontNote;
    View view;
    ImageView imgShowNote;
    Button btnShowCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        setUpViews();
    }

    private void setUpViews() {
        changeStatusBarColor("#136a8a");
        ShowListToolbar = (ImageView) findViewById(R.id.img_toolbar_showNote);
        OkToolbar = (ImageView) findViewById(R.id.img_toolbar_ok);
        OkToolbar.setVisibility(View.GONE);
        recyclerShowNote = (RecyclerView) findViewById(R.id.rv_showNote_rv);
        NoteDate = new ArrayList<>();
        myDatabase = new MyDatabase(getApplicationContext());
        txtDontNote = (TextView)findViewById(R.id.txtDontNote);
        btnShowCreate = (Button) findViewById(R.id.btn_show_create);
        view = (View)findViewById(R.id.createView);
        imgShowNote = (ImageView)findViewById(R.id.imgLogoShowNote);
        ShowListToolbar.setImageResource(R.drawable.back);
        ShowListToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getNotes();

        if(NoteDate!=null && NoteDate.size()>0){

            btnShowCreate.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            txtDontNote.setVisibility(View.GONE);
            imgShowNote.setVisibility(View.GONE);

        }else{
            btnShowCreate.setVisibility(View.VISIBLE);
            txtDontNote.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            imgShowNote.setVisibility(View.VISIBLE);
            recyclerShowNote.setVisibility(View.GONE);

            btnShowCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        recyclerShowNote.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerShowNote.setAdapter(new NoteAdapter(this, NoteDate));
    }

    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public void getNotes() {

        Cursor cursor = myDatabase.GetNotes();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            String time = cursor.getString(3);
            String date = cursor.getString(4);
            String ctime = cursor.getString(5);
            int remember = cursor.getInt(6);

            NoteModels noteModels = new NoteModels();
            noteModels.setId(id);
            noteModels.setTitle(title);
            noteModels.setDesc(desc);
            noteModels.setDate(date);
            noteModels.setClockTime(time);
            noteModels.setCDate(ctime);
            noteModels.setRemember(remember);

            NoteDate.add(noteModels);
        }
    }
}
