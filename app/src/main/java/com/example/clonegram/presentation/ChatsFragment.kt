package com.example.clonegram.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.ChatsFragmentBinding
import com.example.clonegram.presentation.authication.StartCommunicationFragment
import com.example.clonegram.presentation.contacts.ContactsFragment
import com.example.clonegram.presentation.settings.SettingsFragment
import com.example.clonegram.utils.AUTH
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class ChatsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader
    private lateinit var drawerLayout: DrawerLayout

    private var _binding: ChatsFragmentBinding? = null
    private val binding: ChatsFragmentBinding
        get() = _binding ?: throw RuntimeException("ChatsFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatsFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onResume() {
        super.onResume()
        createHeader()
        createDrawer()
        drawerLayout = drawer.drawerLayout
    }

    private fun createHeader() {
        header = AccountHeaderBuilder()
            .withActivity(requireActivity())
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
            .withActivity(requireActivity())
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
                            startFragment(SettingsFragment.newInstance())
                        }
                        101 -> {
                            startFragment(ContactsFragment.newInstance())
                        }
                    }
                    return false
                }
            })
            .withAccountHeader(header)
            .build()
    }

    private fun disableDrawer(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun startFragment(fragment : Fragment){
        disableDrawer()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container,fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ChatsFragment()
    }
}