package com.Englishword_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;
    public String[] frommainac = new String[3];
    public String user="taslak";
    public String pass="taslak";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);


        DB = new DBHelper(this);




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 user = username.getText().toString();
                 pass = password.getText().toString();
                String repass = repassword.getText().toString();



                if(user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser==false){

                            //Boolean insert = DB.insertData(user, pass,null);
                            Boolean insert = true;

                            if(insert==true){
                                Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                                // MainActivity.java



// Intent oluşturma ve verileri ekleyerek HomeActivity'e geçiş yapma
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra("data", 1);
                                intent.putExtra("username", user);
                                startActivity(intent);


                                DBHelper dbHelper = new DBHelper(MainActivity.this);
                                boolean success = dbHelper.addUser(user, pass);

                                if (success) {
                                    Toast.makeText(MainActivity.this, "Kullanıcı kaydedildi:"+success, Toast.LENGTH_SHORT).show();
                                    // Ana sayfaya yönlendirme veya istediğiniz işlemleri yapma
                                } else {
                                    Toast.makeText(MainActivity.this, "Kullanıcı kaydedilemedi:"+success, Toast.LENGTH_SHORT).show();
                                }


                            }else{
                                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("data","true");
                startActivity(intent);
            }
        });















        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}