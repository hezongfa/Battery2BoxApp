package com.batterbox.power.phone.app.hybrid;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by ass on 2018/2/26.
 */
@RuntimePermissions
@Route(path = ARouteHelper.HYBRID_NAV)
public class HybridNavigationActivity extends HybridBaseNavigationActivity implements IJavaScriptInterface {
    BaseActivity activity;
//    IJavaScriptInterface ewgJavaScriptInterface;
//    JsPayReceiver payReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.act_hybrid;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
//        webView.addJavascriptInterface(ewgJavaScriptInterface = new IJavaScriptInterface(this), IJavaScriptInterface.JsObjectName);
        webView.addJavascriptInterface(this, IJavaScriptInterface.JsObjectName);
    }

    @Override
    public void onDestroy() {
//        PayHelper.unregister(this, payReceiver);
        super.onDestroy();
    }

    @JavascriptInterface
    public String getLanguage() {
        String language = LanguageUtil.getLanguage();
        if (StringUtil.isEmpty(language)) {
            language = LanguageUtil.CN;
        }
        return language;
    }

    @JavascriptInterface
    public void save_img64(String imgBase64) {
//        if (activity == null) return;
//        if (imgBase64 == null) {
//            FQT.showShort(activity, activity.getString(R.string.user_51));
//            return;
//        }
//        String base64 = imgBase64.replace("data:image/png;base64,", "")
//                .replace("data:image/jpg;base64,", "");
//        Bitmap bitmap = Base64Util.base64ToBitmap(base64);
//        if (bitmap == null) {
//            FQT.showShort(activity, activity.getString(R.string.user_51));
//            return;
//        }
//        File file = FileUtil.savePictureToAlbum(activity, bitmap);
//        if (file != null) {
//            FQT.showShort(activity, activity.getString(R.string.user_50));
//        } else {
//            FQT.showShort(activity, activity.getString(R.string.user_51));
//        }
    }


    @JavascriptInterface
    public void shopkeeper_log(String log) {
        FQL.d("chromium————shopkeeper_log", StringUtil.fixNullStr(log));
    }


    @JavascriptInterface
    public String shopkeeper_getToken() {
        return StringUtil.fixNullStr(UserUtil.getToken());
    }


    @JavascriptInterface
    public void shopkeeper_closeWindow() {
        if (activity != null && !activity.isFinishing())
            activity.finish();
    }


    @JavascriptInterface
    public void shopkeeper_share_file(String json) {
        HybridNavigationActivityPermissionsDispatcher.shareWithPermissionCheck(this, "share_file", json);
    }

    @JavascriptInterface
    public void shopkeeper_share_link(String json) {
        HybridNavigationActivityPermissionsDispatcher.shareWithPermissionCheck(this, "share_link", json);
    }

    @JavascriptInterface
    public void shopkeeper_down_file(String json) {
        HybridNavigationActivityPermissionsDispatcher.shareWithPermissionCheck(this, "down_file", json);

        //{"files":["httpxxx","httpxxx","httpxxx","httpxxx","httpxxx","httssss"]
        //}
    }

    @JavascriptInterface
    public void shopkeeper_share_imgs(String json) {
        HybridNavigationActivityPermissionsDispatcher.shareWithPermissionCheck(this, "share_images", json);
        //{"img_urls":["http://touch.openmeitao.com/76955a5c424e52bff760d76b4bae6686.jpg","httpxxx","httpxxx","httpxxx","httpxxx","httssss"],"text":"ssssss文案"}

    }

    @JavascriptInterface
    public void shopkeeper_user_agreement_agree() {
        finish();
    }

    @JavascriptInterface
    public void shopkeeper_user_agreement_refuse() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HybridNavigationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void share(String type, String json) {
//        if (StringUtil.isEmpty(json) || StringUtil.isEquals("undefind", json)) {
//            return;
//        }
//        if (activity != null && !activity.isFinishing()) {
//            activity.runOnUiThread(() -> {
//                if ("down_file".equals(type)) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(json);
//                        JSONArray fileArray = jsonObject.getJSONArray("files");
//                        if (fileArray != null && fileArray.length() > 0) {
//                            ArrayList<String> file_urls = new ArrayList<>();
////                            String file_url = fileArray.optString(0);
////                            if (StringUtil.isEmpty(file_url) || !file_url.startsWith("http"))
////                                return;
//                            for (int i = 0; i < fileArray.length(); i++) {
//                                String file_url = fileArray.optString(i);
//                                file_urls.add(file_url);
//                            }
//                            if (StringUtil.isEmpty(file_urls)) {
//                                FQT.showShort(HybridNavigationActivity.this, "数据异常");
//                                return;
//                            }
//
//                            DownLoadImagesHelper downLoadImagesHelper = new DownLoadImagesHelper();
//                            downLoadImagesHelper.down(activity, file_urls, img_paths -> {
//                                if (!StringUtil.isEmpty(img_paths) && !StringUtil.isEmpty(img_paths.get(0))) {
//                                    FQT.showShort(HybridNavigationActivity.this, "已保存到" + img_paths.get(0));
//                                }
//                            });
//
//                        }
//                    } catch (JSONException e) {
//                        FQL.e(StringUtil.fixNullStr(e.getMessage()));
//                    }
//                } else if ("share_file".equals(type)) {
//                    String file_url = JsonExplain.getStringValue(json, "file_url");
//                    String title = JsonExplain.getStringValue(json, "title");
//                    if (StringUtil.isEmpty(file_url) || !file_url.startsWith("http")) return;
//                    LoadingDialog loadingDialog = DialogUtils.showLoading(activity.getSupportFragmentManager());
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BuildConfig.APPLICATION_ID +
//                            "/sharefile/";
//                    FileUtil.makeDir(path);
//                    String fileName = FileUtil.getFileNameFromUrl(file_url);
//                    DownloadUtil.getInstance().downloadFile(file_url, path + fileName, new DownloadListener() {
//                        @Override
//                        public void onFinish(File file) {
//                            if (loadingDialog != null)
//                                loadingDialog.dismissAllowingStateLoss();
//                            activity.runOnUiThread(() -> {
//                                if (file != null)
//                                    ShareDialogFragment.shareData(activity.getSupportFragmentManager(), ShareDataEntity.createFileShare(title, file.getAbsolutePath(), ShareLogicContants.Logic_Normal));
//                            });
//                        }
//
//                        @Override
//                        public void onProgress(int progress) {
//
//                        }
//
//                        @Override
//                        public void onFailed(String errMsg) {
//                            if (loadingDialog != null)
//                                loadingDialog.dismissAllowingStateLoss();
//                        }
//                    });
//                } else if ("share_link".equals(type)) {
//                    String link = JsonExplain.getStringValue(json, "link");
//                    String title = JsonExplain.getStringValue(json, "title");
//                    String des = JsonExplain.getStringValue(json, "des");
//                    if (StringUtil.isEmpty(link) || !link.startsWith("http")) return;
//                    ShareDialogFragment.shareData(activity.getSupportFragmentManager(), ShareDataEntity.createLinkShare(link, title, des, ShareLogicContants.Logic_Normal));
//                } else if ("share_images".equals(type)) {
//                    ShareHelper.jsShareCircle(json, activity);
//                }
//            });
//        }
    }
}
