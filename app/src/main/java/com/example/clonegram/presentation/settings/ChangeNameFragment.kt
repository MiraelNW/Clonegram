package com.example.clonegram.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.ChangeNameFragmentBinding
import com.example.clonegram.utils.*

class ChangeNameFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: ChangeNameFragmentBinding? = null
    private val binding: ChangeNameFragmentBinding
        get() = _binding ?: throw RuntimeException("ChangeNameFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChangeNameFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etChangeName.setText(USER.name)
        binding.accept.setOnClickListener {
            changeName()
        }
        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


    }

    private fun changeName() {
        val name = binding.etChangeName.text.toString().trim()
        if (name.isNotEmpty()) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_NAME).setValue(name)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "Your name is saved", Toast.LENGTH_SHORT)
                            .show()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }

        } else {
            Toast.makeText(requireContext(), "Name must not be empty", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ChangeNameFragment()
    }

}