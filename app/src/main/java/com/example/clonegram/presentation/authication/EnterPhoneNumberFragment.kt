package com.example.clonegram.presentation.authication

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.EnterPhoneNumberFragmentBinding
import com.example.clonegram.presentation.ChatsFragment
import com.example.clonegram.utils.AUTH
import com.example.clonegram.utils.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.santalu.maskara.MaskChangedListener
import java.util.concurrent.TimeUnit


class EnterPhoneNumberFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: EnterPhoneNumberFragmentBinding? = null
    private val binding: EnterPhoneNumberFragmentBinding
        get() = _binding ?: throw RuntimeException("EnterPhoneNumberFragmentBinding is null")

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnterPhoneNumberFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = binding.inputPhoneNumber
        binding.registerBtnNext.setOnClickListener {
            if (phoneNumber.isDone) {
                callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        AUTH.signInWithCredential(credential).addOnCompleteListener {task ->
                            if (task.isSuccessful) {
                                showToast("Добро пожаловать")
                               startChatsFragment()
                            } else {
                                showToast(task.exception?.message.toString())
                            }
                        }
                    }

                    override fun onVerificationFailed(error: FirebaseException) {
                        showToast( error.message.toString())
                    }

                    override fun onCodeSent(id: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(id, p1)
                        startEnterCodeFragment(phoneNumber.masked, id)
                    }
                }
                authUser(phoneNumber.masked)
            } else {
                showToast("Incorrect input")
            }
        }

    }

    private fun startChatsFragment(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ChatsFragment.newInstance())
            .commit()
    }

    private fun startEnterCodeFragment(phoneNumber: String, id: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, EnterCodeFragment.newInstance(phoneNumber, id))
            .addToBackStack(null)
            .commit()
    }

    private fun authUser(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(phoneNumber)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .setTimeout(60L, TimeUnit.SECONDS)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = EnterPhoneNumberFragment()
    }
}