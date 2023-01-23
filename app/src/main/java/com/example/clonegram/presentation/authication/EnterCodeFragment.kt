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
import com.example.clonegram.presentation.ChatsFragment
import com.example.clonegram.utils.AUTH
import com.google.firebase.auth.PhoneAuthProvider


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
            Log.d("tag", code)
            AUTH.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("tag", "start")
                    makeToast("Welcome to the Clonegram")
                    startChatFragment()
                } else {
                    makeToast(it.exception?.message ?: "")
                }
            }
        }

    }

    private fun startChatFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ChatsFragment.newInstance())
            .commit()
    }

    private fun makeToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
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
        private const val KEY_VERIFICATION_ID = "key_verification_id"
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