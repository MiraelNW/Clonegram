package com.example.clonegram.presentation.navigatorViewPager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager
import java.lang.Exception

class NavigatorViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        init()
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    private fun init() {
        setPageTransformer(false, NavigatorPageTransformer())
        overScrollMode = View.OVER_SCROLL_NEVER
        setDurationScroll(300)
    }

    private fun setDurationScroll(millis: Int) {
        try {
            val viewPager = ViewPager::class.java
            val scroller = viewPager.getDeclaredField("Scroller")
            scroller.isAccessible = true
            scroller.set(this, OwnScroller(context, millis))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class OwnScroller(context: Context, durationScroll: Int) :
        Scroller(context, DecelerateInterpolator(1.5f)) {
        private var durationScrollMillis = -1

        init {
            this.durationScrollMillis = durationScroll
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
            super.startScroll(startX, startY, dx, dy)
        }

    }
}