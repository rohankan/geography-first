package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class BasicTextInputActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.ropkastudios.android.thegeographyapp.extra_text";
    public static final String EXTRA_POSITION = "com.ropkastudios.android.thegeographyapp.extra_position";

    private static final String ARG_DIALOG_TITLE_BASIC = "dialog_basic_txt_input_title";
    private static final String ARG_EDIT_HINT = "dialog_basic_txt_input_edit_hint";
    private static final String ARG_EDIT_TEXT = "dialog_basic_txt_input_edit_text";
    private static final String ARG_INDEX = "positive_button_basic_index";
    private static final String ARG_POSITION = "positive_button_basic_position";

    EditText editText;

    public static Intent newIntent(Context packageContext, String dialogTitle, String editHint,
                                   String editText, int Index, int Position) {
        Intent intent = new Intent(packageContext, BasicTextInputActivity.class);

        intent.putExtra(ARG_DIALOG_TITLE_BASIC, dialogTitle);
        intent.putExtra(ARG_EDIT_HINT, editHint);
        intent.putExtra(ARG_EDIT_TEXT, editText);
        intent.putExtra(ARG_POSITION, Position);
        intent.putExtra(ARG_INDEX, Index);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_text_input_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_basic_text_input);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editText = (EditText) findViewById(R.id.dialog_basic_text_input_edit_text);
        editText.setHint(getIntent().getStringExtra(ARG_EDIT_HINT));
        editText.setText(getIntent().getStringExtra(ARG_EDIT_TEXT));
        editText.setSelection(getIntent().getIntExtra(ARG_INDEX, 0));

        Button bullet = (Button) findViewById(R.id.bullet);
        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = editText.getSelectionStart();
                editText.setText(editText.getText().insert(editText.getSelectionStart(), getString(R.string.bullet)));
                editText.setSelection(start + 1);
            }
        });
        Button wbullet = (Button) findViewById(R.id.wbullet);
        wbullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = editText.getSelectionStart();
                editText.setText(editText.getText().insert(editText.getSelectionStart(), getString(R.string.wbullet)));
                editText.setSelection(start + 1);
            }
        });
        Button rarrow = (Button) findViewById(R.id.rarrow);
        rarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = editText.getSelectionStart();
                editText.setText(editText.getText().insert(editText.getSelectionStart(), getString(R.string.rarrow)));
                editText.setSelection(start + 1);
            }
        });
        Button box = (Button) findViewById(R.id.box);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = editText.getSelectionStart();
                editText.setText(editText.getText().insert(editText.getSelectionStart(), getString(R.string.box)));
                editText.setSelection(start + 1);
            }
        });

        setTitle(R.string.change_text_colon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                finish();
                return true;
            case R.id.menu_done_action_done:
                InputMethodManager immm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                immm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                Intent intent = new Intent();

                intent.putExtra(EXTRA_TEXT, editText.getText().toString());
                intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(ARG_POSITION, -1));

                setResult(Activity.RESULT_OK, intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
