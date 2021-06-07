package com.batterbox.power.phone.app.act.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.main_chat.MainChatFragment;
import com.batterbox.power.phone.app.event.ExitEvent;
import com.batterbox.power.phone.app.utils.RefreshLanguageHelper;
import com.batterbox.power.phone.app.utils.ScanUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.ui.BaseFragment;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import org.greenrobot.eventbus.Subscribe;

import q.rorbin.badgeview.QBadgeView;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends BaseActivity {
    MainChatFragment mainChatFragment;
    MainUserFragment mainUserFragment;
    MainSearchFragment mainSearchFragment;
    MainMapFragment mainMapFragment;
    TextView homeTv, shopTv, chatTv, userTv;
    int defColor, selectedColor;
    int curIndex = -1;

    QBadgeView chatCountBadge;

    //    MainMenuHelper mainMenuHelper;
//    Intent locServiceIntent;

    public static void goToMain(Context context, boolean newTask) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newTask)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    //    EWGShareReceiver ewgShareReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_m);
        RefreshLanguageHelper.init(this);
        StatusBarCompat.translucentStatusBar(this, true);
        registerEventBus(this);
        if (getIntent() != null) {
            curIndex = getIntent().getIntExtra("curIndex", 0);
        }
        defColor = Color.parseColor("#555555");
        selectedColor = Color.parseColor("#eea636");
        homeTv = findViewById(R.id.act_main_home);
        shopTv = findViewById(R.id.act_main_shop);
        chatTv = findViewById(R.id.act_main_chat);
        userTv = findViewById(R.id.act_main_user);
        homeTv.setOnClickListener(this::changeContentFragment);
        shopTv.setOnClickListener(this::changeContentFragment);
        chatTv.setOnClickListener(this::changeContentFragment);
        userTv.setOnClickListener(this::changeContentFragment);
        chatCountBadge = new QBadgeView(this);
        chatCountBadge.bindTarget(findViewById(R.id.act_main_chat_fl))
                .setShowShadow(false)
                .setBadgeTextSize(8, true)
                .setGravityOffset(16, 0, true)
                .setBadgeTextColor(Color.WHITE);

//        changeContentFragment(homeTv);
        processDefIndex();
        findViewById(R.id.act_m_scan_iv).setOnClickListener(v -> ScanUtil.scan(MainActivity.this));
        ConversationManagerKit.getInstance().addUnreadWatcher(new ConversationManagerKit.MessageUnreadWatcher() {
            @Override
            public void updateUnread(int count) {
                chatCountBadge.setBadgeNumber(count);
            }
        });
        ConversationManagerKit.getInstance().loadConversation(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
//        mainMenuHelper = new MainMenuHelper(this);
//        locServiceIntent = new Intent(this, LocService.class);
//        startService(locServiceIntent);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("android:support:fragments", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (curIndex==0){
            if (mainMapFragment!=null){
                mainMapFragment.onResume();
            }
        }
        if (!UserUtil.isLogin()) {
            UserUtil.gotoLogin();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (curIndex==0){
            if (mainMapFragment!=null){
                mainMapFragment.onPause();
            }
        }
    }

    long temp = 0L;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        if (!ft.isEmpty()) {
//            if (mainChatFragment != null)
//                ft.remove(mainChatFragment);
//            if (mainUserFragment != null)
//                ft.remove(mainUserFragment);
//            if (mainSearchFragment != null)
//                ft.remove(mainSearchFragment);
//            if (mainMapFragment != null)
//                ft.remove(mainMapFragment);
//            ft.commit();
//        }
        mainChatFragment = null;
        mainUserFragment = null;
        mainSearchFragment = null;
        mainMapFragment = null;
//        if (locServiceIntent != null) {
//            stopService(locServiceIntent);
//        }
        super.onDestroy();
    }

    protected void exit() {
        if (System.currentTimeMillis() - temp < 2000) {
            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            FQT.showShort(getApplicationContext(), getString(R.string.app_1));
        }
        temp = System.currentTimeMillis();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            curIndex = intent.getIntExtra("curIndex", -1);
        }
        processDefIndex();
    }

    /**
     * 登录成功后 打开主页 默认选中defIndex的tab
     */
    private void processDefIndex() {
        if (curIndex < 0) {
            curIndex = 0;
        }
        if (curIndex == 0) {
            changeContentFragment(homeTv);
        } else if (curIndex == 1) {
            changeContentFragment(shopTv);
        } else if (curIndex == 2) {
            changeContentFragment(chatTv);
        } else if (curIndex == 3) {
            changeContentFragment(userTv);
        }

    }

    private void resetView() {
        homeTv.setTextColor(defColor);
        shopTv.setTextColor(defColor);
        chatTv.setTextColor(defColor);
        userTv.setTextColor(defColor);

        homeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_logo_def, 0, 0);
        shopTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_search_def, 0, 0);
        chatTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_chat_def, 0, 0);
        userTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_user_def, 0, 0);
    }

    private void changeContentFragment(View view) {
        int id = view.getId();
//        if (R.id.act_main_home != id && !UserUtil.checkLogin()) {
//            return;
//        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) findViewById(R.id.main_layContent).getLayoutParams();
        if (R.id.act_main_home == id) {
            lp.setMargins(0, 0, 0, ViewUtil.getDimen(this, R.dimen.x30));
        } else {
            lp.setMargins(0, 0, 0, ViewUtil.getDimen(this, R.dimen.x110));
        }
        findViewById(R.id.main_layContent).setLayoutParams(lp);

        resetView();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mainChatFragment != null) {
            ft.hide(mainChatFragment);
//        } else {
//            mainChatFragment = (Fragment1) getSupportFragmentManager().findFragmentByTag(Fragment1.class.getName());
//            if (fragment1 != null) {
//                ft.hide(fragment1);
//            }
        }

        if (mainUserFragment != null) {
            ft.hide(mainUserFragment);
//        } else {
//            fragment5 = (Fragment5) getSupportFragmentManager().findFragmentByTag(Fragment5.class.getName());
//            if (fragment5 != null) {
//                ft.hide(fragment5);
//            }
        }

        if (mainSearchFragment != null) {
            ft.hide(mainSearchFragment);
//        } else {
//            fragment2 = (Fragment2) getSupportFragmentManager().findFragmentByTag(Fragment2.class.getName());
//            if (fragment2 != null) {
//                ft.hide(fragment2);麦n
//            }
        }


        if (mainMapFragment != null) {
            ft.hide(mainMapFragment);
//        } else {
//            fragment3 = (Fragment3) getSupportFragmentManager().findFragmentByTag(Fragment3.class.getName());
//            if (fragment3 != null) {
//                ft.hide(fragment3);
//            }
        }
        switch (id) {
            case R.id.act_main_home:
                if (mainMapFragment == null) {
                    mainMapFragment = BaseFragment.newInstance(this, MainMapFragment.class);
                    ft.add(R.id.main_layContent, mainMapFragment, mainMapFragment.getClass().getName());
                }
                ft.show(mainMapFragment);

                homeTv.setTextColor(selectedColor);
                homeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_logo_press, 0, 0);
                curIndex = 0;
                break;
            case R.id.act_main_shop:

                if (mainSearchFragment == null) {
                    mainSearchFragment = BaseFragment.newInstance(this, MainSearchFragment.class);
                    ft.add(R.id.main_layContent, mainSearchFragment, mainSearchFragment.getClass().getName());
                }
                ft.show(mainSearchFragment);
                shopTv.setTextColor(selectedColor);
                shopTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_search_press, 0, 0);
                curIndex = 1;
                break;
            case R.id.act_main_chat:

                if (mainChatFragment == null) {
                    mainChatFragment = BaseFragment.newInstance(this, MainChatFragment.class);
                    ft.add(R.id.main_layContent, mainChatFragment, mainChatFragment.getClass().getName());
                }
                ft.show(mainChatFragment);
                chatTv.setTextColor(selectedColor);
                chatTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_chat_press, 0, 0);
                curIndex = 2;
                break;


            case R.id.act_main_user:
                if (mainUserFragment == null) {
                    mainUserFragment = BaseFragment.newInstance(this, MainUserFragment.class);
                    ft.add(R.id.main_layContent, mainUserFragment, mainUserFragment.getClass().getName());
                }
                ft.show(mainUserFragment);
                userTv.setTextColor(selectedColor);
                userTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_user_press, 0, 0);
                curIndex = 3;
                break;
            default:
                break;
        }
        ft.commitAllowingStateLoss();
    }


    public void cleanTop(int index, boolean newTask) {
        goToMain(this, index, newTask);
    }

    public static void goToMain(Context context, int defIndex, boolean newTask) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("curIndex", defIndex);
        if (newTask)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        else
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Subscribe
    public void exitEvent(ExitEvent exitEvent) {
        if (exitEvent != null) {
            finish();
        }
    }
}
