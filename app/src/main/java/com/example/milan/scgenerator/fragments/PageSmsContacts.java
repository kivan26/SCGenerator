package com.example.milan.scgenerator.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.milan.scgenerator.GeneratorConstants;
import com.example.milan.scgenerator.R;
import com.example.milan.scgenerator.activities.MainActivity;
import com.example.milan.scgenerator.services.GenerateService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by milan on 18.11.15..
 */
public class PageSmsContacts extends Fragment {
    private int value;
    @Bind(R.id.edittextnumberofimg) EditText mEditTextNumber;
    @Bind(R.id.linearlayoutsmscontacts) LinearLayout mLinearLayoutBody;
    @Bind(R.id.generate) Button mButtonGenerate;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.relativelayoutmain) RelativeLayout mRelativeLayoutMain;
    private int counter;

    public static PageSmsContacts init(int value) {
        PageSmsContacts pageSmsContacts = new PageSmsContacts();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        pageSmsContacts.setArguments(bundle);
        return pageSmsContacts;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        value = getArguments() != null ? getArguments().getInt("value") : 1;
        //ButterKnife.bind(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smscontacts, null);

        ButterKnife.bind(this, view);
        if (value == 1) {
            mLinearLayoutBody.setBackgroundColor(getResources().getColor(R.color.colorCyan));
        }

        return view;
    }


    /** On click listener for button generate. */
    @OnClick(R.id.generate)
    public void generate() {
        if (MainActivity.genService == null) {
            return;
        }
        // show progressbar
        mProgressBar.setVisibility(View.VISIBLE);

        // default 1
        int number = 1;
        if (!mEditTextNumber.getText().toString().isEmpty()) {
            number = Integer.parseInt(mEditTextNumber.getText().toString());
        }

        // set max
        mProgressBar.setMax(number);

        if (value == 1) {
            MainActivity.genService.generateSms(number);
        } else {
            MainActivity.genService.generateContacts(number);
        }

    }



    /** State reciver from service. */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String state = bundle.getString(GeneratorConstants.BROADCAST_EXTRA_GET_ORDER);

            switch (state) {
                case GeneratorConstants.BROADCAST_ACTION_FINISHED:
                    final Snackbar snackbar = Snackbar.make(mRelativeLayoutMain, "Done"
                            , Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    // enable button
                    mButtonGenerate.setEnabled(true);
                    // hide progressbar
                    mProgressBar.setVisibility(View.GONE);

                    // reset counter
                    counter = 0;
                    break;

                case GeneratorConstants.BROADCAST_ACTION_ONEFINISHED:
                    mProgressBar.setProgress(counter);
                    break;

                case GeneratorConstants.BROADCAST_ACTION_FAILED:
                    final Snackbar snackbar1 = Snackbar.make(mRelativeLayoutMain, "Failed"
                            , Snackbar.LENGTH_LONG);
                    snackbar1.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar1.dismiss();
                        }
                    });
                    snackbar1.show();
                    break;

                default:
                    break;
            }
        }
    };




    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver
                , new IntentFilter(GeneratorConstants.BROADCAST_EXTRA_ORDER));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



}
