package com.example.a202sgi_jess_ong.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.a202sgi_jess_ong.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private EditText eTextEmailR;
    private EditText eTextPasswordR;
    private InputMethodManager imm;
    private Button button;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        button = (Button) view.findViewById(R.id.btn_register);
        eTextEmailR = (EditText) view.findViewById(R.id.editText_emailR);
        eTextPasswordR = (EditText) view.findViewById(R.id.editText_passwordR);
        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        MenuItem searchItem = view.findViewById(R.id.app_bar_search);
        searchItem.setVisible(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String emailR = eTextEmailR.getText().toString().trim();
                String passwordR = eTextPasswordR.getText().toString().trim();

                if(TextUtils.isEmpty(emailR)){
                    //email is empty
                    Snackbar.make(view, "Please enter the email.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(passwordR)){
                    //password is empty
                    Snackbar.make(view, "Please enter the password.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailR).matches())
                {
                    //email is invalid format
                    Snackbar.make(view, "Please enter a valid email address.", Snackbar.LENGTH_SHORT).show();
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
                                    Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new SignInFragment()).addToBackStack(null).commit();
                                }
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
        return view;
    }
}
