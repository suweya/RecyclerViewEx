package net.suweya.recyclerviewex;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * WrapAdapter
 */
public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER =  -3;
    private static final int TYPE_NORMAL =  0;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private AbsStateFooterView mFooterView;

    public WrapAdapter(@NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, @NonNull AbsStateFooterView footerView) {
        mAdapter = adapter;
        mFooterView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new VH(mFooterView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            if (position < adapterCount) {
                mAdapter.onBindViewHolder(holder, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter == null) {
            return 1;
        } else {
            return  mAdapter.getItemCount() + 1;
        }
    }

    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isFooter(position)){
            return TYPE_FOOTER;
        }
        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            if (position < adapterCount) {
                return mAdapter.getItemViewType(position);
            }
        }
        return TYPE_NORMAL;
    }

    static class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }
}
