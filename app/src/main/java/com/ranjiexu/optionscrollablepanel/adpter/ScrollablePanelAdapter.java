package com.ranjiexu.optionscrollablepanel.adpter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ranjiexu.optionscrollablepanel.R;
import com.ranjiexu.optionscrollablepanel.bean.TermName;
import com.ranjiexu.optionscrollablepanel.bean.TermValue;
import com.ranjiexu.optionscrollablepanel.bean.ExecutionPrice;
import com.ranjiexu.optionscrollablepanel.library.PanelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
*  Created by devpc-05 on 2017/4/11.
**/
public class ScrollablePanelAdapter extends PanelAdapter {
    private static final int TITLE_TYPE = 4;
    private static final int EXECUTION_PRICE_TYPE = 0;
    private static final int TERM_NAME_TYPE = 1;
    private static final int TERM_VALUE_TYPE = 2;

    private List<ExecutionPrice> executionPriceList = new ArrayList<>();
    private List<TermName> termNameList = new ArrayList<>();
    private List<List<TermValue>> termValuesList = new ArrayList<>();


    @Override
    public int getRowCount() {
        return executionPriceList.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return termNameList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case TERM_NAME_TYPE:
                setTermNameView(column, (TermNameViewHolder) holder);
                break;
            case EXECUTION_PRICE_TYPE:
                setPriceView(row, (ExecutionPriceViewHolder) holder);
                break;
            case TERM_VALUE_TYPE:
                setTermValueView(row, column, (TermValueViewHolder) holder);
                break;
            case TITLE_TYPE:
                break;
            default:
                setTermValueView(row, column, (TermValueViewHolder) holder);
        }
    }

    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return EXECUTION_PRICE_TYPE;
        }
        if (row == 0) {
            return TERM_NAME_TYPE;
        }
        return TERM_VALUE_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TERM_NAME_TYPE:
                return new TermNameViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_term_name, parent, false));
            case EXECUTION_PRICE_TYPE:
                return new ExecutionPriceViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_execution_price, parent, false));
            case TERM_VALUE_TYPE:
                return new TermValueViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_term_value, parent, false));
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_title, parent, false));
            default:
                break;
        }
        return new TermValueViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_term_value, parent, false));
    }


    private void setTermNameView(int pos, TermNameViewHolder viewHolder) {
        TermName dateInfo = termNameList.get(pos - 1);
        if (dateInfo != null && pos > 0) {
            viewHolder.termNameTextView.setText(dateInfo.getName());
        }
    }

    private void setPriceView(int pos, ExecutionPriceViewHolder viewHolder) {
        ExecutionPrice executionPrice = executionPriceList.get(pos - 1);
        if (executionPrice != null && pos > 0) {
            viewHolder.executionPriceTextView.setText(executionPrice.getExcutionPrice());
        }
    }

    private void setTermValueView(final int row, final int column, TermValueViewHolder viewHolder) {
        final TermValue termValue = termValuesList.get(row - 1).get(column - 1);
        if (termValue != null) {
            if (termValue.getStatus() == TermValue.Status.GRAY) {
                viewHolder.termValueView.setText(termValue.getValue());
                viewHolder.termValueView.setTextColor(Color.GRAY);
            } else if (termValue.getStatus() == TermValue.Status.RED) {
                viewHolder.termValueView.setText(termValue.getValue());
                viewHolder.termValueView.setTextColor(Color.RED);
            } else if (termValue.getStatus() == TermValue.Status.GREEN) {
                viewHolder.termValueView.setText(termValue.getValue());
                viewHolder.termValueView.setTextColor(Color.GREEN);
            }
            viewHolder.itemView.setClickable(true);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "name:" + termValue.getValue(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private static class TermNameViewHolder extends RecyclerView.ViewHolder {
        public TextView termNameTextView;

        public TermNameViewHolder(View itemView) {
            super(itemView);
            this.termNameTextView = (TextView) itemView.findViewById(R.id.term_name);
        }

    }

    private static class ExecutionPriceViewHolder extends RecyclerView.ViewHolder {
        public TextView executionPriceTextView;

        public ExecutionPriceViewHolder(View view) {
            super(view);
            this.executionPriceTextView = (TextView) view.findViewById(R.id.execution_price);
        }
    }

    private static class TermValueViewHolder extends RecyclerView.ViewHolder {
        public TextView termValueView;
        public View view;

        public TermValueViewHolder(View view) {
            super(view);
            this.view = view;
            this.termValueView = (TextView) view.findViewById(R.id.term_value);
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(View view) {
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.title);
        }
    }


    public void setExecutionPriceList(List<ExecutionPrice> executionPriceList) {
        this.executionPriceList = executionPriceList;
    }

    public void setTermNameList(List<TermName> termNameList) {
        this.termNameList = termNameList;
    }

    public void setTermValuesList(List<List<TermValue>> termValuesList) {
        this.termValuesList = termValuesList;
    }
}
