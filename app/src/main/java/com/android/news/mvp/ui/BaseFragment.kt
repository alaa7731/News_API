package com.android.news.mvp.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.android.news.R
import com.android.news.dagger.components.FragComponents
import com.android.news.dagger.modules.FragModule
import com.android.news.mvp.presenter.BasePresenter
import com.android.news.mvp.presenter.BasePresenterListener


abstract class BaseFragment<T : BasePresenterListener> : Fragment(),
    BasePresenterListener {
    var fragComponents: FragComponents? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (fragComponents == null) setUpComponents()

    }

    override fun showMessageListener(message: String, listener: View.OnClickListener) {
        if (!isFinishing()) {
        }
    }

    private lateinit var parentView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentView = view

        mDialog = Dialog(context)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setContentView(R.layout.dialog_loading)
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)

    }


    private fun setUpComponents() {
        this.fragComponents = (activity as BaseActivity).activityComponents!!
            .plus(FragModule(this))
        injectComponents(fragComponents!!)
    }

    override fun showRetrySnackBar(messageId: Int) {
        if (!isFinishing()) {
            val mySnackbar = Snackbar.make(
                activity?.findViewById(android.R.id.content)!!,
                R.string.alert_no_internet_connection, Snackbar.LENGTH_LONG
            )
            mySnackbar.setAction(R.string.action_retry, object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mainRequest()
                }
            })
            mySnackbar.show()
        }
    }

    override fun showErrorSnackBar(message: String) {
        if (!isFinishing()) {
            val mySnackbar = Snackbar.make(
                activity?.findViewById(android.R.id.content)!!,
                message, Snackbar.LENGTH_LONG
            )

            mySnackbar.show()
        }
    }

    abstract fun mainRequest()

    protected fun isFinishing(): Boolean {
        return activity == null || !(!activity!!.isFinishing && isAdded)
    }

    protected fun isReadyForFragmentTransaction() = !isFinishing()
            && (activity as BaseActivity).mIsSafeToCommitTransactions


    abstract fun getPresenter(): BasePresenter<*, *>?

    override fun onDestroy() {
        super.onDestroy()
        getPresenter()?.destroy()
    }

    abstract fun injectComponents(fragComponents: FragComponents)

    open fun onBackPressed(): Boolean {
        if (childFragmentManager.fragments?.isEmpty() == false) {
            val fragment: Fragment? = childFragmentManager.fragments?.last()
            fragment?.let {
                if (childFragmentManager.popBackStackImmediate()) return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragmentsList = childFragmentManager.fragments
        for (i in fragmentsList.size - 1 downTo 0) {
            val fragment: Fragment? = fragmentsList[i]
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun showMessage(message: String) {
        if (!isFinishing()) {
//            DialogUtils.showMessage(activity!!, message)
        }
    }

    override fun showSuccessMessage(message: String) {

        if (!isFinishing()) {
        }
    }

    override fun showErrorMessage(message: String) {
        if (!isFinishing()) {
            showErrorSnackBar(message)
        }
    }

    override fun showWarningMessage(message: String) {

        if (!isFinishing()) {
        }
    }

    lateinit var mDialog: Dialog

    override fun showProgress() {

        if (mDialog != null && !isFinishing())
            mDialog.show()

    }

    override fun hideProgress() {

        if (mDialog != null && !isFinishing())
            mDialog.dismiss()
    }
}

fun Fragment.reloadData(tag: String) {
    ((childFragmentManager.findFragmentByTag(tag)) as? BaseFragment<*>)?.mainRequest()
}