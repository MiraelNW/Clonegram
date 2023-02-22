package com.example.clonegram.presentation.chats

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.ChatsFragmentBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.chats.chatsRecyclerView.SingleChatsAdapter
import com.example.clonegram.utils.*
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
import javax.inject.Inject

class ChatsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel : ChatsViewModel


    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SingleChatsAdapter

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
        viewModel = ViewModelProvider(this,factory)[ChatsViewModel::class.java]
        startLocationPermissionRequest()
        UserState.updateState(UserState.ONLINE)
        createMenu()
    }

    override fun onResume() {
        super.onResume()
        updateMenu()
        initRecycleView()
    }

    private fun initRecycleView() {
        recyclerView = binding.singleChatRecyclerView
        adapter = SingleChatsAdapter()
        recyclerView.adapter = adapter
        getChatsList().observe(this) { list ->
            adapter.submitList(list)
            adapter.onChatClickListener = object : SingleChatsAdapter.OnChatClickListener {
                override fun onChatClick(user: UserInfo) {
                    findNavController().navigate(
                        ChatsFragmentDirections.actionChatsFragmentToSingleChatFragment(
                            user
                        )
                    )
                }
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            requestContacts()
        } else {
            showToast("we cant see contacts,permission denied")
            requireActivity().finish()
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    private fun requestContacts(){
        val arrayContacts = arrayListOf<UserInfo>()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (cursor.moveToNext()) {
                val name = cursor
                    .getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val id = cursor
                    .getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                var phone = cursor
                    .getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val number = phone.replace(Regex("-"), "").replace(Regex(" "), "")
                phone = number.substring(0, 2) + " (" +
                        number.substring(2, 5) + ") " +
                        number.substring(5, 8) + " " +
                        number.substring(8, 10) + " " +
                        number.substring(10, 12)
                if (phone != USER.phone) {
                    val contact = UserInfo(
                        id = id,
                        name = name,
                        phone = phone
                    )

                    arrayContacts.add(contact)
                }
            }
        }
        viewModel.insertContactListUseCase(arrayContacts)
        cursor?.close()
    }

    private fun updateMenu() {
        val profileItem = ProfileDrawerItem()
            .withName(USER.name)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)
        header.updateProfile(profileItem)
    }

    private fun createMenu() {
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
                    loadFragment(drawerItem.identifier.toInt())
                    disableDrawer()
                    return false
                }
            })
            .withAccountHeader(header)
            .build()
    }

    private fun loadFragment(position: Int) {
        when (position) {
            IDENTIFIER_SETTINGS.toInt() -> {
                findNavController().navigate(R.id.action_chatsFragment_to_settingsFragment)
            }
            IDENTIFIER_CONTACTS.toInt() -> {
                findNavController().navigate(R.id.action_chatsFragment_to_contactsFragment)
            }
            IDENTIFIER_FAVOURITES.toInt() -> {
                findNavController().navigate(
                    ChatsFragmentDirections.actionChatsFragmentToSingleChatFragment(
                        USER
                    )
                )
            }
            else -> {
                findNavController().navigate(R.id.action_chatsFragment_to_settingsFragment)
            }
        }
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }

    private fun disableDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
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