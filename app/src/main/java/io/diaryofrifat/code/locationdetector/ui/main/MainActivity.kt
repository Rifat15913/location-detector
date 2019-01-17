package io.diaryofrifat.code.locationdetector.ui.main

import android.view.View
import io.diaryofrifat.code.locationdetector.R
import io.diaryofrifat.code.locationdetector.databinding.ActivityMainBinding
import io.diaryofrifat.code.locationdetector.ui.base.component.BaseActivity


class MainActivity : BaseActivity<MainMvpView, MainPresenter>(), MainMvpView {
    /**
     * Fields
     * */
    private lateinit var mBinding: ActivityMainBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun getActivityPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun shouldShowBackIconAtToolbar(): Boolean {
        return false
    }

    override fun startUI() {
        mBinding = viewDataBinding as ActivityMainBinding
    }

    override fun stopUI() {

    }

    override fun onClick(view: View) {
        when (view.id) {

        }
    }
}
