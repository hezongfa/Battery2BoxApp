package com.chenyi.baselib.widget;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;

import com.chenyi.baselib.utils.FileUtil;
import com.chenyi.baselib.utils.sys.PermissionUtil;
import com.chenyi.baselib.widget.dialog.BottomListDialog;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.chenyi.tao.shop.baselib.BuildConfig;
import com.chenyi.tao.shop.baselib.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.chenyi.baselib.utils.StringUtil.text;

/**
 * Created by ass on 2018/4/9.
 */

public class PhotoPickHelper {
    public static final int CAMERA_REQUEST_CODE = 991;
    public static final int GALLERY_REQUEST_CODE = 992;

    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getTempImgPath(Context context) {
        String cameraPath = File.separator + "temp_" + getStringToday() + ".jpg";

        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            String imgsFilePath = Environment.getExternalStorageDirectory() + File.separator + BuildConfig.APPLICATION_ID + "_temp_photo" + File.separator;
            FileUtil.mkFile(new File(imgsFilePath));
            cameraPath = imgsFilePath + cameraPath;
        } else {
            String imgsFilePath = context.getFilesDir() + File.separator + BuildConfig.APPLICATION_ID + "_temp_photo" + File.separator;
            FileUtil.mkFile(new File(imgsFilePath));
            cameraPath = imgsFilePath + cameraPath;
        }
        return cameraPath;
    }

    public static void pick(Activity activity, String cameraPath) {
        ArrayList<String> list = new ArrayList<>();
        list.add(text(activity, R.string.app_28));
        list.add(text(activity, R.string.app_29));
        DialogUtils.showListDialog(activity, text(activity, R.string.app_6), list, new BottomListDialog.BottomListItemClick() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0: {
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        /*获取当前系统的android版本号*/
                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion < 24) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cameraPath)));
                            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        } else {
                            ContentValues contentValues = new ContentValues(1);
                            contentValues.put(MediaStore.Images.Media.DATA, cameraPath);
                            Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        }


                    }
                    break;
                    case 1: {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        activity.startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
                    }
                    break;
                }
            }
        });
    }

    public static void showDefindDialog(Context context, FragmentManager fragmentManager) {
        DialogUtils.showDialog(fragmentManager, text(context, R.string.permission_1), text(context, R.string.permission_2), text(context, R.string.app_16), null, text(context, R.string.app_3), v -> PermissionUtil.gotoAppSetting(context));
    }

}
