package com.example.a202sgi_jess_ong.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.example.a202sgi_jess_ong.NewNoteActivity;
import com.example.a202sgi_jess_ong.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    //signIn
    private Button btnSignIn;
    private EditText eTextEmail;
    private EditText eTextPassword;
    private TextView textViewRegister;
    private TextView textView2;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    private LottieAnimationView animationView;
    private InputMethodManager imm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button button = (Button) view.findViewById(R.id.btn_signIn);
        eTextEmail = (EditText) view.findViewById(R.id.editText_email);
        eTextPassword = (EditText) view.findViewById(R.id.editText_password);
        textViewRegister = (TextView) view.findViewById(R.id.textView_register);
        final InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String email = eTextEmail.getText().toString().trim();
                String password = eTextPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    //email is empty
                    Snackbar.make(view, "Please enter the email.", Snackbar.LENGTH_SHORT).show();
                    //stopping the function execution further
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    //password is empty
                    Snackbar.make(view, "Please enter the password.", Snackbar.LENGTH_SHORT).show();
                    //stopping the function execution further
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    //email is invalid format
                    Snackbar.make(view, "Please enter a valid email address.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                mProgressDialog = new ProgressDialog(getActivity());
                mFirebaseAuth = FirebaseAuth.getInstance();

                //if the fields all filled up
                mProgressDialog.setMessage("Signing In...");
                mProgressDialog.show();

                mFirebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Toast.makeText(getActivity(),"Sign In Successfully",Toast.LENGTH_SHORT).show();
                                    //show user's list of notes
                                    startActivity(new Intent(getActivity(), NewNoteActivity.class));
                                }else {
                                    //TODO: set why is failed, wrong pw or no user
                                    Toast.makeText(getActivity(),"Sign In Failed. Please Try Again.",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

        // TODO: 26-Nov-19 Set animation transaction to show diff 
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
