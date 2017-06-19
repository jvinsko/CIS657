package gvsu.vinsko.bluetoothscannerx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    //Firebase Variables
    private FirebaseAuth mAuth;

    //SignIn Variablae
    private Button signInBtn;
    private Button createBtn;
    private TextView email;
    private TextView password;

    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.content_login);

        //Signin Variables
        signInBtn = (Button) findViewById(R.id.signInBtn);
        createBtn = (Button) findViewById(R.id.createBtn);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        signInBtn.setOnClickListener(v -> {
            if(email.getText().toString().matches("") == false && password.getText().toString().matches("") == false) {
                signIn(email.getText().toString(), password.getText().toString());
            }
        });

        createBtn.setOnClickListener(v -> {
            if(email.getText().toString().matches("") == false && password.getText().toString().matches("") == false) {
                createAccount(email.getText().toString(), password.getText().toString());
            }
        });

        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
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
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("email", currentUser.getEmail());
            startActivity(intent);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("FB", "Logged in");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", mAuth.getCurrentUser().getEmail());
                            startActivity(intent);
                        }
                    }
                });
    }

    private void createAccount(String emailStr, String pw) {
        mAuth.createUserWithEmailAndPassword(emailStr, pw).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("email", emailStr);
                startActivity(intent);
            } else {
                String msg = task.getException().getMessage();
                Snackbar.make(email, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn(String emailStr, String pw) {
        mAuth.signInWithEmailAndPassword(emailStr, pw).addOnCompleteListener(this, task -> {
           if(task.isSuccessful()) {
               Intent intent = new Intent(this, MainActivity.class);
               intent.putExtra("email", emailStr);
               startActivity(intent);
           } else {
               String msg = "Incorrect password or username";
               Snackbar.make(email, msg, Snackbar.LENGTH_LONG).show();
           }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null) {
            mAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        }
    }
}
