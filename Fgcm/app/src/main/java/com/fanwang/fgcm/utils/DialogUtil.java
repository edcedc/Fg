package com.fanwang.fgcm.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.fgcm.R;
import com.fanwang.fgcm.bean.User;
import com.fanwang.fgcm.callback.Code;
import com.fanwang.fgcm.controller.CloudApi;
import com.fanwang.fgcm.event.BundleDataInEvent;
import com.fanwang.fgcm.weight.WPopupWindow;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;


/**
 * Created by yuejiaoli on 2017/10/16.
 */

public class DialogUtil {

    /**
     *  腾讯云的
     * @param context
     * @param title
     * @param content
     * @param listener
     */
    public static void showDialog(Context context, String title, String content, final View.OnClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.ConfirmDialogStyle);
        final View v = LayoutInflater.from(context).inflate(R.layout.dialog_ugc_tip, null);
        dialog.setContentView(v);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tv_msg);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        tvTitle.setText(title);
        tvContent.setText(content);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
        dialog.show();
    }

    /**
     *  红包动画
     */
    private static boolean isClick = false;

    private static void RedEnvelopes(final Context act, final double red_envelopes, final String id,
                                     final int redState, final int position, final ReceiveRedListener listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_red_envelopes, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        final ImageView ivSrc = wh.findViewById(R.id.iv_src);
        final ImageView ivEnvelopes = wh.findViewById(R.id.iv_envelopes);
        final ImageView ivDelete = wh.findViewById(R.id.iv_delete);
        final TextView tvNumber = wh.findViewById(R.id.tv_number);

        final RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();//匀速
        rotate.setInterpolator(lin);
        rotate.setDuration(3000);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        ivSrc.setAnimation(rotate);
        isClick = false;
        ivEnvelopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (redState){
                    case BundleDataInEvent.mEncloureFabulous:
                    case BundleDataInEvent.mHotFabulous:
                        red_video(id, listener, ivSrc, popupWindow, rotate, ivEnvelopes, tvNumber, red_envelopes, ivDelete, redState, (Activity) act, position);
                        break;
                    case BundleDataInEvent.reb_Advertisement:
                        red_advertisement(id, listener, ivSrc, popupWindow, rotate, ivEnvelopes, tvNumber, red_envelopes, ivDelete, redState, (Activity) act, position);
                        break;
                    default:
                        popupWindow.dismiss();
                    break;
                }
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivSrc.clearAnimation();
                popupWindow.dismiss();
            }
        });
    }

    private static void red_video(String id, final ReceiveRedListener listener,
                                  final ImageView ivSrc, final WPopupWindow popupWindow,
                                  final RotateAnimation rotate, final ImageView ivEnvelopes,
                                  final TextView tvNumber, final double red_envelopes, final ImageView ivDelete, final int redState, final Activity act, final int position) {
        CloudApi.videoReceiveRedEnvelopes(id)
                .doOnSubscribe(new Consumer<Disposable>(){
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            if (isClick) {
                                ivSrc.clearAnimation();
                                popupWindow.dismiss();
                                return;
                            }
                            isClick = true;
                            ivSrc.clearAnimation();
                            ivSrc.setBackgroundResource(R.mipmap.b30);
                            ivSrc.setAnimation(rotate);
                            ivEnvelopes.setBackgroundResource(R.mipmap.b31);
                            tvNumber.setVisibility(View.VISIBLE);
                            tvNumber.setText(jsonObject.optString("data") + "元");
                            ivDelete.setVisibility(View.VISIBLE);

                            //判断红包是否还存zai
                            if (red_envelopes == jsonObject.optDouble("data")){
                                switch (redState){
                                    case BundleDataInEvent.mEncloureFabulous:
                                        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.mEncloureRedState, position));
                                        break;
                                    case BundleDataInEvent.mHotFabulous:
                                        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.Hot_Red_state, position));
                                        break;
                                    case BundleDataInEvent.weekRanking:
                                        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.weekRankingRed, position));
                                        break;
                                    case BundleDataInEvent.monthRanking:
                                        EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.monthRankingRed, position));
                                        break;
                                }
                            }
                            //判断红包是否被领取
                            switch (redState){
                                case BundleDataInEvent.mEncloureRedState:
                                    EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.Encloure_Red, position));
                                    break;
                                case BundleDataInEvent.mHotFabulous:
                                    EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.Hot_Red, position));
                                    break;
                                case BundleDataInEvent.weekRanking:
                                    EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.weekRankingIsRed, position));
                                    break;
                                case BundleDataInEvent.monthRanking:
                                    EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.monthRankingIsRed, position));
                                    break;
                            }
                            EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.me_update));

                        }else {
                            ivSrc.clearAnimation();
                            popupWindow.dismiss();
                            ToastUtils.showShort(jsonObject.optString("desc"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                    }
                });
    }
    private static void red_advertisement(String id, final ReceiveRedListener listener, final ImageView ivSrc,
                                          final WPopupWindow popupWindow, final RotateAnimation rotate,
                                          final ImageView ivEnvelopes, final TextView tvNumber,
                                          final double red_envelopes, final ImageView ivDelete, final double redState, final Activity act, final int position) {
        CloudApi.advReceiveRedEnvelopes(id)
                .doOnSubscribe(new Consumer<Disposable>(){
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            if (isClick) {
                                ivSrc.clearAnimation();
                                popupWindow.dismiss();
                                return;
                            }
                            isClick = true;
                            ivSrc.clearAnimation();
                            ivSrc.setBackgroundResource(R.mipmap.b30);
                            ivSrc.setAnimation(rotate);
                            ivEnvelopes.setBackgroundResource(R.mipmap.b31);
                            tvNumber.setVisibility(View.VISIBLE);
                            tvNumber.setText(jsonObject.optString("data") + "元");
                            ivDelete.setVisibility(View.VISIBLE);
                            EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.reb_Advertisement));
                            EventBusActivityScope.getDefault(act).post(new BundleDataInEvent(BundleDataInEvent.me_update));
                            JSONObject data = User.getInstance().getUserInformation();
//
//                            double red = data.optDouble("red_envelopes");
//                            try {
//                                data.put("red_envelopes", (red + red_envelopes));
//                                LogUtils.e(red, red_envelopes, (red + red_envelopes));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }

                        }else {
                            ivSrc.clearAnimation();
                            popupWindow.dismiss();
                            ToastUtils.showShort(jsonObject.optString("desc"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        listener.hideLoading();
                    }
                });
    }

    /**
     *  视频领红包
     */
    public static void showRedEnvelopes(final Context act, final double red_envelopes, final String id,
                                        final int redState, final int position,
                                        final ReceiveRedListener listener) {

        RedEnvelopes(act, red_envelopes, id, redState, position, listener);
    }
    /**
     *  广告领红包
     */
    public static void showRedEnvelopes(final Context act,final double red_envelopes,  final String id,
                                       final ReceiveRedListener listener) {
        RedEnvelopes(act, red_envelopes, id, BundleDataInEvent.reb_Advertisement, -1, listener);
    }

    public interface ReceiveRedListener{
        void showLoading();
        void hideLoading();
        void addDisposable(Disposable disposable);
    }

    /**
     *
     * @param act
     * @param listener
     */
    public static final int showSelectListType_0 = 0;// 显示 地图  和列表模式
    public static final int showSelectListType_1 = 1;// 显示 是否选择红包
    public static final int showSelectListType_2 = 2;// 选择支付方式
    public static void showSelectEquipmentModel(Context act, int type, final OnClickListener listener) {
        View wh = LayoutInflater.from(act).inflate(R.layout.p_sel_model, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        TextView tv1 = wh.findViewById(R.id.tv_map);
        TextView tv2 = wh.findViewById(R.id.tv_list);
        switch (type){
            case showSelectListType_0:
                tv1.setText("地图模式");
                tv2.setText("列表模式");
                break;
            case showSelectListType_1:
                tv1.setText("免费视频");
                tv2.setText("红包视频");
                break;
            case showSelectListType_2:
                tv1.setText("支付宝支付");
                tv2.setText("微信支付");
                break;
        }
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (listener != null) {
                    listener.onClick(0);
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (listener != null) {
                    listener.onClick(1);
                }
            }
        });
    }

    public interface OnClickListener {
        void onClick(int position);
    }


    /**
     *  拨打电话
     * @param act
     */
    public static final int showDialog0 = 0;//拨打电话
    public static final int showDialog1 = 1;//拨打电话

    public static void showDialogType(final Context act, final String phone, final int state, final OnClickListener2 listener2){
        showCall(act, phone, state, listener2);
    }
     public static void showDialogType(final Context act, final int state, final OnClickListener2 listener2){
        showCall(act, null, state, listener2);
    }


    private static void showCall(final Context act, final String text, final int state, final OnClickListener2 listener2) {
        View wh = LayoutInflater.from(act).inflate(R.layout.p_call, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        TextView tvText = wh.findViewById(R.id.tv_text);
        TextView btSubmit = wh.findViewById(R.id.bt_submit);
        switch (state){
            case showDialog0:
                tvText.setText("是否拨打商家号码" + text +
                         "?");
                break;
            case showDialog1:
                tvText.setText("确定支付广告币"+ text);
                break;
        }
        wh.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener2 != null){
                    listener2.onClick(state);
                }
                popupWindow.dismiss();
            }
        });
    }
    public interface OnClickListener2 {
        void onClick(int position);
    }



    /**
     *  选择视频编辑框
     */
    public static void showEdit(Context act, final OnEditClickListener  listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_video_edit, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        final EditText etText = wh.findViewById(R.id.et_text);
        final InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);

        wh.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        wh.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etText.getText().toString();
                if (StringUtils.isEmpty(s)){
                    return;
                }
                imm.hideSoftInputFromWindow(etText.getWindowToken(), 0); //强制隐藏键盘
                popupWindow.dismiss();
                if (listener != null){
                    listener.onClick(s);
                }
            }
        });
    }
    public interface OnEditClickListener {
        void onClick(String text);
    }

}
