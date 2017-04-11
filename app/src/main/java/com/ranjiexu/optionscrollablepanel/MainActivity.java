package com.ranjiexu.optionscrollablepanel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ranjiexu.optionscrollablepanel.adpter.ScrollablePanelAdapter;
import com.ranjiexu.optionscrollablepanel.bean.TermName;
import com.ranjiexu.optionscrollablepanel.bean.TermValue;
import com.ranjiexu.optionscrollablepanel.bean.ExecutionPrice;
import com.ranjiexu.optionscrollablepanel.library.ScrollablePanel;

import java.util.ArrayList;
import java.util.List;

/**
*  Created by devpc-05 on 2017/4/11.
**/
public class MainActivity extends AppCompatActivity {

    private static final String[] TERM_NAMES = {"最新价", "涨跌", "涨跌幅", "买价", "卖价", "总量",
            "持仓量", "仓差", "隐含波动率", "期权理论价", "杠杆比率", "真实杠杆率", "溢价率",
            "Delta", "Gamma", "Rho", "Theta", "Vega"};

    private static final String[] TERM_VALUES = {"56.0", "-12.0", "-17.65%", "56.0", "58.0", "226",
            "396", "84", "0.1211", "68.7182", "49.91", "-24.78", "1.82",
            "-0.4965", "0.0024", "-231.4481", "-205.1411", "441.3905"};

    private static final String[] EXECUTION_PRICE = {"2500", "2550", "2600", "2650", "2700", "2750",
            "2800", "2850", "2900", "2950", "3000", "3050"};

    private static ScrollablePanel scrollablePanel;
    private static ScrollablePanelAdapter scrollablePanelAdapter;

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_panel);
        scrollablePanelAdapter = new ScrollablePanelAdapter();
        generateTestData(scrollablePanelAdapter);
        scrollablePanel.setPanelAdapter(scrollablePanelAdapter);
        // 开启定时更新单条记录
        startSingle();
    }

    private static Runnable changeItem = new Runnable() {
        @Override
        public void run() {
            Log.e("ranjiexu", "更新单条行情数据。。。");
            generateTestData(scrollablePanelAdapter);
            scrollablePanel.setPanelAdapter(scrollablePanelAdapter);
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

    private static void generateTestData(ScrollablePanelAdapter scrollablePanelAdapter) {

        List<ExecutionPrice> executionPriceList = new ArrayList<>();
        for (int i = 0; i < EXECUTION_PRICE.length; i++) {
            ExecutionPrice executionPrice = new ExecutionPrice();
            executionPrice.setPriceId(i);
            executionPrice.setExcutionPrice(EXECUTION_PRICE[i]);
            executionPriceList.add(executionPrice);
        }
        scrollablePanelAdapter.setExecutionPriceList(executionPriceList);

        List<TermName> termNameList = new ArrayList<>();

        for (int i = 0; i < TERM_NAMES.length; i++) {
            TermName termName = new TermName();
            termName.setName(TERM_NAMES[i]);
            termNameList.add(termName);
        }
        scrollablePanelAdapter.setTermNameList(termNameList);

        List<List<TermValue>> termValuesList = new ArrayList<>();
        for (int i = 0; i < EXECUTION_PRICE.length; i++) {
            List<TermValue> termValueList = new ArrayList<>();
            for (int j = 0; j < TERM_VALUES.length; j++) {
                TermValue termValue = new TermValue();
                termValue.setValue(TERM_VALUES[j]);
                termValue.setStatus(TermValue.Status.randomStatus());
                termValueList.add(termValue);
            }
            termValuesList.add(termValueList);
        }
        scrollablePanelAdapter.setTermValuesList(termValuesList);
    }


}
