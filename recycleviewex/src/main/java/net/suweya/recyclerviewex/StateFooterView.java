package net.suweya.recyclerviewex;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * StateFooterView
 */
public class StateFooterView extends AbsStateFooterView {

    private TextView mTvMessage;
    private ProgressBar mProgressBar;

    public StateFooterView(Context context) {
        this(context, null);
    }

    public StateFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int height = getResources().getDimensionPixelOffset(R.dimen.footer_height);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        setLayoutParams(params);

        //LayoutInflater.from(context).inflate(R.layout.layout_footer, this, false);
        inflate(context, R.layout.layout_footer, this);

        mTvMessage = (TextView) findViewById(R.id.text);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void setLoadingState() {
        mTvMessage.setText(R.string.loading_more);
        mProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void setLoadFailedState() {
        mTvMessage.setText(R.string.load_failed);
        mProgressBar.setVisibility(INVISIBLE);
    }

    @Override
    public void setNoMoreDataState() {
        mTvMessage.setText(R.string.no_more_data);
        mProgressBar.setVisibility(INVISIBLE);
    }

    @Override
    public int footerViewLayoutId() {
        return R.layout.layout_footer;
    }
}
