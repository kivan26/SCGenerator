package com.example.milan.scgenerator.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.milan.scgenerator.GeneratorConstants;
import com.example.milan.scgenerator.R;
import com.example.milan.scgenerator.activities.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by milan on 19.11.15..
 */
public class PageAll extends Fragment {
    @Bind(R.id.generate) Button mButtonGenerate;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.edittextnumberall) EditText mEditTextNumber;
    @Bind(R.id.swsms) Switch mSwitchSms;
    @Bind(R.id.swcontact) Switch mSwitchCon;
    @Bind(R.id.swimage) Switch mSwithcImg;
    @Bind(R.id.swvideo) Switch mSwitchVid;
    private boolean[] swstatus = new boolean[4];



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, null);

        ButterKnife.bind(this, view);

        // switch listeners
        mSwitchSms.setOnCheckedChangeListener(new SmsSwitch());
        mSwitchCon.setOnCheckedChangeListener(new ConSwitch());
        mSwithcImg.setOnCheckedChangeListener(new ImgSwitch());
        mSwitchVid.setOnCheckedChangeListener(new VidSwitch());

        return view;
    }

    /** On checked change listener for sms switch. */
    private class SmsSwitch implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                swstatus[0] = true;
            } else {
                swstatus[0] = false;
            }
        }
    }

    /** On checked change listener for contact switch. */
    private class ConSwitch implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                swstatus[1] = true;
            } else {
                swstatus[1] = false;
            }
        }
    }

    /** On checked change listener for image switch. */
    private class ImgSwitch implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                swstatus[2] = true;
            } else {
                swstatus[2] = false;
            }
        }
    }

    /** On checked change listener for video switch. */
    private class VidSwitch implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                swstatus[3] = true;
            } else {
                swstatus[3] = false;
            }
        }
    }


    @OnClick(R.id.generate)
    public void generate() {
        if (MainActivity.genService == null) {
            return;
        }
        // show progress bar
        mProgressBar.setVisibility(View.VISIBLE);

        int number = 1;
        if (!mEditTextNumber.getText().toString().isEmpty()) {
            number = Integer.parseInt(mEditTextNumber.getText().toString());
        }

        // set max in progressbar
        mProgressBar.setMax(number);


        MainActivity.genService.generateAll(number, swstatus);

    }


    /** State reciver from service. */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String state = bundle.getString(GeneratorConstants.BROADCAST_EXTRA_GET_ORDER);

            switch (state) {
                case GeneratorConstants.BROADCAST_ACTION_FINISHED:
                    mProgressBar.setVisibility(View.GONE);
                    mButtonGenerate.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };




    /** Click on SMS body. */
    @OnClick(R.id.relativelayoutsms)
    public void smsbody() {
        ((MainActivity) getActivity()).viewPager.setCurrentItem(1);
    }


    /** Click on Contact body. */
    @OnClick(R.id.relativelayoutcontacts)
    public void contactbody() {
        ((MainActivity) getActivity()).viewPager.setCurrentItem(2);
    }


    /** Click on Image body. */
    @OnClick(R.id.relativelayoutimage)
    public void imagebody() {
        ((MainActivity) getActivity()).viewPager.setCurrentItem(3);
    }


    /** Click on Video body. */
    @OnClick(R.id.relativelayoutvideo)
    public void videobody() {
        ((MainActivity) getActivity()).viewPager.setCurrentItem(4);
    }



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
