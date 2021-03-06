//package com.batterbox.power.phone.app.act;
//
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.AssetFileDescriptor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.Point;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Vibrator;
//import android.provider.MediaStore;
//import android.support.annotation.Nullable;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.batterbox.power.phone.app.R;
//import com.batterbox.power.phone.app.aroute.ARouteHelper;
//import com.batterbox.power.phone.app.scaner.CaptureActivityHandler;
//import com.chenyi.baselib.ui.NavigationActivity;
//import com.chenyi.baselib.utils.StringUtil;
//import com.chenyi.baselib.utils.print.FQT;
//import com.zbar.lib.CameraManager;
//import com.zbar.lib.DecoderLocalFile;
//import com.zbar.lib.InactivityTimer;
//
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Created by ass on 2018/1/19.
// */
//
//@Route(path = ARouteHelper.APP_SCAN)
//public class ScanActivity extends NavigationActivity implements SurfaceHolder.Callback {
//    private final int CHOOSE_PICTURE = 1003;
//    private CaptureActivityHandler handler;
//    private boolean hasSurface;
//    private InactivityTimer inactivityTimer;
//    private MediaPlayer mediaPlayer;
//    private boolean playBeep;
//    private static final float BEEP_VOLUME = 0.50f;
//    private boolean vibrate;
//    private int x = 0;
//    private int y = 0;
//    private int cropWidth = 0;
//    private int cropHeight = 0;
//    private RelativeLayout mContainer = null;
//    private ImageView mCropLayout = null;
//
////    private LinearLayout ll_scan_help;//??????????????? & ????????? ??????
//
//    private ImageView mIvLight;//????????? ??????
//
//    private Context context;
//    boolean lightOn = false;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setNavigationVisible(false);
//        context = this;
//        initView();//?????????????????????
////        initScanerAnimation();//?????????????????????
//        CameraManager.init(getApplication());//????????? CameraManager
//
//        hasSurface = false;
//        inactivityTimer = new InactivityTimer(this);
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void onResume() {
//        super.onResume();
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder);//Camera?????????
//        } else {
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//        playBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
//        initBeepSound();
//        vibrate = true;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        CameraManager.get().closeDriver();
//    }
//
//    @Override
//    protected void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.act_scan;
//    }
//
//    private void initView() {
////        mIvLight = (ImageView) findViewById(R.id.top_mask);
//        ImageView lightIv = findViewById(R.id.act_scan_light_iv);
//        lightIv.setOnClickListener(v -> {
//            if (lightOn) {
//                lightIv.setImageResource(R.mipmap.ic_light_off);
//                CameraManager.get().offLight();
//            } else {
//                lightIv.setImageResource(R.mipmap.ic_light_on);
//                CameraManager.get().openLight();
//            }
//            lightOn = !lightOn;
//        });
//        mContainer = findViewById(R.id.capture_containter);
//        mCropLayout = findViewById(R.id.act_scan_center_iv);
//        findViewById(R.id.act_scan_back_btn).setOnClickListener(v -> finish());
////        mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
////        ll_scan_help = (LinearLayout) findViewById(R.id.ll_scan_help);
////        ll_scan_help.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                // RxActivityUtils.skipActivity(ActivityScanerCode.this, ActivityCreateQRCode.class);
////            }
////        });
////        //??????Camera?????? ??? ???????????? ??????
////        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
////                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
////        }
//    }
//
////    private void initScanerAnimation() {
////        ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
////        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
////        animation.setRepeatCount(-1);
////        animation.setRepeatMode(Animation.RESTART);
////        animation.setInterpolator(new LinearInterpolator());
////        animation.setDuration(1200);
////        mQrLineView.startAnimation(animation);
////    }
//
//    public int getX() {
//        return x;
//    }
//
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public void setY(int y) {
//        this.y = y;
//    }
//
//    public int getCropWidth() {
//        return cropWidth;
//    }
//
//    public void setCropWidth(int cropWidth) {
//        this.cropWidth = cropWidth;
//    }
//
//    public int getCropHeight() {
//        return cropHeight;
//    }
//
//    public void setCropHeight(int cropHeight) {
//        this.cropHeight = cropHeight;
//    }
//
//
//    boolean flag = true;
//
//    private void light() {
//        if (flag) {
//            flag = false;
//            // ????????????
//            CameraManager.get().openLight();
//        } else {
//            flag = true;
//            // ????????????
//            CameraManager.get().offLight();
//        }
//
//    }
//
////    public void btn(View view) {
////        int i = view.getId();
////        if (i == R.id.top_mask) {
////            light();
////        } else if (i == R.id.top_back) {
////            finish();
////        } else if (i == R.id.top_openpicture) {
////            getPicture();
////        } else {
////
////        }
////    }
//
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        try {
//            CameraManager.get().openDriver(surfaceHolder);
//
//            Point point = CameraManager.get().getCameraResolution();
//            int width = point.y;
//            int height = point.x;
//            int x = mCropLayout.getLeft() * width / mContainer.getWidth();
//            int y = mCropLayout.getTop() * height / mContainer.getHeight();
//            int cropWidth = mCropLayout.getWidth() * width
//                    / mContainer.getWidth();
//            int cropHeight = mCropLayout.getHeight() * height
//                    / mContainer.getHeight();
//            setX(x);
//            setY(y);
//            setCropWidth(cropWidth);
//            setCropHeight(cropHeight);
//        } catch (IOException ioe) {
//            return;
//        } catch (RuntimeException e) {
//            return;
//        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(ScanActivity.this);
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//
//    //----------------------------------------------------------------------------------------------????????????????????????????????? start
//
//    /***
//     * ??????????????????
//     */
//    private void getPicture() {
//        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        openAlbumIntent.setType("image/*");
//        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            ContentResolver resolver = getContentResolver();
//            // ???????????????????????????
//            Uri originalUri = data.getData();
//            try {
//                // ??????ContentProvider??????URI??????????????????
//                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//                if (photo != null) {
//                    Bitmap smallBitmap = zoomBitmap(photo, photo.getWidth() / 2, photo.getHeight() / 2);// ????????????????????????????????????????????????????????????????????????????????????????????????Bitmap???????????????
//                    photo.recycle(); // ??????????????????????????????????????????out of memory????????????
//                    String bitmappath = saveFile(smallBitmap, setImageName());
//                    DecoderLocalFile decoder = new DecoderLocalFile(bitmappath);
//                    String phone = decoder.handleQRCodeFormPhoto(ScanActivity.this, DecoderLocalFile.loadBitmap(bitmappath));
//                    if ("-1".equals(phone)) {
//                        Toast.makeText(context, "???????????????????????????.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "???????????????" + phone, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + File.separator + "fengci/";
//
//    /*
//     * ????????????
//     */
//    public static String saveFile(Bitmap bm, String fileName) throws IOException {
//        String path;
//        File dirFile = new File(ALBUM_PATH);
//        if (!dirFile.exists()) {
//            dirFile.mkdir();
//        }
//        File myCaptureFile = new File(ALBUM_PATH + fileName);
//        path = myCaptureFile.getAbsolutePath();
//        FileOutputStream fileOutputStream = new FileOutputStream(myCaptureFile);
//        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ???????????????????????????100????????????????????????????????????????????????baos???
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 100) { // ?????????????????????????????????????????????100kb,??????????????????
//            baos.reset();// ??????baos?????????baos
//            if (options > 10) {
//                options -= 10;// ???????????????10
//            } else {
//                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// ????????????options%?????????????????????????????????baos???
//                break;
//            }
//            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// ????????????options%?????????????????????????????????baos???
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ?????????????????????baos?????????ByteArrayInputStream???
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ???ByteArrayInputStream??????????????????
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        bos.flush();
//        bos.close();
//        return path;
//    }
//
//    /**
//     * Resize the bitmap
//     *
//     * @param bitmap
//     * @param width
//     * @param height
//     * @return
//     */
//    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        Matrix matrix = new Matrix();
//        float scaleWidth = ((float) width / w);
//        float scaleHeight = ((float) height / h);
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
//        return newbmp;
//    }
//
//    /**
//     * ??????????????????
//     *
//     * @return
//     */
//    public static String setImageName() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// ????????????????????????????????????????????????
//        return simpleDateFormat.format(new Date()) + ".jpg";
//    }
//    //==============================================================================================????????????????????????????????? end
//
//    //----------------------------------------------------------------------------------------------???????????? ??? ???????????? start
////    private String mResult;
//
//    public void handleDecode(String result) {
//        inactivityTimer.onActivity();
//        playBeepSoundAndVibrate();
//        if (StringUtil.isEmpty(result)) {
//            FQT.showShort(this, getString(R.string.scan_4));
//            finish();
//            return;
//        }
////        if (result.contains("box=")) {
//        Intent intent = new Intent();
//        intent.putExtra("scan_url", result);
//        setResult(RESULT_OK, intent);
////        } else {
////            FQT.showShort(this, getString(R.string.scan_4));
////        }
//        finish();
////        if (null != handler) {
////            handler.sendEmptyMessage(R.id.restart_preview);
////        }
//    }
//    //==============================================================================================???????????? ??? ???????????? end
//
//
//    //----------------------------------------------------------------------------------------------?????????????????????????????????????????? start
//    private void initBeepSound() {
//        if (playBeep && mediaPlayer == null) {
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    mediaPlayer.seekTo(0);
//                }
//            });
//
//            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
//            try {
//                mediaPlayer.setDataSource(file.getFileDescriptor(),
//                        file.getStartOffset(), file.getLength());
//                file.close();
//                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null;
//            }
//        }
//    }
//
//    private static final long VIBRATE_DURATION = 200L;
//
//    private void playBeepSoundAndVibrate() {
//        if (playBeep && mediaPlayer != null) {
//            mediaPlayer.start();
//        }
//        if (vibrate) {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(VIBRATE_DURATION);
//        }
//    }
//}
