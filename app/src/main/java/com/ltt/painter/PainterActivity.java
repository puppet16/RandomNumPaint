package com.ltt.painter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class PainterActivity extends AppCompatActivity {

    public static final String TAG_NAME = "number";
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter);
        content = getIntent().getStringExtra(TAG_NAME);
    }
}
