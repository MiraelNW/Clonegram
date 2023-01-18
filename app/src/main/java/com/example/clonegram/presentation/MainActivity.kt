package com.example.clonegram.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.clonegram.R
import com.example.clonegram.databinding.ActivityMainBinding
import com.example.clonegram.presentation.navigatorViewPager.NavigatorAdapter
import com.example.clonegram.presentation.navigatorViewPager.NavigatorViewPager
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class MainActivity : AppCompatActivity() {

    private lateinit var navigatorAdapter: NavigatorAdapter
    private lateinit var navigatorViewPager: NavigatorViewPager

    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader

    private lateinit var chatsFragment: ChatsFragment

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        createHeader()
        createDrawer()

        navigatorViewPager = binding.dataContainer
        navigatorAdapter = NavigatorAdapter(supportFragmentManager)
        navigatorAdapter.addFragment(ChatsFragment())
        navigatorViewPager.adapter = navigatorAdapter

        navigatorViewPager.offscreenPageLimit = 30

        var canRemoveFragment = false
        var sumPositionAndPositionOffset = 0.0f

        navigatorViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
                if (state == 0 && canRemoveFragment) {
                    while ((navigatorAdapter.getFragmentsCount() - 1) > navigatorViewPager.currentItem) {
                        navigatorAdapter.removeLastFragment()
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                canRemoveFragment = position + positionOffset < sumPositionAndPositionOffset
                sumPositionAndPositionOffset = position + positionOffset
            }

            override fun onPageSelected(position: Int) {
                if (position == 0){
                    binding.toolbar.visibility = View.VISIBLE
                }else{
                    binding.toolbar.visibility = View.GONE
                }
            }
        })
    }

    fun addFragment(fragment: Fragment) {
        navigatorAdapter.addFragment(fragment)
        navigatorViewPager.currentItem = navigatorViewPager.currentItem + 1
    }

    override fun onBackPressed() {
        if (navigatorAdapter.getFragmentsCount() > 1) {
            navigatorViewPager.setCurrentItem(navigatorViewPager.currentItem - 1, true)
        } else {
            finish()
        }
    }

    private fun createHeader() {
        header = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Buhstoevcky")
                    .withEmail("+79137974695")
            )
            .build()
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(binding.toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(100)
                    .withIcon(R.drawable.ic_menu_create_groups)
                    .withName("Создать группу"),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(101)
                    .withIcon(R.drawable.ic_menu_contacts)
                    .withName("Контакты"),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(102)
                    .withIcon(R.drawable.ic_menu_phone)
                    .withName("Звонки"),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(103)
                    .withIcon(R.drawable.ic_menu_create_channel)
                    .withName("Люди рядом"),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(104)
                    .withIcon(R.drawable.ic_menu_favorites)
                    .withName("Избранное"),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(105)
                    .withIcon(R.drawable.ic_menu_settings)
                    .withName("Настройки"),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(106)
                    .withIcon(R.drawable.ic_menu_invate)
                    .withName("Пригласить друзей"),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(107)
                    .withIcon(R.drawable.ic_menu_help)
                    .withName("Возможности Clonegram")
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (drawerItem.identifier.toInt()) {
                        105 -> {
                            addFragment(SettingsFragment.newInstance())
                        }
                        101 -> {
                            addFragment(ContactsFragment.newInstance())
                        }
                    }
                    return false
                }

            })
            .withAccountHeader(header)
            .build()
    }
}