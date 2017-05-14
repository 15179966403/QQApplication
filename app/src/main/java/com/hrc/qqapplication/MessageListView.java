package com.hrc.qqapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/14.
 */

public class MessageListView extends ListView implements AbsListView.OnScrollListener{
    private float mLastY=-1;    //保存event的y坐标
    private Scroller mScroller;
    private OnScrollListener mScrollListener;

    private IXListViewListener mListViewListener;

    //header view
    private ListViewHeaderView mHeaderView;
    //header view content,use it to calculate the Header's height.And hide it when disable pull prefresh
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight;      //header view's height
    private boolean mEnablePullRefresh=true;
    private boolean mPullRefreshing=false;  //is refreashing

    private int mTotalItemCount;

    private int mScrollBack;
    private final static int SCROLLBACK_HEADER=0;

    private final static int SCROLL_DURATION=400;

    private final static float OFFSET_RADIO=1.8f;

    public MessageListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public MessageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public MessageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWithContext(context);
    }

    private void initWithContext(Context context){
        mScroller=new Scroller(context,new DecelerateInterpolator());
        super.setOnScrollListener(this);

        //init headerview
        mHeaderView=new ListViewHeaderView(context);
        mHeaderViewContent=(RelativeLayout)mHeaderView.findViewById(R.id.xlistview_header_content);
        addHeaderView(mHeaderView);

        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight=mHeaderViewContent.getHeight();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setPullRefreshEnable(boolean enable){
        mEnablePullRefresh=enable;
        if (!mEnablePullRefresh){
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        }else{
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    //stop refresh , reset header view
    public void stopRefresh(){
        if (mPullRefreshing==true){
            mPullRefreshing=false;
            resetHeaderHeight();
        }
    }

    private void invokeOnScrolling(){
        if (mScrollListener instanceof onXScrollListener){
            onXScrollListener l= (onXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta){
        mHeaderView.setVisiableHeight((int)delta+mHeaderView.getVisibleHeight());
        if (mEnablePullRefresh && !mPullRefreshing){
            if (mHeaderView.getVisibleHeight()>mHeaderViewHeight){
                mHeaderView.setState(ListViewHeaderView.STATE_READY);
            }else{
                mHeaderView.setState(ListViewHeaderView.STATE_NORMAL);
            }
        }
        setSelection(0);
    }

    private void resetHeaderHeight(){
        int height=mHeaderView.getVisibleHeight();
        if (height==0){
            return;
        }
        if (mPullRefreshing&&height<=mHeaderViewHeight){
            return;
        }
        int finalHeight=0;
        if (mPullRefreshing&&height>mHeaderViewHeight){
            finalHeight=mHeaderViewHeight;
        }
        mScrollBack=SCROLLBACK_HEADER;
        mScroller.startScroll(0,height,0,finalHeight-height,SCROLL_DURATION);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY==-1){
            mLastY=ev.getRawY();
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY=ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY=ev.getRawY()-mLastY;
                mLastY=ev.getRawY();
                if (getFirstVisiblePosition()==0&&(mHeaderView.getVisibleHeight()>0||deltaY>0)){
                    updateHeaderHeight(deltaY/OFFSET_RADIO);
                    invokeOnScrolling();
                }
                break;
            default:
                mLastY=-1;  //reset
                if (getFirstVisiblePosition()==0){
                    //invoke refresh
                    if (mEnablePullRefresh&&mHeaderView.getVisibleHeight()>mHeaderViewHeight){
                        mPullRefreshing=true;
                        mHeaderView.setState(ListViewHeaderView.STATE_REFRESHING);
                        if (mListViewListener!=null){
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            if (mScrollBack==SCROLLBACK_HEADER){
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener=l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener!=null){
            mScrollListener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount=totalItemCount;
        if (mScrollListener!=null){
            mScrollListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l){
        mListViewListener=l;
    }

    public interface onXScrollListener extends OnScrollListener{
        public void onXScrolling(View view);
    }

    public interface IXListViewListener{
        public void onRefresh();
    }
}
