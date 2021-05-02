package com.hcare.homeopathy.hcare.NavigationItems;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.hcare.homeopathy.hcare.R;
import com.hcare.homeopathy.hcare.Start.LoginActivity;

public class LogoutDialog {

    Context context;
    Dialog dialog;

    public LogoutDialog(Context context) {
        this.context = context;
        confirmationDialog();
    }

    public void confirmationDialog(){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        setListeners();

        final Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawableResource(R.color.signOutDialogBackground);
        dialog.show();
    }

    private void setListeners(){
        dialog.findViewById(R.id.logout).setOnClickListener(v -> signOut());
        dialog.findViewById(R.id.cancel).setOnClickListener(v -> dialog.dismiss());
    }

    private void signOut() {
        dialog.dismiss();

        FirebaseAuth.getInstance().signOut();
        ((Activity) context).finishAffinity();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
