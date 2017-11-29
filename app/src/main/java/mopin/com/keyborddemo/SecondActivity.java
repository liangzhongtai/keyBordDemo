package mopin.com.keyborddemo;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * Created by liangzhongtai on 2017/11/27.
 */

public class SecondActivity extends android.support.v4.app.FragmentActivity{
    private View mRootV;
    public boolean lastVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | params.flags);
        }
        setContentView(R.layout.activity_second);
        initView();
        initFragment();
    }

    private void initView() {
        mRootV  = findViewById(R.id.root);
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.root, new SecondFragment())
                .commitAllowingStateLoss();
    }


    public void addKeyBordListener(MainActivity.IKeyBoardListener kbListener){
        this.kbListener = kbListener;
        mRootV.getViewTreeObserver().addOnGlobalLayoutListener(glListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDestroy() {
        (findViewById(R.id.root)).getViewTreeObserver().removeOnGlobalLayoutListener(glListener);
        super.onDestroy();
    }


    public MainActivity.IKeyBoardListener kbListener;

    public ViewTreeObserver.OnGlobalLayoutListener glListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int navigationBarHeight = UiUtil.getNavigationBarHeight(SecondActivity.this);
            int statusBarHeight = UiUtil.getStatusBarHeight(SecondActivity.this);
            int totalbarHeight = navigationBarHeight + statusBarHeight;
            Rect r = new Rect();
            mRootV.getWindowVisibleDisplayFrame(r);
            int screenHeight = mRootV.getRootView().getHeight();
            int heightDiff = screenHeight - (r.bottom - r.top);
            if (heightDiff > totalbarHeight) {
                int keyboardHeight = heightDiff - statusBarHeight- UiUtil.getVirtualHeight(SecondActivity.this);
                if(lastVisible!=true)
                    kbListener.onKeyBoardVisible(true, keyboardHeight);
                lastVisible = true;
            }else{
                if(lastVisible!=false)
                    kbListener.onKeyBoardVisible(false, 0);
                lastVisible = false;
            }
        }
    };
}
