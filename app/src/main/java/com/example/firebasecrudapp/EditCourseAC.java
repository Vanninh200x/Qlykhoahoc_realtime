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

import java.util.HashMap;
import java.util.Map;

public class EditCourseAC extends AppCompatActivity {
    private TextInputEditText courseNameEdt, coursePriceEdt, courseSuitedFEdt;
    private TextInputEditText courseImgEdt, courseLinkEdt, courseDescripEdt;
    private Button updateCourseBtn, deleteCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseID;
    private CourseRVmodal courseRVmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        firebaseDatabase = FirebaseDatabase.getInstance();

        courseNameEdt = findViewById(R.id.idEditCourseName);
        coursePriceEdt = findViewById(R.id.idEditCoursePrice);
        courseSuitedFEdt = findViewById(R.id.idEditCourseSuitF);
        courseImgEdt = findViewById(R.id.idEditCourseImgLink);
        courseLinkEdt = findViewById(R.id.idEditCourseLink);
        courseDescripEdt = findViewById(R.id.idEditCourseDes);
        updateCourseBtn = findViewById(R.id.idBtnUpdateCourse);
        deleteCourseBtn = findViewById(R.id.idBtnDelCourse);
        loadingPB = findViewById(R.id.idProgressLoad);

        courseRVmodal = getIntent().getParcelableExtra("course");
        if(courseRVmodal != null){
            courseNameEdt.setText(courseRVmodal.getCourseName());
            coursePriceEdt.setText(courseRVmodal.getCoursePrice());
            courseSuitedFEdt.setText(courseRVmodal.getCourseSuitedFor());
            courseImgEdt.setText(courseRVmodal.getCourseImg());
            courseLinkEdt.setText(courseRVmodal.getCourseLink());
            courseDescripEdt.setText(courseRVmodal.getCourseDescription());
            courseID = courseRVmodal.getCourseID();
        }

        databaseReference = firebaseDatabase.getReference("Courses").child(courseID);
        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String courseSuited = courseSuitedFEdt.getText().toString();
                String courseImg  = courseImgEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDescrip = courseDescripEdt.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("courseName", courseName);
                map.put("courseDescription", courseDescrip);
                map.put("coursePrice", coursePrice);
                map.put("courseSuitedFor", courseSuited);
                map.put("courseImg", courseImg);
                map.put("courseLink", courseLink);
                map.put("courseID",courseID);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditCourseAC.this, "Đã update Course", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditCourseAC.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditCourseAC.this, "Update Course thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
            }
        });
    }

    private void deleteCourse(){
        databaseReference.removeValue();
        Toast.makeText(this, "Xóa Course thành công", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditCourseAC.this, MainActivity.class));
    }



}