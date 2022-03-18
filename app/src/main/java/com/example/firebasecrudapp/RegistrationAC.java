package com.example.firebasecrudapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationAC extends AppCompatActivity {
    private TextInputEditText userNameEdt, pwEdt, confPwEdt;
    private Button registerButton;
    private ProgressBar loadingPB;
    private TextView loginTv;
    private FirebaseAuth mAuth;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        unit();
        
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationAC.this, LoginAC.class));
            }
        });
        
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String userName = userNameEdt.getText().toString();
                String pwd = pwEdt.getText().toString();
                String confPswd = confPwEdt.getText().toString();
                if(!pwd.equals(confPswd)){
                    Toast.makeText(RegistrationAC.this, "Kiểm tra lại mật khẩu", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(confPswd)){
                    Toast.makeText(RegistrationAC.this, "Đề nghị nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationAC.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationAC.this,LoginAC.class));
                                finish();
                            }else{
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationAC.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private void unit(){
        userNameEdt = findViewById(R.id.idEditUserName);
        pwEdt = findViewById(R.id.idEditPasswd);
        confPwEdt = findViewById(R.id.idEditConfirm_Pass);
        registerButton = findViewById(R.id.idBtnRegister);
        loadingPB = findViewById(R.id.idProgressLoad);
        loginTv = findViewById(R.id.idTVLogin);
        mAuth = FirebaseAuth.getInstance();
    }
    
    
}