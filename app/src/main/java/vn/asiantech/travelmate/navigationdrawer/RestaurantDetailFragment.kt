package vn.asiantech.travelmate.navigationdrawer

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_restaurant_detail.*
import vn.asiantech.travelmate.R
import java.util.*

class RestaurantDetailFragment : Fragment() {
    private var param: Int = 0
    private var listImage: MutableList<String> = mutableListOf()
    private var imageAdapter: ImageAdapter? = null

    private var currentPage = 0
    private val DELAY_MS: Long = 3000
    private val PERIOD_MS: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param = arguments?.getInt(ARG_PARAM) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listImage.apply {
            add("https://images.pexels.com/photos/326055/pexels-photo-326055.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940")
            add("https://images.pexels.com/photos/17743/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500")
            add("https://images.pexels.com/photos/8370/nature-flower.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500")
        }
        imageAdapter = context?.let { ImageAdapter(it, listImage) }
        viewPagerSlideImage.adapter = imageAdapter
        //viewPagerSlideImage.setPageTransformer(true, ZoomOutSlideTransformer())
        circleIndicator.setViewPager(viewPagerSlideImage)
        val handler = Handler()
        val update = Runnable {
            if (currentPage == listImage.size) {
                currentPage = 0
            }
            viewPagerSlideImage.setCurrentItem(currentPage++, true)
        }
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    companion object {
        private val ARG_PARAM = "param"
        fun newInstance(position: Int): RestaurantDetailFragment {
            val fragment = RestaurantDetailFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM, position)
            fragment.arguments = args
            return fragment
        }
    }
}
