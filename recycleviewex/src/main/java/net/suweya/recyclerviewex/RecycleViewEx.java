package net.suweya.recyclerviewex;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * RecycleViewEx
 */
public class RecycleViewEx extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

    private EndlessRecyclerView mRecyclerView;
    private OnRefreshListener mOnRefreshListener;

    public RecycleViewEx(Context context) {
        this(context, null);
    }

    public RecycleViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initView(context);
    }

    private void initView(Context context) {
        mRecyclerView = new EndlessRecyclerView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, params);

        super.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
            mRecyclerView.setIsRefreshing(true);
        }
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public void setStateOnRefreshSuccess() {
        setRefreshing(false);
        mRecyclerView.resetStateOnRefreshSuccess();
        mRecyclerView.setIsRefreshing(false);
    }

    public void setStateOnRefreshError() {
        setRefreshing(false);
        mRecyclerView.setIsRefreshing(false);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setPageIndex(int pageIndex) {
        mRecyclerView.setPageIndex(pageIndex);
    }

    public void setOnLoadNextPageListener(EndlessRecyclerView.OnLoadNextPageListener onLoadNextPageListener) {
        mRecyclerView.setOnLoadNextPageListener(onLoadNextPageListener);
    }

    public void loadMoreComplete() {
        mRecyclerView.loadMoreComplete();
    }

    public void loadMoreError() {
        mRecyclerView.loadMoreError();
    }

    public void loadMoreCancel() {
        mRecyclerView.loadMoreCancel();
    }
}
