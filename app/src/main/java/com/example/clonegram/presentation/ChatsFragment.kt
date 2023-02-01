package com.example.clonegram.presentation

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
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
import com.example.clonegram.utils.USER
import com.example.clonegram.utils.downloadAndSetImage
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

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
        createMenu()
    }

    private fun createMenu(){
        initLoader()
        createHeader()
        createDrawer()
        drawerLayout = drawer.drawerLayout
    }

    private fun createHeader() {
        val profileItem = ProfileDrawerItem()
            .withName(USER.name)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)
        header = AccountHeaderBuilder()
            .withActivity(requireActivity())
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                profileItem
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
                    .withIdentifier(IDENTIFIER_CREATE_GROUP)
                    .withIcon(R.drawable.ic_menu_create_groups)
                    .withName(CREATE_GROUP),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(IDENTIFIER_CONTACTS)
                    .withIcon(R.drawable.ic_menu_contacts)
                    .withName(CONTACTS),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(IDENTIFIER_CALLINGS)
                    .withIcon(R.drawable.ic_menu_phone)
                    .withName(CALLINGS),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(IDENTIFIER_PEOPLE_NEARBY)
                    .withIcon(R.drawable.ic_menu_create_channel)
                    .withName(PEOPLE_NEARBY),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(IDENTIFIER_FAVOURITES)
                    .withIcon(R.drawable.ic_menu_favorites)
                    .withName(FAVOURITES),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(IDENTIFIER_SETTINGS)
                    .withIcon(R.drawable.ic_menu_settings)
                    .withName(SETTINGS),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(IDENTIFIER_INVITE_FRIENDS)
                    .withIcon(R.drawable.ic_menu_invate)
                    .withName(INVITE_FRIENDS),
                PrimaryDrawerItem()
                    .withIconTintingEnabled(true)
                    .withIdentifier(IDENTIFIER_CLONEGRAM_OPPORTUNITIES)
                    .withIcon(R.drawable.ic_menu_help)
                    .withName(CLONEGRAM_OPPORTUNITIES)
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

    private fun initLoader(){
        DrawerImageLoader.init(object : AbstractDrawerImageLoader(){
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.downloadAndSetImage(uri.toString())
            }
        })
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

        private const val IDENTIFIER_CREATE_GROUP = 100L
        private const val CREATE_GROUP = "Создать группу"

        private const val IDENTIFIER_CONTACTS = 101L
        private const val CONTACTS = "Контакты"

        private const val IDENTIFIER_CALLINGS = 102L
        private const val CALLINGS = "Звонки"

        private const val IDENTIFIER_PEOPLE_NEARBY = 103L
        private const val PEOPLE_NEARBY = "Люди рядом"

        private const val IDENTIFIER_FAVOURITES = 104L
        private const val FAVOURITES = "Избранное"

        private const val IDENTIFIER_SETTINGS = 105L
        private const val SETTINGS = "Настройки"

        private const val IDENTIFIER_INVITE_FRIENDS = 106L
        private const val INVITE_FRIENDS = "Пригласить друзей"

        private const val IDENTIFIER_CLONEGRAM_OPPORTUNITIES = 107L
        private const val CLONEGRAM_OPPORTUNITIES = "Возможности Clonegram"

    }
}