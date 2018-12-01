package com.ltt.painter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.jp.jcanvas.JCanvas;
import com.jp.jcanvas.brush.BaseBrush;
import com.jp.jcanvas.widget.BrushSelector;
import com.ltt.painter.brush.BrushTag01;
import com.ltt.painter.brush.EraserTag01;
import com.ltt.painter.utils.SDUtil;
import com.ltt.painter.utils.ToastUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PainterActivity extends AppCompatActivity {

    private BaseBrush mBrush;
    public static final String TAG_NAME = "digits";
    private String mDigits;
    private int mCount;
    @BindView(R.id.tv_content)
    AppCompatTextView mTvContent;
    @BindView(R.id.btn_generate)
    AppCompatButton mBtnGenerate;
    @BindView(R.id.btn_clear)
    AppCompatButton mBtnClear;
    @BindView(R.id.btn_save)
    AppCompatButton mBtnSave;
    @BindView(R.id.sp_painter)
    JCanvas mPainter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter);
        ButterKnife.bind(this);
        mDigits = getIntent().getStringExtra(TAG_NAME);
        BrushSelector bs = new BrushSelector(getApplicationContext());
        mBrush = new BrushTag01();
        bs.addBrush(mBrush);
        bs.addBrush(new EraserTag01());
        mCount = 0;
        mBrush.setColor(Color.RED);
        mPainter.setBrush(mBrush);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_DENIED == permission) {
            String[] req = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, req, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onGenerateClick();
    }

    @OnClick({R.id.btn_save, R.id.btn_clear, R.id.btn_generate})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                onSaveClick();
                break;
            case R.id.btn_generate:
                onGenerateClick();
                break;
            case R.id.btn_clear:
                onClearClick();
        }
    }

    private void onSaveClick() {
        if (!SDUtil.initBitmapDir()) {
            new AlertDialog.Builder(PainterActivity.this)
                    .setTitle("保存失败--没权限")
                    .setPositiveButton("ok", null)
                    .show();
            return;
        }

        String fileName = mDigits + "-" + mTvContent.getText().toString() + "-" + System.currentTimeMillis() + ".jpg";

        if (SDUtil.saveBitmap(fileName, mPainter.getBitmap())) {
            mCount++;
            onGenerateClick();
            switch (mCount) {
                case 20:
                    new AlertDialog.Builder(PainterActivity.this)
                            .setTitle("你连续存了20张图片，可以凭图找连晓磊兑换KFC优惠券")
                            .setPositiveButton("ok", null)
                            .show();
                    break;
                case 50:
                    new AlertDialog.Builder(PainterActivity.this)
                            .setTitle("你连续存了50张图片，可以凭图找连晓磊，让他请客吃饭")
                            .setPositiveButton("ok", null)
                            .show();
                    break;
            }
        } else {
            new AlertDialog.Builder(PainterActivity.this)
                    .setTitle("保存失败--存失败")
                    .setPositiveButton("ok", null)
                    .show();
        }
    }

    private void onClearClick() {
        mPainter.resetCanvas();
    }

    private void onGenerateClick() {
        mPainter.resetCanvas();
        StringBuilder result = new StringBuilder();
        String content = mDigits;
        Random random = new Random();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show("请重新输入数字位数");
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
