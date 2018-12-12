package com.example.sander.locationaware;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class HelpFragment extends android.support.v4.app.Fragment {

    TextView textname;
    TextView textinfo;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);



        textname = v.findViewById(R.id.txt_name_id);
        textname.setText(R.string.InfoApp);

        textinfo = v.findViewById(R.id.txt_info_id);
        textinfo.setText(R.string.TxtInfoApp);




        return v;
    }


}
