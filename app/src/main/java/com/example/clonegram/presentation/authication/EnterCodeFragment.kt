package com.example.clonegram.presentation.authication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.EnterCodeFragmentBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.ChatsFragment
import com.example.clonegram.utils.*
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class EnterCodeFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: EnterCodeFragmentBinding? = null
    private val binding: EnterCodeFragmentBinding
        get() = _binding ?: throw RuntimeException("EnterCodeFragmentBinding is null")

    private lateinit var phoneNumber: String
    private lateinit var id: String


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnterCodeFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArgs()

        binding.registerInputCode.setOnFinishListener { code ->
            val credential = PhoneAuthProvider.getCredential(id, code)
            AUTH.signInWithCredential(credential).addOnCompleteListener {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                UID = uid
                REF_DATABASE_ROOT.child(NODE_PHONES_NUMBER).child(phoneNumber).setValue(uid)
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                            .addOnSuccessListener {
                                showToast("Welcome to the Clonegram")
                                initUser {
                                    startChatFragment()
                                }
                            }
                            .addOnFailureListener { showToast(it.message.toString()) }
                    }
                    .addOnFailureListener { showToast(it.message ?: "") }
            }
        }
    }
    private fun initUser(function: () -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                    function()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun startChatFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ChatsFragment.newInstance())
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        phoneNumber = requireArguments().getString(PHONE_NUMBER) ?: ""
        id = requireArguments().getString(ID) ?: ""
    }

    companion object {
        private const val PHONE_NUMBER = "phone_number"
        private const val ID = "id"
        fun newInstance(phoneNumber: String, id: String): EnterCodeFragment {
            return EnterCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(PHONE_NUMBER, phoneNumber)
                    putString(ID, id)
                }
            }
        }
    }
}