package com.example.whynotdoctor.slider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.whynotdoctor.LoginSignupActivity
import com.example.whynotdoctor.MainActivity
import com.example.whynotdoctor.MedicineListActivity
import com.example.whynotdoctor.R


class IntroSliderActivity : AppCompatActivity() {

    private val fragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_intro_slider)

        val vpIntroSlider= findViewById<ViewPager2>(R.id.vpIntroSlider)
        val indicatorLayout = IndicatorLayout(this)
        val adapter = IntroSliderAdapter(this)
        vpIntroSlider.adapter = adapter

        fragmentList.addAll(listOf(
            Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)

        indicatorLayout.setIndicatorCount(adapter.itemCount)
        indicatorLayout.selectCurrentPosition(0)

        registerListeners()
    }

    private fun registerListeners() {
        val vpIntroSlider= findViewById<ViewPager2>(R.id.vpIntroSlider)
        val tvSkip= findViewById<View>(R.id.tvSkip)
        val tvNext= findViewById<View>(R.id.tvNext)

        val indicatorLayout = IndicatorLayout(this)


        vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                indicatorLayout.selectCurrentPosition(position)

                if (position < fragmentList.lastIndex) {
                    tvSkip.visibility = View.VISIBLE
//                    tvNext. = "Next"
                } else {
                    tvSkip.visibility = View.GONE
//                    tvNext.text = "Get Started"
                }
            }
        })

        tvSkip.setOnClickListener {
            startActivity(Intent(this, LoginSignupActivity::class.java))
            finish()
        }

        tvNext.setOnClickListener {
            val position = vpIntroSlider.currentItem

            if (position < fragmentList.lastIndex) {
                vpIntroSlider.currentItem = position + 1
            } else {
                startActivity(Intent(this, LoginSignupActivity::class.java))
                finish()
            }
        }
    }
}
