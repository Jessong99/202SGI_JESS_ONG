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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private EditText eTextEmailR;
    private EditText eTextPasswordR;
    private TextView textViewSignIn;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button button = (Button) view.findViewById(R.id.btn_register);
        eTextEmailR = (EditText) view.findViewById(R.id.editText_emailR);
        eTextPasswordR = (EditText) view.findViewById(R.id.editText_passwordR);
        textViewSignIn = (TextView) view.findViewById(R.id.textView_signIn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String emailR = eTextEmailR.getText().toString().trim();
                String passwordR = eTextPasswordR.getText().toString().trim();

                //TODO: Compile this error check with the sign in part
                if(TextUtils.isEmpty(emailR)){
                    //email is empty
                    Toast.makeText(getActivity(),"Please enter the email.",Toast.LENGTH_SHORT).show();
                    //stopping the function execution further
                    return;
                }

                if(TextUtils.isEmpty(passwordR)){
                    //password is empty
                    Toast.makeText(getActivity(),"Please enter the password.",Toast.LENGTH_SHORT).show();
                    //stopping the function execution further
                    return;
                }

                mProgressDialog = new ProgressDialog(getActivity());
                mFirebaseAuth = FirebaseAuth.getInstance();

                //if the fields all filled up
                mProgressDialog.setMessage("Registering user...");
                mProgressDialog.show();

                mFirebaseAuth.createUserWithEmailAndPassword(emailR,passwordR)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    //if registration is complete
                                    //TODO: start profile activity
                                    Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new SignInFragment());
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                                //TODO: 24-Nov-19 Delete or not
                                /*}else {
                                    //TODO: set why is failed, specific for 6 pw and legal email
                                    Toast.makeText(getActivity(),"Registered Failed. Please Try Again.",Toast.LENGTH_SHORT).show();
                                }*/
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

             }
        });

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new SignInFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
