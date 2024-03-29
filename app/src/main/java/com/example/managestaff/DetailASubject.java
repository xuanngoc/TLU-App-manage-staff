package com.example.managestaff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import user.Book;
import user.Subject;
import user.Teacher;

public class DetailASubject extends AppCompatActivity {

    private TextInputEditText subjectCodeEditText;
    private TextInputEditText subjectNameEditText;
    private TextInputEditText subjectCreditsEditText;
    private TextInputEditText subjectHoursEditText;
    private TextInputEditText subjectCoefficientEditText;
    private MaterialButton btnUpdateInfoSubject;
    private MaterialButton btnDeleteSubject;
    private MaterialButton btnSaveSubject;


    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_asubject);

        subjectCodeEditText =  findViewById(R.id.subject_code_edit_text);
        subjectNameEditText = findViewById(R.id.subject_name_edit_text);
        subjectCreditsEditText = findViewById(R.id.subject_credits_edit_text);
        subjectHoursEditText = findViewById(R.id.subject_hours_edit_text);
        subjectCoefficientEditText = findViewById(R.id.subject_coefficient_edit_text);
        btnUpdateInfoSubject = findViewById(R.id.btn_update_subject);
        btnDeleteSubject = findViewById(R.id.btn_delete_subject);
        btnSaveSubject = findViewById(R.id.btn_save_subject);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("Subjects");


        final Bundle bundle = getIntent().getExtras();
        subjectCodeEditText.setText(bundle.get("subjectCode").toString());
        subjectNameEditText.setText(bundle.get("subjectName").toString());
        subjectCreditsEditText.setText(bundle.get("subjectCredits").toString());
        subjectHoursEditText.setText(bundle.get("subjectHours").toString());
        subjectCoefficientEditText.setText(bundle.get("subjectCoefficient").toString());



        subjectCodeEditText.setEnabled(false);
        subjectNameEditText.setEnabled(false);
        subjectCreditsEditText.setEnabled(false);
        subjectHoursEditText.setEnabled(false);
        subjectCoefficientEditText.setEnabled(false);

        btnUpdateInfoSubject.setOnClickListener(btnUpdateInfoSubjectClickListener);
        btnDeleteSubject.setOnClickListener(btnDeleteSubjectClickListener);
        btnSaveSubject.setOnClickListener(btnSaveInfoSubjectClickListener);

    }

    View.OnClickListener btnUpdateInfoSubjectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            subjectCodeEditText.setEnabled(true);
            subjectNameEditText.setEnabled(true);
            subjectCreditsEditText.setEnabled(true);
            subjectHoursEditText.setEnabled(true);
            subjectCoefficientEditText.setEnabled(true);

            btnSaveSubject.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener btnDeleteSubjectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    DetailASubject.this);
            // set title
            alertDialogBuilder.setTitle("Xóa môn học");
            // set dialog message
            alertDialogBuilder
                    .setMessage("Bạn muốn xóa môn học này ?")
                    .setCancelable(true)
                    .setPositiveButton("Có",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Subject subject = new Subject(subjectCodeEditText.getText().toString(),
                                    subjectNameEditText.getText().toString(),
                                    subjectCreditsEditText.getText().toString(),
                                    subjectHoursEditText.getText().toString(),
                                    subjectCoefficientEditText.getText().toString());

                            deleteSubject(subject);
                            // if this button is clicked, close
                            finish();
                        }
                    })
                    .setNegativeButton("Không",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
    };

    View.OnClickListener btnSaveInfoSubjectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Subject subject = new Subject(subjectCodeEditText.getText().toString(),
                    subjectNameEditText.getText().toString(),
                    subjectCreditsEditText.getText().toString(),
                    subjectHoursEditText.getText().toString(),
                    subjectCoefficientEditText.getText().toString());
            updateInfoSubject(subject);
            finish();
        }
    };

    private void updateInfoSubject(Subject subject){
        mFirebaseDatabase.child(subject.getSubjectCode()).setValue(subject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailASubject.this, "Updating data is success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteSubject(Subject subject){
        mFirebaseDatabase.child(subject.getSubjectCode()).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailASubject.this, "Deleting data is success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_subject, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_book:
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DetailASubject.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_spinner_add_book, null);
                builder.setTitle("Chọn sách, giáo trình");
                final Spinner spinner = view.findViewById(R.id.spinner);


                ArrayAdapter<String> adapter = new ArrayAdapter<>(DetailASubject.this, android.R.layout.simple_spinner_item,
                        ShowListBookActivity.listBookName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //belongDepartmentView.setText(spinner.getSelectedItem().toString());
                        //mListSubject1.add(ShowListSubjectActivity.getSubject(spinner.getSelectedItem().toString()));
                        //addSubject(ShowListSubjectActivity.getSubject(spinner.getSelectedItem().toString()));
                        addBook(ShowListBookActivity.getBook(spinner.getSelectedItem().toString()));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setView(view);
                android.app.AlertDialog dialog = builder.create();
                dialog.show();

        }
        return true;
    }
    private void addBook(Book book){
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance()
                .getReference("Books").child(book.getBookName())
                .child("SubjectsUsage")
                .child(subjectNameEditText.getText().toString());
        firebaseDatabase.setValue(true);

    }

}
