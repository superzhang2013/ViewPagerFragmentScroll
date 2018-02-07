package com.zh.viewpagerfragmentscroll;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private VideoView mVideoView;

    private ViewPager mViewPager;

    private ViewPagerAdapter mViewPagerAdapter;

    private List<ChildItemBean> mChildItemBeanList = new ArrayList<>();

    private int viewPagerMarginTop = 0;

    private float sumPositionAndPositionOffset;

    private int mSelectPage = -1;   //初始话本次选中页面

    private int mCurrentSelectKnowledge = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        initData();
        initListener();
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            ChildItemBean childItemBean = new ChildItemBean();
            childItemBean.childId = i;
            childItemBean.childItemString = "第 " + i + " 个fragment页面";
            childItemBean.isHaveVideoView = (i % 2 == 0);
            mChildItemBeanList.add(childItemBean);
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private boolean isCurrentTaskHaveVideo(int position) {
        return mChildItemBeanList.get(position).isHaveVideoView;
    }

    private void initListener(){
        viewPagerMarginTop = dip2px(this, 210);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                YLog.info(this, " on page scroll page position is ---------------------" + position);
                if (position + positionOffset > sumPositionAndPositionOffset) {
                    //向左滑
//                    YLog.info(this, " on page scroll page position is left <------------------" + position);
                    if (position == mSelectPage) {
                        if (isCurrentTaskHaveVideo(position)) {
                            mVideoView.setVisibility(View.VISIBLE);
                        } else {
                            mVideoView.setVisibility(View.GONE);
                        }
                        return;
                    }
                    if (isCurrentTaskHaveVideo(position)) {
                        //当前页存在
                        mVideoView.setVisibility(View.VISIBLE);
                        if (position + 1 < mChildItemBeanList.size() && isCurrentTaskHaveVideo(position + 1)) {
                            mVideoView.setVisibility(View.VISIBLE);
                        } else {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mVideoView.getLayoutParams();
                            params.height = (int) (viewPagerMarginTop * (1 - positionOffset));
                            mVideoView.setLayoutParams(params);
                            if (positionOffset > 0.8) {
                                mVideoView.setVisibility(View.GONE);
                            } else {
                                mVideoView.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        //当前页不存在
                        if (position + 1 < mChildItemBeanList.size() && isCurrentTaskHaveVideo(position + 1)) {
                            //下一页存在
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mVideoView.getLayoutParams();
                            params.height = (int) (viewPagerMarginTop * positionOffset);
                            if (positionOffset > 0.8) {
                                params.height = viewPagerMarginTop;
                            }
                            mVideoView.setLayoutParams(params);
                            mVideoView.setVisibility(View.VISIBLE);
                        } else {
                            //下一页不存在
                            mVideoView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    //向右滑
                    if (position == mSelectPage) {
                        if (isCurrentTaskHaveVideo(position)) {
                            mVideoView.setVisibility(View.VISIBLE);
                        } else {
//                            mVideoView.release();
                            mVideoView.setVisibility(View.GONE);
                        }
                        return;
                    }
                    if (position + 1 < mChildItemBeanList.size() && isCurrentTaskHaveVideo(position + 1)) {
                        //当前页存在
                        mVideoView.setVisibility(View.VISIBLE);
                        if (isCurrentTaskHaveVideo(position)) {
                            mVideoView.setVisibility(View.VISIBLE);
                        } else {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mVideoView.getLayoutParams();
                            params.height = (int) (viewPagerMarginTop * positionOffset);
                            mVideoView.setLayoutParams(params);
                            if (positionOffset < 0.2) {
                                mVideoView.setVisibility(View.GONE);
                            } else {
                                mVideoView.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        //当前页不存在
                        if (isCurrentTaskHaveVideo(position)) {
                            //下一页存在
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mVideoView.getLayoutParams();
                            params.height = (int) (viewPagerMarginTop * (1 - positionOffset));
                            if (positionOffset < 0.2) {
                                params.height = viewPagerMarginTop;
                            }
                            mVideoView.setLayoutParams(params);
                            mVideoView.setVisibility(View.VISIBLE);
                        } else {
                            //下一页不存在
                            mVideoView.setVisibility(View.GONE);
                        }
                    }
                }
                sumPositionAndPositionOffset = position + positionOffset;

            }

            @Override
            public void onPageSelected(int position) {
//                YLog.info(this, " on page scroll select position is ================ " + position);
                mSelectPage = position;
                if (isCurrentTaskHaveVideo(position)) {
                    mVideoView.post(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mVideoView.getLayoutParams();
                            params.height = viewPagerMarginTop;
                            mVideoView.setLayoutParams(params);
                            mVideoView.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    mVideoView.setVisibility(View.GONE);
                }
                mCurrentSelectKnowledge = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    //开始滑动
                    mSelectPage = -1;
                }
            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ChildFragment childFragment = (ChildFragment) super.instantiateItem(container, position);
            childFragment.setChildItemBean(mChildItemBeanList.get(position));
            return childFragment;
        }

        @Override
        public Fragment getItem(int position) {
            return ChildFragment.newInstance();
        }

        @Override
        public int getCount() {
            return mChildItemBeanList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
