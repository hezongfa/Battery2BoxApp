package com.chenyi.baselib.utils;

import android.content.Context;

import com.chenyi.baselib.utils.print.FQL;

import java.util.Locale;

import static com.chenyi.baselib.utils.StringUtil.text;


/**
 * Created by ass on 2018/3/24.
 */

public class LanguageUtil {
    public static final String ZH = "cn";
    public static final String EN = "en";
    public static final String ES = "es";// 西班牙
    public static final String DE = "de";// 德语
    public static final String FR = "fr";// 法语
    public static final String IT = "it";// 意大利
    public static final String PT = "pt";// 葡萄牙

//    public static String getText(MultilingualEntity multilingualEntity) {
//        return multilingualEntity == null ? "" : multilingualEntity.getValue();
//    }

    public static String getCurLanguageName(Context context) {
        String name = null;
        String language = getLanguage();
        if (LanguageUtil.ZH.equals(language)) {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_1);
        } else if (LanguageUtil.ES.equals(language)) {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_3);
        } else if (LanguageUtil.EN.equals(language)) {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_2);
        } else if (LanguageUtil.DE.equals(language)) {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_4);
        } else if (LanguageUtil.FR.equals(language)) {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_5);
        } else if (LanguageUtil.IT.equals(language)) {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_6);
        } else if (LanguageUtil.PT.equals(language)) {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_7);
        } else {
            name = text(context, com.chenyi.tao.shop.baselib.R.string.l_7);
        }

        return name;
    }

    public static String getLanguage() {
        String language = SharedPreferencesUtil.getInstance().getString("cur_language");
        if (language != null && language.equals("zh")) {
            language = ZH;
            saveLanguage(ZH);
        }
//        if (StringUtil.isEmpty(language)) {
//            language = cn;
////            saveLanguage();
//        }
        return language;
    }

    public static void saveLanguage(String language) {
//        SharedPreferencesUtil.getInstance(context).remove("cur_language");
        SharedPreferencesUtil.getInstance().saveString("cur_language", language);
    }

    public static void initLanguage(Context context) {
        String curLanguage = getLanguage();
        if (StringUtil.isEmpty(curLanguage)) {
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            FQL.d("language--" + language);
            saveLanguage(StringUtil.fixNullStr(language, LanguageUtil.EN));
        }
    }

    public static Locale getLocaleByLanguage(String language) {
        Locale locale = null;
        if (LanguageUtil.ZH.equals(language)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (LanguageUtil.ES.equals(language)) {
            locale = new Locale(ES);
        } else if (LanguageUtil.EN.equals(language)) {
            locale = Locale.ENGLISH;
        } else if (LanguageUtil.DE.equals(language)) {
            locale = Locale.GERMAN;
        } else if (LanguageUtil.FR.equals(language)) {
            locale = Locale.FRENCH;
        } else if (LanguageUtil.IT.equals(language)) {
            locale = Locale.ITALIAN;
        } else if (LanguageUtil.PT.equals(language)) {
            locale = new Locale(PT);
        } else {
            locale = new Locale(ES);
        }
        return locale;
    }

//    @SuppressWarnings("deprecation")
//    public static void changeAppLanguage(Context context, String newLanguage) {
//        Resources resources = context.getResources();
//        Configuration configuration = resources.getConfiguration();
//
//        // app locale
//        Locale locale = getLocaleByLanguage(newLanguage);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLocale(locale);
//        } else {
//            configuration.locale = locale;
//        }
//
//        // updateConfiguration
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        resources.updateConfiguration(configuration, dm);
//    }


//    public static Context attachBaseContext(Context context, String language) {
//        FQL.d("------attachBaseContext-------"+language);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return updateResources(context, language);
//        } else {
//            changeAppLanguage(context,language);
////            Resources resources = context.getResources();
////            Configuration configuration = resources.getConfiguration();
////            configuration.setLocale(getLocaleByLanguage(language));
////            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//            return context;
//        }
//    }
//
//
//    @TargetApi(Build.VERSION_CODES.N)
//    private static Context updateResources(Context context, String language) {
//        Resources resources = context.getResources();
//        Locale locale = getLocaleByLanguage(language);
//        Configuration configuration = resources.getConfiguration();
//        configuration.setLocale(locale);
//        configuration.setLocales(new LocaleList(locale));
//        return context.createConfigurationContext(configuration);
//    }
}
