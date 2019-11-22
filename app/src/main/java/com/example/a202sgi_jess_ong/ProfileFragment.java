package com.example.a202sgi_jess_ong;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
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


public class ProfileFragment extends Fragment{

        //TODO: Add back btn
        //TODO: Display profile

        private TextView mTextView;
        private Button btnLogOut;

        private FirebaseAuth mFirebaseAuth;
        private ProgressDialog mProgressDialog;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_profile, container, false);

                 mTextView = (TextView)view.findViewById(R.id.textViewUserEmail);
                 btnLogOut = (Button)view.findViewById(R.id.btn_logOut);

                 btnLogOut.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {

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