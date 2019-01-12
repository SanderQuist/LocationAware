package com.example.sander.locationaware;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class DetailedFragment extends DialogFragment {

    TextView poiName;
    TextView poiPrice;
    TextView poiDescription;
    Button poiGoTO;
    Locale myLocale;

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

        Bundle info = getArguments();

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
//        if(Locale.getDefault().getDisplayLanguage().equals( "nl_NL")){
//            poiDescription.setText(info.getString("description", ""));
//        }
//        else{
//            poiDescription.setText(info.getString("endescription", ""));
//        }

        poiGoTO = view.findViewById(R.id.buttongoto_id);
        poiGoTO.setText(R.string.gotothemarker);
        poiGoTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
            }
        });
        return view;
    }


}
