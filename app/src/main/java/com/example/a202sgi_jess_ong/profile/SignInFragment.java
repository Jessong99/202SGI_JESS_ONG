package com.example.a202sgi_jess_ong.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.a202sgi_jess_ong.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    //signIn
    private Button btnSignIn;
    private EditText eTextEmail;
    private EditText eTextPassword;
    private TextView textViewRegister;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button button = (Button) view.findViewById(R.id.btn_signIn);
        eTextEmail = (EditText) view.findViewById(R.id.editText_email);
        eTextPassword = (EditText) view.findViewById(R.id.editText_password);
        textViewRegister = (TextView) view.findViewById(R.id.textView_register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String email = eTextEmail.getText().toString().trim();
                String password = eTextPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    //email is empty
                    Toast.makeText(getActivity(),"Please enter the email.",Toast.LENGTH_SHORT).show();
                    //stopping the function execution further
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    //password is empty
                    Toast.makeText(getActivity(),"Please enter the password.",Toast.LENGTH_SHORT).show();
                    //stopping the function execution further
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
                                    //TODO: start profile activity
                                    //pass data to profile

                                    Toast.makeText(getActivity(),"Sign In Successfully",Toast.LENGTH_SHORT).show();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();

                                }else {
                                    //TODO: set why is failed, wrong pw or no user
                                    Toast.makeText(getActivity(),"Sign In Failed. Please Try Again.",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

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
