package com.tencent.qcloud.tim.uikit.component.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chenyi.baselib.utils.LanguageContextWrapper;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.video.listener.ClickListener;
import com.tencent.qcloud.tim.uikit.component.video.listener.ErrorListener;
import com.tencent.qcloud.tim.uikit.component.video.listener.JCameraListener;
import com.tencent.qcloud.tim.uikit.component.video.util.DeviceUtil;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.inputmore.InputMoreFragment;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.IOException;

public class CameraActivity extends Activity {

    private static final String TAG = CameraActivity.class.getSimpleName();
    public static IUIKitCallBack mCallBack;
    private JCameraView jCameraView;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageContextWrapper.wrap(newBase, LanguageUtil.getLocaleByLanguage(StringUtil.fixNullStr(LanguageUtil.getLanguage(), LanguageUtil.ES))));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TUIKitLog.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        jCameraView = findViewById(R.id.jcameraview);
        //设置视频保存路径
        //jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        int state = getIntent().getIntExtra(TUIKitConstants.CAMERA_TYPE, JCameraView.BUTTON_STATE_BOTH);
        jCameraView.setFeatures(state);
        if (state == JCameraView.BUTTON_STATE_ONLY_CAPTURE) {
            jCameraView.setTip(getString(R.string.take_photo));
        } else if (state == JCameraView.BUTTON_STATE_ONLY_RECORDER) {
            jCameraView.setTip(getString(R.string.take_video));
        }

        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                TUIKitLog.i(TAG, "camera error");
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
//                ToastUtil.toastShortMessage("给点录音权限可以?");
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtil.saveBitmap("JCamera", bitmap);
               /* Intent intent = new Intent();
                intent.putExtra(ILiveConstants.CAMERA_IMAGE_PATH, path);
                setResult(-1, intent);*/
                if (mCallBack != null) {
                    mCallBack.onSuccess(path);
                }
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame, long duration) {
                //获取视频路径
                String path = FileUtil.saveBitmap("JCamera", firstFrame);
                Intent intent = new Intent();
                intent.putExtra(TUIKitConstants.IMAGE_WIDTH, firstFrame.getWidth());
                intent.putExtra(TUIKitConstants.IMAGE_HEIGHT, firstFrame.getHeight());
                intent.putExtra(TUIKitConstants.VIDEO_TIME, duration);
                intent.putExtra(TUIKitConstants.CAMERA_IMAGE_PATH, path);
                intent.putExtra(TUIKitConstants.CAMERA_VIDEO_PATH, url);
//                firstFrame.getWidth();
                //setResult(-1, intent);
                if (mCallBack != null) {
                    mCallBack.onSuccess(intent);
                }
                finish();
            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                int state = getIntent().getIntExtra(TUIKitConstants.CAMERA_TYPE, JCameraView.BUTTON_STATE_BOTH);
                if (state == JCameraView.BUTTON_STATE_ONLY_CAPTURE) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1016);
                } else if (state == JCameraView.BUTTON_STATE_ONLY_RECORDER) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1017);
                }

            }
        });
        //jCameraView.setVisibility(View.GONE);
        TUIKitLog.i(TAG, DeviceUtil.getDeviceModel());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 1016) {
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = FileUtil.getPathFromUri(uri);
            if (mCallBack != null) {
                mCallBack.onSuccess(path);
            }
            finish();
        } else if (requestCode == 1017) {
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = FileUtil.getPathFromUri(uri);
            try {
                // 获取预览图
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//                AssetFileDescriptor afd = getAssets().openFd(path);
//            mmr.setDataSource(afd.getFileDescriptor()); // failed
//                mmr.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mmr.setDataSource(path);
                Bitmap previewBitmap = mmr.getFrameAtTime();

                // 缩放
                int PREVIEW_VIDEO_IMAGE_HEIGHT = 300; // Pixels
                int videoWidth = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int videoHeight = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                int videoViewWidth = PREVIEW_VIDEO_IMAGE_HEIGHT * videoWidth / videoHeight;
                int videoViewHeight = PREVIEW_VIDEO_IMAGE_HEIGHT;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(previewBitmap, videoViewWidth, videoViewHeight, true);

                // 获取时长
                String strDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int duration = Integer.parseInt(strDuration) / 1000;

                Intent intent = new Intent();
                intent.putExtra(TUIKitConstants.IMAGE_WIDTH, scaledBitmap.getWidth());
                intent.putExtra(TUIKitConstants.IMAGE_HEIGHT, scaledBitmap.getHeight());
                intent.putExtra(TUIKitConstants.VIDEO_TIME, duration);
                String scaledPath = FileUtil.saveBitmap("JCamera", scaledBitmap);
                intent.putExtra(TUIKitConstants.CAMERA_IMAGE_PATH, scaledPath);
                intent.putExtra(TUIKitConstants.CAMERA_VIDEO_PATH, path);
                mmr.release();
                //setResult(-1, intent);
                if (mCallBack != null) {
                    mCallBack.onSuccess(intent);
                }
                finish();
//            } catch (IOException e) {
//                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        TUIKitLog.i(TAG, "onResume");
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        TUIKitLog.i(TAG, "onPause");
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        TUIKitLog.i(TAG, "onDestroy");
        super.onDestroy();
        mCallBack = null;
    }

}
