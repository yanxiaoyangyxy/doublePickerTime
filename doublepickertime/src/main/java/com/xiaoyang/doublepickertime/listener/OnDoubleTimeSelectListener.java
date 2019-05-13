package com.xiaoyang.doublepickertime.listener;

import android.view.View;

import java.util.Date;

/**
 * Created by xiaosong on 2018/3/20.
 */

public interface OnDoubleTimeSelectListener {


    /**
     * 时间段选择器
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param v
     */
    void onTimeSelect(Date startDate, Date endDate, View v);
}
