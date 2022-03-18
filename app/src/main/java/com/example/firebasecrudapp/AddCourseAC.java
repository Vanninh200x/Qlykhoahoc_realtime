package com.example.firebasecrudapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCourseAC extends AppCompatActivity {
    private TextInputEditText courseNameEdt, coursePriceEdt, courseSuitedFEdt;
    private TextInputEditText courseImgEdt, courseLinkEdt, courseDescripEdt;
    private Button addCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseNameEdt = findViewById(R.id.idEditCourseName);
        coursePriceEdt = findViewById(R.id.idEditCoursePrice);
        courseSuitedFEdt = findViewById(R.id.idEditCourseSuitF);
        courseImgEdt = findViewById(R.id.idEditCourseImgLink);
        courseLinkEdt = findViewById(R.id.idEditCourseLink);
        courseDescripEdt = findViewById(R.id.idEditCourseDes);
        addCourseBtn = findViewById(R.id.idBtnAddCourse);
        loadingPB = findViewById(R.id.idProgressLoad);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loadingPB.setVisibility(View.VISIBLE);
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String courseSuited = courseSuitedFEdt.getText().toString();
                String courseImg  = courseImgEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDescrip = courseDescripEdt.getText().toString();
                courseID = courseName;
                CourseRVmodal courseRVmodal = new CourseRVmodal(courseName, courseDescrip, coursePrice, courseSuited, courseImg, courseLink, courseID);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.child(courseID).setValue(courseRVmodal);
                        Toast.makeText(AddCourseAC.this, "Course, đã được thêm.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCourseAC.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCourseAC.this, "Lỗi "+error.toString(),  Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}