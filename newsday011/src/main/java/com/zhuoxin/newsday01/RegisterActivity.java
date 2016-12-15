package com.zhuoxin.newsday01;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuoxin.base.BaseActivity;
import com.zhuoxin.dao.UserDAO;
import com.zhuoxin.entity.UserItem;

/**
 * Created by l on 2016/11/22.
 * 用户注册
 */

public class RegisterActivity extends BaseActivity {
    private EditText etEmailRegister;
    private EditText etNameRegister;
    private EditText etPwdRegister;
    private Button btnRegisterRegister;
    CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitleBar(R.mipmap.btn_homeasup_default,R.string.user_register,0);
        assignViews();//实例化控件
    }
    //实例化控件
    private void assignViews() {
        etEmailRegister = (EditText) findViewById(R.id.et_email_register);
        etNameRegister = (EditText) findViewById(R.id.et_name_register);
        etPwdRegister = (EditText) findViewById(R.id.et_pwd_register);
        btnRegisterRegister = (Button) findViewById(R.id.btn_register_register);
        cb= (CheckBox) findViewById(R.id.checkBox);
        //给CheckBox设置监听
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){//CheckBox选中时，注册按钮可点击
                    btnRegisterRegister.setClickable(true);
                    btnRegisterRegister.setBackgroundResource(R.mipmap.normalbutton_normal);
                }else{//CheckBox未选中时，注册按钮不可点击
                    btnRegisterRegister.setClickable(false);
                    btnRegisterRegister.setBackgroundColor(Color.GRAY);
                }
            }
        });
    }
    String email;
    String name;
    String pwd;
    boolean isEmail ;
    boolean isName;
    boolean isPwd;
    private void initData() {//获取输入的内容，并判断是否符合格式要求
        email=etEmailRegister.getText().toString();
        name=etNameRegister.getText().toString();
        pwd=etPwdRegister.getText().toString();
        isEmail=email.matches("\\w+@\\w+\\.\\w+");
        isName=name.matches(".+");
        isPwd=pwd.matches("\\w{6,16}");
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.btn_register_register://立即注册
                initData();//判断数据是否符合格式要求
                if(email.equals("") || name.equals("") || pwd.equals("")){
                    Toast.makeText(this,"邮箱，昵称或者密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(isEmail && isName && isPwd){
                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                    //把注册信息添加到数据库
                    UserItem userMsg=new UserItem();
                    userMsg.setUserEmail(email);
                    userMsg.setUserName(name);
                    userMsg.setUserPwd(pwd);
                    UserDAO.getUserDAO().addUserItem(userMsg);
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this,"邮箱，昵称或者密码不正确",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
