package com.example.xhello;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;

/**
 *
 */

public class SmartActivity extends Activity {

    private ListView lvList;

    //聊天对象集合
    private ArrayList<TalkBean> mTalkList = new ArrayList<TalkBean>();

    private ChatAdapter mAdapter;

    private StringBuffer mBuffer;

    private String[] mAnswers = new String[] { "长沙很美哦", "长沙很美哦", "长沙很美哦",
            "长沙很美哦", "长沙很美哦" };

    private int[] mImageIds = new int[] { R.drawable.p1, R.drawable.p2,
            R.drawable.p3, R.drawable.p4 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_detail);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
// 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=562c44bb");

        lvList = (ListView) findViewById(R.id.lv_list);

        mAdapter = new ChatAdapter();
        lvList.setAdapter(mAdapter);
    }



    public void startVoice(View view) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, null);
        //2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");

        mBuffer = new StringBuffer();

        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {

            //听写结果回调接口(返回Json格式结果，用户可参见附录13.1)；
            //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
            //关于解析Json的代码可参见Demo中JsonParser类；
            //isLast等于true时会话结束。
            @Override
            public void onResult(RecognizerResult results, boolean isLast) {
                String result = results.getResultString();//语音听写的结果
                //System.out.println(result);
                //System.out.println("isLast:" + isLast);
                String resultString = processData(result);

                mBuffer.append(resultString);

                if (isLast) {
                    //话已经说完了
                    String finalResult = mBuffer.toString();
                    System.out.println("解析结果:" + finalResult);

                    //初始化提问对象
                    TalkBean askBean = new TalkBean(finalResult, true, -1);
                    mTalkList.add(askBean);

                    //初始化回答对象
                    String answerContent = "没听清";
                    int imageId = -1;
                    if (finalResult.contains("你好")) {
                        answerContent = "你好呀!";
                    } else if (finalResult.contains("你是谁")) {
                        answerContent = "我是你的小助手!";
                    } else if (finalResult.contains("长沙")) {
                        //随机回答
                        int i = (int) (Math.random() * mAnswers.length);
                        answerContent = mAnswers[i];
                        //随机图片
                        int j = (int) (Math.random() * mImageIds.length);
                        imageId = mImageIds[j];
                    } else if(finalResult.contains("中南大学")){
                        answerContent = "是一所位于长沙的知名高校哦";
                    } else if(finalResult.contains("天气")){
                        answerContent = "娄底市娄星区：小雨，气温5到10度";
                    } else if(finalResult.contains("日期")){
                        answerContent = "今天2月25日，农历正月十四";
                    } else if(finalResult.contains("新年")){
                        answerContent = "也祝你新年快乐，吉祥如意";
                    } else if(finalResult.contains("哪里")){
                        answerContent = "我住在武汉的一台服务器里呢";
                    } else if(finalResult.contains("新闻")){
                        answerContent = "女孩乘货拉拉跳车前一分钟监控曝光;\r\n" +
                                        "天问一号进入火星停泊轨道\r\n";
                    }

                    TalkBean answerBean = new TalkBean(answerContent, false,
                            imageId);
                    mTalkList.add(answerBean);

                    //刷新listview
                    mAdapter.notifyDataSetChanged();

                    //显示在最后一个item上
                    lvList.setSelection(mTalkList.size() - 1);

                    startSpeak(answerContent);
                }

            }

            @Override
            public void onError(SpeechError error) {

            }
        });

        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    //朗诵
    protected void startSpeak(String answerContent) {
        //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer
                .createSynthesizer(this, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录12.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(answerContent, null);
    }

    //解析json
    protected String processData(String result) {
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(result, VoiceBean.class);

        StringBuffer sb = new StringBuffer();

        ArrayList<VoiceBean.WsBean> ws = voiceBean.ws;
        for (VoiceBean.WsBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }

        return sb.toString();
    }

    class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTalkList.size();
        }

        @Override
        public TalkBean getItem(int position) {
            return mTalkList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.list_item, null);

                holder = new ViewHolder();
                holder.tvAsk = (TextView) convertView.findViewById(R.id.tv_ask);
                holder.tvAnswer = (TextView) convertView
                        .findViewById(R.id.tv_answer);
                holder.llAnswer = (LinearLayout) convertView
                        .findViewById(R.id.ll_answer);
                holder.ivAnswer = (ImageView) convertView
                        .findViewById(R.id.iv_answer);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TalkBean item = getItem(position);

            if (item.isAsk) {
                //提问
                holder.tvAsk.setVisibility(View.VISIBLE);
                holder.llAnswer.setVisibility(View.GONE);

                holder.tvAsk.setText(item.content);
            } else {
                //回答
                holder.tvAsk.setVisibility(View.GONE);
                holder.llAnswer.setVisibility(View.VISIBLE);

                holder.tvAnswer.setText(item.content);

                if (item.imageId > 0) {
                    //有图片
                    holder.ivAnswer.setVisibility(View.VISIBLE);
                    holder.ivAnswer.setImageResource(item.imageId);
                } else {
                    //无图
                    holder.ivAnswer.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

    }

    static class ViewHolder {
        public TextView tvAsk;
        public TextView tvAnswer;
        public LinearLayout llAnswer;
        public ImageView ivAnswer;
    }
}
