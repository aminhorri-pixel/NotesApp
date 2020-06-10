package com.amin.notesapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;

import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    List<NoteModels> noteModels;
    MyDatabase myDatabase;
    Calendar calendar;
    public boolean isPm = false;
    public Context context;

    public NoteAdapter(Context context, List<NoteModels> noteModels) {
        this.noteModels = noteModels;
        myDatabase = new MyDatabase(context);
        calendar = calendar.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_row, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {
        final NoteModels models = noteModels.get(position);
        if (models.getRemember()==0){
            holder.txtRemember.setText("NoteDate:");
            holder.imgCloc.setVisibility(View.GONE);
            holder.txtTime.setVisibility(View.GONE);
            holder.txtDate.setText(models.getCDate());
            holder.txtNoteCdate.setVisibility(View.GONE);
            holder.txtCDate.setVisibility(View.GONE);

        }else {
            holder.txtTime.setText(models.getClockTime());
            holder.txtDate.setText(models.getDate());
            holder.txtCDate.setText(models.getCDate());
        }

        holder.txtTitle.setText(models.getTitle());
        holder.txtDesc.setText(models.getDesc());

        String time = holder.txtTime.getText().toString();
        String date = holder.txtDate.getText().toString();

        String[] times = time.split(":");
        String[] dates = date.split("/");

        String dayCurrent = getDate();
        String[] daysC = dayCurrent.split("/");

        String timeCurent = getTime();
        String[] timesC = timeCurent.split(":");

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final MaDialog.Builder builder = new MaDialog.Builder(context);

                new MaDialog.Builder(context)
                        .setTitle("Delete Note?")
                        .setMessage("Would you like to delete this note?")
                        .setPositiveButtonText("cancel")
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setTitleTextColor(Color.BLACK)
                        .setMessageTextColor(Color.BLACK)
                        .setButtonTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
                        .setPositiveButtonText("Cancel")
                        .setNegativeButtonText("Ok")
                        .setPositiveButtonListener(new MaDialogListener() {
                            @Override
                            public void onClick() {

                            }
                        })
                        .setNegativeButtonListener(new MaDialogListener() {
                            @Override
                            public void onClick() {

                                myDatabase.DeleteNotes(models.getId());
                                noteModels.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,noteModels.size());


                               // Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                            }
                        })

                        .build();
            }
        });


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_dialog);
                Button btnUpdate = (Button) dialog.findViewById(R.id.btn_editDialog_update);
                Button btnCancel = (Button) dialog.findViewById(R.id.btn_editDialog_cancel);
                final EditText edtTitle = (EditText) dialog.findViewById(R.id.edt_ediDialog_title);
                final EditText edtDesc = (EditText) dialog.findViewById(R.id.edt_ediDialog_desc);
                ImageView imgOk = (ImageView)dialog.findViewById(R.id.img_toolbar_ok);
                ImageView imgShow = (ImageView)dialog.findViewById(R.id.img_toolbar_showNote);
                TextView txtInfo = (TextView)dialog.findViewById(R.id.txt_toolbar_info);
                imgOk.setVisibility(View.GONE);
                imgShow.setVisibility(View.GONE);
                txtInfo.setText("Change information");
                edtTitle.setText(models.getTitle());
                edtDesc.setText(models.getDesc());
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtTitle.getText().toString().isEmpty()){
                            Toast.makeText(context, "Title should not be empty !!", Toast.LENGTH_SHORT).show();

                        }else if (edtDesc.getText().toString().isEmpty()){
                            Toast.makeText(context, "Description should not be empty !!", Toast.LENGTH_SHORT).show();

                        }else {
                            myDatabase.UpdateNote(models.getId(), edtTitle.getText().toString(), edtDesc.getText().toString());
                            dialog.dismiss();
                            NoteModels updateModels = new NoteModels();
                            updateModels.setTitle(edtTitle.getText().toString());
                            updateModels.setDesc(edtDesc.getText().toString());
                            updateModels.setClockTime(models.getClockTime());
                            updateModels.setDate(models.getDate());
                            updateModels.setCDate(models.getCDate());
                            updateModels.setRemember(models.getRemember());
                            updateModels.setId(models.getId());
                            noteModels.remove(position);
                            noteModels.add(position, updateModels);
                            notifyItemChanged(position);

                        }
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtDesc;
        TextView txtRemember;
        ImageView imgCloc;
        ImageView imgDelete, imgEdit;
        TextView txtNoteCdate;
        TextView txtTime, txtDate, txtCDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_showNotes_title);
            txtDesc = (TextView) itemView.findViewById(R.id.txt_showNotes_desc);
            txtNoteCdate = (TextView) itemView.findViewById(R.id.txtNoteDate);
            txtTime = (TextView) itemView.findViewById(R.id.txt_ShowNotes_time);
            txtDate = (TextView) itemView.findViewById(R.id.txt_remmember_date);
            txtCDate = (TextView) itemView.findViewById(R.id.txt_ShowNotes_noteDate);
            imgCloc = (ImageView)itemView.findViewById(R.id.imgClock);
            txtRemember = (TextView)itemView.findViewById(R.id.txtRemmember);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_noteRow_delete);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_noteRow_edit);
        }
    }
    public String getTime(){
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        return hour+":"+min;
    }
    public String getDate(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year+"/"+month+"/"+day;
    }
}
