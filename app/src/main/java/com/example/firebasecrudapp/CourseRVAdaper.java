package com.example.firebasecrudapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseRVAdaper extends RecyclerView.Adapter<CourseRVAdaper.ViewHolder> {
    int lastPos = -1;
    private ArrayList<CourseRVmodal> courseRVmodalArrayList;
    private Context context;
    private CourseClickInterface courseClickInterface;

    public CourseRVAdaper(ArrayList<CourseRVmodal> courseRVmodalArrayList, Context context, CourseClickInterface courseClickInterface) {
        this.courseRVmodalArrayList = courseRVmodalArrayList;
        this.context = context;
        this.courseClickInterface = courseClickInterface;
    }

    @NonNull
    @Override
    public CourseRVAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_rv_item,parent, false);
        return new ViewHolder(view);
    }

    public interface CourseClickInterface{
        void onCourseClick(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CourseRVmodal courseRVmodal = courseRVmodalArrayList.get(position);
        holder.courseNameTV.setText(courseRVmodal.getCourseName());
        holder.coursePriceTV.setText("$"+courseRVmodal.getCoursePrice());
        Picasso.get().load(courseRVmodal.getCourseImg()).into(holder.courseImgV);
        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseClickInterface.onCourseClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position){
        if(position > lastPos){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }


    @Override
    public int getItemCount() {
        return courseRVmodalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView courseNameTV, coursePriceTV;
        private ImageView courseImgV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.idTextView_CourseName);
            coursePriceTV = itemView.findViewById(R.id.idTextView_Price);
            courseImgV = itemView.findViewById(R.id.idImgViewCourse_item);
        }
    }


}
