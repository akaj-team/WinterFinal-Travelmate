package vn.asiantech.travelmate.navigationdrawer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_setting.*
import vn.asiantech.travelmate.R
import vn.asiantech.travelmate.extensions.getInputText
import vn.asiantech.travelmate.popularcityactivity.PopularCityFragment
import vn.asiantech.travelmate.utils.ErrorUtil

class SettingFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangePassword -> {
                if (edtOldPassword.getInputText().isEmpty() || edtNewPassword.getInputText().isEmpty() || edtConfirmPassword.getInputText().isEmpty()) {
                    val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
                    val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                    ErrorUtil.showMessage(tvMessageError,getString(R.string.fragmentSettingTvError),slideUp,slideDown)
                }else{
                    // Todo: Use Firebase
                }
            }
            R.id.btnCancel -> {
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frameLayoutDrawer, PopularCityFragment())
                    commit()
                }
            }
            else -> {
                print(getString(R.string.noCaseSatisfied))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnChangePassword.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }
}
