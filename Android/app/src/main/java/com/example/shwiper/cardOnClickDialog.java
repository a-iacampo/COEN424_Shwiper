package com.example.shwiper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;


public class cardOnClickDialog extends AppCompatDialogFragment {
    private HorizontalScrollView horizontalScrollView;
    private TextView itemName;
    private TextView itemPrice;
    private TextView itemLocation;
    private TextView itemSize;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog, null);

        horizontalScrollView = view.findViewById(R.id.imageScrollView);
        itemName = view.findViewById(R.id.itemNameText);
        itemPrice = view.findViewById(R.id.itemPriceText);
        itemLocation = view.findViewById(R.id.itemLocationText);
        itemSize = view.findViewById(R.id.itemSizeText);

        builder.setView(view).setTitle("More Information")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }


}
