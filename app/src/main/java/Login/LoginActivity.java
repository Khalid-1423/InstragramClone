package Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.instragramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import Home.HomeActivity;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    //firebase

    private FirebaseAuth mAuth;
    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;
    private TextView mPleaseWait;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = findViewById(R.id.progressBar);
        mPleaseWait = findViewById(R.id.pleaseWait);
        mEmail = findViewById(R.id.input_email);

        mPassword = findViewById(R.id.input_password);
        mContext = LoginActivity.this;

        Log.d(TAG,"onCreate: started");

        mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();



        checkCurrentUserLoggedIn();

        init();



    }

    private void checkCurrentUserLoggedIn()
    {
        if(mAuth.getCurrentUser() !=null)
        {
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void init()
    {
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
                {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    Log.d(TAG,"sognInwithEmail:onComplete: "+task.isSuccessful());

                                    FirebaseUser user =  mAuth.getCurrentUser();


                                    if (task.isSuccessful())
                                    {
                                        try
                                        {
                                            if(user.isEmailVerified())
                                            {
                                                Log.d(TAG,"onComplete: success. email is verified");
                                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Toast.makeText(mContext, "Email is not verified", Toast.LENGTH_SHORT).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                mPleaseWait.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }

                                        }
                                        catch (NullPointerException e)
                                        {
                                            Log.e(TAG,"onComplete: NullPointerException: "+e.getMessage());
                                        }



                                    }
                                    else
                                        {
                                            mProgressBar.setVisibility(View.GONE);
                                            mPleaseWait.setVisibility(View.GONE);
                                            String message = task.getException().toString();
                                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                }
                


            }
        });

        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG,"onClick: navigating to register screen");

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });



    }



}
