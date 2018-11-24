package com.ltt.painter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.ltt.painter.utils.ToastUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_content)
    AppCompatTextView mTvContent;
    @BindView(R.id.btn_generate)
    AppCompatButton mBtnGenerate;
    @BindView(R.id.btn_next)
    AppCompatButton mBtnNext;
    @BindView(R.id.edt_number)
    AppCompatEditText mEdtNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_next, R.id.btn_generate})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                onNextClick();
                break;
            case R.id.btn_generate:
                onGenerateClick();
                break;
        }
    }

    private void onNextClick() {
        if (TextUtils.isEmpty(mTvContent.getText().toString())) {
            ToastUtil.show("请生成随机数字~");
            return;
        }
        Intent intent = new Intent(MainActivity.this, PainterActivity.class);
        intent.putExtra(PainterActivity.TAG_NAME, mTvContent.getText().toString());
        mTvContent.setText("");
        mEdtNumber.setText("");
        startActivity(intent);
    }

    private void onGenerateClick() {
        StringBuilder result = new StringBuilder();
        String content = mEdtNumber.getText().toString();
        Random random = new Random();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show("请输入数字位数啊");
            return;
        }
        int contentDigits = Integer.parseInt(content);
        if (contentDigits == 1) {
            result = new StringBuilder(String.valueOf(random.nextInt(10)));//生成的[0,10)之间的数，包含0，不包含10;
        } else {
            for (int i = 0; i < contentDigits; i++) {
                if (i == 0) {
                    result.append(String.valueOf(random.nextInt(9) + 1));//1--9
                } else {
                    result.append(String.valueOf(random.nextInt(10)));//0--9
                }
            }
        }
        mTvContent.setText(result.toString());
    }
}
