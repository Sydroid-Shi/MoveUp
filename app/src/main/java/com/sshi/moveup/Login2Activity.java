package com.sshi.moveup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sshi.moveup.util.StringUtil;

public class Login2Activity extends AppCompatActivity {

    private Button mRegisterBtn;
    private Button mLoginBtn;

    private EditText mUserEmailEt;//The user must enter the Email for register.
    private EditText mPasswordEt;
    private EditText mUserNameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        initView();
    }

    private void initView(){
        //Register
        mRegisterBtn = (Button)findViewById(R.id.loginview2_login_btn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mUserEmailEt.getText().toString();
                String userPsw = mPasswordEt.getText().toString();
                String userName = mUserNameEt.getText().toString();

                if(!StringUtil.isEmpty(userEmail) &&
                        !StringUtil.isEmpty(userPsw)){
                    Log.d("sydroid-debug", userEmail+" "+userPsw+""+userName);
                    SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                    if(StringUtil.isEmpty(sp.getString(userEmail,""))) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(userEmail, userPsw);
                        if (!StringUtil.isEmpty(userName)) {
                            editor.putString(userName, userPsw);
                        }
                        editor.commit();
                        Toast toast = Toast.makeText(Login2Activity.this, "已成功注册！请登录...", Toast.LENGTH_SHORT);//Register successfully! Please login...
                        toast.show();
                        Log.d("sydroid-debug","register successfully");
                    }else{
                        Toast toast = Toast.makeText(Login2Activity.this, "已注册过，请直接登录或者换其他账号。", Toast.LENGTH_SHORT);//Register successfully! Please login...
                        toast.show();
                        Log.d("sydroid-debug","already registered, should just login.");
                    }
                }else{
                    Toast toast = Toast.makeText(Login2Activity.this, "邮箱和密码都不能为空！", Toast.LENGTH_SHORT);//Email and Password cannot be empty!
                    toast.show();
                    Log.d("sydroid-debug","register failed.");
                }
            }
        });

        mLoginBtn = (Button)findViewById(R.id.loginview2_register_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mUserEmailEt.getText().toString();
                String userPsw = mPasswordEt.getText().toString();
                Log.d("sydroid-debug",userEmail + " " + userPsw);
//                String userName = mUserNameEt.getText().toString();
                if(!StringUtil.isEmpty(userEmail) &&
                        !StringUtil.isEmpty(userPsw)){
                    SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                    if(userPsw.equals(sp.getString(userEmail,""))){
                        //Login success. Switch to the MainActivity
                        Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });

        mUserEmailEt = (EditText)findViewById(R.id.loginview2_username_et);


        mPasswordEt = (EditText)findViewById(R.id.loginview2_psw_et);

        mUserNameEt = (EditText)findViewById(R.id.loginview2_name_et);
    }
}
