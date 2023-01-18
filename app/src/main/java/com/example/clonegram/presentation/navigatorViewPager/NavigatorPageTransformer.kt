package com.example.clonegram.presentation.navigatorViewPager

import android.view.View
import androidx.viewpager.widget.ViewPager

class NavigatorPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            when{
                position <= -1 -> {
                    visibility = View.INVISIBLE
                }
                position > 0 && position <= 1 -> {
                    alpha = 1f
                    visibility = View.VISIBLE
                    translationX = 0f
                }
                position <= 0 -> {
                    alpha = 1.0F - Math.abs(position) / 2
                    translationX = -pageWidth * position / 1.3F
                    visibility = View.VISIBLE
                }
                else -> {
                    visibility = View.INVISIBLE
                }
            }
        }
    }


}