package com.example.jingbin.cloudreader.ui.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.base.BaseFragment;
import com.example.jingbin.cloudreader.databinding.FragmentGankBinding;
import com.example.jingbin.cloudreader.http.rx.RxBus;
import com.example.jingbin.cloudreader.http.rx.RxCodeConstants;
import com.example.jingbin.cloudreader.ui.gank.child.AndroidFragment;
import com.example.jingbin.cloudreader.ui.gank.child.CustomFragment;
import com.example.jingbin.cloudreader.ui.gank.child.EverydayFragment;
import com.example.jingbin.cloudreader.ui.gank.child.WelfareFragment;
import com.example.jingbin.cloudreader.view.MyFragmentPagerAdapter;
import com.example.jingbin.cloudreader.viewmodel.menu.NoViewModel;

import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by jingbin on 16/11/21.
 * 展示干货的页面
 */
public class GankFragment extends BaseFragment<NoViewModel, FragmentGankBinding> {

    private ArrayList<String> mTitleList = new ArrayList<>(4);
    private ArrayList<Fragment> mFragments = new ArrayList<>(4);
    private boolean mIsFirst = true;
    private boolean mIsPrepared;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showContentView();
        mIsPrepared = true;
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        initFragmentList();
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments, mTitleList);
        bindingView.vpGank.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        // 左右预加载页面的个数
        bindingView.vpGank.setOffscreenPageLimit(3);
        bindingView.tabGank.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tabGank.setupWithViewPager(bindingView.vpGank);
        // item点击跳转
        initRxBus();
        mIsFirst = false;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_gank;
    }

    private void initFragmentList() {
        mTitleList.clear();
        mTitleList.add("每日推荐");
        mTitleList.add("福利");
        mTitleList.add("干货订制");
        mTitleList.add("大安卓");
        mFragments.add(new EverydayFragment());
        mFragments.add(new WelfareFragment());
        mFragments.add(new CustomFragment());
        mFragments.add(AndroidFragment.newInstance("Android"));
    }

    /**
     * 每日推荐点击"更多"跳转
     */
    private void initRxBus() {
        Subscription subscription = RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TYPE, Integer.class)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer == 0) {
                            bindingView.vpGank.setCurrentItem(3);
                        } else if (integer == 1) {
                            bindingView.vpGank.setCurrentItem(1);
                        } else if (integer == 2) {
                            bindingView.vpGank.setCurrentItem(2);
                        }
                    }
                });
        addSubscription(subscription);
    }
}
