package com.kazeik.screenview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class InputActivity extends Activity {

    @InjectView(R.id.tv_titleName)
    TextView tvTitleName;
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.et_value)
    EditText etValue;
    @InjectView(R.id.btn_screen)
    Button btnScreen;
    @InjectView(R.id.btn_check)
    Button btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.inject(this);

        ivLeft.setVisibility(View.VISIBLE);
//        etValue.setText("1861677034100018");
    }

    @OnClick({R.id.iv_left, R.id.btn_screen, R.id.btn_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_screen:
                break;
            case R.id.btn_check:
                String value = etValue.getText().toString();
                if (TextUtils.isEmpty(value)) {
                    Toast.makeText(this, "提交内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendCode(value);
                break;
        }
    }

    private void sendCode(String code) {
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
                    Toast.makeText(InputActivity.this, "数据异常，请重试", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(value);
                        Toast.makeText(InputActivity.this, obj.optString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(InputActivity.this, "数据异常，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(InputActivity.this, "数据发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
