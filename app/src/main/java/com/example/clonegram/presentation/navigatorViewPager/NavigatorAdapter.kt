package com.example.clonegram.presentation.navigatorViewPager

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class NavigatorAdapter(val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val listOfFragments: ArrayList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment) {
        listOfFragments.add(fragment)
        notifyDataSetChanged()
    }

    fun removeLastFragment() {
        listOfFragments.removeAt(listOfFragments.size - 1)
        notifyDataSetChanged()
    }

    fun getFragmentsCount(): Int {
        return listOfFragments.size
    }

    override fun getItemPosition(`object`: Any): Int {
        val index = listOfFragments.indexOf(`object`)
        return if (index == -1) {
            PagerAdapter.POSITION_NONE
        } else {
            index
        }
    }

    override fun getCount(): Int {
        return listOfFragments.size
    }

    override fun getItem(position: Int): Fragment {
        return listOfFragments[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}