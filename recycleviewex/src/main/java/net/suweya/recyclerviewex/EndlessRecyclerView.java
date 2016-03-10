package net.suweya.recyclerviewex;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Endless RecyclerView
 */
public class EndlessRecyclerView extends RecyclerView {

    private static final String TAG = EndlessRecyclerView.class.getSimpleName();

    public interface OnLoadNextPageListener {
        void loadNextPage(int page);
    }

    /**
     * 默认分页起始页索引
     */
    private static final int DEFAULT_PAGE_BEGIN_INDEX = 1;

    private OnLoadNextPageListener mListener;
    /**
     * 是否正在加载更多数据
     */
    private boolean mIsLoadingData = false;
    /**
     * 是否启用加载更多功能
     */
    private boolean mLoadingMoreEnabled = true;
    /**
     * 是否已经没有更多数据
     */
    private boolean mIsNoMore = false;
    /**
     * 是否正在刷新
     */
    private boolean mIsRefreshing = false;
    private AbsStateFooterView mFooterView;
    private int mPreviousTotal;

    /**
     * 默认第一页
     */
    private int mCurrentPage = DEFAULT_PAGE_BEGIN_INDEX;

    private WrapAdapter mWrapAdapter;
    /**
     * 保存用户自定义的起始页索引
     */
    private int mCustomPageIndex = -1;

    public EndlessRecyclerView(Context context) {
        this(context, null);
    }

    public EndlessRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mFooterView = new StateFooterView(context);
        mFooterView.setVisibility(GONE);
    }

    public void resetStateOnRefreshSuccess() {
        mIsNoMore = false;
        mCurrentPage = mCustomPageIndex == -1 ? DEFAULT_PAGE_BEGIN_INDEX : mCustomPageIndex;
        mPreviousTotal = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter, mFooterView);
        adapter.registerAdapterDataObserver(mDataObserver);
        super.setAdapter(mWrapAdapter);
    }

    public void loadMoreComplete() {
        mIsLoadingData = false;

        if(mPreviousTotal <  getLayoutManager().getItemCount()) {
            mCurrentPage++;
        } else {
            mIsNoMore = true;
            mFooterView.setNoMoreDataState();
        }
        mPreviousTotal = getLayoutManager().getItemCount();
    }

    public void loadMoreError() {
        mIsLoadingData = false;
        mFooterView.setLoadFailedState();
    }

    public void loadMoreCancel() {
        mIsLoadingData = false;
        mFooterView.setVisibility(GONE);
    }

    /**
     * 设置是否正在下拉刷新，防止刷新的时候向下滑动View触发loadNextPage
     */
    public void setIsRefreshing(boolean refreshing) {
        this.mIsRefreshing = refreshing;
    }

    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.mLoadingMoreEnabled = loadingMoreEnabled;
    }

    public void setFooterView(AbsStateFooterView view) {
        this.mFooterView = view;
    }

    public void setOnLoadNextPageListener(OnLoadNextPageListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置页数起始值(默认从1开始)
     * @param index 页数起始值
     */
    public void setPageIndex(int index) {
        this.mCustomPageIndex = index;
        this.mCurrentPage = index;
    }

    /**
     * <ul>
     *     <li>1.1 RecyclerView 处于SCROLL_STATE_IDLE</li>
     *     <li>1.2 mListener不为null</li>
     *     <li>1.3 mIsLoadingData为false</li>
     *     <li>1.4 mLoadingMoreEnabled为true</li>
     *     <li>2. 根据LayoutManager获取lastVisibleItemPosition</li>
     *     <li>3.1 layoutManager.getChildCount() > 0 (列表不为空)</li>
     *     <li>3.2 lastVisibleItemPosition >= layoutManager.getItemCount() - 1 (已经到最底部, 1可以设为动态，比如当剩下3个Item就加载下一页)</li>
     *     <li>3.3 layoutManager.getItemCount() > layoutManager.getChildCount() (getItemCount获取所有的Item， getChildCount获取当前屏幕上的Item)</li>
     *     <li>3.4 !mIsNoMore 数据是否已经加载完</li>
     *     <li>3.5 mIsRefreshing 是否正在刷新</li>
     * </ul>
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && mListener != null && !mIsLoadingData && mLoadingMoreEnabled) {

            // 2. 根据LayoutManager获取lastVisibleItemPosition
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            // 3. 判断
            int itemCount = layoutManager.getItemCount() - 1;
            int childCount = layoutManager.getChildCount();
            if (childCount > 0
                    && lastVisibleItemPosition >= itemCount
                    && layoutManager.getItemCount() > childCount
                    && !mIsNoMore
                    && !mIsRefreshing) {

                mIsLoadingData = true;
                mFooterView.setLoadingState();
                mFooterView.setVisibility(VISIBLE);
                mListener.loadNextPage(mCurrentPage);
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

}
