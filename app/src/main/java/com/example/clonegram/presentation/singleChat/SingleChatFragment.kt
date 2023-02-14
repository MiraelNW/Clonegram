package com.example.clonegram.presentation.singleChat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.R
import com.example.clonegram.databinding.SingleChatFragmentBinding
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.singleChat.singleChatAdapter.SingleChatAdapter
import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.ViewFactory
import com.example.clonegram.utils.*
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleChatFragment : Fragment() {


    private lateinit var receiver: UserInfo
    private lateinit var refMessages: DatabaseReference
    private lateinit var messageListener: ValueEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SingleChatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var voiceRecorder: VoiceRecorder
    private var countMessages = 20
    private var isNeedSmoothScroll = true
    private var isScrolling = false
    private var isPermissionGranted = false

    private var _binding: SingleChatFragmentBinding? = null
    private val binding: SingleChatFragmentBinding
        get() = _binding ?: throw RuntimeException("SingleChatFragmentBinding is null")

    private val args by navArgs<SingleChatFragmentArgs>()
    private val contact by lazy {
        args.contact
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
    }

    override fun onResume() {
        super.onResume()
        initReceiver() {
            initFields()
        }
        initRecycler()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        voiceRecorder.releaseRecorder()
        adapter.destroy()
        binding.rvSwipeRefresh.isEnabled = false
    }


    private fun initRecycler() {

        recyclerView = binding.chatRecycleView
        adapter = SingleChatAdapter()
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        refMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
        messageListener = AppEventListener {
            it.children.map { snapshot ->
                val message = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                if (isNeedSmoothScroll) {
                    Log.d("rv",message.toString())
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
    private fun initFields() {
        setVisibilityMode()
        voiceRecorder = VoiceRecorder()
        layoutManager = LinearLayoutManager(requireContext())
        binding.chatUserPhoto.downloadAndSetImage(receiver.photoUrl)
        binding.chatUsername.text = receiver.name
        binding.userState.text = receiver.state
        binding.chatInputMessage.isCursorVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            binding.btnVoiceMessage.setOnTouchListener { view, motionEvent ->
                if(isPermissionGranted){
                    if(motionEvent.action == MotionEvent.ACTION_DOWN){
                        binding.chatInputMessage.setText("Recording...")
                        binding.btnVoiceMessage.setColorFilter(
                            ContextCompat.getColor(requireActivity(),R.color.colorPrimary)
                        )
                        val messageKey =
                            REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
                                .push().key.toString()
                        voiceRecorder.startRecord(messageKey)
                    }else if(motionEvent.action == MotionEvent.ACTION_UP){
                        binding.chatInputMessage.setText("")
                        binding.btnVoiceMessage.colorFilter = null
                        voiceRecorder.stopRecord(){ file, messageKey ->
                            uploadFileToStorage(Uri.fromFile(file),messageKey,contact.id, TYPE_VOICE)
                        }
                    }

                }
                true
            }
        }

        binding.btnAttachFile.setOnClickListener { attachFile() }
        binding.btnSendMessage.setOnClickListener {
            recyclerView.smoothScrollToPosition(adapter.itemCount)
            val message = binding.chatInputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message, contact.id, TYPE_TEXT) {
                    binding.chatInputMessage.setText("")
                }
            }

        }
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun attachFile() {
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
                if (string.isEmpty() || string=="Recording...") {
                    binding.btnSendMessage.visibility = View.GONE
                    binding.btnAttachFile.visibility = View.VISIBLE
                    binding.btnVoiceMessage.visibility = View.VISIBLE
                } else {
                    binding.btnSendMessage.visibility = View.VISIBLE
                    binding.btnAttachFile.visibility = View.GONE
                    binding.btnVoiceMessage.visibility = View.GONE
                }
            }

        })

    }

    private fun sendMessage(
        message: String,
        receivingUserId: String,
        messageType: String,
        function: () -> Unit
    ) {
        val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
        val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"
        val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = UID
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_ID] = messageKey.toString()
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        mapMessage[CHILD_TYPE] = messageType

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

        REF_DATABASE_ROOT
            .updateChildren(mapDialog)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showToast(it.message.toString()) }
    }

    private fun initReceiver(function: () -> Unit) {
        getReceiverId {
            getReceiver() { snapshot ->
                receiver = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                function()
            }
        }
    }

    private fun getReceiver(function: (snapshot: DataSnapshot) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    function(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun getReceiverId(function: (snapshot: DataSnapshot) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS_ID).child(contact.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    function(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val messageKey =
                REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
                    .push().key.toString()
            uploadFileToStorage(uri,messageKey,contact.id, TYPE_IMAGE)
            isNeedSmoothScroll = true
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isPermissionGranted = true
        } else {
            showToast("Permission denied")
        }
    }

    private fun startRecordAudioPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

}