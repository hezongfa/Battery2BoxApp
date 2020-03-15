package com.batterbox.power.phone.app.http;

import com.batterbox.power.phone.app.BuildConfig;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.SharedPreferencesUtil;
import com.chenyi.baselib.utils.StringUtil;

/**
 * Created by ass on 2019/3/13.
 * Description
 */
public class DomainHelper {
    public static String getBaseUrl() {
        int domainType = getDomainType();
        switch (domainType) {
            case 2:
                baseUrl = BuildConfig.DOMAIN_URL_DEV;
//                baseH5Url = BuildConfig.DOMAIN_H5_DEV;
                break;
//            case 1:
//                baseUrl = BuildConfig.DOMAIN_URL_TEST;
//                baseH5Url = BuildConfig.DOMAIN_H5_TEST;
//                break;
            case 0:
                baseUrl = BuildConfig.DOMAIN_URL;
//                baseH5Url = BuildConfig.DOMAIN_H5;
                break;
        }
        return baseUrl;
    }

    public static String baseUrl = BuildConfig.DOMAIN_URL;
//    public static String baseH5Url = BuildConfig.DOMAIN_H5;

    public static void saveDomainType(int domainType) {
        SharedPreferencesUtil.getInstance().saveInt("DomainType", domainType);
    }

    public static int getDomainType() {
        return SharedPreferencesUtil.getInstance().getInt("DomainType", 2);
    }

    public static void initDomain() {
        int domainType = getDomainType();
        if (0 == domainType) {
            baseUrl = BuildConfig.DOMAIN_URL;
//            baseH5Url = BuildConfig.DOMAIN_H5;
        }
//        else if (1 == domainType) {
//            baseUrl = BuildConfig.DOMAIN_URL_TEST;
////            baseH5Url = BuildConfig.DOMAIN_H5_TEST;
//        }
        else if (2 == domainType) {
            baseUrl = BuildConfig.DOMAIN_URL_DEV;
//            baseH5Url = BuildConfig.DOMAIN_H5_DEV;
        }

    }

//    public static boolean letsDev(String The_key_to_romantic) {
//        if (StringUtil.isEquals(The_key_to_romantic, BuildConfig.DOMAIN_ENTER_KEY)) {
//            ARouteHelper.app_develop().navigation();
//            return true;
//        }
//        return false;
//    }

    private static String addLanguage(String url) {
        return url + "?Accept-Language=" + StringUtil.fixNullStr(LanguageUtil.getLanguage(), LanguageUtil.ES);
    }

    //用户协议
    public static String getSpecificationH5() {
        return addLanguage(getH5Url() + "specification.html");
    }

    //隐私政策
    public static String getAgreementH5() {
        return addLanguage(getH5Url() + "agreement.html");
    }

    //关于我们
    public static String getAboutUsH5() {
//        return "http://dev.battery2box.com/aboutUs.html";
        return addLanguage(getH5Url() + "aboutUs.html");
    }

    //充电宝使用说明
    public static String getUserAgreementH5() {
        return addLanguage(getH5Url() + "userAgreement.html");
    }

    private static String getH5Url() {
        return baseUrl.replaceAll(":8888", "");
    }

    //问题
    public static String getUserCommonProblemH5() {
        return addLanguage(getH5Url() + "CommonProblem.html");
    }
}