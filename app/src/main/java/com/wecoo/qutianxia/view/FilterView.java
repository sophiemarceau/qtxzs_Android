package com.wecoo.qutianxia.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.FilterIndustryAdapter;
import com.wecoo.qutianxia.adapter.FilterOneAdapter;
import com.wecoo.qutianxia.models.FilterData;
import com.wecoo.qutianxia.models.FilterEntity;

/**
 * Created by mwl on 2016/10/25.
 * 排序和分类的列表
 */

public class FilterView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private TextView tvSorts, tvTypes;
    private ImageView ivSorts, ivTypes;
    private LinearLayout llSorts, llTypes;
    private ListView filterLV;
    private GridView filterLGrid;
    //    private LinearLayout /*headLayout,*/footLayout;
    private View viewMaskBg;

    private FilterData filterData;
    private boolean isShowing = false;
    private int panelHeight;
    private FilterEntity selectedSortEntity; // 被选择的排序项
    private FilterEntity selectedFilterEntity; // 被选择的分类项

    private FilterOneAdapter sortAdapter;
    private FilterIndustryAdapter filterAdapter;
    private int filterPosition = -1;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.filter_sort_typy_view, this);

        initView(view);
        initListener();
    }

    private void initView(View view) {

        llSorts = (LinearLayout) view.findViewById(R.id.filter_sort_linear);
        llTypes = (LinearLayout) view.findViewById(R.id.filter_types_linear);
//        headLayout = (LinearLayout) view.findViewById(R.id.filter_head_layout);
//        footLayout = (LinearLayout) view.findViewById(R.id.filter_foot_layout);
        tvSorts = (TextView) view.findViewById(R.id.filter_tv_sort);
        tvTypes = (TextView) view.findViewById(R.id.filter_tv_types);
        ivSorts = (ImageView) view.findViewById(R.id.filter_iv_sort_arrow);
        ivTypes = (ImageView) view.findViewById(R.id.filter_iv_types_arrow);
        filterLV = (ListView) view.findViewById(R.id.filter_listView);
        filterLGrid = (GridView) view.findViewById(R.id.filter_gridView);
        viewMaskBg = view.findViewById(R.id.filter_view_mask_bg);

        viewMaskBg.setVisibility(GONE);
//        footLayout.setVisibility(GONE);
    }

    public void setSortShow(String strSort) {
        if (!TextUtils.isEmpty(strSort)) {
            tvSorts.setText(strSort);
        }
    }

    public void setTypeShow(String strType) {
        if (!TextUtils.isEmpty(strType)) {
            tvTypes.setText(strType);
        }
    }

    private void initListener() {
        llSorts.setOnClickListener(this);
        llTypes.setOnClickListener(this);
        viewMaskBg.setOnClickListener(this);
//        footLayout.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_sort_linear:
                filterPosition = 0;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.filter_types_linear:
                filterPosition = 1;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.filter_view_mask_bg:
                hide();
                break;
        }

    }

    // 复位筛选的显示状态
    public void resetFilterStatus() {
        tvSorts.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
        ivSorts.setImageResource(R.mipmap.project_down_arrow);

        tvTypes.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
        ivTypes.setImageResource(R.mipmap.project_down_arrow);
    }

    // 复位所有的状态
    public void resetAllStatus() {
        hide();
    }

    // 显示筛选布局
    public void showFilterLayout(int position) {
        resetAllStatus();
        switch (position) {
            case 0:
                setSortAdapter();
                break;
            case 1:
                setFilterAdapter();
                break;
        }
        if (isShowing) return;
        show();
    }

    // 是否显示
    public boolean isShowing() {
        return isShowing;
    }

    // 设置排序数据
    private void setSortAdapter() {
        tvSorts.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
        ivSorts.setImageResource(R.mipmap.project_up_arrow);
        filterLV.setVisibility(View.VISIBLE);
        filterLGrid.setVisibility(View.GONE);
        sortAdapter = new FilterOneAdapter(mContext, filterData.getSorts());
        filterLV.setAdapter(sortAdapter);
        filterLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSortEntity = filterData.getSorts().get(position);
                sortAdapter.setSelectedEntity(selectedSortEntity);
                hide();
                if (onItemSortClickListener != null) {
                    onItemSortClickListener.onItemSortClick(selectedSortEntity);
                }
            }
        });
    }

    // 设置意向行业数据
    private void setFilterAdapter() {
        tvTypes.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
        ivTypes.setImageResource(R.mipmap.project_up_arrow);
        filterLV.setVisibility(View.GONE);
        filterLGrid.setVisibility(View.VISIBLE);
        filterAdapter = new FilterIndustryAdapter(mContext, filterData.getFilters());
        filterLGrid.setAdapter(filterAdapter);
        for (int i = 0; i < filterData.getFilters().size(); i++) {
            if (filterData.getFilters().get(i).isSelected()) {
                filterLGrid.setSelection(i);
            }
        }
        filterLGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterEntity = filterData.getFilters().get(position);
                filterAdapter.setSelectedEntity(selectedFilterEntity);
                hide();
                if (onItemTypeClickListener != null) {
                    onItemTypeClickListener.onItemTypeClick(selectedFilterEntity);
                }
            }
        });
    }

    // 动画显示
    private void show() {
        isShowing = true;
        viewMaskBg.setVisibility(View.VISIBLE);
        if (filterPosition == 1) {
            filterLGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    filterLGrid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    panelHeight = filterLGrid.getHeight();
                    ObjectAnimator.ofFloat(filterLGrid, "translationY", -panelHeight, 0).setDuration(200).start();
                }
            });
        } else {
            filterLV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    filterLV.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    panelHeight = filterLV.getHeight();
                    ObjectAnimator.ofFloat(filterLV, "translationY", -panelHeight, 0).setDuration(200).start();
                }
            });
        }
    }

    // 隐藏动画
    public void hide() {
        isShowing = false;
        resetFilterStatus();
        viewMaskBg.setVisibility(View.GONE);
        if (filterPosition == 1) {
            filterLGrid.setVisibility(View.GONE);
            ObjectAnimator.ofFloat(filterLGrid, "translationY", 0, -panelHeight).setDuration(200).start();
        } else {
            filterLV.setVisibility(View.GONE);
            ObjectAnimator.ofFloat(filterLV, "translationY", 0, -panelHeight).setDuration(200).start();
        }
    }

    // 设置筛选数据
    public void setFilterData(Context context, FilterData filterData) {
        this.mContext = context;
        this.filterData = filterData;
    }

    // 视图点击
    private OnFilterClickListener onFilterClickListener;

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }

    // 排序Item点击
    private OnItemSortClickListener onItemSortClickListener;

    public void setOnItemSortClickListener(OnItemSortClickListener onItemSortClickListener) {
        this.onItemSortClickListener = onItemSortClickListener;
    }

    public interface OnItemSortClickListener {
        void onItemSortClick(FilterEntity entity);
    }

    // 分类Item点击
    private OnItemTypeClickListener onItemTypeClickListener;

    public void setOnItemTypeClickListener(OnItemTypeClickListener onItemTypeClickListener) {
        this.onItemTypeClickListener = onItemTypeClickListener;
    }

    public interface OnItemTypeClickListener {
        void onItemTypeClick(FilterEntity entity);
    }
}
