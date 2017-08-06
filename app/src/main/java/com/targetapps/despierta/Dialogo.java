package com.targetapps.despierta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by JOKO on 29/06/2015.
 */
public class Dialogo extends DialogFragment {

    public Dialog onCreateDialog(Bundle saveInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Quiere cerrar Despierta?");
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton(R.string.minimizar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Despierta funcionara en segundo plano", Toast.LENGTH_LONG).show();
                getActivity().moveTaskToBack(true);
            }
        });
        builder.setPositiveButton(R.string.action_salir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Despierta se ha cerrado", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });

        Dialog dialog = builder.create();
        return dialog;
    }



}
