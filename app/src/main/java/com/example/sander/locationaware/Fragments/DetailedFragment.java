package com.example.sander.locationaware.Fragments;

import android.app.Dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.sander.locationaware.Activity.MapsActivity;
import com.example.sander.locationaware.R;

import java.util.Locale;

public class DetailedFragment extends DialogFragment {

    TextView poiName;
    TextView poiPrice;
    TextView poiDescription;
    Button poiGoTO;
    Locale myLocale;
    private SharedPreferences pref;
    Bundle info;
    String key;

    public static DetailedFragment newInstance(){
        DetailedFragment detailedFragment = new DetailedFragment();
        return detailedFragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        info = getArguments();

        poiName = view.findViewById(R.id.DetailedFragmentPOITitle);
        poiName.setText(info.getString("title", ""));
        poiPrice = view.findViewById(R.id.DetailedFragmentPrice);
        poiPrice.setText(String.valueOf(info.getString("price", "")));


        poiDescription = view.findViewById(R.id.DetailedFragmentDescription);

        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        myLocale = conf.locale;
        System.out.println(myLocale.toString());
        switch(myLocale.toString()){
            case "en":
                poiDescription.setText(info.getString("endescription", ""));
                break;
            case "nl_NL":
                poiDescription.setText(info.getString("description", ""));
                break;
            default:
                poiDescription.setText(info.getString("description", ""));
                break;
        }
        poiGoTO = view.findViewById(R.id.buttongoto_id);
        poiGoTO.setText(R.string.gotothemarker);
        poiGoTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MapsActivity)getActivity()).goHere();
                getActivity().getSupportFragmentManager().beginTransaction().remove(DetailedFragment.this).commit();
            }
        });
        return view;
    }
    //OnClickListenerCode
    /**
     * key = info.getString("title");
     * editor.putBoolean(key, true);
     * editor.commit();
     */
    private void checkPreferences(){
        key = info.getString("title");
        boolean fav = pref.getBoolean(key, false);
    }
    //Code for deleting preferences
    /**
     * key = info.getString("title");
     * editor.remove(key);
     */


}
