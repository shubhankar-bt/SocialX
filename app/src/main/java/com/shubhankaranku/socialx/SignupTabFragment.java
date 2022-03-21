package com.shubhankaranku.socialx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupTabFragment extends Fragment {

    private TextInputEditText mName,mEmail,mNumber,mPassword;
    public static Button mRegister;
    private CheckBox mcheckBox;
    private TextView mAlreadyRegister;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private LinearLayout loginLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        mName = root.findViewById(R.id.nameText);
        mEmail = root.findViewById(R.id.emailText);
        mNumber = root.findViewById(R.id.numberText);
        mPassword = root.findViewById(R.id.passwordText);
        mRegister = root.findViewById(R.id.buttonRegister);
        mcheckBox = root.findViewById(R.id.checkBox);
        mAlreadyRegister = root.findViewById(R.id.mAlreadyRegister);
        mProgressBar = root.findViewById(R.id.progressbar);
        loginLayout = root.findViewById(R.id.bottmView2);



        loginLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                registerNewUser();
            }
        });



        mAlreadyRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginActivity.viewPager.setCurrentItem(0);
            }
        });





        return root;
    }

    private void registerNewUser() {

        // show the visibility of progress bar to show loading

        // Take the value of two edit texts in Strings
        String name, email, number, password;
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        name = mName.getText().toString();
        number = mNumber.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }else if (TextUtils.isEmpty(name)){
                Toast.makeText(getContext(), "Please enter your name!!", Toast.LENGTH_LONG).show();
                return;
        }else if (TextUtils.isEmpty(number)){
                Toast.makeText(getContext(), "Please enter your Phone number!!", Toast.LENGTH_LONG).show();
                return;
        }else if (!mcheckBox.isChecked()){
            Toast.makeText(getContext(), "Please agree our terms and conditions!!", Toast.LENGTH_LONG).show();
        }else {
            mProgressBar.setVisibility(View.VISIBLE);
            // create new user or register new user
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(getContext(), "Sign up error", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                        // hide the progress bar
                        mProgressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                        User user = new User(user_id, name, email,number, password);
                        currentUser_db.setValue(user);

                    }
                }
            });

        }
    }


}


