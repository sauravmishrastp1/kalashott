package com.dd;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.dd.samplevideoplayer.R;


public class ProgressCircularDialog extends Dialog {
    public ProgressCircularDialog(@NonNull Context context) {
        super(context, R.style.Progress_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_circular_dialog);
    }

    public void setTitle(String text) {

    }

    public void setMessage(String text) {

    }
}
