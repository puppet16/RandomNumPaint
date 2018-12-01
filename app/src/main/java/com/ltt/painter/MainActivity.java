package com.ltt.painter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ltt.painter.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_next)
    AppCompatButton mBtnNext;
    @BindView(R.id.spinner)
    Spinner mSpinner;

    String number = "";
    String[] mStrArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSpinner.setAdapter(new ArrayAdapter<>(this,
                R.layout.spinner_item, mStrArray));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                number = mStrArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                number = mStrArray[0];
            }
        });
    }

    @OnClick({R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                onNextClick();
                break;
        }
    }

    private void onNextClick() {
        if (TextUtils.isEmpty(number)) {
            ToastUtil.show("请选择数字位数啊");
            return;
        }
        Intent intent = new Intent(MainActivity.this, PainterActivity.class);
        intent.putExtra(PainterActivity.TAG_NAME, number);
        startActivity(intent);
    }
}
