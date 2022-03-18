package com.example.firebasecrudapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseRVAdaper.CourseClickInterface {
    private RecyclerView courseRV_Rec;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<CourseRVmodal> courseRVmodalArrayList;
    private RelativeLayout bottomSheetRL;
    private CourseRVAdaper courseRVAdaper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseRV_Rec = findViewById(R.id.idRCVCourse);
        loadingPB = findViewById(R.id.idProgressLoad);
        addFAB = findViewById(R.id.idAddFAB);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");
        courseRVmodalArrayList = new ArrayList<>();
        bottomSheetRL = findViewById(R.id.idRLBSheet);
        mAuth = FirebaseAuth.getInstance();

        courseRVAdaper = new CourseRVAdaper(courseRVmodalArrayList, this,this);
        courseRV_Rec.setLayoutManager(new LinearLayoutManager(this));
        courseRV_Rec.setAdapter(courseRVAdaper);



        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddCourseAC.class));
            }
        });
        getAllCourse();

    }


    private void getAllCourse(){
        courseRVmodalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVmodalArrayList.add(snapshot.getValue(CourseRVmodal.class));
                courseRVAdaper.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdaper.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdaper.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                courseRVAdaper.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







    @Override
    public void onCourseClick(int position) {
        displayBottomSheet(courseRVmodalArrayList.get(position));
    }

    private void displayBottomSheet(CourseRVmodal courseRVmodal){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView courseNameTV = layout.findViewById(R.id.idTextView_CourseName);
        TextView courseDesscTV = layout.findViewById(R.id.idTVCourseDescrip);
        TextView courseSuitedFTV = layout.findViewById(R.id.idTVCourseSuitedfor);
        TextView coursePriceTV = layout.findViewById(R.id.idTVCoursePrice);
        TextView courseImgV = layout.findViewById(R.id.idImgVCourse);
        Button editBtn = layout.findViewById(R.id.idBtnEdit);
        Button viewDetails = layout.findViewById(R.id.idBtnViewDetails);

        courseNameTV.setText(courseRVmodal.getCourseName());
        courseDesscTV.setText(courseRVmodal.getCourseDescription());
        courseSuitedFTV.setText(courseRVmodal.getCourseSuitedFor());
        coursePriceTV.setText("$"+courseRVmodal.getCoursePrice());
        Picasso.get().load(courseRVmodal.getCourseImg()).into((Target) courseImgV);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditCourseAC.class);
                i.putExtra("course",courseRVmodal);
                startActivity(i);
            }
        });

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(courseRVmodal.getCourseLink()));
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.idLogOut:
                Toast.makeText(this, "Đăng xuất...", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginAC.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}