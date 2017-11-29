package mopin.com.keyborddemo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by liangzhongtai on 2017/11/27.
 */

public class MainActivity extends Activity{
    private View mRootV;
    private RelativeLayout mEditRL;
    public boolean lastVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | params.flags);
        }
        setContentView(R.layout.activity_main);
        initNavOffset();
        initView();
        initListener();
    }

    private void initNavOffset() {
        TextView navTV = (TextView) findViewById(R.id.tv_nav);
        int height = UiUtil.getStatusBarHeight(this);
        navTV.getLayoutParams().height = (int) (getResources().getDisplayMetrics().density*50+height);
        navTV.setPadding((int) (getResources().getDisplayMetrics().density*14),height,0,0);
        navTV.getParent().requestLayout();
    }


    private void initView() {
        mRootV  = findViewById(R.id.root);
        mEditRL = (RelativeLayout) findViewById(R.id.rl_edit);
    }


    private void initListener() {
      addKeyBordListener(new IKeyBoardListener() {
          @Override
          public void onKeyBoardVisible(boolean visible, int keyboardHeight) {
              ((FrameLayout.LayoutParams)mEditRL.getLayoutParams()).bottomMargin = visible?keyboardHeight:0;
              mEditRL.getParent().requestLayout();
          }
      });

      (findViewById(R.id.btn_second)).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
          }
      });
    }


    public void addKeyBordListener(IKeyBoardListener kbListener){
        this.kbListener = kbListener;
        mRootV.getViewTreeObserver().addOnGlobalLayoutListener(glListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDestroy() {
        (findViewById(R.id.root)).getViewTreeObserver().removeOnGlobalLayoutListener(glListener);
        super.onDestroy();
    }


    public IKeyBoardListener kbListener;
    public interface IKeyBoardListener{
        void onKeyBoardVisible(boolean visible , int keyboardHeight);
    }

    public ViewTreeObserver.OnGlobalLayoutListener glListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int navigationBarHeight = UiUtil.getNavigationBarHeight(MainActivity.this);
            int statusBarHeight = UiUtil.getStatusBarHeight(MainActivity.this);
            int totalbarHeight = navigationBarHeight + statusBarHeight;
            Rect r = new Rect();
            mRootV.getWindowVisibleDisplayFrame(r);
            int screenHeight = mRootV.getRootView().getHeight();
            int heightDiff = screenHeight - (r.bottom - r.top);
            if (heightDiff > totalbarHeight) {
                int keyboardHeight = heightDiff - statusBarHeight- UiUtil.getVirtualHeight(MainActivity.this);
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
