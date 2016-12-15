package com.zhuoxin.newsday01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuoxin.NewsApplication;
import com.zhuoxin.base.BaseActivity;
import com.zhuoxin.dao.UserDAO;
import com.zhuoxin.entity.UserItem;

/**
 * Created by l on 2016/11/22.
 * 用户登录
 */

public class LoginActivity extends BaseActivity {
    private EditText etEmailLogin;
    private EditText etPwdLogin;
    private Button btnRegisterLogin;
    private Button btnForgetpwdLogin;
    private Button btnLoginLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置导航条
        setTitleBar(R.mipmap.btn_homeasup_default,R.string.user_login,0);
        assignViews();//实例化控件
    }
    //实例化控件
    private void assignViews() {
        etEmailLogin = (EditText) findViewById(R.id.et_email_login);
        etPwdLogin = (EditText) findViewById(R.id.et_pwd_login);
        btnRegisterLogin = (Button) findViewById(R.id.btn_register_login);
        btnForgetpwdLogin = (Button) findViewById(R.id.btn_forgetpwd_login);
        btnLoginLogin = (Button) findViewById(R.id.btn_login_login);
    }
    String email;
    String pwd;
    private void initData(){
        email=etEmailLogin.getText().toString();
        pwd=etPwdLogin.getText().toString();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.btn_register_login://注册
                Intent intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_forgetpwd_login://忘记密码
                Toast.makeText(this,"亲，忘记了就重新注册吧！！！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_login_login://登录
                initData();
                UserItem userItem=UserDAO.getUserDAO().getUsers(email);
                String userEmail=userItem.getUserEmail();
                String userPwd=userItem.getUserPwd();
                if(email.equals(userEmail) && pwd.equals(userPwd)){
                    Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                    NewsApplication.setUserItem(userItem);
                    Intent intent1=new Intent(this,MyAccountActivity.class);
                    startActivity(intent1);
                    finish();
                }else if(email.equals("") || pwd.equals("")){
                    Toast.makeText(this,"邮箱或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"邮箱或密码不正确",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
