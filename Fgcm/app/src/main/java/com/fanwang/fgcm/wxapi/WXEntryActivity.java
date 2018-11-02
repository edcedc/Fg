package com.fanwang.fgcm.wxapi;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            LogUtils.e("onPayFinish,errCode="+resp.errCode);

        }
    }
}
