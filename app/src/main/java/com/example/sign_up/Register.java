package com.example.sign_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText un,em,pw,mn,cp;
    RadioGroup rg1;
    RadioButton rb,r;
    Button b,b1;
    ProgressBar p;
    TextView l,tv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    int j =0;
    Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        un = (EditText)findViewById(R.id.et1);
        em =(EditText)findViewById(R.id.et2);
        pw = (EditText)findViewById(R.id.et3);
        mn = (EditText)findViewById(R.id.et5);
        cp = (EditText)findViewById(R.id.et4);
        rg1 =(RadioGroup)findViewById(R.id.rg);
        b = (Button)findViewById(R.id.bt);
        l =(TextView)findViewById(R.id.tv3);
        auth =FirebaseAuth.getInstance();
        p = (ProgressBar)findViewById(R.id.pb);
        tv = (TextView)findViewById(R.id.tv4);
        rb = (RadioButton)findViewById(R.id.rb1);
        r = (RadioButton)findViewById(R.id.rb2);
        b1 = (Button)findViewById(R.id.bt2);
        member = new Member();
        reference = database.getInstance().getReference().child("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    j=(int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Register.this,"Error!"+error.getDetails(),Toast.LENGTH_SHORT).show();

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return;
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = un.getText().toString().trim();
                String email = em.getText().toString().trim();
                String pswd = pw.getText().toString().trim();
                String mb = mn.getText().toString().trim();
                String cpswd = cp.getText().toString().trim();
                String Customer = rb.getText().toString().trim();
                String Business = r.getText().toString().trim();
                Pattern p1 = Pattern.compile("(0/91)?[7-9][0-9]{9}");


                if(TextUtils.isEmpty(user)){
                    un.setError("User Name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(email))
                {
                    em.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(pswd))
                {
                    pw.setError("Password is Required.");
                    return;
                }
                if(pswd.length()<6)
                {
                    pw.setError("Password must be >= 6 Characters.");
                    return;

                }
                if(pswd.equals(cpswd)!=true){
                    cp.setError("Password is Not Matched");
                    return;}
                if(TextUtils.isEmpty(mb))
                {
                    mn.setError("Mobile Number is required");
                    return;
                }
                Matcher m = p1.matcher(mb);
                if(!(m.find()&&m.group().equals(mb))){
                    mn.setError("Please Enter a Valid Number");
                    return;}

                    int sr = rg1.getCheckedRadioButtonId();
                    if(sr==-1){
                        rb.setError("");
                        r.setError("");
                    tv.setText("! Please Select any One");}



                p.setVisibility(View.VISIBLE);

                // Firebase
                auth.createUserWithEmailAndPassword(email,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            p.setVisibility(View.INVISIBLE);
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Register.this, "Account Created, Please Verify your Email Id", Toast.LENGTH_SHORT).show();
                                        member.setEmail(email);
                                        member.setNumber(mb);
                                        member.setName(user);
                                        reference.child(String.valueOf(j+1)).setValue(member);
                                        if(rb.isChecked())
                                        {
                                            member.setUser(Customer);
                                            reference.child(String.valueOf(j+1)).setValue(member);
                                        }
                                        else
                                        {
                                            member.setUser(Business);
                                            reference.child(String.valueOf(j+1)).setValue(member);
                                        }
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                    }
                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(Register.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }});


    }
    public void Sh(View view)
    {
        if(view.getId()==R.id.sp){

            if(pw.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.sp1);

                //Show Password
                pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                pw.setSelection(pw.getText().length());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.sp2);


                //Hide Password
                pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                pw.setSelection(pw.getText().length());
            }
        }
    }
    public void Sh1(View view)
    {
        if(view.getId()==R.id.sp12){

            if(cp.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.sp1);

                //Show Password
                cp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                cp.setSelection(cp.getText().length());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.sp2);


                //Hide Password
                cp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                cp.setSelection(cp.getText().length());
            }
        }
    }

}