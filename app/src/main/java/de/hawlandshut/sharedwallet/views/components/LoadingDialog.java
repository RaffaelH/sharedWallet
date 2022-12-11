package de.hawlandshut.sharedwallet.views.components;

import static android.graphics.Color.TRANSPARENT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import de.hawlandshut.sharedwallet.R;

public class LoadingDialog {

    private Context context;
    private Dialog dialog;

   public LoadingDialog(Context context)
   {
        this.context = context;
    }

    public void showDialog(){
       dialog = new Dialog(context);
       dialog.setContentView(R.layout.loading_dialog);
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       dialog.create();
       dialog.show();
    }

    public void closeDialog(){
       dialog.dismiss();
    }

}
