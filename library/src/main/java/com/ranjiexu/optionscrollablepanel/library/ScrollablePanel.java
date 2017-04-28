package com.ranjiexu.optionscrollablepanel.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;

/**
 * A flexible view for providing a limited window into a large data set,like a two-dimensional recyclerView.
 * but it will pin the itemView of first row and first column in their original location.
 */
public class ScrollablePanel extends FrameLayout {
    protected BetterRecyclerView recyclerView;
    protected BetterRecyclerView rightHeaderRecyclerView;
    protected BetterRecyclerView leftHeaderRecyclerView;
    protected PanelLineAdapter panelLineAdapter;
    protected PanelAdapter leftPanelAdapter;
    protected PanelAdapter rightPanelAdapter;
    protected LinearLayout leftRecyclerLinearLayout;
    protected LinearLayout rightRecyclerLinearLayout;
    protected LinearLayout centerRecyclerLinearLayout;

    private static final int CENTER_VIEW_WIDTH = 80;
    private int recyclerWidth;
    private int centerWidth;

    public ScrollablePanel(Context context, PanelAdapter leftPanelAdapter, PanelAdapter rightPanelAdapter) {
        super(context);
        this.leftPanelAdapter = leftPanelAdapter;
        this.rightPanelAdapter = rightPanelAdapter;
        initView();
    }

    public ScrollablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScrollablePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_scrollable_panel, this, true);
        recyclerView = (BetterRecyclerView) findViewById(R.id.recycler_content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rightHeaderRecyclerView = (BetterRecyclerView) findViewById(R.id.right_recycler_header_list);
        rightHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rightHeaderRecyclerView.setHasFixedSize(true);
        leftHeaderRecyclerView = (BetterRecyclerView) findViewById(R.id.left_recycler_header_list);
        leftHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        leftHeaderRecyclerView.setHasFixedSize(true);

        // 设置固定宽度，weight 设置无效
        leftRecyclerLinearLayout = (LinearLayout) findViewById(R.id.left_recycler_linear_layout);
        rightRecyclerLinearLayout = (LinearLayout) findViewById(R.id.right_recycler_linear_layout);
        centerRecyclerLinearLayout = (LinearLayout) findViewById(R.id.center_recycler_linear_layout);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        centerWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CENTER_VIEW_WIDTH, displayMetrics));
        Log.e("ranjiexu", "DisplayMetrics, densityDpi:" + displayMetrics.densityDpi);
        Log.e("ranjiexu", "centerWidth:" + centerWidth);
        recyclerWidth = (displayMetrics.widthPixels - centerWidth)/2;
        Log.e("ranjiexu", "recyclerWidth:" + recyclerWidth);
        ViewGroup.LayoutParams leftLayoutParams = leftRecyclerLinearLayout.getLayoutParams();
        leftLayoutParams.width = recyclerWidth;
        ViewGroup.LayoutParams rightLayoutParams = rightRecyclerLinearLayout.getLayoutParams();
        rightLayoutParams.width = recyclerWidth;
        ViewGroup.LayoutParams centerLayoutParams = centerRecyclerLinearLayout.getLayoutParams();
        centerLayoutParams.width = centerWidth;


        if (leftPanelAdapter != null && rightPanelAdapter != null) {
            panelLineAdapter = new PanelLineAdapter(leftPanelAdapter, rightPanelAdapter, recyclerView, leftHeaderRecyclerView, rightHeaderRecyclerView, null);
            recyclerView.setAdapter(panelLineAdapter);
        }
    }

    public void notifyDataSetChanged() {
        if (panelLineAdapter != null) {
//            setUpFirstItemView(leftPanelAdapter);
            panelLineAdapter.notifyDataChanged();
        }
    }

    public void notifyDataItemChanged(int position){
        if (panelLineAdapter != null){
            panelLineAdapter.notifyDataItemChanged(position);
        }
    }

    /**
     * @param leftPanelAdapter {@link PanelAdapter}
     */
    public void setPanelAdapter(PanelAdapter leftPanelAdapter, PanelAdapter rightPanelAdapter, String[] prices) {
        if (this.panelLineAdapter != null) {
            panelLineAdapter.setLeftPanelAdapter(leftPanelAdapter);
            panelLineAdapter.setRightPanelAdapter(rightPanelAdapter);
            panelLineAdapter.notifyDataSetChanged();
        } else {
            panelLineAdapter = new PanelLineAdapter(leftPanelAdapter, rightPanelAdapter, recyclerView, leftHeaderRecyclerView, rightHeaderRecyclerView, prices);
            recyclerView.setAdapter(panelLineAdapter);
        }
        this.leftPanelAdapter = leftPanelAdapter;
        this.rightPanelAdapter = rightPanelAdapter;
    }

    /**
     * Adapter used to bind dataSet to cell View that are displayed within every row of {@link ScrollablePanel}.
     */
    private static class PanelLineItemAdapter extends RecyclerView.Adapter {

        private PanelAdapter panelAdapter;
        private int row;

        public PanelLineItemAdapter(int row, PanelAdapter panelAdapter) {
            this.row = row;
            this.panelAdapter = panelAdapter;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return this.panelAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            this.panelAdapter.onBindViewHolder(holder, row, position);
        }

        @Override
        public int getItemViewType(int position) {
            return this.panelAdapter.getItemViewType(row, position);
        }


        @Override
        public int getItemCount() {
            return panelAdapter.getColumnCount();
        }

        public void setRow(int row) {
            this.row = row;
        }
    }


    /**
     * Adapter used to bind dataSet to views that are displayed within a{@link ScrollablePanel}.
     */
    private static class PanelLineAdapter extends RecyclerView.Adapter<PanelLineAdapter.ViewHolder> {

        private PanelAdapter leftPanelAdapter;
        private PanelAdapter rightPanelAdapter;
        private RecyclerView leftHeaderRecyclerView;
        private RecyclerView rightHeaderRecyclerView;
        private RecyclerView contentRV;
        private String[] prices;
        private HashSet<RecyclerView> leftObserverList = new HashSet<>();
        private int leftFirstPos = -1;
        private int leftFirstOffset = -1;
        private HashSet<RecyclerView> rightObserverList = new HashSet<>();
        private int rightFirstPos = -1;
        private int rightFirstOffset = -1;


        public PanelLineAdapter(PanelAdapter leftPanelAdapter, PanelAdapter rightPanelAdapter, RecyclerView contentRV, RecyclerView leftHeaderRecyclerView, RecyclerView rightHeaderRecyclerView, String [] prices) {
            this.leftPanelAdapter = leftPanelAdapter;
            this.rightPanelAdapter = rightPanelAdapter;
            this.leftHeaderRecyclerView = leftHeaderRecyclerView;
            this.rightHeaderRecyclerView = rightHeaderRecyclerView;
            this.contentRV = contentRV;
            this.prices = prices;
            initLeftRecyclerView(leftHeaderRecyclerView);
            initRightRecyclerView(rightHeaderRecyclerView);
            setUpLeftHeaderRecyclerView();
            setUpRightHeaderRecyclerView();
        }

        public void setLeftPanelAdapter(PanelAdapter leftPanelAdapter) {
            this.leftPanelAdapter = leftPanelAdapter;
            setUpLeftHeaderRecyclerView();
            setUpRightHeaderRecyclerView();
        }

        public void setRightPanelAdapter(PanelAdapter rightPanelAdapter) {
            this.rightPanelAdapter = rightPanelAdapter;
            setUpLeftHeaderRecyclerView();
            setUpRightHeaderRecyclerView();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return leftPanelAdapter.getRowCount() - 1;
        }

        @Override
        public PanelLineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PanelLineAdapter.ViewHolder viewHolder = new PanelLineAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_content_row, parent, false));
            initLeftRecyclerView(viewHolder.leftRecyclerView);
            initRightRecyclerView(viewHolder.rightRecyclerView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PanelLineItemAdapter leftLineItemAdapter = (PanelLineItemAdapter) holder.leftRecyclerView.getAdapter();
            PanelLineItemAdapter rightLineItemAdapter = (PanelLineItemAdapter) holder.rightRecyclerView.getAdapter();
            if (leftLineItemAdapter == null) {
                leftLineItemAdapter = new PanelLineItemAdapter(position + 1, leftPanelAdapter);
                holder.leftRecyclerView.setAdapter(leftLineItemAdapter);
            } else {
                leftLineItemAdapter.setRow(position + 1);
                leftLineItemAdapter.notifyDataSetChanged();
            }
            if (rightLineItemAdapter == null) {
                rightLineItemAdapter = new PanelLineItemAdapter(position + 1, rightPanelAdapter);
                holder.rightRecyclerView.setAdapter(rightLineItemAdapter);
            } else {
                rightLineItemAdapter.setRow(position + 1);
                rightLineItemAdapter.notifyDataSetChanged();
            }
            // 设置执行价
            holder.executionPriceView.setText((prices[position]));
        }


        public void notifyDataChanged() {
            setUpLeftHeaderRecyclerView();
            setUpRightHeaderRecyclerView();
            notifyDataSetChanged();
        }

        public void notifyDataItemChanged(int position) {
            Log.e("ranjiexu", "接收到单条更新，position=" + position);
            notifyItemChanged(position);
        }

        private void setUpLeftHeaderRecyclerView() {
            if (leftPanelAdapter != null) {
                if (leftHeaderRecyclerView.getAdapter() == null) {
                    PanelLineItemAdapter lineItemAdapter = new PanelLineItemAdapter(0, leftPanelAdapter);
                    leftHeaderRecyclerView.setAdapter(lineItemAdapter);
                } else {
                    leftHeaderRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }

        private void setUpRightHeaderRecyclerView() {
            if (rightPanelAdapter != null) {
                if (rightHeaderRecyclerView.getAdapter() == null) {
                    PanelLineItemAdapter lineItemAdapter = new PanelLineItemAdapter(0, rightPanelAdapter);
                    rightHeaderRecyclerView.setAdapter(lineItemAdapter);
                } else {
                    rightHeaderRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }

        public void initLeftRecyclerView(RecyclerView recyclerView) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            if (leftFirstPos == -1) {
                recyclerView.scrollToPosition(leftPanelAdapter.getColumnCount() - 1);
            }

            Log.e("ranjiexu", "leftPanelAdapter.getColumnCount():" + (leftPanelAdapter.getColumnCount() - 1));
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null && leftFirstPos > 0 && leftFirstOffset > 0) {
                layoutManager.scrollToPositionWithOffset(PanelLineAdapter.this.leftFirstPos + 1, PanelLineAdapter.this.leftFirstOffset);
            }
            leftObserverList.add(recyclerView);
            recyclerView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            for (RecyclerView rv : leftObserverList) {
                                rv.stopScroll();
                            }
                    }
                    return false;
                }
            });
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int centerWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CENTER_VIEW_WIDTH, recyclerView.getResources().getDisplayMetrics()));
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstPos = linearLayoutManager.findFirstVisibleItemPosition();
                    View firstVisibleItem = linearLayoutManager.getChildAt(0);
                    if (firstVisibleItem != null) {
                        int width = recyclerView.getWidth();
//                        Log.e("ranjiexu", "initLeftRecyclerView with:" + width);
                        int number = rightPanelAdapter.getColumnCount() - (width/centerWidth + 1);
//                        Log.e("ranjiexu", "initLeftRecyclerView rightPanelAdapter.getColumnCount()" + rightPanelAdapter.getColumnCount());
//                        Log.e("ranjiexu", "initLeftRecyclerView number:" + number);
                        int firstRight = linearLayoutManager.getDecoratedRight(firstVisibleItem);
                        for (RecyclerView rv : leftObserverList) {
                            if (recyclerView != rv) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                                if (layoutManager != null) {
                                    PanelLineAdapter.this.leftFirstPos = firstPos;
                                    PanelLineAdapter.this.leftFirstOffset = firstRight;
//                                    Log.e("ranjiexu", "leftFirstPos:" + leftFirstPos);
//                                    Log.e("ranjiexu", "leftFirstOffset:" + leftFirstOffset);
                                    layoutManager.scrollToPositionWithOffset(firstPos + 1, firstRight);
                                }
                            }
                        }
                        for (RecyclerView rv : rightObserverList) {
                            if (recyclerView != rv) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                                if (layoutManager != null) {
                                    int ops = number - firstPos;
                                    int offset = width - firstRight;
                                    if (offset > centerWidth){
                                        offset = width - firstRight - centerWidth;
                                        ops = ops - 1;
                                    }
                                    PanelLineAdapter.this.rightFirstPos = ops;
                                    PanelLineAdapter.this.rightFirstOffset = offset;
//                                    Log.e("ranjiexu", "rightFirstPos:" + rightFirstPos);
//                                    Log.e("ranjiexu", "rightFirstOffset:" + rightFirstOffset);
                                    layoutManager.scrollToPositionWithOffset(ops + 1, offset);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }

        public void initRightRecyclerView(RecyclerView recyclerView) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null && rightFirstPos > 0 && rightFirstOffset > 0) {
                layoutManager.scrollToPositionWithOffset(PanelLineAdapter.this.rightFirstPos + 1, PanelLineAdapter.this.rightFirstOffset);
            }
            rightObserverList.add(recyclerView);
            recyclerView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            for (RecyclerView rv : rightObserverList) {
                                rv.stopScroll();
                            }
                    }
                    return false;
                }
            });
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int centerWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CENTER_VIEW_WIDTH, recyclerView.getResources().getDisplayMetrics()));
                    int firstPos = linearLayoutManager.findFirstVisibleItemPosition();
                    View firstVisibleItem = linearLayoutManager.getChildAt(0);
                    if (firstVisibleItem != null) {
                        int width = recyclerView.getWidth();
//                        Log.e("ranjiexu", "initRightRecyclerView with:" + width);
                        int number = leftPanelAdapter.getColumnCount() - (width/centerWidth + 1);
//                        Log.e("ranjiexu", "initRightRecyclerView leftPanelAdapter.getColumnCount()" + leftPanelAdapter.getColumnCount());
//                        Log.e("ranjiexu", "initRightRecyclerView number:" + number);
                        int firstRight = linearLayoutManager.getDecoratedRight(firstVisibleItem);
                        for (RecyclerView rv : rightObserverList) {
                            if (recyclerView != rv) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                                if (layoutManager != null) {
                                    PanelLineAdapter.this.rightFirstPos = firstPos;
                                    PanelLineAdapter.this.rightFirstOffset = firstRight;
//                                    Log.e("ranjiexu", "rightFirstPos:" + rightFirstPos);
//                                    Log.e("ranjiexu", "rightFirstOffset:" + rightFirstOffset);
                                    layoutManager.scrollToPositionWithOffset(firstPos + 1, firstRight);
                                }
                            }
                        }

                        for (RecyclerView rv : leftObserverList) {
                            if (recyclerView != rv) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                                if (layoutManager != null) {
                                    int ops = number - firstPos;
                                    int offset = width - firstRight;
                                    if (offset > centerWidth){
                                        offset = width - firstRight - centerWidth;
                                        ops = ops - 1;
                                    }
                                    PanelLineAdapter.this.leftFirstPos = ops;
                                    PanelLineAdapter.this.leftFirstOffset = offset;
//                                    Log.e("ranjiexu", "leftFirstPos:" + leftFirstPos);
//                                    Log.e("ranjiexu", "leftFirstOffset:" + leftFirstOffset);
                                    layoutManager.scrollToPositionWithOffset(ops + 1, offset);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }

        private HashSet<RecyclerView> getLeftRecyclerViews() {
            HashSet<RecyclerView> recyclerViewHashSet = new HashSet<>();
            recyclerViewHashSet.add(leftHeaderRecyclerView);

            for (int i = 0; i < contentRV.getChildCount(); i++) {
                recyclerViewHashSet.add((RecyclerView) contentRV.getChildAt(i).findViewById(R.id.left_recycler_line_list));
            }
            return recyclerViewHashSet;
        }

        private HashSet<RecyclerView> getRightRecyclerViews() {
            HashSet<RecyclerView> recyclerViewHashSet = new HashSet<>();
            recyclerViewHashSet.add(rightHeaderRecyclerView);

            for (int i = 0; i < contentRV.getChildCount(); i++) {
                recyclerViewHashSet.add((RecyclerView) contentRV.getChildAt(i).findViewById(R.id.right_recycler_line_list));
            }
            return recyclerViewHashSet;
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            public BetterRecyclerView rightRecyclerView;
            public BetterRecyclerView leftRecyclerView;
            public TextView executionPriceView;
            public LinearLayout leftRecyclerLinearList;
            public LinearLayout centRecyclerLinearList;
            public LinearLayout rightRecyclerLinearList;

            public ViewHolder(View view) {
                super(view);
                this.leftRecyclerView = (BetterRecyclerView) view.findViewById(R.id.left_recycler_line_list);
                this.rightRecyclerView = (BetterRecyclerView) view.findViewById(R.id.right_recycler_line_list);
                this.executionPriceView = (TextView) view.findViewById(R.id.execution_price);
                this.rightRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
                this.leftRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

                // 设置固定宽度
                leftRecyclerLinearList = (LinearLayout) view.findViewById(R.id.left_recycler_linear_list);
                rightRecyclerLinearList = (LinearLayout) view.findViewById(R.id.right_recycler_linear_list);
                centRecyclerLinearList = (LinearLayout) view.findViewById(R.id.middle_column_item);
                DisplayMetrics displayMetrics = view.getResources().getDisplayMetrics();
                int centerWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CENTER_VIEW_WIDTH, displayMetrics));
                int recyclerWidth = (displayMetrics.widthPixels - centerWidth)/2;
                ViewGroup.LayoutParams leftLayoutParams = leftRecyclerLinearList.getLayoutParams();
                leftLayoutParams.width = recyclerWidth;
                ViewGroup.LayoutParams rightLayoutParams = rightRecyclerLinearList.getLayoutParams();
                rightLayoutParams.width = recyclerWidth;
                ViewGroup.LayoutParams centerLayoutParams = centRecyclerLinearList.getLayoutParams();
                centerLayoutParams.width = centerWidth;
            }
        }

    }


}
