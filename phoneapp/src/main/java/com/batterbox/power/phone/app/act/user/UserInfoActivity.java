package com.batterbox.power.phone.app.act.user;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.FileUtil;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.PhotoPickHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by ass on 2019-08-13.
 * Description
 */
@RuntimePermissions
@Route(path = ARouteHelper.USER_INFO)
public class UserInfoActivity extends NavigationActivity {
    TextView nameTv, phoneTv, emailTv;
    ImageView headIv;
    String tempCameraPath;

    @Override
    protected int getLayoutId() {
        return R.layout.act_user_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.user_6);
        nameTv = findViewById(R.id.act_user_info_name_tv);
        phoneTv = findViewById(R.id.act_user_info_phone_tv);
        emailTv = findViewById(R.id.act_user_info_email_tv);
        headIv = findViewById(R.id.act_user_info_iv);
        findViewById(R.id.act_user_info_photo_rl).setOnClickListener(v -> UserInfoActivityPermissionsDispatcher.pickPhotoWithPermissionCheck(UserInfoActivity.this));
        findViewById(R.id.act_user_info_name_rl).setOnClickListener(v -> {
            UserEntity userEntity = UserUtil.getUserInfo();
            if (userEntity != null) {
                ARouteHelper.user_info_edit(getString(R.string.user_2), StringUtil.fixNullStr(userEntity.userName), getString(R.string.user_7)).navigation(UserInfoActivity.this, 121);
            }
        });
        findViewById(R.id.act_user_info_email_rl).setOnClickListener(v -> {
            UserEntity userEntity = UserUtil.getUserInfo();
            if (userEntity != null) {
                ARouteHelper.user_info_edit(getString(R.string.user_4), StringUtil.fixNullStr(userEntity.email), getString(R.string.user_8)).navigation(UserInfoActivity.this, 122);
            }
        });

    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        UserEntity userEntity = UserUtil.getUserInfo();
        if (userEntity != null) {
            ImageLoaderUtil.load_round(this, userEntity.headImg, headIv, ViewUtil.getDimen(this, R.dimen.x10));
            nameTv.setText(StringUtil.fixNullStr(userEntity.userName));
            phoneTv.setText(StringUtil.fixNullStr(userEntity.phone));
            emailTv.setText(StringUtil.fixNullStr(userEntity.email));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 121 && data != null) {
                String content = data.getStringExtra("content");
                //名字
                updateInfo(content, null, null);
            } else if (requestCode == 122 && data != null) {
                String content = data.getStringExtra("content");
                //email
                updateInfo(null, null, content);
            } else if (requestCode == (PhotoPickHelper.CAMERA_REQUEST_CODE)) {
                String path = tempCameraPath;
                ucrop(path);
            } else if (requestCode == PhotoPickHelper.GALLERY_REQUEST_CODE && data != null && data.getData() != null) {
                String path = FileUtil.getRealFilePath(this, data.getData());
                ucrop(path);
            } else if (requestCode == UCrop.REQUEST_CROP && data != null) {
                Uri croppedFileUri = UCrop.getOutput(data);
                String headPath = FileUtil.getRealFilePath(this, croppedFileUri);
                uploadImg(headPath);
            }
        }
    }

    private void uploadImg(String path) {
        HttpClient.getInstance().uploadImg(path, new NormalHttpCallBack<ResponseEntity<String>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<String> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
//                    ImageLoaderUtil.load_round(UserInfoActivity.this, path, headIv, ViewUtil.getDimen(UserInfoActivity.this, R.dimen.x10));
                    updateInfo(null, responseEntity.getData(), null);
//                    if (!StringUtil.isEmpty(responseEntity.getData())) {
//                        UserEntity userEntity = UserUtil.getUserInfo();
//                        if (userEntity != null) {
//                            userEntity.headImg = responseEntity.getData();
//                            UserUtil.saveUserInfo(userEntity);
//                        }
//                    }
                }
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {

            }
        });
    }

    private void updateInfo(String nickName, String logoUrl, String email) {
        HttpClient.getInstance().us_setUserInfo(nickName, logoUrl, email, new NormalHttpCallBack<ResponseEntity>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                UserEntity userEntity = UserUtil.getUserInfo();
                if (userEntity != null) {
                    if (!StringUtil.isEmpty(nickName)) {
                        userEntity.userName = nickName;
                        nameTv.setText(StringUtil.fixNullStr(userEntity.userName));
                    }
                    if (!StringUtil.isEmpty(logoUrl)) {
                        userEntity.headImg = logoUrl;
                        ImageLoaderUtil.load_round(UserInfoActivity.this, userEntity.headImg, headIv, ViewUtil.getDimen(UserInfoActivity.this, R.dimen.x10));
                    }
                    if (!StringUtil.isEmpty(email)) {
                        userEntity.email = email;
                        emailTv.setText(StringUtil.fixNullStr(userEntity.email));
                    }
                    UserUtil.saveUserInfo(userEntity);
                }
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(UserInfoActivity.this, msg);
            }
        });
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void pickPhoto() {
        PhotoPickHelper.pick(this, tempCameraPath = PhotoPickHelper.getTempImgPath(this));
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
        PhotoPickHelper.showDefindDialog(this, getSupportFragmentManager());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void ucrop(String path) {
        UCrop.Options options = new UCrop.Options();
        //下面参数分别是缩放,旋转,裁剪框的比例
//        options.setToolbarTitle("移动和缩放");//设置标题栏文字
//        options.setCropGridStrokeWidth(2);//设置裁剪网格线的宽度(我这网格设置不显示，所以没效果)
//        options.setCropFrameStrokeWidth(10);//设置裁剪框的宽度
//        options.setMaxScaleMultiplier(3);//设置最大缩放比例
        options.setHideBottomControls(true);//隐藏下边控制栏
        options.setShowCropGrid(false);  //设置是否显示裁剪网格
        options.setShowCropFrame(true); //设置是否显示裁剪边框(true为方形边框)
        options.setToolbarWidgetColor(Color.BLACK);//标题字的颜色以及按钮颜色
        options.setCircleDimmedLayer(false);//设置是否为圆形裁剪框
//        options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
        options.setToolbarColor(Color.WHITE); // 设置标题栏颜色
        options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
//        options.setCropGridColor(Color.parseColor("#ffffff"));//设置裁剪网格的颜色
//        options.setCropFrameColor(Color.parseColor("#ffffff"));//设置裁剪框的颜色
        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(new File(PhotoPickHelper.getTempImgPath(this))))
                .withAspectRatio(1, 1)
                .withMaxResultSize(300, 300)
                .withOptions(options)
                .start(this);
    }
}
