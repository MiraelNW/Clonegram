package com.example.clonegram.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clonegram.R
import com.example.clonegram.databinding.ChatsFragmentBinding
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class ChatsFragment : Fragment() {

    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader

    private var _binding: ChatsFragmentBinding? = null
    private val binding: ChatsFragmentBinding
        get() = _binding ?: throw RuntimeException("ChatsFragmentBinding is null")

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
                            findNavController().navigate(R.id.action_chatsFragment_to_settingsFragment)
                        }
                        101 -> {
                            findNavController().navigate(R.id.action_chatsFragment_to_contactsFragment)
                        }
                    }
                    return false
                }

            })
            .withAccountHeader(header)
            .build()
    }

    companion object {

    }
}