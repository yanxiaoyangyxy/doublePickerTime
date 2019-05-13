package com.xiaoyang.doublepickertime.view;

import android.view.View;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.xiaoyang.doublepickertime.R;
import com.xiaoyang.doublepickertime.adapter.ArrayWheelAdapter;
import com.xiaoyang.doublepickertime.adapter.NumericWheelAdapter;
import com.xiaoyang.doublepickertime.listener.ISelectTimeCallback;
import com.xiaoyang.doublepickertime.utils.ChinaDate;
import com.xiaoyang.doublepickertime.utils.LunarCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 *
 * Package:
 * ClassName:      DoubleWheelTime
 * Author:         xiaoyangyan
 * CreateDate:     2019-05-13 15:33
 * Description:    时间段选择期支持年月日
 */
public class DoubleWheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private View view;
    private WheelView wv_start_year;
    private WheelView wv_start_month;
    private WheelView wv_start_day;
    private WheelView wv_end_year;
    private WheelView wv_end_month;
    private WheelView wv_end_day;
    private int gravity;

    private boolean[] type;
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_MONTH = 1;
    private static final int DEFAULT_END_MONTH = 12;
    private static final int DEFAULT_START_DAY = 1;
    private static final int DEFAULT_END_DAY = 31;

    private int startYear = DEFAULT_START_YEAR;
    private int endYear = DEFAULT_END_YEAR;
    private int startMonth = DEFAULT_START_MONTH;
    private int endMonth = DEFAULT_END_MONTH;
    private int startDay = DEFAULT_START_DAY;
    private int endDay = DEFAULT_END_DAY; //表示31天的
    private int currentYear;

    private int textSize;

    //文字的颜色和分割线的颜色
    private int textColorOut;
    private int textColorCenter;
    private int dividerColor;

    private float lineSpacingMultiplier;
    private WheelView.DividerType dividerType;
    private boolean isLunarCalendar = false;
    private ISelectTimeCallback mSelectChangeCallback;

    public DoubleWheelTime(View view, boolean[] type, int gravity, int textSize) {
        super();
        this.view = view;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
    }

    public void setLunarMode(boolean isLunarCalendar) {
        this.isLunarCalendar = isLunarCalendar;
    }

    public boolean isLunarMode() {
        return isLunarCalendar;
    }


    public void setPicker(int year, final int month, int day) {
        setSolar(year, month, day);
    }


    /**
     * 设置公历
     *
     * @param year
     * @param month
     * @param day
     */
    private void setSolar(int year, final int month, int day) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        currentYear = year;
        // 开始年
        wv_start_year = (WheelView) view.findViewById(R.id.start_year);
        wv_start_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
        // 结束年
        wv_end_year = (WheelView) view.findViewById(R.id.end_year);
        wv_end_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据


        wv_start_year.setCurrentItem(year - startYear);// 初始化时显示的数据
        wv_start_year.setGravity(gravity);
        wv_end_year.setCurrentItem(year - startYear);// 初始化时显示的数据
        wv_end_year.setGravity(gravity);
        // 月
        wv_start_month = (WheelView) view.findViewById(R.id.start_month);
        wv_end_month = (WheelView) view.findViewById(R.id.end_month);
        if (startYear == endYear) {//开始年等于终止年
            wv_start_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
            wv_start_month.setCurrentItem(month + 1 - startMonth);
            wv_end_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
            wv_end_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == startYear) {
            //起始日期的月份控制
            wv_start_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
            wv_start_month.setCurrentItem(month + 1 - startMonth);
            wv_end_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
            wv_end_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == endYear) {
            //终止日期的月份控制
            wv_start_month.setAdapter(new NumericWheelAdapter(1, endMonth));
            wv_start_month.setCurrentItem(month);
            wv_end_month.setAdapter(new NumericWheelAdapter(1, endMonth));
            wv_end_month.setCurrentItem(month);
        } else {
            wv_start_month.setAdapter(new NumericWheelAdapter(1, 12));
            wv_start_month.setCurrentItem(month);
            wv_end_month.setAdapter(new NumericWheelAdapter(1, 12));
            wv_end_month.setCurrentItem(month);
        }
        wv_start_month.setGravity(gravity);
        wv_end_month.setGravity(gravity);
        // 日
        wv_start_day = (WheelView) view.findViewById(R.id.start_day);
        wv_end_day = (WheelView) view.findViewById(R.id.end_day);
        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_start_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                wv_end_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_start_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                wv_end_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_start_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                    wv_end_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_start_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                    wv_end_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                }
            }
            wv_start_day.setCurrentItem(day - startDay);
            wv_end_day.setCurrentItem(day - startDay);
        } else if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_start_day.setAdapter(new NumericWheelAdapter(startDay, 31));
                wv_end_day.setAdapter(new NumericWheelAdapter(startDay, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_start_day.setAdapter(new NumericWheelAdapter(startDay, 30));
                wv_end_day.setAdapter(new NumericWheelAdapter(startDay, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    wv_start_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                    wv_end_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                } else {
                    wv_start_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                    wv_end_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                }
            }
            wv_start_day.setCurrentItem(day - startDay);
            wv_end_day.setCurrentItem(day - startDay);
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_start_day.setAdapter(new NumericWheelAdapter(1, endDay));
                wv_end_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_start_day.setAdapter(new NumericWheelAdapter(1, endDay));
                wv_end_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_start_day.setAdapter(new NumericWheelAdapter(1, endDay));
                    wv_end_day.setAdapter(new NumericWheelAdapter(1, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_start_day.setAdapter(new NumericWheelAdapter(1, endDay));
                    wv_end_day.setAdapter(new NumericWheelAdapter(1, endDay));
                }
            }
            wv_start_day.setCurrentItem(day - 1);
            wv_end_day.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_start_day.setAdapter(new NumericWheelAdapter(1, 31));
                wv_end_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_start_day.setAdapter(new NumericWheelAdapter(1, 30));
                wv_end_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_start_day.setAdapter(new NumericWheelAdapter(1, 29));
                    wv_end_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {
                    wv_start_day.setAdapter(new NumericWheelAdapter(1, 28));
                    wv_end_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_start_day.setCurrentItem(day - 1);
            wv_end_day.setCurrentItem(day - 1);
        }

        wv_start_day.setGravity(gravity);
        wv_end_day.setGravity(gravity);
        // 添加"年"监听
        wv_start_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                currentYear = year_num;
                int currentMonthItem = wv_start_month.getCurrentItem();//记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (startYear == endYear) {
                    //重新设置月份
                    wv_start_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));

                    if (currentMonthItem > wv_start_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_start_month.getAdapter().getItemsCount() - 1;
                        wv_start_month.setCurrentItem(currentMonthItem);
                    }

                    int monthNum = currentMonthItem + startMonth;

                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, endDay, list_big, list_little);
                    } else if (monthNum == startMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, 31, list_big, list_little);
                    } else if (monthNum == endMonth) {
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {//重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }
                } else if (year_num == startYear) {//等于开始的年
                    //重新设置月份
                    wv_start_month.setAdapter(new NumericWheelAdapter(startMonth, 12));

                    if (currentMonthItem > wv_start_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_start_month.getAdapter().getItemsCount() - 1;
                        wv_start_month.setCurrentItem(currentMonthItem);
                    }

                    int month = currentMonthItem + startMonth;
                    if (month == startMonth) {
                        //重新设置日
                        setReDay(year_num, month, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, month, 1, 31, list_big, list_little);
                    }

                } else if (year_num == endYear) {
                    //重新设置月份
                    wv_start_month.setAdapter(new NumericWheelAdapter(1, endMonth));
                    if (currentMonthItem > wv_start_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_start_month.getAdapter().getItemsCount() - 1;
                        wv_start_month.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + 1;

                    if (monthNum == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }

                } else {
                    //重新设置月份
                    wv_start_month.setAdapter(new NumericWheelAdapter(1, 12));
                    //重新设置日
                    setReDay(year_num, wv_start_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
                }

                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });


        // 添加"月"监听
        wv_start_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;

                if (startYear == endYear) {
                    month_num = month_num + startMonth - 1;
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, endDay, list_big, list_little);
                    } else if (startMonth == month_num) {

                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else if (endMonth == month_num) {
                        setReDay(currentYear, month_num, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }
                } else if (currentYear == startYear) {
                    month_num = month_num + startMonth - 1;
                    if (month_num == startMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }

                } else if (currentYear == endYear) {
                    if (month_num == endMonth) {
                        //重新设置日
                        setReDay(currentYear, wv_start_month.getCurrentItem() + 1, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, wv_start_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
                    }

                } else {
                    //重新设置日
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                }

                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });

        setChangedListener(wv_start_day);

        if (type.length != 6) {
            throw new IllegalArgumentException("type[] length is not 6");
        }
        wv_start_year.setVisibility(type[0] ? View.VISIBLE : View.GONE);
        wv_start_month.setVisibility(type[1] ? View.VISIBLE : View.GONE);
        wv_start_day.setVisibility(type[2] ? View.VISIBLE : View.GONE);
        setContentTextSize();
    }

    private void setChangedListener(WheelView wheelView) {
        if (mSelectChangeCallback != null) {
            wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            });
        }

    }


    private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = wv_start_day.getCurrentItem();

//        int maxItem;
        if (list_big.contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            wv_start_day.setAdapter(new NumericWheelAdapter(startD, endD));
//            maxItem = endD;
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            wv_start_day.setAdapter(new NumericWheelAdapter(startD, endD));
//            maxItem = endD;
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                wv_start_day.setAdapter(new NumericWheelAdapter(startD, endD));
//                maxItem = endD;
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                wv_start_day.setAdapter(new NumericWheelAdapter(startD, endD));
//                maxItem = endD;
            }
        }

        if (currentItem > wv_start_day.getAdapter().getItemsCount() - 1) {
            currentItem = wv_start_day.getAdapter().getItemsCount() - 1;
            wv_start_day.setCurrentItem(currentItem);
        }
    }


    private void setContentTextSize() {
        wv_start_year.setTextSize(textSize);
        wv_start_month.setTextSize(textSize);
        wv_start_day.setTextSize(textSize);
        wv_end_year.setTextSize(textSize);
        wv_end_month.setTextSize(textSize);
        wv_end_day.setTextSize(textSize);
    }

    private void setTextColorOut() {
        wv_start_day.setTextColorOut(textColorOut);
        wv_start_month.setTextColorOut(textColorOut);
        wv_start_year.setTextColorOut(textColorOut);
        wv_end_day.setTextColorOut(textColorOut);
        wv_end_month.setTextColorOut(textColorOut);
        wv_end_year.setTextColorOut(textColorOut);
    }

    private void setTextColorCenter() {
        wv_start_day.setTextColorCenter(textColorCenter);
        wv_start_month.setTextColorCenter(textColorCenter);
        wv_start_year.setTextColorCenter(textColorCenter);
        wv_end_day.setTextColorCenter(textColorCenter);
        wv_end_month.setTextColorCenter(textColorCenter);
        wv_end_year.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
        wv_start_day.setDividerColor(dividerColor);
        wv_start_month.setDividerColor(dividerColor);
        wv_start_year.setDividerColor(dividerColor);
        wv_end_day.setDividerColor(dividerColor);
        wv_end_month.setDividerColor(dividerColor);
        wv_end_year.setDividerColor(dividerColor);
    }

    private void setDividerType() {

        wv_start_day.setDividerType(dividerType);
        wv_start_month.setDividerType(dividerType);
        wv_start_year.setDividerType(dividerType);
        wv_end_day.setDividerType(dividerType);
        wv_end_month.setDividerType(dividerType);
        wv_end_year.setDividerType(dividerType);
    }

    private void setLineSpacingMultiplier() {
        wv_start_day.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_start_month.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_start_year.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_end_day.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_end_month.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_end_year.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    public void setLabels(String label_year, String label_month, String label_day) {
        if (isLunarCalendar) {
            return;
        }

        if (label_year != null) {
            wv_start_year.setLabel(label_year);
            wv_end_year.setLabel(label_year);
        } else {
            wv_start_year.setLabel(view.getContext().getString(R.string.pickerview_year));
            wv_end_year.setLabel(view.getContext().getString(R.string.pickerview_year));
        }
        if (label_month != null) {
            wv_start_month.setLabel(label_month);
            wv_end_month.setLabel(label_month);
        } else {
            wv_start_month.setLabel(view.getContext().getString(R.string.pickerview_month));
            wv_end_month.setLabel(view.getContext().getString(R.string.pickerview_month));
        }
        if (label_day != null) {
            wv_start_day.setLabel(label_day);
            wv_end_day.setLabel(label_day);
        } else {
            wv_start_day.setLabel(view.getContext().getString(R.string.pickerview_day));
            wv_end_day.setLabel(view.getContext().getString(R.string.pickerview_day));
        }

    }

    public void setTextXOffset(int x_offset_year, int x_offset_month, int x_offset_day) {
        wv_start_year.setTextXOffset(x_offset_year);
        wv_start_month.setTextXOffset(x_offset_month);
        wv_start_day.setTextXOffset(x_offset_day);
        wv_end_year.setTextXOffset(x_offset_year);
        wv_end_month.setTextXOffset(x_offset_month);
        wv_end_day.setTextXOffset(x_offset_day);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_start_year.setCyclic(cyclic);
        wv_start_month.setCyclic(cyclic);
        wv_start_day.setCyclic(cyclic);
        wv_end_year.setCyclic(cyclic);
        wv_end_month.setCyclic(cyclic);
        wv_end_day.setCyclic(cyclic);
    }

    public String getStartTime() {
        if (isLunarCalendar) {
            //如果是农历 返回对应的公历时间
            return getLunarTime();
        }
        StringBuilder sb = new StringBuilder();
        if (currentYear == startYear) {
           /* int i = wv_start_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/
            if ((wv_start_month.getCurrentItem() + startMonth) == startMonth) {
                sb.append((wv_start_year.getCurrentItem() + startYear)).append("-")
                        .append((wv_start_month.getCurrentItem() + startMonth)).append("-")
                        .append((wv_start_day.getCurrentItem() + startDay)).append(" ");
            } else {
                sb.append((wv_start_year.getCurrentItem() + startYear)).append("-")
                        .append((wv_start_month.getCurrentItem() + startMonth)).append("-")
                        .append((wv_start_day.getCurrentItem() + 1)).append(" ");
            }

        } else {
            sb.append((wv_start_year.getCurrentItem() + startYear)).append("-")
                    .append((wv_start_month.getCurrentItem() + 1)).append("-")
                    .append((wv_start_day.getCurrentItem() + 1));
        }

        return sb.toString();
    }

    public String getEndTime() {
        if (isLunarCalendar) {
            //如果是农历 返回对应的公历时间
            return getLunarTime();
        }
        StringBuilder sb = new StringBuilder();
        if (currentYear == startYear) {
           /* int i = wv_start_month.getCurrentItem() + startMonth;
            System.out.println("i:" + i);*/
            if ((wv_end_month.getCurrentItem() + startMonth) == startMonth) {
                sb.append((wv_end_year.getCurrentItem() + startYear)).append("-")
                        .append((wv_end_month.getCurrentItem() + startMonth)).append("-")
                        .append((wv_end_day.getCurrentItem() + startDay)).append(" ");
            } else {
                sb.append((wv_end_year.getCurrentItem() + startYear)).append("-")
                        .append((wv_end_month.getCurrentItem() + startMonth)).append("-")
                        .append((wv_end_day.getCurrentItem() + 1)).append(" ");
            }

        } else {
            sb.append((wv_end_year.getCurrentItem() + startYear)).append("-")
                    .append((wv_end_month.getCurrentItem() + 1)).append("-")
                    .append((wv_end_day.getCurrentItem() + 1));
        }

        return sb.toString();
    }

    /**
     * 农历返回对应的公历时间
     *
     * @return
     */
    private String getLunarTime() {
        StringBuilder sb = new StringBuilder();
        int year = wv_start_year.getCurrentItem() + startYear;
        int month = 1;
        boolean isLeapMonth = false;
        if (ChinaDate.leapMonth(year) == 0) {
            month = wv_start_month.getCurrentItem() + 1;
        } else {
            if ((wv_start_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) <= 0) {
                month = wv_start_month.getCurrentItem() + 1;
            } else if ((wv_start_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) == 1) {
                month = wv_start_month.getCurrentItem();
                isLeapMonth = true;
            } else {
                month = wv_start_month.getCurrentItem();
            }
        }
        int day = wv_start_day.getCurrentItem() + 1;
        int[] solar = LunarCalendar.lunarToSolar(year, month, day, isLeapMonth);

        sb.append(solar[0]).append("-")
                .append(solar[1]).append("-")
                .append(solar[2]).append(" ");
        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }


    public void setRangDate(Calendar startDate, Calendar endDate) {

        if (startDate == null && endDate != null) {
            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH) + 1;
            int day = endDate.get(Calendar.DAY_OF_MONTH);
            if (year > startYear) {
                this.endYear = year;
                this.endMonth = month;
                this.endDay = day;
            } else if (year == startYear) {
                if (month > startMonth) {
                    this.endYear = year;
                    this.endMonth = month;
                    this.endDay = day;
                } else if (month == startMonth) {
                    if (day > startDay) {
                        this.endYear = year;
                        this.endMonth = month;
                        this.endDay = day;
                    }
                }
            }

        } else if (startDate != null && endDate == null) {
            int year = startDate.get(Calendar.YEAR);
            int month = startDate.get(Calendar.MONTH) + 1;
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            if (year < endYear) {
                this.startMonth = month;
                this.startDay = day;
                this.startYear = year;
            } else if (year == endYear) {
                if (month < endMonth) {
                    this.startMonth = month;
                    this.startDay = day;
                    this.startYear = year;
                } else if (month == endMonth) {
                    if (day < endDay) {
                        this.startMonth = month;
                        this.startDay = day;
                        this.startYear = year;
                    }
                }
            }

        } else if (startDate != null && endDate != null) {
            this.startYear = startDate.get(Calendar.YEAR);
            this.endYear = endDate.get(Calendar.YEAR);
            this.startMonth = startDate.get(Calendar.MONTH) + 1;
            this.endMonth = endDate.get(Calendar.MONTH) + 1;
            this.startDay = startDate.get(Calendar.DAY_OF_MONTH);
            this.endDay = endDate.get(Calendar.DAY_OF_MONTH);
        }

    }

    /**
     * 设置间距倍数,但是只能在1.0-4.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }

    /**
     * @param isCenterLabel 是否只显示中间选中项的
     */
    public void isCenterLabel(boolean isCenterLabel) {
        wv_start_year.isCenterLabel(isCenterLabel);
        wv_start_month.isCenterLabel(isCenterLabel);
        wv_start_day.isCenterLabel(isCenterLabel);
        wv_end_year.isCenterLabel(isCenterLabel);
        wv_end_month.isCenterLabel(isCenterLabel);
        wv_end_day.isCenterLabel(isCenterLabel);
    }

    public void setSelectChangeCallback(ISelectTimeCallback mSelectChangeCallback) {
        this.mSelectChangeCallback = mSelectChangeCallback;
    }
}
