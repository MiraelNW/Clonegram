package com.example.clonegram.presentation.singleChat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.databinding.SingleChatFragmentBinding
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.singleChat.singleChatAdapter.SingleChatAdapter
import com.example.clonegram.utils.*
import com.google.firebase.database.*

class SingleChatFragment : Fragment() {

    private lateinit var receiver: UserInfo
    private lateinit var refMessages: DatabaseReference
    private lateinit var messageListener: ChildEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SingleChatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var countMessages = 20
    private var isNeedSmoothScroll = true
    private var isScrolling = false

    private var _binding: SingleChatFragmentBinding? = null
    private val binding: SingleChatFragmentBinding
        get() = _binding ?: throw RuntimeException("SingleChatFragmentBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SingleChatFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        initReceiver() {
            initFields()
        }
        initRecycler()
    }

    private fun initRecycler() {

        recyclerView = binding.chatRecycleView
        adapter = SingleChatAdapter()
        recyclerView.adapter = adapter

        refMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(parseContact().id)
        messageListener = AppEventListener {
            adapter.addItem(it.getValue(UserInfo::class.java) ?: UserInfo(), isNeedSmoothScroll) {
                if (isNeedSmoothScroll) {
                    recyclerView.smoothScrollToPosition(adapter.itemCount)
                }
            }
        }

        refMessages.limitToLast(countMessages).addChildEventListener(messageListener)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling && dy < 0 && layoutManager.findFirstVisibleItemPosition() <= 6) {
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
    }

    private fun updateMessageList() {
        isScrolling = false
        isNeedSmoothScroll = false
        countMessages += 10
        refMessages.limitToLast(countMessages).addChildEventListener(messageListener)
    }


    private fun initFields() {
        layoutManager = LinearLayoutManager(requireContext())
        binding.chatUserPhoto.downloadAndSetImage(receiver.photoUrl)
        binding.chatUsername.text = receiver.name
        binding.userState.text = receiver.state
        binding.btnSendMessage.setOnClickListener {
            recyclerView.smoothScrollToPosition(adapter.itemCount)
            val message = binding.chatInputMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message, parseContact().id, TYPE_TEXT) {
                    binding.chatInputMessage.setText("")
                }
            }

        }
        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
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

    private fun parseContact(): Contact {
        return arguments?.getParcelable(CONTACT) ?: Contact()
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
        REF_DATABASE_ROOT.child(NODE_USERS).child(parseContact().id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    function(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun getReceiverId(function: (snapshot: DataSnapshot) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS_ID).child(parseContact().id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    function(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    companion object {
        private const val CONTACT = "contact"
        fun newInstance(contact: Contact): SingleChatFragment {
            return SingleChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CONTACT, contact)
                }
            }
        }
    }
}