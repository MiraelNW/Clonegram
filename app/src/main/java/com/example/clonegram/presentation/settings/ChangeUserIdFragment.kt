package com.example.clonegram.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.ChangeUserIdFragmentBinding
import com.example.clonegram.utils.*

class ChangeUserIdFragment : Fragment() {

    private lateinit var userId: String

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: ChangeUserIdFragmentBinding? = null
    private val binding: ChangeUserIdFragmentBinding
        get() = _binding ?: throw RuntimeException("ChangeUserIdFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChangeUserIdFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etUserId.setText(USER.id)
        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }
        binding.accept.setOnClickListener {
            userId = binding.etUserId.text.toString().trim()
            if (userId.isEmpty()) {
                showToast("This field should not be empty")
            } else {
                changeId()
            }

        }
    }


    private fun changeId() {
        REF_DATABASE_ROOT.child(NODE_USERS_ID).child(userId).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUserId()
                } else {
                    showToast(it.exception?.message ?: "")
                }
            }
    }

    private fun updateCurrentUserId() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_ID).setValue(userId)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    deleteOldUserId()
                } else {
                    showToast(it.exception?.message ?: "")
                }
            }
    }

    private fun deleteOldUserId() {
        REF_DATABASE_ROOT.child(NODE_USERS_ID).child(USER.id).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Your id is saved")
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    showToast(it.exception?.message ?: "")
                }

            }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ChangeUserIdFragment()
    }

}