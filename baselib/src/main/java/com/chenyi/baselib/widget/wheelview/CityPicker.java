package com.chenyi.baselib.widget.wheelview;//package com.ass.nb.pro.widget.wheelview;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//
//import com.timepost.shiyi.R;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import static u.aly.x.R;
//
//public class CityPicker {
//    private Activity ct;
//    private View view;
//    private Dialog dialog;
//    private Button sumbit;
//    private WheelView wvProvince;
//    private WheelView wvCity;
//    private WheelView wvArea;
//    private List<String[]> listProvince;
//    private List<String[]> listCity;
//    private String province;
//    private String city;
//int screenheight;
//
//    public CityPicker(Activity ct) {
//        this.ct = ct;
//        LayoutInflater inflater = LayoutInflater.from(ct);
//        view = inflater.inflate(R.layout.dialog_city_picker, null);
//        view.setMinimumWidth(ct.getWindowManager().getDefaultDisplay()
//                .getWidth());
//        ScreenInfo screenInfo = new ScreenInfo(ct);
//        screenheight = screenInfo.getHeight();
//        wvProvince = (WheelView) view.findViewById(R.id.province);
//        wvCity = (WheelView) view.findViewById(R.id.city);
//        wvArea = (WheelView) view.findViewById(R.id.area);
//        initCitys();
//        listProvince = new ArrayList<String[]>();
//        for (String[] city : citys) {
//            if ("1".equals(city[1])) {
//                listProvince.add(city);
//            }
//        }
//
//        String code = listProvince.get(0)[0].substring(0, 3);
//        listCity = new ArrayList<String[]>();
//        for (String[] city : citys) {
//            if ("2".equals(city[1]) && city[0].startsWith(code)) {
//                listCity.add(city);
//            }
//        }
//        province = listProvince.get(0)[3];
//        city = listCity.get(0)[3];
//
//        wvProvince.setAdapter(new WheelAdapter() {
//
//            @Override
//            public int getMaximumLength() {
//                // TODO Auto-generated method stub
//                return 100;
//            }
//
//            @Override
//            public int getItemsCount() {
//                return listProvince.size();
//            }
//
//            @Override
//            public String getItem(int index) {
//                return listProvince.get(index)[3];
//            }
//        });
//
//        final WheelAdapter cityAdapter = new WheelAdapter() {
//
//            @Override
//            public int getMaximumLength() {
//                // TODO Auto-generated method stub
//                return 100;
//            }
//
//            @Override
//            public int getItemsCount() {
//                return listCity.size();
//            }
//
//            @Override
//            public String getItem(int index) {
//                return listCity.get(index)[3];
//            }
//        };
//        wvCity.setAdapter(cityAdapter);
//        wvProvince.addChangingListener(new OnWheelChangedListener() {
//
//            @Override
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                String[] p = listProvince.get(newValue);
//                String code = p[0].substring(0, 3);
//                listCity = new ArrayList<String[]>();
//                for (String[] city : citys) {
//                    if ("2".equals(city[1]) && city[0].startsWith(code)) {
//                        listCity.add(city);
//                    }
//                }
//                province = p[3];
//                city = listCity.get(0)[3];
//                wvCity.setAdapter(cityAdapter);
//                wvCity.setCurrentItem(0);
//            }
//        });
//
//        wvCity.addChangingListener(new OnWheelChangedListener() {
//
//            @Override
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                city = listCity.get(newValue)[3];
//            }
//        });
//
//        sumbit = (Button) view.findViewById(R.id.btnOk);
//        view.findViewById(R.id.btnCancle).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
////        int textsize= (screenheight / 100) * 4;
////        wvArea.ITEMS_TEXT_SIZE=textsize;
////        wvArea.TEXT_SIZE=textsize;
////        wvCity.ITEMS_TEXT_SIZE=textsize;
////        wvCity.TEXT_SIZE=textsize;
////        wvProvince.ITEMS_TEXT_SIZE=textsize;
////        wvProvince.TEXT_SIZE=textsize;
//
//    }
//
//    public interface PickerListner {
//        void OnPick(String p, String c);
//    }
//
//    public void show() {
//        dialog = new AlertDialog.Builder(ct).create();
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setContentView(view);
//        window.setGravity(Gravity.BOTTOM); // ??????????????????dialog???????????????
//    }
//
//    public void setPickerListner(final PickerListner l) {
//        if (null == l) {
//            return;
//        }
//        sumbit.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                l.OnPick(province, city);
//                dialog.dismiss();
//            }
//        });
//    }
//
//
//    List<String[]> citys = new ArrayList<String[]>();
//
//    private void initCitys() {
//        citys.add(new String[]{"1000000", "1", "", "??????", "BJ"});
//        citys.add(new String[]{"1000050", "2", "1000000", "??????", "DC"});
//        citys.add(new String[]{"1000100", "2", "1000000", "??????", "XC"});
//        citys.add(new String[]{"1000150", "2", "1000000", "??????", "CY"});
//        citys.add(new String[]{"1000200", "2", "1000000", "??????", "FT"});
//        citys.add(new String[]{"1000250", "2", "1000000", "?????????", "SJS"});
//        citys.add(new String[]{"1000300", "2", "1000000", "??????", "HD"});
//        citys.add(new String[]{"1000350", "2", "1000000", "?????????", "MTG"});
//        citys.add(new String[]{"1000400", "2", "1000000", "??????", "FS"});
//        citys.add(new String[]{"1000450", "2", "1000000", "??????", "TZ"});
//        citys.add(new String[]{"1000500", "2", "1000000", "??????", "SY"});
//        citys.add(new String[]{"1000550", "2", "1000000", "??????", "CP"});
//        citys.add(new String[]{"1000600", "2", "1000000", "??????", "DX"});
//        citys.add(new String[]{"1000650", "2", "1000000", "??????", "HR"});
//        citys.add(new String[]{"1000700", "2", "1000000", "??????", "PG"});
//        citys.add(new String[]{"1000750", "2", "1000000", "?????????", "MYX"});
//        citys.add(new String[]{"1000800", "2", "1000000", "?????????", "YQX"});
//        citys.add(new String[]{"1010000", "1", "", "??????", "TJ"});
//        citys.add(new String[]{"1010050", "2", "1010000", "??????", "HP"});
//        citys.add(new String[]{"1010100", "2", "1010000", "??????", "HD"});
//        citys.add(new String[]{"1010150", "2", "1010000", "??????", "HX"});
//        citys.add(new String[]{"1010200", "2", "1010000", "??????", "NK"});
//        citys.add(new String[]{"1010250", "2", "1010000", "??????", "HB"});
//        citys.add(new String[]{"1010300", "2", "1010000", "??????", "HQ"});
//        citys.add(new String[]{"1010350", "2", "1010000", "??????", "DL"});
//        citys.add(new String[]{"1010400", "2", "1010000", "??????", "XQ"});
//        citys.add(new String[]{"1010450", "2", "1010000", "??????", "JN"});
//        citys.add(new String[]{"1010500", "2", "1010000", "??????", "BC"});
//        citys.add(new String[]{"1010550", "2", "1010000", "??????", "WQ"});
//        citys.add(new String[]{"1010600", "2", "1010000", "??????", "BC"});
//        citys.add(new String[]{"1010650", "2", "1010000", "??????", "BH"});
//        citys.add(new String[]{"1010700", "2", "1010000", "?????????", "NHX"});
//        citys.add(new String[]{"1010750", "2", "1010000", "?????????", "JHX"});
//        citys.add(new String[]{"1010800", "2", "1010000", "??????", "JX"});
//        citys.add(new String[]{"1020000", "1", "", "??????", "HB"});
//        citys.add(new String[]{"1020050", "2", "1020000", "?????????", "SJZ"});
//        citys.add(new String[]{"1020100", "2", "1020000", "??????", "TS"});
//        citys.add(new String[]{"1020150", "2", "1020000", "?????????", "QHD"});
//        citys.add(new String[]{"1020200", "2", "1020000", "??????", "HD"});
//        citys.add(new String[]{"1020250", "2", "1020000", "??????", "XT"});
//        citys.add(new String[]{"1020300", "2", "1020000", "??????", "BD"});
//        citys.add(new String[]{"1020350", "2", "1020000", "?????????", "ZJK"});
//        citys.add(new String[]{"1020400", "2", "1020000", "??????", "CD"});
//        citys.add(new String[]{"1020450", "2", "1020000", "??????", "CZ"});
//        citys.add(new String[]{"1020500", "2", "1020000", "??????", "LF"});
//        citys.add(new String[]{"1020550", "2", "1020000", "??????", "HS"});
//        citys.add(new String[]{"1030000", "1", "", "??????", "SX"});
//        citys.add(new String[]{"1030050", "2", "1030000", "??????", "TY"});
//        citys.add(new String[]{"1030100", "2", "1030000", "??????", "DT"});
//        citys.add(new String[]{"1030150", "2", "1030000", "??????", "YQ"});
//        citys.add(new String[]{"1030200", "2", "1030000", "??????", "ZZ"});
//        citys.add(new String[]{"1030250", "2", "1030000", "??????", "JC"});
//        citys.add(new String[]{"1030300", "2", "1030000", "??????", "SZ"});
//        citys.add(new String[]{"1030350", "2", "1030000", "??????", "JZ"});
//        citys.add(new String[]{"1030400", "2", "1030000", "??????", "YC"});
//        citys.add(new String[]{"1030450", "2", "1030000", "??????", "XZ"});
//        citys.add(new String[]{"1030500", "2", "1030000", "??????", "LF"});
//        citys.add(new String[]{"1030550", "2", "1030000", "??????", "LL"});
//        citys.add(new String[]{"1040000", "1", "", "?????????", "NMG"});
//        citys.add(new String[]{"1040050", "2", "1040000", "????????????", "HHHT"});
//        citys.add(new String[]{"1040100", "2", "1040000", "??????", "BT"});
//        citys.add(new String[]{"1040150", "2", "1040000", "??????", "WH"});
//        citys.add(new String[]{"1040200", "2", "1040000", "??????", "CF"});
//        citys.add(new String[]{"1040250", "2", "1040000", "??????", "TL"});
//        citys.add(new String[]{"1040300", "2", "1040000", "????????????", "EEDS"});
//        citys.add(new String[]{"1040350", "2", "1040000", "????????????", "HLBE"});
//        citys.add(new String[]{"1040400", "2", "1040000", "????????????", "BYNE"});
//        citys.add(new String[]{"1040450", "2", "1040000", "????????????", "WLCB"});
//        citys.add(new String[]{"1040500", "2", "1040000", "??????", "XA"});
//        citys.add(new String[]{"1040550", "2", "1040000", "????????????", "XLGL"});
//        citys.add(new String[]{"1040600", "2", "1040000", "?????????", "ALS"});
//        citys.add(new String[]{"1050000", "1", "", "??????", "LN"});
//        citys.add(new String[]{"1050050", "2", "1050000", "??????", "SY"});
//        citys.add(new String[]{"1050100", "2", "1050000", "??????", "DL"});
//        citys.add(new String[]{"1050150", "2", "1050000", "??????", "AS"});
//        citys.add(new String[]{"1050200", "2", "1050000", "??????", "FS"});
//        citys.add(new String[]{"1050250", "2", "1050000", "??????", "BX"});
//        citys.add(new String[]{"1050300", "2", "1050000", "??????", "DD"});
//        citys.add(new String[]{"1050350", "2", "1050000", "??????", "JZ"});
//        citys.add(new String[]{"1050400", "2", "1050000", "??????", "YK"});
//        citys.add(new String[]{"1050450", "2", "1050000", "??????", "FX"});
//        citys.add(new String[]{"1050500", "2", "1050000", "??????", "LY"});
//        citys.add(new String[]{"1050550", "2", "1050000", "??????", "PJ"});
//        citys.add(new String[]{"1050600", "2", "1050000", "??????", "TL"});
//        citys.add(new String[]{"1050650", "2", "1050000", "??????", "CY"});
//        citys.add(new String[]{"1050700", "2", "1050000", "?????????", "HLD"});
//        citys.add(new String[]{"1060000", "1", "", "??????", "JL"});
//        citys.add(new String[]{"1060050", "2", "1060000", "??????", "ZC"});
//        citys.add(new String[]{"1060100", "2", "1060000", "??????", "JL"});
//        citys.add(new String[]{"1060150", "2", "1060000", "??????", "SP"});
//        citys.add(new String[]{"1060200", "2", "1060000", "??????", "LY"});
//        citys.add(new String[]{"1060250", "2", "1060000", "??????", "TH"});
//        citys.add(new String[]{"1060300", "2", "1060000", "??????", "BS"});
//        citys.add(new String[]{"1060350", "2", "1060000", "??????", "SY"});
//        citys.add(new String[]{"1060400", "2", "1060000", "??????", "BC"});
//        citys.add(new String[]{"1060450", "2", "1060000", "??????", "YB"});
//        citys.add(new String[]{"1070000", "1", "", "?????????", "HLJ"});
//        citys.add(new String[]{"1070050", "2", "1070000", "?????????", "HEB"});
//        citys.add(new String[]{"1070100", "2", "1070000", "????????????", "QQHE"});
//        citys.add(new String[]{"1070150", "2", "1070000", "??????", "JX"});
//        citys.add(new String[]{"1070200", "2", "1070000", "??????", "HG"});
//        citys.add(new String[]{"1070250", "2", "1070000", "?????????", "SYS"});
//        citys.add(new String[]{"1070300", "2", "1070000", "??????", "DQ"});
//        citys.add(new String[]{"1070350", "2", "1070000", "??????", "YC"});
//        citys.add(new String[]{"1070400", "2", "1070000", "?????????", "JMS"});
//        citys.add(new String[]{"1070450", "2", "1070000", "?????????", "QTH"});
//        citys.add(new String[]{"1070500", "2", "1070000", "?????????", "MDJ"});
//        citys.add(new String[]{"1070550", "2", "1070000", "??????", "HH"});
//        citys.add(new String[]{"1070600", "2", "1070000", "??????", "SH"});
//        citys.add(new String[]{"1070650", "2", "1070000", "????????????", "DXAL"});
//        citys.add(new String[]{"1080000", "1", "", "??????", "SH"});
//        citys.add(new String[]{"1080050", "2", "1080000", "??????", "HP"});
//        citys.add(new String[]{"1080100", "2", "1080000", "??????", "LW"});
//        citys.add(new String[]{"1080150", "2", "1080000", "??????", "XH"});
//        citys.add(new String[]{"1080200", "2", "1080000", "??????", "ZN"});
//        citys.add(new String[]{"1080250", "2", "1080000", "??????", "JA"});
//        citys.add(new String[]{"1080300", "2", "1080000", "??????", "PT"});
//        citys.add(new String[]{"1080350", "2", "1080000", "??????", "ZB"});
//        citys.add(new String[]{"1080400", "2", "1080000", "??????", "HK"});
//        citys.add(new String[]{"1080450", "2", "1080000", "??????", "YP"});
//        citys.add(new String[]{"1080500", "2", "1080000", "??????", "MX"});
//        citys.add(new String[]{"1080550", "2", "1080000", "??????", "BS"});
//        citys.add(new String[]{"1080600", "2", "1080000", "??????", "JD"});
//        citys.add(new String[]{"1080650", "2", "1080000", "??????", "PD"});
//        citys.add(new String[]{"1080700", "2", "1080000", "??????", "JS"});
//        citys.add(new String[]{"1080750", "2", "1080000", "??????", "SJ"});
//        citys.add(new String[]{"1080800", "2", "1080000", "??????", "QP"});
//        citys.add(new String[]{"1080850", "2", "1080000", "??????", "FX"});
//        citys.add(new String[]{"1080900", "2", "1080000", "?????????", "CMX"});
//        citys.add(new String[]{"1090000", "1", "", "??????", "JS"});
//        citys.add(new String[]{"1090050", "2", "1090000", "??????", "NJ"});
//        citys.add(new String[]{"1090100", "2", "1090000", "??????", "WX"});
//        citys.add(new String[]{"1090150", "2", "1090000", "??????", "XZ"});
//        citys.add(new String[]{"1090200", "2", "1090000", "??????", "CZ"});
//        citys.add(new String[]{"1090250", "2", "1090000", "??????", "SZ"});
//        citys.add(new String[]{"1090300", "2", "1090000", "??????", "NT"});
//        citys.add(new String[]{"1090350", "2", "1090000", "?????????", "LYG"});
//        citys.add(new String[]{"1090400", "2", "1090000", "??????", "HA"});
//        citys.add(new String[]{"1090450", "2", "1090000", "??????", "YC"});
//        citys.add(new String[]{"1090500", "2", "1090000", "??????", "YZ"});
//        citys.add(new String[]{"1090550", "2", "1090000", "??????", "ZJ"});
//        citys.add(new String[]{"1090600", "2", "1090000", "??????", "TZ"});
//        citys.add(new String[]{"1090650", "2", "1090000", "??????", "SQ"});
//        citys.add(new String[]{"1100000", "1", "", "??????", "ZJ"});
//        citys.add(new String[]{"1100050", "2", "1100000", "??????", "HZ"});
//        citys.add(new String[]{"1100100", "2", "1100000", "??????", "NB"});
//        citys.add(new String[]{"1100150", "2", "1100000", "??????", "WZ"});
//        citys.add(new String[]{"1100200", "2", "1100000", "??????", "JX"});
//        citys.add(new String[]{"1100250", "2", "1100000", "??????", "HZ"});
//        citys.add(new String[]{"1100300", "2", "1100000", "??????", "SX"});
//        citys.add(new String[]{"1100350", "2", "1100000", "??????", "JH"});
//        citys.add(new String[]{"1100400", "2", "1100000", "??????", "QZ"});
//        citys.add(new String[]{"1100450", "2", "1100000", "??????", "ZS"});
//        citys.add(new String[]{"1100500", "2", "1100000", "??????", "TZ"});
//        citys.add(new String[]{"1100550", "2", "1100000", "??????", "LS"});
//        citys.add(new String[]{"1110000", "1", "", "??????", "AH"});
//        citys.add(new String[]{"1110050", "2", "1110000", "??????", "HF"});
//        citys.add(new String[]{"1110100", "2", "1110000", "??????", "WH"});
//        citys.add(new String[]{"1110150", "2", "1110000", "??????", "BB"});
//        citys.add(new String[]{"1110200", "2", "1110000", "??????", "HN"});
//        citys.add(new String[]{"1110250", "2", "1110000", "?????????", "MAS"});
//        citys.add(new String[]{"1110300", "2", "1110000", "??????", "HB"});
//        citys.add(new String[]{"1110350", "2", "1110000", "??????", "TL"});
//        citys.add(new String[]{"1110400", "2", "1110000", "??????", "AQ"});
//        citys.add(new String[]{"1110450", "2", "1110000", "??????", "HS"});
//        citys.add(new String[]{"1110500", "2", "1110000", "??????", "CZ"});
//        citys.add(new String[]{"1110550", "2", "1110000", "??????", "FY"});
//        citys.add(new String[]{"1110600", "2", "1110000", "??????", "SZ"});
//        citys.add(new String[]{"1110650", "2", "1110000", "??????", "CH"});
//        citys.add(new String[]{"1110700", "2", "1110000", "??????", "LA"});
//        citys.add(new String[]{"1110750", "2", "1110000", "??????", "BZ"});
//        citys.add(new String[]{"1110800", "2", "1110000", "??????", "CZ"});
//        citys.add(new String[]{"1110850", "2", "1110000", "??????", "XC"});
//        citys.add(new String[]{"1120000", "1", "", "??????", "FJ"});
//        citys.add(new String[]{"1120050", "2", "1120000", "??????", "FZ"});
//        citys.add(new String[]{"1120100", "2", "1120000", "??????", "SM"});
//        citys.add(new String[]{"1120150", "2", "1120000", "??????", "PT"});
//        citys.add(new String[]{"1120200", "2", "1120000", "??????", "SM"});
//        citys.add(new String[]{"1120250", "2", "1120000", "??????", "QZ"});
//        citys.add(new String[]{"1120300", "2", "1120000", "??????", "ZZ"});
//        citys.add(new String[]{"1120350", "2", "1120000", "??????", "NP"});
//        citys.add(new String[]{"1120400", "2", "1120000", "??????", "LY"});
//        citys.add(new String[]{"1120450", "2", "1120000", "??????", "ND"});
//        citys.add(new String[]{"1130000", "1", "", "??????", "JX"});
//        citys.add(new String[]{"1130050", "2", "1130000", "??????", "NC"});
//        citys.add(new String[]{"1130100", "2", "1130000", "?????????", "JDZ"});
//        citys.add(new String[]{"1130150", "2", "1130000", "??????", "PX"});
//        citys.add(new String[]{"1130200", "2", "1130000", "??????", "JJ"});
//        citys.add(new String[]{"1130250", "2", "1130000", "??????", "XY"});
//        citys.add(new String[]{"1130300", "2", "1130000", "??????", "YT"});
//        citys.add(new String[]{"1130350", "2", "1130000", "??????", "GZ"});
//        citys.add(new String[]{"1130400", "2", "1130000", "??????", "JA"});
//        citys.add(new String[]{"1130450", "2", "1130000", "??????", "YC"});
//        citys.add(new String[]{"1130500", "2", "1130000", "??????", "FZ"});
//        citys.add(new String[]{"1130550", "2", "1130000", "??????", "SR"});
//        citys.add(new String[]{"1140000", "1", "", "??????", "SD"});
//        citys.add(new String[]{"1140050", "2", "1140000", "??????", "JN"});
//        citys.add(new String[]{"1140100", "2", "1140000", "??????", "QD"});
//        citys.add(new String[]{"1140150", "2", "1140000", "??????", "ZB"});
//        citys.add(new String[]{"1140200", "2", "1140000", "??????", "ZZ"});
//        citys.add(new String[]{"1140250", "2", "1140000", "??????", "DY"});
//        citys.add(new String[]{"1140300", "2", "1140000", "??????", "YT"});
//        citys.add(new String[]{"1140350", "2", "1140000", "??????", "WF"});
//        citys.add(new String[]{"1140400", "2", "1140000", "??????", "JN"});
//        citys.add(new String[]{"1140450", "2", "1140000", "??????", "TA"});
//        citys.add(new String[]{"1140500", "2", "1140000", "??????", "WH"});
//        citys.add(new String[]{"1140550", "2", "1140000", "??????", "RZ"});
//        citys.add(new String[]{"1140600", "2", "1140000", "??????", "LW"});
//        citys.add(new String[]{"1140650", "2", "1140000", "??????", "LY"});
//        citys.add(new String[]{"1140700", "2", "1140000", "??????", "DZ"});
//        citys.add(new String[]{"1140750", "2", "1140000", "??????", "LC"});
//        citys.add(new String[]{"1140800", "2", "1140000", "??????", "BZ"});
//        citys.add(new String[]{"1140850", "2", "1140000", "??????", "HZ"});
//        citys.add(new String[]{"1150000", "1", "", "??????", "HN"});
//        citys.add(new String[]{"1150050", "2", "1150000", "??????", "ZZ"});
//        citys.add(new String[]{"1150100", "2", "1150000", "??????", "KF"});
//        citys.add(new String[]{"1150150", "2", "1150000", "??????", "LY"});
//        citys.add(new String[]{"1150200", "2", "1150000", "?????????", "PDS"});
//        citys.add(new String[]{"1150250", "2", "1150000", "??????", "AY"});
//        citys.add(new String[]{"1150300", "2", "1150000", "??????", "HB"});
//        citys.add(new String[]{"1150350", "2", "1150000", "??????", "XX"});
//        citys.add(new String[]{"1150400", "2", "1150000", "??????", "JZ"});
//        citys.add(new String[]{"1150450", "2", "1150000", "??????", "PY"});
//        citys.add(new String[]{"1150500", "2", "1150000", "??????", "XC"});
//        citys.add(new String[]{"1150550", "2", "1150000", "??????", "LH"});
//        citys.add(new String[]{"1150600", "2", "1150000", "?????????", "SMX"});
//        citys.add(new String[]{"1150650", "2", "1150000", "??????", "NY"});
//        citys.add(new String[]{"1150700", "2", "1150000", "??????", "SQ"});
//        citys.add(new String[]{"1150750", "2", "1150000", "??????", "XY"});
//        citys.add(new String[]{"1150800", "2", "1150000", "??????", "ZK"});
//        citys.add(new String[]{"1150850", "2", "1150000", "?????????", "ZMD"});
//        citys.add(new String[]{"1150900", "2", "1150000", "??????", "JY"});
//        citys.add(new String[]{"1160000", "1", "", "??????", "HB"});
//        citys.add(new String[]{"1160050", "2", "1160000", "??????", "WH"});
//        citys.add(new String[]{"1160100", "2", "1160000", "??????", "HS"});
//        citys.add(new String[]{"1160150", "2", "1160000", "??????", "SY"});
//        citys.add(new String[]{"1160200", "2", "1160000", "??????", "YC"});
//        citys.add(new String[]{"1160250", "2", "1160000", "??????", "XY"});
//        citys.add(new String[]{"1160300", "2", "1160000", "??????", "EZ"});
//        citys.add(new String[]{"1160350", "2", "1160000", "??????", "JM"});
//        citys.add(new String[]{"1160400", "2", "1160000", "??????", "XG"});
//        citys.add(new String[]{"1160450", "2", "1160000", "??????", "JZ"});
//        citys.add(new String[]{"1160500", "2", "1160000", "??????", "HG"});
//        citys.add(new String[]{"1160550", "2", "1160000", "??????", "XN"});
//        citys.add(new String[]{"1160600", "2", "1160000", "??????", "SZ"});
//        citys.add(new String[]{"1160650", "2", "1160000", "??????", "ES"});
//        citys.add(new String[]{"1160700", "2", "1160000", "??????", "QJ"});
//        citys.add(new String[]{"1160750", "2", "1160000", "??????", "XT"});
//        citys.add(new String[]{"1160800", "2", "1160000", "??????", "TM"});
//        citys.add(new String[]{"1160850", "2", "1160000", "?????????", "SNJ"});
//        citys.add(new String[]{"1170000", "1", "", "??????", "HN"});
//        citys.add(new String[]{"1170050", "2", "1170000", "??????", "ZS"});
//        citys.add(new String[]{"1170100", "2", "1170000", "??????", "ZZ"});
//        citys.add(new String[]{"1170150", "2", "1170000", "??????", "XT"});
//        citys.add(new String[]{"1170200", "2", "1170000", "??????", "HY"});
//        citys.add(new String[]{"1170250", "2", "1170000", "??????", "SY"});
//        citys.add(new String[]{"1170300", "2", "1170000", "??????", "YY"});
//        citys.add(new String[]{"1170350", "2", "1170000", "??????", "CD"});
//        citys.add(new String[]{"1170400", "2", "1170000", "?????????", "ZJJ"});
//        citys.add(new String[]{"1170450", "2", "1170000", "??????", "YY"});
//        citys.add(new String[]{"1170500", "2", "1170000", "??????", "CZ"});
//        citys.add(new String[]{"1170550", "2", "1170000", "??????", "YZ"});
//        citys.add(new String[]{"1170600", "2", "1170000", "??????", "HH"});
//        citys.add(new String[]{"1170650", "2", "1170000", "??????", "LD"});
//        citys.add(new String[]{"1170700", "2", "1170000", "??????", "XX"});
//        citys.add(new String[]{"1180000", "1", "", "??????", "GD"});
//        citys.add(new String[]{"1180050", "2", "1180000", "??????", "GZ"});
//        citys.add(new String[]{"1180100", "2", "1180000", "??????", "SG"});
//        citys.add(new String[]{"1180150", "2", "1180000", "??????", "SZ"});
//        citys.add(new String[]{"1180200", "2", "1180000", "??????", "ZH"});
//        citys.add(new String[]{"1180250", "2", "1180000", "??????", "ST"});
//        citys.add(new String[]{"1180300", "2", "1180000", "??????", "FS"});
//        citys.add(new String[]{"1180350", "2", "1180000", "??????", "JM"});
//        citys.add(new String[]{"1180400", "2", "1180000", "??????", "ZJ"});
//        citys.add(new String[]{"1180450", "2", "1180000", "??????", "MM"});
//        citys.add(new String[]{"1180500", "2", "1180000", "??????", "ZQ"});
//        citys.add(new String[]{"1180550", "2", "1180000", "??????", "HZ"});
//        citys.add(new String[]{"1180600", "2", "1180000", "??????", "MZ"});
//        citys.add(new String[]{"1180650", "2", "1180000", "??????", "SW"});
//        citys.add(new String[]{"1180700", "2", "1180000", "??????", "HY"});
//        citys.add(new String[]{"1180750", "2", "1180000", "??????", "YJ"});
//        citys.add(new String[]{"1180800", "2", "1180000", "??????", "QY"});
//        citys.add(new String[]{"1180850", "2", "1180000", "??????", "DG"});
//        citys.add(new String[]{"1180900", "2", "1180000", "??????", "ZS"});
//        citys.add(new String[]{"1180950", "2", "1180000", "????????????", "DSQD"});
//        citys.add(new String[]{"1181000", "2", "1180000", "??????", "CZ"});
//        citys.add(new String[]{"1181050", "2", "1180000", "??????", "JY"});
//        citys.add(new String[]{"1181100", "2", "1180000", "??????", "YF"});
//        citys.add(new String[]{"1190000", "1", "", "??????", "GX"});
//        citys.add(new String[]{"1190050", "2", "1190000", "??????", "NN"});
//        citys.add(new String[]{"1190100", "2", "1190000", "??????", "LZ"});
//        citys.add(new String[]{"1190150", "2", "1190000", "??????", "GL"});
//        citys.add(new String[]{"1190200", "2", "1190000", "??????", "WZ"});
//        citys.add(new String[]{"1190250", "2", "1190000", "??????", "BH"});
//        citys.add(new String[]{"1190300", "2", "1190000", "?????????", "FCG"});
//        citys.add(new String[]{"1190350", "2", "1190000", "??????", "QZ"});
//        citys.add(new String[]{"1190400", "2", "1190000", "??????", "GG"});
//        citys.add(new String[]{"1190450", "2", "1190000", "??????", "YL"});
//        citys.add(new String[]{"1190500", "2", "1190000", "??????", "BS"});
//        citys.add(new String[]{"1190550", "2", "1190000", "??????", "HZ"});
//        citys.add(new String[]{"1190600", "2", "1190000", "??????", "HC"});
//        citys.add(new String[]{"1190650", "2", "1190000", "??????", "LB"});
//        citys.add(new String[]{"1190700", "2", "1190000", "??????", "CZ"});
//        citys.add(new String[]{"1200000", "1", "", "??????", "HN"});
//        citys.add(new String[]{"1200050", "2", "1200000", "??????", "HK"});
//        citys.add(new String[]{"1200100", "2", "1200000", "??????", "SY"});
//        citys.add(new String[]{"1200150", "2", "1200000", "?????????", "LGX"});
//        citys.add(new String[]{"1200200", "2", "1200000", "??????", "DZ"});
//        citys.add(new String[]{"1200250", "2", "1200000", "?????????", "TCX"});
//        citys.add(new String[]{"1200300", "2", "1200000", "?????????", "DAX"});
//        citys.add(new String[]{"1200350", "2", "1200000", "?????????", "CMX"});
//        citys.add(new String[]{"1200400", "2", "1200000", "??????", "WC"});
//        citys.add(new String[]{"1200450", "2", "1200000", "??????", "QH"});
//        citys.add(new String[]{"1200500", "2", "1200000", "??????", "WN"});
//        citys.add(new String[]{"1200550", "2", "1200000", "?????????", "QZX"});
//        citys.add(new String[]{"1200600", "2", "1200000", "?????????", "BTX"});
//        citys.add(new String[]{"1200650", "2", "1200000", "?????????", "WZS"});
//        citys.add(new String[]{"1200700", "2", "1200000", "?????????", "BSX"});
//        citys.add(new String[]{"1200750", "2", "1200000", "??????", "DF"});
//        citys.add(new String[]{"1200800", "2", "1200000", "?????????", "CJX"});
//        citys.add(new String[]{"1200850", "2", "1200000", "?????????", "LDX"});
//        citys.add(new String[]{"1200900", "2", "1200000", "?????????", "LSX"});
//        citys.add(new String[]{"1200950", "2", "1200000", "????????????", "XSQD"});
//        citys.add(new String[]{"1201000", "2", "1200000", "????????????", "NSQD"});
//        citys.add(new String[]{"1210000", "1", "", "??????", "CQ"});
//        citys.add(new String[]{"1210050", "2", "1210000", "??????", "WZ"});
//        citys.add(new String[]{"1210100", "2", "1210000", "??????", "FL"});
//        citys.add(new String[]{"1210150", "2", "1210000", "??????", "YZ"});
//        citys.add(new String[]{"1210200", "2", "1210000", "?????????", "DDK"});
//        citys.add(new String[]{"1210250", "2", "1210000", "??????", "JB"});
//        citys.add(new String[]{"1210300", "2", "1210000", "?????????", "SPB"});
//        citys.add(new String[]{"1210350", "2", "1210000", "?????????", "JLP"});
//        citys.add(new String[]{"1210400", "2", "1210000", "??????", "NA"});
//        citys.add(new String[]{"1210450", "2", "1210000", "??????", "BB"});
//        citys.add(new String[]{"1210500", "2", "1210000", "??????", "WS"});
//        citys.add(new String[]{"1210550", "2", "1210000", "??????", "SQ"});
//        citys.add(new String[]{"1210600", "2", "1210000", "??????", "YB"});
//        citys.add(new String[]{"1210650", "2", "1210000", "??????", "BN"});
//        citys.add(new String[]{"1210700", "2", "1210000", "??????", "QJ"});
//        citys.add(new String[]{"1210750", "2", "1210000", "??????", "ZS"});
//        citys.add(new String[]{"1210800", "2", "1210000", "??????", "JJ"});
//        citys.add(new String[]{"1210850", "2", "1210000", "??????", "HC"});
//        citys.add(new String[]{"1210900", "2", "1210000", "??????", "YC"});
//        citys.add(new String[]{"1210950", "2", "1210000", "??????", "NC"});
//        citys.add(new String[]{"1211000", "2", "1210000", "??????", "QJ"});
//        citys.add(new String[]{"1211050", "2", "1210000", "??????", "TN"});
//        citys.add(new String[]{"1211100", "2", "1210000", "??????", "TL"});
//        citys.add(new String[]{"1211150", "2", "1210000", "??????", "DZ"});
//        citys.add(new String[]{"1211200", "2", "1210000", "??????", "RC"});
//        citys.add(new String[]{"1211250", "2", "1210000", "??????", "BS"});
//        citys.add(new String[]{"1211300", "2", "1210000", "??????", "LP"});
//        citys.add(new String[]{"1211350", "2", "1210000", "??????", "CK"});
//        citys.add(new String[]{"1211400", "2", "1210000", "??????", "FD"});
//        citys.add(new String[]{"1211450", "2", "1210000", "??????", "DJ"});
//        citys.add(new String[]{"1211500", "2", "1210000", "??????", "WL"});
//        citys.add(new String[]{"1211550", "2", "1210000", "??????", "ZX"});
//        citys.add(new String[]{"1211600", "2", "1210000", "??????", "KX"});
//        citys.add(new String[]{"1211650", "2", "1210000", "??????", "YY"});
//        citys.add(new String[]{"1211700", "2", "1210000", "??????", "FJ"});
//        citys.add(new String[]{"1211750", "2", "1210000", "??????", "WS"});
//        citys.add(new String[]{"1211800", "2", "1210000", "??????", "WX"});
//        citys.add(new String[]{"1211850", "2", "1210000", "??????", "SZ"});
//        citys.add(new String[]{"1211900", "2", "1210000", "??????", "XS"});
//        citys.add(new String[]{"1211950", "2", "1210000", "??????", "YY"});
//        citys.add(new String[]{"1212000", "2", "1210000", "??????", "PS"});
//        citys.add(new String[]{"1220000", "1", "", "??????", "SC"});
//        citys.add(new String[]{"1220050", "2", "1220000", "??????", "CD"});
//        citys.add(new String[]{"1220100", "2", "1220000", "??????", "ZG"});
//        citys.add(new String[]{"1220150", "2", "1220000", "?????????", "PZH"});
//        citys.add(new String[]{"1220200", "2", "1220000", "??????", "LZ"});
//        citys.add(new String[]{"1220250", "2", "1220000", "??????", "DY"});
//        citys.add(new String[]{"1220300", "2", "1220000", "??????", "MY"});
//        citys.add(new String[]{"1220350", "2", "1220000", "??????", "GY"});
//        citys.add(new String[]{"1220400", "2", "1220000", "??????", "SN"});
//        citys.add(new String[]{"1220450", "2", "1220000", "??????", "NJ"});
//        citys.add(new String[]{"1220500", "2", "1220000", "??????", "LS"});
//        citys.add(new String[]{"1220550", "2", "1220000", "??????", "NC"});
//        citys.add(new String[]{"1220600", "2", "1220000", "??????", "MS"});
//        citys.add(new String[]{"1220650", "2", "1220000", "??????", "YB"});
//        citys.add(new String[]{"1220700", "2", "1220000", "??????", "GA"});
//        citys.add(new String[]{"1220750", "2", "1220000", "??????", "DZ"});
//        citys.add(new String[]{"1220800", "2", "1220000", "??????", "YA"});
//        citys.add(new String[]{"1220850", "2", "1220000", "??????", "BZ"});
//        citys.add(new String[]{"1220900", "2", "1220000", "??????", "ZY"});
//        citys.add(new String[]{"1220950", "2", "1220000", "??????", "AB"});
//        citys.add(new String[]{"1221000", "2", "1220000", "??????", "GZ"});
//        citys.add(new String[]{"1221050", "2", "1220000", "??????", "LS"});
//        citys.add(new String[]{"1230000", "1", "", "??????", "GZ"});
//        citys.add(new String[]{"1230050", "2", "1230000", "??????", "GY"});
//        citys.add(new String[]{"1230100", "2", "1230000", "?????????", "LPS"});
//        citys.add(new String[]{"1230150", "2", "1230000", "??????", "ZY"});
//        citys.add(new String[]{"1230200", "2", "1230000", "??????", "AS"});
//        citys.add(new String[]{"1230250", "2", "1230000", "??????", "TR"});
//        citys.add(new String[]{"1230300", "2", "1230000", "??????", "QX"});
//        citys.add(new String[]{"1230350", "2", "1230000", "??????", "BJ"});
//        citys.add(new String[]{"1230400", "2", "1230000", "??????", "QD"});
//        citys.add(new String[]{"1230450", "2", "1230000", "??????", "QN"});
//        citys.add(new String[]{"1240000", "1", "", "??????", "YN"});
//        citys.add(new String[]{"1240050", "2", "1240000", "??????", "KM"});
//        citys.add(new String[]{"1240100", "2", "1240000", "??????", "QJ"});
//        citys.add(new String[]{"1240150", "2", "1240000", "??????", "YX"});
//        citys.add(new String[]{"1240200", "2", "1240000", "??????", "BS"});
//        citys.add(new String[]{"1240250", "2", "1240000", "??????", "ZT"});
//        citys.add(new String[]{"1240300", "2", "1240000", "??????", "LJ"});
//        citys.add(new String[]{"1240350", "2", "1240000", "??????", "PE"});
//        citys.add(new String[]{"1240400", "2", "1240000", "??????", "LC"});
//        citys.add(new String[]{"1240450", "2", "1240000", "??????", "CX"});
//        citys.add(new String[]{"1240500", "2", "1240000", "??????", "HH"});
//        citys.add(new String[]{"1240550", "2", "1240000", "??????", "WS"});
//        citys.add(new String[]{"1240600", "2", "1240000", "????????????", "XSBN"});
//        citys.add(new String[]{"1240650", "2", "1240000", "??????", "DL"});
//        citys.add(new String[]{"1240700", "2", "1240000", "??????", "DH"});
//        citys.add(new String[]{"1240750", "2", "1240000", "??????", "NJ"});
//        citys.add(new String[]{"1240800", "2", "1240000", "??????", "DQ"});
//        citys.add(new String[]{"1250000", "1", "", "??????", "XZ"});
//        citys.add(new String[]{"1250050", "2", "1250000", "??????", "LS"});
//        citys.add(new String[]{"1250100", "2", "1250000", "??????", "CD"});
//        citys.add(new String[]{"1250150", "2", "1250000", "??????", "SN"});
//        citys.add(new String[]{"1250200", "2", "1250000", "?????????", "RKZ"});
//        citys.add(new String[]{"1250250", "2", "1250000", "??????", "NQ"});
//        citys.add(new String[]{"1250300", "2", "1250000", "??????", "AL"});
//        citys.add(new String[]{"1250350", "2", "1250000", "??????", "LZ"});
//        citys.add(new String[]{"1260000", "1", "", "??????", "SX"});
//        citys.add(new String[]{"1260050", "2", "1260000", "??????", "XA"});
//        citys.add(new String[]{"1260100", "2", "1260000", "??????", "TC"});
//        citys.add(new String[]{"1260150", "2", "1260000", "??????", "BJ"});
//        citys.add(new String[]{"1260200", "2", "1260000", "??????", "XY"});
//        citys.add(new String[]{"1260250", "2", "1260000", "??????", "WN"});
//        citys.add(new String[]{"1260300", "2", "1260000", "??????", "YA"});
//        citys.add(new String[]{"1260350", "2", "1260000", "??????", "HZ"});
//        citys.add(new String[]{"1260400", "2", "1260000", "??????", "YL"});
//        citys.add(new String[]{"1260450", "2", "1260000", "??????", "AK"});
//        citys.add(new String[]{"1260500", "2", "1260000", "??????", "SL"});
//        citys.add(new String[]{"1270000", "1", "", "??????", "GS"});
//        citys.add(new String[]{"1270050", "2", "1270000", "??????", "LZ"});
//        citys.add(new String[]{"1270100", "2", "1270000", "?????????", "JYG"});
//        citys.add(new String[]{"1270150", "2", "1270000", "??????", "JC"});
//        citys.add(new String[]{"1270200", "2", "1270000", "??????", "BY"});
//        citys.add(new String[]{"1270250", "2", "1270000", "??????", "TS"});
//        citys.add(new String[]{"1270300", "2", "1270000", "??????", "WW"});
//        citys.add(new String[]{"1270350", "2", "1270000", "??????", "ZY"});
//        citys.add(new String[]{"1270400", "2", "1270000", "??????", "PL"});
//        citys.add(new String[]{"1270450", "2", "1270000", "??????", "JQ"});
//        citys.add(new String[]{"1270500", "2", "1270000", "??????", "QY"});
//        citys.add(new String[]{"1270550", "2", "1270000", "??????", "DX"});
//        citys.add(new String[]{"1270600", "2", "1270000", "??????", "LN"});
//        citys.add(new String[]{"1270650", "2", "1270000", "??????", "LX"});
//        citys.add(new String[]{"1270700", "2", "1270000", "??????", "GN"});
//        citys.add(new String[]{"1280000", "1", "", "??????", "QH"});
//        citys.add(new String[]{"1280050", "2", "1280000", "??????", "XN"});
//        citys.add(new String[]{"1280100", "2", "1280000", "??????", "HD"});
//        citys.add(new String[]{"1280150", "2", "1280000", "??????", "HB"});
//        citys.add(new String[]{"1280200", "2", "1280000", "??????", "HN"});
//        citys.add(new String[]{"1280250", "2", "1280000", "??????", "HN"});
//        citys.add(new String[]{"1280300", "2", "1280000", "??????", "GL"});
//        citys.add(new String[]{"1280350", "2", "1280000", "??????", "YS"});
//        citys.add(new String[]{"1280400", "2", "1280000", "??????", "HX"});
//        citys.add(new String[]{"1290000", "1", "", "??????", "NX"});
//        citys.add(new String[]{"1290050", "2", "1290000", "??????", "YC"});
//        citys.add(new String[]{"1290100", "2", "1290000", "?????????", "SZS"});
//        citys.add(new String[]{"1290150", "2", "1290000", "??????", "WZ"});
//        citys.add(new String[]{"1290200", "2", "1290000", "??????", "GY"});
//        citys.add(new String[]{"1290250", "2", "1290000", "??????", "ZW"});
//        citys.add(new String[]{"1300000", "1", "", "??????", "XJ"});
//        citys.add(new String[]{"1300050", "2", "1300000", "????????????", "WLMQ"});
//        citys.add(new String[]{"1300100", "2", "1300000", "????????????", "KLMY"});
//        citys.add(new String[]{"1300150", "2", "1300000", "?????????", "TLF"});
//        citys.add(new String[]{"1300200", "2", "1300000", "??????", "HM"});
//        citys.add(new String[]{"1300250", "2", "1300000", "??????", "CJ"});
//        citys.add(new String[]{"1300300", "2", "1300000", "????????????", "BETL"});
//        citys.add(new String[]{"1300350", "2", "1300000", "????????????", "BYGL"});
//        citys.add(new String[]{"1300400", "2", "1300000", "?????????", "AKS"});
//        citys.add(new String[]{"1300450", "2", "1300000", "????????????", "KZLS"});
//        citys.add(new String[]{"1300500", "2", "1300000", "??????", "KS"});
//        citys.add(new String[]{"1300550", "2", "1300000", "??????", "HT"});
//        citys.add(new String[]{"1300600", "2", "1300000", "??????", "YL"});
//        citys.add(new String[]{"1300650", "2", "1300000", "??????", "TC"});
//        citys.add(new String[]{"1300700", "2", "1300000", "?????????", "ALT"});
//        citys.add(new String[]{"1300750", "2", "1300000", "?????????", "SHZ"});
//        citys.add(new String[]{"1300800", "2", "1300000", "?????????", "ALE"});
//        citys.add(new String[]{"1300850", "2", "1300000", "????????????", "TMSK"});
//        citys.add(new String[]{"1300900", "2", "1300000", "?????????", "WJQ"});
//
//        Collections.sort(citys, new Comparator<String[]>() {
//
//            @Override
//            public int compare(String[] lhs, String[] rhs) {
//                return lhs[4].compareTo(rhs[4]);
//            }
//        });
//    }
//}
