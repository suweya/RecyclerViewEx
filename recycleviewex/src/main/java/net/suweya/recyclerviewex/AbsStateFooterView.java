package net.suweya.recyclerviewex;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * AbsStateFooterView
 */
public abstract class AbsStateFooterView extends RelativeLayout {

    public AbsStateFooterView(Context context) {
        this(context, null);
    }

    public AbsStateFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsStateFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    abstract int footerViewLayoutId();

    abstract void setLoadingState();

    abstract void setLoadFailedState();

    abstract void setNoMoreDataState();

}
