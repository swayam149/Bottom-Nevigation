package com.example.bottomnevigation.Comman;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.bottomnevigation.R;


public class NetworkChangeListner extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!CheckNetworkInfo.isConnectedToInternet(context))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_connection,null);
            ad.setView(layout_dialog);

            AlertDialog alertDialog = ad.create();
            alertDialog.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            AppCompatButton btn_retry = layout_dialog.findViewById(R.id.btn_check_internet_connection_retry);

            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });

        }
        else
        {
            Toast.makeText(context, "Your Internet is Connected", Toast.LENGTH_SHORT).show();
        }

    }
}
