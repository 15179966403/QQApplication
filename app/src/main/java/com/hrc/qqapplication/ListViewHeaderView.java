package com.hrc.qqapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Message 消息的头视图
 */

public class ListViewHeaderView extends LinearLayout{
    private LinearLayout mContainer;        //头部视图
    private ImageView mArrowImageView;      //箭头图像
    private ProgressBar mProgressBar;       //圆形进度
    private TextView mHintTextView;         //刷新提示

    private Animation mRotateUpAnim;        //箭头向上动画
    private Animation mRotateDownAnim;      //箭头向下动画

    private final int ROTATE_ANIM_DURATION=180;     //动画的播放时长

    private int mState=STATE_NORMAL;        //记录状态
    public final static int STATE_NORMAL=0;
    public final static int STATE_READY=1;
    public final static int STATE_REFRESHING=2;

    public ListViewHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public ListViewHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        LinearLayout.LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        mContainer=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_header,null);
        addView(mContainer,lp);     //加入头部视图
        setGravity(Gravity.BOTTOM); //设置布局方式

        //控件的初始化
        mArrowImageView=(ImageView)findViewById(R.id.xlistview_header_arrow);
        mHintTextView= (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar= (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

        mRotateUpAnim=new RotateAnimation(0.0f,-180.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim=new RotateAnimation(-180.0f,0.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state){
        if (state==mState){
            return;
        }
        if (state==STATE_REFRESHING){
            //重新刷新状态
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            //准备状态
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state){
            case STATE_NORMAL:
                if (mState==STATE_READY){
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState==STATE_REFRESHING){
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText("下拉刷新");
                break;
            case STATE_READY:
                if (mState!=STATE_READY){
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText("释放立即刷新");
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText("正在刷新...");
                break;
        }
        mState=state;
    }

    public void setVisiableHeight(int height){
        if (height<0){
            height=0;
        }
        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)mContainer.getLayoutParams();
        lp.height=height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight(){
        return mContainer.getHeight();
    }
}
