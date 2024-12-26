package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        TextView textView;
        EditText name, dob, email, email2, pass, pass2, address, city, province, post, phone;
        Button register;

        textView = findViewById(R.id.signin);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        email = findViewById(R.id.emailAddress);
        email2 = findViewById(R.id.confirmemail);
        pass = findViewById(R.id.password);
        pass2 = findViewById(R.id.confirmpassword);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        post = findViewById(R.id.postal);
        phone = findViewById(R.id.phone);
        register = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get all the data from user input
                String Name = name.getText().toString().trim();
                String Dob = dob.getText().toString().trim();
                String Email1 = email.getText().toString().trim();
                String Email2 = email2.getText().toString().trim();
                String Pass = pass.getText().toString().trim();
                String Pass2 = pass2.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String City = city.getText().toString().trim();
                String Province = province.getText().toString().trim();
                String Post = post.getText().toString().trim();
                String Phone = phone.getText().toString().trim();

                // Passing all the conditions
                if(!Name.isEmpty() && !Dob.isEmpty() && !Email1.isEmpty() && !Email2.isEmpty() &&
                        !Pass.isEmpty() && !Pass2.isEmpty() && !Address.isEmpty() && !City.isEmpty() && !Province.isEmpty()
                && !Post.isEmpty() && !Phone.isEmpty() && Pass.equals(Pass2) && Email1.equals(Email2))
                {
                    // Create user with firebase authentication
                    auth.createUserWithEmailAndPassword(Email1, Pass).addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            // Get user ID
                            FirebaseUser user = auth.getCurrentUser();
                            String userId = user != null ? user.getUid() : null;
                            if(userId != null)
                            {
                                // Save user data to the realtime database
                                HashMap <String, String> userData = new HashMap<>();
                                userData.put("name", Name);
                                userData.put("dob", Dob);
                                userData.put("email1", Email1);
                                userData.put("email2", Email2);
                                //userData.put("pass1",Pass);
                                //userData.put("pass2", Pass2);
                                userData.put("address", Address);
                                userData.put("city", City);
                                userData.put("province",Province);
                                userData.put("post", Post);
                                userData.put("phone", Phone);

                                database.child(userId).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> dbTask) {
                                        if (dbTask.isSuccessful())
                                        {
                                            Toast.makeText (signup.this,"Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(signup.this, "Sorry data saved failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                {

                                }

                            } else {
                                Toast.makeText(signup.this, "Sorry something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(signup.this, "Sorry Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}