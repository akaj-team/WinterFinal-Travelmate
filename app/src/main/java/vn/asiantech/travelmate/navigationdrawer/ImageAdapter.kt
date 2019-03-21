package vn.asiantech.travelmate.navigationdrawer

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageAdapter(val context: Context, val listImage: List<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listImage.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imgSlide = ImageView(context)
        imgSlide.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(context).load(listImage[position]).into(imgSlide)
        container.addView(imgSlide, 0)
        return imgSlide
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }
}
