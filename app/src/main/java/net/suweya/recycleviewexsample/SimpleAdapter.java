package net.suweya.recycleviewexsample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VH> {

    private final Object mLock = new Object();

    private ArrayList<String> mDatas;

    public SimpleAdapter(ArrayList<String> datas) {
        mDatas = datas;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.text.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addAll(ArrayList<String> data) {
        synchronized (mLock) {
            int positionStart = mDatas.size();
            int itemCount = data.size();
            mDatas.addAll(data);
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    public void onRefresh(ArrayList<String> data) {
        synchronized (mLock) {
            // 切换数据源
            Log.d("SimpleAdapter", "onRefresh : " + data.get(0));
            mDatas = data;
            notifyDataSetChanged();
        }
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView text;

        public VH(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
