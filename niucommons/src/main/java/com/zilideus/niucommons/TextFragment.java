package com.zilideus.niucommons;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zilideus.niucommons.api.models.Record;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextFragment extends Fragment {

    public static final String TAG = "TextFragment";

    @BindView(R2.id.t_title_tv) TextView t_title_tv;
    @BindView(R2.id.t_textcontent) TextView t_textcontent;

    public TextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String strRecord = bundle.getString(Constants.RECORD);
            Record record = new Gson().fromJson(strRecord, Record.class);
            this.t_title_tv.setText(record.getTitle());
            this.t_textcontent.setText(record.getNote());
        }
    }
}
