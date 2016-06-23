package com.sshi.moveup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.sshi.moveup.dbcontroler.DatabaseController;
import com.sshi.moveup.dbcontroler.TableControler;
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
        Log.i("sydroid-info","Login2Activity onCreate finished.");

    }

    private void initView(){
        //Register

        mUserEmailEt = (EditText)findViewById(R.id.loginview2_name_et);

        mPasswordEt = (EditText)findViewById(R.id.loginview2_psw_et);

        mUserNameEt = (EditText)findViewById(R.id.loginview2_username_et);

        mRegisterBtn = (Button)findViewById(R.id.loginview2_register_btn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mUserEmailEt.getText().toString();
                String userPsw = mPasswordEt.getText().toString();
                String userName = mUserNameEt.getText().toString();
                String emailRegex = "^[A-Za-zd]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$";

                if(!StringUtil.isEmpty(userEmail) &&
                        !StringUtil.isEmpty(userPsw)){
                    Log.d("sydroid-debug", userEmail+" "+userPsw+" "+userName);
                    SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                    if(!userEmail.matches(emailRegex)){
                        Toast toast = Toast.makeText(Login2Activity.this, "邮件不合法,请重新输入...", Toast.LENGTH_SHORT);//Email not legal, please rewrite the email.
                        toast.show();
                        Log.d("sydroid-debug","email not legal.");
                    }else if(StringUtil.isEmpty(sp.getString(userEmail,""))) {
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

        mLoginBtn = (Button)findViewById(R.id.loginview2_login_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mUserEmailEt.getText().toString();
                String userPsw = mPasswordEt.getText().toString();
                Log.d("sydroid-debug",userEmail + " - " + userPsw);
//                String userName = mUserNameEt.getText().toString();
                if(!StringUtil.isEmpty(userEmail) &&
                        !StringUtil.isEmpty(userPsw)){
                    SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                    Log.d("sydroid-debug",sp.getString(userEmail,""));
                    if(userPsw.equals(sp.getString(userEmail,""))){
                        Log.i("sydroid-info","Login successfully and turn to MainActivity.");
                        //TODO If user database not exist, create it.
                        //TableControler
                        SQLiteDatabase db = openOrCreateDatabase("move_up.db",Context.MODE_PRIVATE,null);
                        if(db == null){
                            Log.d("sydroid-debug","To open or create db is null in Login2Activity");
                        }
                        String tableName = "table_" + StringUtil.changeEmailToTableName(userEmail);
                        String tableName2 = "table_" + StringUtil.changeEmailToTableName(userEmail) + "_tasks";//Doubt: Is table name char limited?
                        DatabaseController.createUserTable(db, tableName, tableName2);//xi@test.com -> table_xi_test_com

                        //Login success. Switch to the MainActivity
                        Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                        intent.putExtra("tableName", tableName);
                        startActivity(intent);
                        Login2Activity.this.finish();
                    }else{
                        Toast toast = Toast.makeText(Login2Activity.this, "用户未注册！请注册后登录...", Toast.LENGTH_SHORT);//The user not register yet, please login after registration.
                    }
                }

            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(this.getCurrentFocus() != null) {//No focus
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
