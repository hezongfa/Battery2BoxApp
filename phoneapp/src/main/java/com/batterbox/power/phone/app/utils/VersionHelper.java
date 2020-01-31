package com.batterbox.power.phone.app.utils;

/**
 * Created by ass on 2019-08-23.
 * Description
 */
public class VersionHelper {

//    public static void checkVersion(BaseActivity baseActivity) {
//        HttpClient.getInstance().us_getNewVersion(new NormalHttpCallBack<ResponseEntity<VersionEntity>>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(ResponseEntity<VersionEntity> responseEntity) {
//                if (responseEntity != null && responseEntity.getData() != null) {
//                    if (!StringUtil.isEmpty(responseEntity.getData().downUrl) &&
//                            !StringUtil.isEmpty(responseEntity.getData().version) &&
//                            !StringUtil.isEquals(BuildConfig.VERSION_NAME, responseEntity.getData().version)) {
//                        String ver = responseEntity.getData().version.replace(".", "");
//                        String cur = BuildConfig.VERSION_NAME.replace(".", "");
//                        int veri = StringUtil.stringToInteger(ver);
//                        int curi = StringUtil.stringToInteger(cur);
//                        if (veri > curi) {
//                            DialogUtils.showDialog(baseActivity.getSupportFragmentManager(), null, baseActivity.getString(R.string.versino_1), baseActivity.getString(R.string.versino_3), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            }, baseActivity.getString(R.string.versino_2), v -> SysUtil.openUrl(baseActivity, responseEntity.getData().downUrl));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(ResponseEntity<VersionEntity> responseEntity, String msg) {
//
//            }
//        });
//    }
}
