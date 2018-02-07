package com.zh.viewpagerfragmentscroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhanghong on 2018/2/7.
 */

public class ChildFragment extends Fragment {

    private ChildItemBean mChildItemBean;

    private TextView mChildTextView;

    public void setChildItemBean(ChildItemBean childItemBean) {
        mChildItemBean = childItemBean;
    }

    public static ChildFragment newInstance() {
        return new ChildFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_frg_item_layout, container, false);
        mChildTextView = (TextView) view.findViewById(R.id.child_title_view);

        if (mChildItemBean != null) {
            mChildTextView.setText(mChildItemBean.childItemString);
        }

        return view;
    }
}
