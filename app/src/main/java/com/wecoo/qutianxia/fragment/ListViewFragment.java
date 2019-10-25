package com.wecoo.qutianxia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ProjectInfoActivity;
import com.wecoo.qutianxia.adapter.SilkListAdapter;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.utils.AttachUtil;

/**
 * Created by mwl.
 * Description : 锦囊
 */
public class ListViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEvent(getActivity(), "ListViewFragment");
    }

    private void initView() {
        ListView listView = (ListView) getView().findViewById(R.id.fragment_tab_list);
        SilkListAdapter adapter = new SilkListAdapter(getActivity(), ModelManager.getInstance().getJingNs());
        listView.setAdapter(adapter);

        // attach top
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (AttachUtil.isAdapterViewAttach(view)) {
                    ProjectInfoActivity.onEvent(true);
                } else {
                    ProjectInfoActivity.onEvent(false);
                }
            }
        });
    }
}
