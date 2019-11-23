package com.example.a202sgi_jess_ong;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class ProfileFragment extends Fragment{

        //TODO: Add back btn
        //TODO: Display profile

        private TextView mTextView;
        private Button btnLogOut;
        ProgressDialog mProgressDialog;
        FirebaseAuth mFirebaseAuth;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);

            mTextView = (TextView)view.findViewById(R.id.textViewUserEmail);
            btnLogOut = (Button)view.findViewById(R.id.btn_logOut);

            //check if user currently log in
            mFirebaseAuth = FirebaseAuth.getInstance();
            if (mFirebaseAuth.getCurrentUser() != null){
                /*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                    */
            }

            //get current user email and display on profile
            FirebaseUser user = mFirebaseAuth.getCurrentUser();
            mTextView.setText("Welcome " + user.getEmail());

            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFirebaseAuth.signOut();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new SignInFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


                //TODO: Check xia
                /*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                return view;
        }

}