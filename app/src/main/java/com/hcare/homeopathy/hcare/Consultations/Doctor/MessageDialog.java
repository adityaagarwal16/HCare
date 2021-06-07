package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class MessageDialog {

    View root;
    AlertDialog dialog;
    Context context;
    ChatObject object;

    @SuppressLint("InflateParams")
    public MessageDialog(Context context, ChatObject object) {
        this.context = context;
        this.object = object;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        root = LayoutInflater.from(context)
                .inflate(R.layout.dialog_message, null);
        alertDialogBuilder.setView(root);

        dialog = alertDialogBuilder.create();
        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(
                (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT);

        setListeners();
        setFields();
    }

    void setFields() {
        ((TextView) root.findViewById(R.id.header)).setText(object.getMessage());
        ((TextView) root.findViewById(R.id.line)).setText(object.getMessage());
    }

    void setListeners() {
        root.findViewById(R.id.copy).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Message", object.getMessage());
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();
        });
    }
}
