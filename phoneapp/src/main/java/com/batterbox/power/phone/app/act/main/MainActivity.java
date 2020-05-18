package com.batterbox.power.phone.app.act.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.MainMenuHelper;
import com.batterbox.power.phone.app.act.main.main_chat.MainChatFragment;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.RefreshLanguageHelper;
import com.batterbox.power.phone.app.utils.ScanUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.ui.BaseFragment;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQT;

import q.rorbin.badgeview.QBadgeView;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends BaseActivity {
    MainChatFragment mainChatFragment;
    MainShopFragment mainShopFragment;
    MainSearchFragment mainSearchFragment;
    MainMapFragment userFragment;
    TextView homeTv, cartTv, teamTv, userTv;
    int defColor, selectedColor;
    int curIndex = -1;

    QBadgeView cartCountBadge;

    MainMenuHelper mainMenuHelper;

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
//        registerEventBus(this);
        if (getIntent() != null) {
            curIndex = getIntent().getIntExtra("curIndex", 0);
        }
        defColor = ContextCompat.getColor(this, R.color.gray_666666);
        selectedColor = ContextCompat.getColor(this, R.color.orange_ff6600);
        homeTv = findViewById(R.id.act_main_home);
        cartTv = findViewById(R.id.act_main_cart);
        teamTv = findViewById(R.id.act_main_team);
        userTv = findViewById(R.id.act_main_user);
        homeTv.setOnClickListener(this::changeContentFragment);
        cartTv.setOnClickListener(this::changeContentFragment);
        teamTv.setOnClickListener(this::changeContentFragment);
        userTv.setOnClickListener(this::changeContentFragment);
        cartCountBadge = new QBadgeView(this);
        cartCountBadge.bindTarget(findViewById(R.id.act_main_cart_fl))
                .setShowShadow(false)
                .setBadgeTextSize(8, true)
                .setGravityOffset(16, 0, true)
                .setBadgeTextColor(Color.WHITE);

//        changeContentFragment(homeTv);
        processDefIndex();
        findViewById(R.id.act_m_scan_iv).setOnClickListener(v -> ScanUtil.scan(MainActivity.this));

        mainMenuHelper = new MainMenuHelper(this);
    }

    public void showMenu() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(Gravity.START);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("android:support:fragments", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UserUtil.isLogin()) {
            UserUtil.gotoLogin();
            return;
        }
        HttpClient.getInstance().user_info(new NormalHttpCallBack<ResponseEntity<UserEntity>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<UserEntity> responseEntity) {
                if (responseEntity != null) {
                    UserUtil.saveUserInfo(responseEntity.getData());
                    mainMenuHelper.setData();
                }
            }

            @Override
            public void onFail(ResponseEntity<UserEntity> responseEntity, String msg) {

            }
        });
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
//            if (mainShopFragment != null)
//                ft.remove(mainShopFragment);
//            if (mainSearchFragment != null)
//                ft.remove(mainSearchFragment);
//            if (userFragment != null)
//                ft.remove(userFragment);
//            ft.commit();
//        }
        mainChatFragment = null;
        mainShopFragment = null;
        mainSearchFragment = null;
        userFragment = null;
        super.onDestroy();
    }

    protected void exit() {
        if (System.currentTimeMillis() - temp < 2000) {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
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
            changeContentFragment(userTv);
        } else if (curIndex == 1) {
            changeContentFragment(cartTv);
        } else if (curIndex == 2) {
            changeContentFragment(teamTv);
        } else if (curIndex == 3) {
            changeContentFragment(homeTv);
        }

    }

    private void resetView() {
        homeTv.setTextColor(defColor);
        cartTv.setTextColor(defColor);
        teamTv.setTextColor(defColor);
        userTv.setTextColor(defColor);

        homeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_chat_def, 0, 0);
        cartTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_business_def, 0, 0);
        teamTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_search_def, 0, 0);
        userTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_logo_def, 0, 0);
    }

    private void changeContentFragment(View view) {
        int id = view.getId();
//        if (R.id.act_main_home != id && !UserUtil.checkLogin()) {
//            return;
//        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) findViewById(R.id.main_layContent).getLayoutParams();
        if (R.id.act_main_user == id) {
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

        if (mainShopFragment != null) {
            ft.hide(mainShopFragment);
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


        if (userFragment != null) {
            ft.hide(userFragment);
//        } else {
//            fragment3 = (Fragment3) getSupportFragmentManager().findFragmentByTag(Fragment3.class.getName());
//            if (fragment3 != null) {
//                ft.hide(fragment3);
//            }
        }
        switch (id) {
            case R.id.act_main_home:
                if (mainChatFragment == null) {
                    mainChatFragment = BaseFragment.newInstance(this, MainChatFragment.class);
                    ft.add(R.id.main_layContent, mainChatFragment, mainChatFragment.getClass().getName());
                }
                ft.show(mainChatFragment);
                homeTv.setTextColor(selectedColor);
                homeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_chat_press, 0, 0);
                curIndex = 3;
                break;

            case R.id.act_main_cart:
                if (mainShopFragment == null) {
                    mainShopFragment = BaseFragment.newInstance(this, MainShopFragment.class);
                    ft.add(R.id.main_layContent, mainShopFragment, mainShopFragment.getClass().getName());
                }
                ft.show(mainShopFragment);
                cartTv.setTextColor(selectedColor);
                cartTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_b_pre, 0, 0);
                curIndex = 1;
                break;

            case R.id.act_main_team:
                if (mainSearchFragment == null) {
                    mainSearchFragment = BaseFragment.newInstance(this, MainSearchFragment.class);
                    ft.add(R.id.main_layContent, mainSearchFragment, mainSearchFragment.getClass().getName());
                }
                ft.show(mainSearchFragment);
                teamTv.setTextColor(selectedColor);
                teamTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_search_press, 0, 0);
                curIndex = 2;
                break;
            case R.id.act_main_user:
                if (userFragment == null) {
                    userFragment = BaseFragment.newInstance(this, MainMapFragment.class);
                    ft.add(R.id.main_layContent, userFragment, userFragment.getClass().getName());
                }
                ft.show(userFragment);
                userTv.setTextColor(selectedColor);
                userTv.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_main_tab_logo_press, 0, 0);

                curIndex = 0;
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

}
