package com.chenyi.baselib.widget.wheelview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.tao.shop.baselib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Administrator on 2015/11/14.
 */
public class DatePickerDialog extends Dialog {
    public static final String FORMAT_MINU = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DAY = "yyyy-MM-dd";
    public static final String FORMAT_MONTH = "yyyy-MM";
    private String format = FORMAT_MINU;

    public DatePickerDialog(Context context, String format) {
        this(context, format, 1900);
    }

    public DatePickerDialog(Context context, String format, int startYear) {
        super(context, R.style.dialog_picker);
        this.format = format;
        init(context, startYear, Calendar.getInstance().get(Calendar.YEAR));
    }

    public DatePickerDialog(Context context, String format, int startYear, int endYear) {
        super(context, R.style.dialog_picker);
        this.format = format;
        init(context, startYear, endYear);
    }

    private void init(Context context, int startYear, int endYear) {
        View timepickerview = LayoutInflater.from(context).inflate(R.layout.dialog_date_picker, null);
        timepickerview.setMinimumWidth(ViewUtil.getScreenDM((BaseActivity) context).widthPixels);
        ScreenInfo screenInfo = new ScreenInfo((BaseActivity) context);
        final DatePicker wheelMain = new DatePicker(timepickerview, format.equals(FORMAT_MINU), !format.equals(FORMAT_MONTH));
        wheelMain.setEnd_year(endYear);
        wheelMain.setStart_year(startYear);
        wheelMain.screenheight = screenInfo.getHeight();
        Window window = getWindow();
        window.setContentView(timepickerview);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM); // ??????????????????dialog???????????????

        String sTime = "";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        try {
            if (!"".equals(sTime)) {
                Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(sTime);
                year = Integer.parseInt(new SimpleDateFormat("yyyy").format(dt));
                month = Integer.parseInt(new SimpleDateFormat("MM").format(dt)) - 1;
                day = Integer.parseInt(new SimpleDateFormat("dd").format(dt));
                hours = Integer.parseInt(new SimpleDateFormat("HH").format(dt));
                minute = Integer.parseInt(new SimpleDateFormat("mm").format(dt));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (format.equals(FORMAT_DAY)) {
            wheelMain.initDateTimePicker(year, month, day);
        } else if (format.equals(FORMAT_MINU)) {
            wheelMain.initDateTimePicker(year, month, day, hours, minute);
        } else if (format.equals(FORMAT_MONTH)) {
            wheelMain.initDateTimePicker(year, month);
        }

        Button sumbit = (Button) timepickerview.findViewById(R.id.btn_datetime_sure);
        Button cancle = (Button) timepickerview.findViewById(R.id.btn_datetime_cancle);
        sumbit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Date date = wheelMain.getTime();
                    long longTime = date.getTime();
                    String time = new SimpleDateFormat(format).format(date);
                    if (onSelectTimeListener != null) {
                        onSelectTimeListener.onSelect(time, longTime, format);
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnSelectTimeListener(OnSelectTimeListener onSelectTimeListener) {
        this.onSelectTimeListener = onSelectTimeListener;
    }

    OnSelectTimeListener onSelectTimeListener;

    public interface OnSelectTimeListener {
        void onSelect(String time, long longTime, String format);
    }
}
