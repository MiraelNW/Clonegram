package com.example.clonegram.presentation.singleChat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.SingleChatFragmentBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.singleChat.singleChatAdapter.SingleChatAdapter
import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.ViewFactory
import com.example.clonegram.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleChatFragment : Fragment() {

    private lateinit var refMessages: DatabaseReference
    private lateinit var messageListener: ValueEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SingleChatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private var countMessages = 20
    private var isNeedSmoothScroll = true
    private var isScrolling = false
    private var isRecordAudioPermissionGranted = false


    private var _binding: SingleChatFragmentBinding? = null
    private val binding: SingleChatFragmentBinding
        get() = _binding ?: throw RuntimeException("SingleChatFragmentBinding is null")

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: SingleChatViewModel

    private val args by navArgs<SingleChatFragmentArgs>()
    private val user by lazy {
        args.userInfo
    }

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SingleChatFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startRecordAudioPermissionRequest()
        viewModel = ViewModelProvider(this, factory)[SingleChatViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        if (user.id == UID) {
            initFields(USER)
        } else {
            viewModel.initReceiverUseCase(user.id) {
                initFields(it)
            }
        }
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // _binding = null
        viewModel.releaseRecorder()
        adapter.destroy()
        binding.rvSwipeRefresh.isEnabled = false
        refMessages.removeEventListener(messageListener)
        refUser.removeEventListener(messageListener)
    }

    private fun initRecycler() {

        recyclerView = binding.chatRecycleView
        adapter = SingleChatAdapter()
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        refMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(user.id)
        messageListener = AppEventListener {
            it.children.map { snapshot ->
                val message = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                if (isNeedSmoothScroll) {
                    adapter.addItemToBottom(ViewFactory.getView(message)) {
                        recyclerView.smoothScrollToPosition(adapter.itemCount)
                    }
                } else {
                    adapter.addItemToTop(ViewFactory.getView(message)) {

                        binding.rvSwipeRefresh.isRefreshing = false
                        binding.rvSwipeRefresh.isEnabled = false
                    }
                }
            }
        }


        refMessages.limitToLast(countMessages).addValueEventListener(messageListener)

        recyclerView.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.postDelayed(
                    { recyclerView.smoothScrollToPosition(adapter.itemCount) },
                    50
                )
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling && dy < 0 && layoutManager.findFirstVisibleItemPosition() <= 8) {
                    updateMessageList()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })
        binding.rvSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark)
        binding.rvSwipeRefresh.setOnRefreshListener { updateMessageList() }

    }

    private fun updateMessageList() {
        isScrolling = false
        isNeedSmoothScroll = false
        countMessages += 10
        refMessages.limitToLast(countMessages).addValueEventListener(messageListener)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFields(receiver: UserInfo) {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetChoice)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        setVisibilityMode()
        layoutManager = LinearLayoutManager(requireContext())

        if (user.id == UID) {
            binding.chatUsername.text = "Favourites"
            Picasso.get().load(R.drawable.ic_favourite).fit().into(binding.chatUserPhoto)
            binding.chatUsername.textSize = 24f
            binding.userState.visibility = View.GONE
        } else {
            binding.chatUserPhoto.downloadAndSetImage(receiver.photoUrl)
            binding.chatUsername.text = receiver.name
            binding.userState.text = receiver.state
        }

        binding.chatInputMessage.isCursorVisible = true

        CoroutineScope(Dispatchers.IO).launch {
            binding.btnVoiceMessage.setOnTouchListener { view, motionEvent ->
                if (isRecordAudioPermissionGranted) {
                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        binding.chatInputMessage.setText("Recording...")
                        binding.btnVoiceMessage.setColorFilter(
                            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
                        )
                        viewModel.startRecord(user.id)
                    } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                        binding.chatInputMessage.setText("")
                        binding.btnVoiceMessage.colorFilter = null
                        viewModel.stopRecord(user.id)
                    }

                }
                true
            }
        }

        binding.btnAttach.setOnClickListener { attach() }

        binding.btnSendMessage.setOnClickListener {
            viewModel.saveToChatList(user.id, TYPE_SINGLE_CHAT)
            recyclerView.smoothScrollToPosition(adapter.itemCount)
            val message = binding.chatInputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message, user.id, TYPE_TEXT) {
                    binding.chatInputMessage.setText("")
                }
            }

        }
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun attach() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.btnAttachFile.setOnClickListener { attachFile() }
        binding.btnAttachImage.setOnClickListener { attachImage() }

    }

    private fun attachFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(300, 400)
            .start(requireActivity(), this)
    }

    private fun setVisibilityMode() {
        binding.chatInputMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.chatInputMessage.isCursorVisible = true
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                val string = str.toString()
                if (string.isEmpty() || string == "Recording...") {
                    binding.btnSendMessage.visibility = View.GONE
                    binding.btnAttachFile.visibility = View.VISIBLE
                    binding.btnVoiceMessage.visibility = View.VISIBLE
                } else {
                    binding.btnSendMessage.visibility = View.VISIBLE
                    binding.btnAttach.visibility = View.GONE
                    binding.btnVoiceMessage.visibility = View.GONE
                }
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val uri = CropImage.getActivityResult(data).uri
                viewModel.insertImageUseCase(uri)
                val messageKey =
                    REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(user.id)
                        .push().key.toString()
                viewModel.uploadFileUseCase(uri, messageKey, user.id, TYPE_IMAGE)
                isNeedSmoothScroll = true
            }
            REQUEST_CODE -> {
                val uri = data.data
                val messageKey =
                    REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(user.id)
                        .push().key.toString()
                uri?.let {
                    val filename = getFilenameFromUri(uri)
                    viewModel.uploadFileUseCase(uri, messageKey, user.id, TYPE_FILE, filename)
                }
                isNeedSmoothScroll = true
            }
        }

    }

    private fun getFilenameFromUri(uri: Uri): String {
        var filename = ""
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToNext()) {
                filename =
                    cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        } finally {
            cursor?.close()
            return filename
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            when (it.key) {
                android.Manifest.permission.RECORD_AUDIO -> {
                    if (it.value) {
                        isRecordAudioPermissionGranted = true
                    } else {
                        showToast("We cant record audio,permission denied")
                    }
                }
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    if (it.value) {
                        AppPermissions.isWriteStoragePermissionGranted = true
                    } else {
                        showToast("We cant write storage,permission denied")
                    }
                }
                android.Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    if (it.value) {
                        AppPermissions.isReadStoragePermissionGranted = true
                    } else {
                        showToast("We cant write storage,permission denied")
                    }
                }
            }
        }

    }

    private fun startRecordAudioPermissionRequest() {
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    companion object {
        private const val REQUEST_CODE = 100
    }

}