package mopin.com.keyborddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by liangzhongtai on 2017/11/27.
 */

public class SecondFragment extends Fragment{
    private View root;
    private RelativeLayout mEditRL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_second,container,false);
        initNavOffset();
        initView();
        initListener();
        return root;
    }

    private void initNavOffset() {
        TextView navTV = (TextView) root.findViewById(R.id.tv_nav);
        int height = UiUtil.getStatusBarHeight(getActivity());
        navTV.getLayoutParams().height = (int) (getResources().getDisplayMetrics().density*50+height);
        navTV.setPadding((int) (getResources().getDisplayMetrics().density*14),height,0,0);
        navTV.getParent().requestLayout();
    }

    private void initView() {
        mEditRL = (RelativeLayout) root.findViewById(R.id.rl_edit);
    }

    private void initListener() {
        ((SecondActivity)getActivity()).addKeyBordListener(new MainActivity.IKeyBoardListener() {
            @Override
            public void onKeyBoardVisible(boolean visible, int keyboardHeight) {
                ((FrameLayout.LayoutParams)mEditRL.getLayoutParams()).bottomMargin = visible?keyboardHeight:0;
                mEditRL.getParent().requestLayout();
            }
        });
    }
}
