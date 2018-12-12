package com.example.sander.locationaware;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedFragment extends DialogFragment {

    TextView poiName;
    TextView poiBuildDate;
    TextView poiCreator;
    ImageView poiImage;
    TextView poiDescription;

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

        poiName = view.findViewById(R.id.DetailedFragmentPOITitle);
        poiBuildDate = view.findViewById(R.id.DetailedFragmentConstructionInfo);
        poiCreator = view.findViewById(R.id.DetailedFragmentCreatorInfo);
        poiImage = view.findViewById(R.id.DetailedFragmentImage);
        poiDescription = view.findViewById(R.id.DetailedFragmentDescription);

        return view;
    }
}
