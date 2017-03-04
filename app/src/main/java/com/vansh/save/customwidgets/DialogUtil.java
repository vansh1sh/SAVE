package com.vansh.save.customwidgets;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

public class DialogUtil {
    public static void createDialog(String message, Activity activity, final OnPositiveButtonClick onPositiveButtonClick){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", null);
        alertDialogBuilder.show();
    }

    public interface OnPositiveButtonClick {
        void onClick();
    }
}
