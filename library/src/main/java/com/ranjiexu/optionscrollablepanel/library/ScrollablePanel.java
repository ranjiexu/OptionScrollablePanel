package com.ranjiexu.optionscrollablepanel.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashSet;

/**
 * A flexible view for providing a limited window into a large data set,like a two-dimensional recyclerView.
 * but it will pin the itemView of first row and first column in their original location.
 */
public class ScrollablePanel extends FrameLayout {
    protected RecyclerView recyclerView;
    protected RecyclerView rightHeaderRecyclerView;
    protected RecyclerView leftHeaderRecyclerView;
    protected PanelLineAdapter panelLineAdapter;
    protected PanelAdapter leftPanelAdapter;
    protected PanelAdapter rightPanelAdapter;
//    protected TextView middleItemView;

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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        middleItemView = (TextView) findViewById(R.id.middle_item);
        rightHeaderRecyclerView = (RecyclerView) findViewById(R.id.right_recycler_header_list);
        rightHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rightHeaderRecyclerView.setHasFixedSize(true);
        leftHeaderRecyclerView = (RecyclerView) findViewById(R.id.left_recycler_header_list);
        leftHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        leftHeaderRecyclerView.setHasFixedSize(true);
        if (leftPanelAdapter != null && rightPanelAdapter != null) {
            panelLineAdapter = new PanelLineAdapter(leftPanelAdapter, rightPanelAdapter, recyclerView, leftHeaderRecyclerView, rightHeaderRecyclerView);
            recyclerView.setAdapter(panelLineAdapter);
//            setUpFirstItemView(leftPanelAdapter);
        }
    }

//    private void setUpFirstItemView(PanelAdapter leftPanelAdapter) {
////        RecyclerView.ViewHolder viewHolder = leftPanelAdapter.onCreateViewHolder(middleItemView, leftPanelAdapter.getItemViewType(0, leftPanelAdapter.getColumnCount()/2));
////        leftPanelAdapter.onBindViewHolder(viewHolder, 0, leftPanelAdapter.getColumnCount()/2);
//        middleItemView.setText("执行价");
//    }

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
    public void setPanelAdapter(PanelAdapter leftPanelAdapter, PanelAdapter rightPanelAdapter) {
        if (this.panelLineAdapter != null) {
            panelLineAdapter.setLeftPanelAdapter(leftPanelAdapter);
            panelLineAdapter.setRightPanelAdapter(rightPanelAdapter);
            panelLineAdapter.notifyDataSetChanged();
        } else {
            panelLineAdapter = new PanelLineAdapter(leftPanelAdapter, rightPanelAdapter, recyclerView, leftHeaderRecyclerView, rightHeaderRecyclerView);
            recyclerView.setAdapter(panelLineAdapter);
        }
        this.leftPanelAdapter = leftPanelAdapter;
        this.rightPanelAdapter = rightPanelAdapter;
//        setUpFirstItemView(leftPanelAdapter);

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
        private HashSet<RecyclerView> leftObserverList = new HashSet<>();
        private int leftFirstPos = -1;
        private int leftFirstOffset = -1;
        private HashSet<RecyclerView> rightObserverList = new HashSet<>();
        private int rightFirstPos = -1;
        private int rightFirstOffset = -1;


        public PanelLineAdapter(PanelAdapter leftPanelAdapter, PanelAdapter rightPanelAdapter, RecyclerView contentRV, RecyclerView leftHeaderRecyclerView, RecyclerView rightHeaderRecyclerView) {
            this.leftPanelAdapter = leftPanelAdapter;
            this.rightPanelAdapter = rightPanelAdapter;
            this.leftHeaderRecyclerView = leftHeaderRecyclerView;
            this.rightHeaderRecyclerView = rightHeaderRecyclerView;
            this.contentRV = contentRV;
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
//            if (holder.middleColumnItemVH == null) {
//                RecyclerView.ViewHolder viewHolder = leftPanelAdapter.onCreateViewHolder(holder.middleColumnItemView, leftPanelAdapter.getItemViewType(position + 1, leftPanelAdapter.getColumnCount()/2));
//                holder.middleColumnItemVH = viewHolder;
//                leftPanelAdapter.onBindViewHolder(holder.middleColumnItemVH, position + 1, leftPanelAdapter.getColumnCount()/2);
//                holder.middleColumnItemView.setText(position + 1 + "");
//            } else {
//                leftPanelAdapter.onBindViewHolder(holder.middleColumnItemVH, position + 1, leftPanelAdapter.getColumnCount()/2);
//            }

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
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstPos = linearLayoutManager.findFirstVisibleItemPosition();
                    View firstVisibleItem = linearLayoutManager.getChildAt(0);
                    if (firstVisibleItem != null) {
                        int firstRight = linearLayoutManager.getDecoratedRight(firstVisibleItem);
                        for (RecyclerView rv : leftObserverList) {
                            if (recyclerView != rv) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                                if (layoutManager != null) {
                                    PanelLineAdapter.this.leftFirstPos = firstPos;
                                    PanelLineAdapter.this.leftFirstOffset = firstRight;
                                    layoutManager.scrollToPositionWithOffset(firstPos + 1, firstRight);
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
            // TODO
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstPos = linearLayoutManager.findFirstVisibleItemPosition();
                    View firstVisibleItem = linearLayoutManager.getChildAt(0);
                    if (firstVisibleItem != null) {
                        int firstRight = linearLayoutManager.getDecoratedRight(firstVisibleItem);
                        for (RecyclerView rv : rightObserverList) {
                            if (recyclerView != rv) {
                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                                if (layoutManager != null) {
                                    PanelLineAdapter.this.rightFirstPos = firstPos;
                                    PanelLineAdapter.this.rightFirstOffset = firstRight;
                                    layoutManager.scrollToPositionWithOffset(firstPos + 1, firstRight);
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
            public RecyclerView rightRecyclerView;
            public RecyclerView leftRecyclerView;
//            public TextView middleColumnItemView;
            public RecyclerView.ViewHolder middleColumnItemVH;

            public ViewHolder(View view) {
                super(view);
                this.leftRecyclerView = (RecyclerView) view.findViewById(R.id.left_recycler_line_list);
                this.rightRecyclerView = (RecyclerView) view.findViewById(R.id.right_recycler_line_list);
//                this.middleColumnItemView = (TextView) view.findViewById(R.id.middle_column_item);
                this.rightRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
                this.leftRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            }
        }

    }


}