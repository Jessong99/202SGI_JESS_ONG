package com.example.a202sgi_jess_ong;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfileFragment extends Fragment{

        int user = 0;
        int m;

        //TODO: Add back btn


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                switch (user)
                {
                        case 0:
                                m = R.layout.fragment_sign_in;
                                break;
                        case 2:
                                m = R.layout.fragment_profile;
                                break;
                }
                return inflater.inflate(m,container,false);
        }
}