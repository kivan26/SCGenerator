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
public class PageImageVideo extends Fragment {
    private int value;
    @Bind(R.id.edittextnumberofimg)EditText mEditTextNumber;
    @Bind(R.id.edittextwidth) EditText mEditTextWidth;
    @Bind(R.id.edittextheight) EditText mEditTextHeight;
    @Bind(R.id.bodyimagevideo) LinearLayout linearLayout;
    @Bind(R.id.buttongenerate) Button mButtonGenerate;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.relativelayoutmain) RelativeLayout mRelativeLayoutMain;

    public static PageImageVideo init(int value) {
        PageImageVideo pageImageVideo = new PageImageVideo();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        pageImageVideo.setArguments(bundle);
        return pageImageVideo;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        value = getArguments()!= null ? getArguments().getInt("value") : 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imagevideo, null);

        ButterKnife.bind(this, view);

        if(value == 3) {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorTreal));
        } else {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

       // snackbar = Snackbar.make(this, "Done", Snackbar.LENGTH_LONG);

        return view;
    }



    @OnClick(R.id.buttongenerate)
    public void generate() {
        if (MainActivity.genService == null) {
            return;
        }

        // disable button
        mButtonGenerate.setEnabled(false);
        // show progress bar
        mProgressBar.setVisibility(View.VISIBLE);

        // set video or image width
        if (!mEditTextWidth.getText().toString().isEmpty()) {
            MainActivity.genService.imageVideoWidth = Integer.parseInt(mEditTextWidth.getText().toString());
        }
        // set video or image height
        if (!mEditTextHeight.getText().toString().isEmpty()) {
            MainActivity.genService.imageVideoHeight = Integer.parseInt(mEditTextHeight.getText().toString());
        }
        // default min 1
        int number = 1;
        if (!mEditTextNumber.getText().toString().isEmpty()) {
            number = Integer.parseInt(mEditTextNumber.getText().toString());
        }
        // set max
        mProgressBar.setMax(number);
        // fragment on position 3 is image generator
        if(value == 3) {
            MainActivity.genService.generateImages(number);
        // fragment on position 4 is video generator
        } else if (value == 4) {
            MainActivity.genService.generateVideo(number);
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
                    break;

                default:
                    break;
            }
        }
    };




    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(GeneratorConstants.BROADCAST_EXTRA_ORDER));
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
