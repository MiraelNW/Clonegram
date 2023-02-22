package com.example.clonegram.presentation.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.EnterCodeFragmentBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.utils.*
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import javax.inject.Inject


class EnterCodeFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: EnterCodeViewModel

    private val args by navArgs<EnterCodeFragmentArgs>()

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
        viewModel = ViewModelProvider(this, factory)[EnterCodeViewModel::class.java]
        binding.registerInputCode.setOnFinishListener { code ->
            val credential = PhoneAuthProvider.getCredential(id, code)
            AUTH.signInWithCredential(credential).addOnCompleteListener {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                val user = UserInfo(uid, phone = phoneNumber)
                UID = uid
                viewModel.firstInitUserUseCase(phoneNumber, uid, dateMap) {
                    showToast("Welcome to the Clonegram")
                    lifecycleScope.launch {
                        viewModel.insertUserUseCase(user)
                    }
                    startChatFragment()
                }
            }
        }
    }

    private fun startChatFragment() {
        findNavController().navigate(R.id.action_enterCodeFragment_to_mainFragment2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        phoneNumber = args.phoneNumber
        id = args.id
    }
}