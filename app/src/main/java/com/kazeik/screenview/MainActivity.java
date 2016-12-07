package com.kazeik.screenview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import google.zxing.sacn.CaptureActivity;

public class MainActivity extends Activity {

    @InjectView(R.id.tv_titleName)
    TextView tvTitleName;
    @InjectView(R.id.btn_screen)
    Button btnScreen;
    @InjectView(R.id.btn_check)
    Button btnCheck;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.btn_peisun)
    Button btnPeisun;
    @InjectView(R.id.btn_finish)
    Button btnFinish;

    String code;
    @InjectView(R.id.ll_view)
    LinearLayout llView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);


    }


    @OnClick({R.id.iv_left, R.id.btn_screen, R.id.btn_check, R.id.btn_finish, R.id.btn_peisun})
    public void onClick(View view) {
        Intent intt = new Intent();
        switch (view.getId()) {
            case R.id.iv_left:
                break;
            case R.id.btn_screen:
                intt.setClass(this, CaptureActivity.class);
                startActivityForResult(intt, 100);
                break;
            case R.id.btn_check:
//                String value = etValue.getText().toString();
                intt.setClass(this, InputActivity.class);
                startActivity(intt);
                break;
            case R.id.btn_peisun:
                break;
            case R.id.btn_finish:
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(this, "提交内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendCode();
                break;
        }
    }

    private void sendCode() {
        String url = Utils.baseUrl+"scanOrderBill?ucode=88616770341&uid=2988&ocode=" + code;
        HttpUtils utils = new HttpUtils();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                String value = responseInfo.result;
                if (TextUtils.isEmpty(value)) {
                    Toast.makeText(MainActivity.this, "数据异常，请重试", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(value);
                        Toast.makeText(MainActivity.this, obj.optString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "数据异常，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(MainActivity.this, "数据发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        if (requestCode == 100) {
            code = data.getStringExtra("code");
            tvNum.setText(code);
            llView.setVisibility(View.VISIBLE);
        }
    }

}
