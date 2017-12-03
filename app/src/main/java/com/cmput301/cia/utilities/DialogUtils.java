/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.TimedClickListener;

/**
 * @author Shipin, Jessica
 */

public class DialogUtils {
    public static void createEditDialog(Context context, String title, String hint, final OnOkClickedListener listener) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mview = LayoutInflater.from(context).inflate(R.layout.dialog_input,null);

        final TextView titleTextView = (TextView) mview.findViewById(R.id.Type_Input);
        titleTextView.setText(title);

        final EditText minput = (EditText) mview.findViewById(R.id.edit_Type_Input);
        minput.setHint(hint);

        Button okButton = (Button) mview.findViewById(R.id.Ok_Button);

        mBuilder.setView(mview);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        okButton.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                listener.onOkClicked(minput.getText().toString());
                dialog.dismiss();
            }
        });
    }

    public interface OnOkClickedListener {
        void onOkClicked(String editString);
    }
}
