package com.ranjiexu.optionscrollablepanel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ranjiexu.optionscrollablepanel.adpter.ScrollablePanelAdapter;
import com.ranjiexu.optionscrollablepanel.bean.TermName;
import com.ranjiexu.optionscrollablepanel.bean.TermValue;
import com.ranjiexu.optionscrollablepanel.library.ScrollablePanel;

import java.util.ArrayList;
import java.util.List;

/**
*  Created by devpc-05 on 2017/4/11.
**/
public class MainActivity extends AppCompatActivity {

    // 右边合约条目名称
    private static final String[] RIGHT_TERM_NAMES = {"最新价", "涨跌", "涨跌幅", "买价", "卖价", "总量",
            "持仓量", "仓差", "隐含波动率", "期权理论价", "杠杆比率", "真实杠杆率", "溢价率",
            "Delta", "Gamma", "Rho", "Theta", "Vega"};

    // 左边合约条目名称
    private static final String[] LEFT_TERM_NAMES = {"Vega", "Theta", "Rho", "Gamma", "Delta", "溢价率",
            "真实杠杆率", "杠杆比率", "期权理论价", "隐含波动率", "仓差", "持仓量", "总量",
            "卖价", "买价", "涨跌幅", "涨跌", "最新价"};

    // 右边合约条目值
    private static final String[] RIGHT_TERM_VALUES = {"56.0", "-12.0", "-17.65%", "56.0", "58.0", "226",
            "396", "84", "0.1211", "68.7182", "49.91", "-24.78", "1.82",
            "-0.4965", "0.0024", "-231.4481", "-205.1411", "441.3905"};

    // 左边合约条目值
    private static final String[] LEFT_TERM_VALUES = {"441.3905", "-205.1411", "-231.4481", "0.0024", "-0.4965", "1.82",
            "-24.78", "49.91", "68.7182", "0.1211", "84", "396", "226",
            "58.0", "56.0", "-17.65%", "-12.0", "56.0"};

//    // 右边合约条目名称
//    private static final String[] RIGHT_TERM_NAMES = {"最新价", "涨跌", "涨跌幅", "买价", "卖价", "总量"};
//
//    // 左边合约条目名称
//    private static final String[] LEFT_TERM_NAMES = { "总量","卖价", "买价", "涨跌幅", "涨跌", "最新价"};
//
//    // 右边合约条目值
//    private static final String[] RIGHT_TERM_VALUES = {"1", "2", "3", "4", "5", "6"};
//
//    // 左边合约条目值
//    private static final String[] LEFT_TERM_VALUES = {"6", "5", "4", "3", "2", "1"};

//    // 右边合约条目名称
//    private static final String[] RIGHT_TERM_NAMES = {"最新价", "涨跌"};
//
//    // 左边合约条目名称
//    private static final String[] LEFT_TERM_NAMES = { "总量", "卖价"};
//
//    // 右边合约条目值
//    private static final String[] RIGHT_TERM_VALUES = {"1", "2"};
//
//    // 左边合约条目值
//    private static final String[] LEFT_TERM_VALUES = {"6", "5"};

    // 行权价
    private static final String[] EXECUTION_PRICE = {"2500", "2550", "2600", "2650", "2700", "2750",
            "2800", "2850", "2900", "2950", "3000", "3050"};

    private static ScrollablePanel scrollablePanel;
    private static ScrollablePanelAdapter leftScrollablePanelAdapter;
    private static ScrollablePanelAdapter rightScrollablePanelAdapter;

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_panel);
        leftScrollablePanelAdapter = new ScrollablePanelAdapter();
        rightScrollablePanelAdapter = new ScrollablePanelAdapter();
        generateData(leftScrollablePanelAdapter, LEFT_TERM_NAMES, LEFT_TERM_VALUES, EXECUTION_PRICE.length);
        generateData(rightScrollablePanelAdapter, RIGHT_TERM_NAMES, RIGHT_TERM_VALUES, EXECUTION_PRICE.length);
        scrollablePanel.setPanelAdapter(leftScrollablePanelAdapter, rightScrollablePanelAdapter, EXECUTION_PRICE);
        // 开启定时更新单条记录
//        startSingle();
    }

    private static Runnable changeItem = new Runnable() {
        @Override
        public void run() {
            Log.e("ranjiexu", "更新单条行情数据。。。");
            generateData(leftScrollablePanelAdapter, LEFT_TERM_NAMES, LEFT_TERM_VALUES, EXECUTION_PRICE.length);
            generateData(rightScrollablePanelAdapter, RIGHT_TERM_NAMES, RIGHT_TERM_VALUES, EXECUTION_PRICE.length);
            scrollablePanel.setPanelAdapter(leftScrollablePanelAdapter , rightScrollablePanelAdapter, EXECUTION_PRICE);
            scrollablePanel.notifyDataItemChanged(0);
            mHandler.postDelayed(this, 10000);
        }
    };

    private void startSingle() {
        Log.e("ranjiexu", "开始更新单条数据。。。");
        if(isFirst){
            mHandler.postDelayed(changeItem, 10000);
            isFirst = false;
        }
    }

    private static void generateData(ScrollablePanelAdapter scrollablePanelAdapter, String[] termNames, String[] termValues, int length) {

        List<TermName> termNameList = new ArrayList<>();
        for (int i = 0; i < termNames.length; i++) {
            TermName termName = new TermName();
            termName.setName(termNames[i]);
            termNameList.add(termName);
        }
        scrollablePanelAdapter.setTermNameList(termNameList);

        List<List<TermValue>> termValuesList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            List<TermValue> termValueList = new ArrayList<>();
            for (int j = 0; j < termValues.length; j++) {
                TermValue termValue = new TermValue();
                termValue.setValue(termValues[j]);
                termValue.setStatus(TermValue.Status.randomStatus());
                termValueList.add(termValue);
            }
            termValuesList.add(termValueList);
        }
        scrollablePanelAdapter.setTermValuesList(termValuesList);
    }
}
