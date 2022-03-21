package com.shubhankaranku.socialx;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginTabFragment extends Fragment {


    private static  String TAG = "";
    TextInputEditText mEmail, mPassword;
    TextView textView;
    Button mLogin;
    FloatingActionButton mFb, mGoogle;
    float v = 0;
    FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    TextInputLayout password;
    TextView mRegisterNow;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 150;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";
    private LinearLayout loginLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        mAuth= FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) root.findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL));

        // Callback registration






        mEmail = root.findViewById(R.id.emailLogin);
        mPassword = root.findViewById(R.id.passwordLogin);
        textView = root.findViewById(R.id.textview);
        mLogin = root.findViewById(R.id.login);
        mFb = root.findViewById(R.id.fab_fb);
        mGoogle = root.findViewById(R.id.fab_google);
        mProgressBar = root.findViewById(R.id.progressbar);
        password = root.findViewById(R.id.password);
        mRegisterNow = root.findViewById(R.id.registerPart);
        loginLayout = root.findViewById(R.id.bottmView1);


        mEmail.setTranslationY(300);
        mPassword.setTranslationY(300);
        password.setTranslationY(300);
        textView.setTranslationY(300);
        mLogin.setTranslationY(300);
        mGoogle.setTranslationY(300);
        mFb.setTranslationY(300);

        mEmail.setAlpha(v);
        mPassword.setAlpha(v);
        textView.setAlpha(v);
        mLogin.setAlpha(v);
        mFb.setAlpha(v);
        mGoogle.setAlpha(v);
        password.setAlpha(v);

        mEmail.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        mPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        textView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        mLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        mFb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        mGoogle.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        password.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("612340765955-sj4m7e643i05kpm7nj0lat7do331cogq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        mFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.callOnClick();
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });



        mGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });


        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        mRegisterNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginActivity.viewPager.setCurrentItem(1);
            }
        });

        return root;


    }



    private void LoginUser() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if(email.isEmpty())
        {
            mEmail.setError("Email is empty");
            mEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mEmail.setError("Enter the valid email");
            mEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            mPassword.setError("Password is empty");
            mPassword.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            mPassword.setError("Length of password is more than 6");
            mPassword.requestFocus();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                mEmail.setText(null);
                mPassword.setText(null);
                mProgressBar.setVisibility(View.GONE);
                startActivity(new Intent(getContext(), MainActivity.class));
            }
            else
            {
                Toast.makeText(getContext(), "Please Check Your login Credentials", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }

        });

    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w((String) TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }






}
