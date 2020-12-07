package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
public class ImageDisplayDialogFragment extends DialogFragment {
    public static final String EXTRA_POSITION = "com.ropkastudios.android.thegeographyapp.position";
    private static final String ARG_DIALOG_TITLE = "dialog_title_text";
    private static final String ARG_PARAGRAPH_TEXT = "paragraph_text";
    private static final String ARG_NEGATIVE_BUTTON_TEXT = "negative_button_text";
    private static final String ARG_POSITIVE_BUTTON_TEXT = "positive_button_text";
    private static final String ARG_NEUTRAL_BUTTON_TEXT = "neutral_button_text";
    private static final String ARG_POSITION = "arg_position";
    private static final String ARG_IMAGE_PATH = "arg_image_ath";

    public static ImageDisplayDialogFragment newInstance(String dialogTitle, String paragraph,
                                                        String neutralButtonText, String negativeButtonText,
                                                        String positiveButtonText, int position, String imagePath) {
        Bundle args = new Bundle();
        args.putString(ARG_DIALOG_TITLE, dialogTitle);
        args.putString(ARG_PARAGRAPH_TEXT, paragraph);
        args.putString(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText);
        args.putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText);
        args.putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_IMAGE_PATH, imagePath);

        ImageDisplayDialogFragment fragment = new ImageDisplayDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogTitle = getArguments().getString(ARG_DIALOG_TITLE, "");
        String paragraphBasic = getArguments().getString(ARG_PARAGRAPH_TEXT, "");
        String negativeButtonText = getArguments().getString(ARG_NEGATIVE_BUTTON_TEXT, "");
        String positiveButtonText = getArguments().getString(ARG_POSITIVE_BUTTON_TEXT, "");
        String neutralButtonText = getArguments().getString(ARG_NEUTRAL_BUTTON_TEXT, "");
        final int position = getArguments().getInt(ARG_POSITION, 0);
        String imagePath = getArguments().getString(ARG_IMAGE_PATH, "");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_image, null);

        TextView paragraphTextView = (TextView) v.findViewById(R.id.fragment_dialog_basic_text_paragraph);
        paragraphTextView.setText(paragraphBasic);

        ImageView imageView = (ImageView) v.findViewById(R.id.fragment_dialog_basic_imageview);



        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (imagePath.endsWith(TextInterpreter.CAMERA_INDICATOR)) {
            File imageFile = new File(imagePath.replace(TextInterpreter.CAMERA_INDICATOR, ""));
            if (imageFile.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            } else {
                imageView.setImageResource(R.drawable.ic_action_tutorial);
            }
        } else {
            Uri imageUri = Uri.parse(imagePath);
            try {
                imageView.setImageBitmap(BitmapOperations.scaleDownBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri),
                        Math.round(metrics.widthPixels / 2.5F), Math.round(metrics.heightPixels / 7.5F)));
            } catch (IOException e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.ic_action_tutorial);
            }
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(dialogTitle)
                .setPositiveButton(positiveButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK, position);
                            }
                        })
                .setNegativeButton(negativeButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_FIRST_USER, position);
                            }

                        })
                .setNeutralButton(neutralButtonText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getTargetFragment() != null) {
                                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                                            Activity.RESULT_CANCELED, null);
                                }
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, int position) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
