package com.chenyi.baselib.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;

import com.chenyi.baselib.utils.print.FQL;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class StringUtil {

    public static final String TAG = StringUtil.class.getSimpleName();
    public static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String getAssets(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String getRadomImgName() {
        ArrayList<Integer> indexs = new ArrayList<>();
        while (indexs.size() < 13) {
            int i = (int) (Math.random() * 32);
            if (!indexs.contains(i)) {
                indexs.add(i);
            }
        }
        Character[] sss = new Character[32];
        String time = System.currentTimeMillis() + "";
        String radomStr = getRadomString(19);
        for (int i = 0; i < time.length(); i++) {
            sss[indexs.get(i)] = time.charAt(i);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, j = 0; i < sss.length; i++) {
            if (null == sss[i]) {
                sss[i] = radomStr.charAt(j);
                j++;
            }
            sb.append(sss[i]);
        }
        sb.append(".jpg");
        return sb.toString();
    }

    public static String getRadomString(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    public static String fixNullStr(String str, String def) {
        return isEmpty(str) ? def : str;
    }

    public static String fixNullStr(String str) {
        return fixNullStr(str, "");
    }

    public static String addStr(String... str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : str) {
            if (s != null)
                stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public static int stringToInteger(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }

    }

    public static long stringToLong(String str) {
        if (isEmpty(str)) {
            return 0L;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return -1L;
        }

    }

    public static float stringToFloat(String str) {
        if (isEmpty(str)) {
            return 0f;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return -1f;
        }

    }

    public static double stringToDouble(String str) {
        if (isEmpty(str)) {
            return 0d;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1d;
        }
    }

    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    public static List<String> splitStringArray(String str, String spliteStr) {
        if (!isEmpty(str) && !isEmpty(spliteStr)) {
            String[] sourceStrArray = str.split(spliteStr);
            List<String> strings = Arrays.asList(sourceStrArray);
            return strings;
        } else {
            return null;
        }

    }

    /**
     * 百分比
     *
     * @param x
     * @param total
     * @return
     */
    public static int getPercent(long x, long total) {
        double x_double = x * 1.0;
        double tempresult = x_double / total;
        return (int) (tempresult * 100);
    }

    /**
     * 去掉空白字符
     *
     * @return
     */
    public static String trimString(String before) {
        String after = (null == before) ? "" : before.trim();
        return after;
    }

    /**
     * 获得字体的缩放密度
     *
     * @param context
     * @return
     */
    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * *************************************************************
     */

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0 || charSequence.equals("null");
    }

    public static boolean isBlank(CharSequence charSequence) {
        return null == charSequence || charSequence.toString().trim().length() <= 0;
    }

    public static boolean isLeast(Object[] objs, int count) {
        return null != objs && objs.length >= count;
    }

    public static boolean isLeast(int[] objs, int count) {
        return null != objs && objs.length >= count;
    }

    public static boolean isEquals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    public static String trim(CharSequence charSequence) {
        return null == charSequence ? null : charSequence.toString().trim();
    }

    /**
     * 摘取里面第一个不为null的字符串
     *
     * @param options
     * @return
     */
    public static String pickFirstNotNull(CharSequence... options) {
        if (isEmpty(options)) {
            return null;
        }
        String result = null;
        for (CharSequence cs : options) {
            if (null != cs) {
                result = cs.toString();
                break;
            }
        }
        return result;
    }

    /**
     * 摘取里面第一个不为null的字符串
     *
     * @param options
     * @return
     */
    @SafeVarargs
    public static <T> T pickFirstNotNull(Class<T> clazz, T... options) {
        if (isEmpty(options)) {
            return null;
        }
        T result = null;
        for (T obj : options) {
            if (null != obj) {
                result = obj;
                break;
            }
        }
        return result;
    }

    /**
     * 替换文本为图片
     *
     * @param charSequence
     * @param regPattern
     * @param drawable
     * @return
     */
    public static SpannableString replaceImageSpan(CharSequence charSequence, String regPattern, Drawable drawable) {
        SpannableString ss =
                charSequence instanceof SpannableString ? (SpannableString) charSequence : new SpannableString(charSequence);
        try {
            ImageSpan is = new ImageSpan(drawable);
            Pattern pattern = Pattern.compile(regPattern);
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                String key = matcher.group();
                ss.setSpan(is, matcher.start(), matcher.start() + key.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ex) {
            FQL.e(TAG, ex.toString());
        }

        return ss;
    }

    /**
     * 压缩字符串到Zip
     *
     * @param str
     * @return 压缩后字符串
     * @throws IOException
     */
    public static String compress(String str)
            throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    /**
     * 解压Zip字符串
     *
     * @param str
     * @return 解压后字符串
     * @throws IOException
     */
    public static String uncompress(String str)
            throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("UTF-8"));
        return uncompress(in);
    }

    /**
     * 解压Zip字符串
     *
     * @param inputStream
     * @return 解压后字符串
     * @throws IOException
     */
    public static String uncompress(InputStream inputStream)
            throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream gunzip = new GZIPInputStream(inputStream);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }

    /**
     * InputStream convert to string
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream in)
            throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * 解压Gzip获取
     *
     * @param is
     * @return
     */
    public static String inputStream2StringFromGZIP(InputStream is) {
        StringBuilder resultSb = new StringBuilder();
        BufferedInputStream bis = null;
        InputStreamReader reader = null;
        try {
            bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            // Gzip流的前两个字节是0x1f8b
            if (result != -1 && headerData == 0x1f8b) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            while ((readSize = reader.read(data)) > 0) {
                resultSb.append(data, 0, readSize);
            }
        } catch (Exception e) {
            FQL.e(TAG, e.toString());
        } finally {
            IOUtil.closeIO(is, bis, reader);
        }
        return resultSb.toString();
    }

    private static int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    public static String getExceptionStackTrace(Throwable throwable) {
        if (null == throwable) {
            return "null";
        }
        StringBuilder sb = new StringBuilder(throwable.getMessage()).append("\n");
        StackTraceElement[] elements = throwable.getStackTrace();
        for (StackTraceElement element : elements) {
            sb.append(element.toString()).append("\r\n");
        }
        return sb.toString();
    }

    public static String text(Context context, int resId) {
        return context.getString(resId);
    }

    public static String text(Context context, int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    public static String fillNumZero(long num) {
        if (num < 10) {
            return "0" + String.valueOf(num);
        } else {
            return String.valueOf(num);
        }
    }

    //    public static String text(int resId) {
//        return AppContextBase.getInstance().getString(resId);
//    }
//
//    public static String text(int resId, Object... formatArgs) {
//        return AppContextBase.getInstance().getString(resId, formatArgs);
//    }
    public static HashMap<String, String> getUrlParams(String url) {
        HashMap<String, String> map = new HashMap<>();
        if (url.contains("?")) {
            String[] sl = url.split("\\?");
            if (sl.length > 1) {
                String paramsStr = sl[1];
                if (!StringUtil.isEmpty(paramsStr) && paramsStr.contains("&")) {
                    String[] params = paramsStr.split("&");
                    if (params.length > 0) {
                        for (String p : params) {
                            if (!StringUtil.isEmpty(p) && p.contains("=")) {
                                String[] keyValues = p.split("=");
                                if (keyValues.length > 1) {
                                    map.put(keyValues[0], keyValues[1]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    public static boolean verPwdError(String pwd) {
        if (pwd == null || pwd.length() < 8 || pwd.length() > 16) {
            return true;
        }
//        if (!isMatcherFinded("^(?![^a-zA-Z]+$)(?!\\D+$).{8,16}$", pwd)) {
//            return true;
//        }
        return false;
    }

    public static boolean isMatcherFinded(String patternStr, CharSequence input) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}
