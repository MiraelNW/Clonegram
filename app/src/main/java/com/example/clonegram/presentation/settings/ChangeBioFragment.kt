package com.example.clonegram.presentation.settings

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.ChangeBioFragmentBinding
import com.example.clonegram.utils.*

class ChangeBioFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: ChangeBioFragmentBinding? = null
    private val binding: ChangeBioFragmentBinding
        get() = _binding ?: throw RuntimeException("ChangeBioFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChangeBioFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etAboutYourself.setText(USER.bio)
        changeBio()
        binding.arrowBack.setOnClickListener {
           findNavController().popBackStack()

        }
        binding.accept.setOnClickListener {
            saveBio()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeBio() {
        val bio = binding.etAboutYourself.text.toString().trim()
        if (bio == DEFAULT_BIO) {
            with(binding.etAboutYourself) {
                setText("")
                hint = DEFAULT_BIO
            }
        }
        binding.etAboutYourself.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val length = str?.length ?: 0
                binding.countSymbols.text = (MAX_LENGTH - length).toString()
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                val length = str?.length ?: 0
                binding.countSymbols.text = (MAX_LENGTH - length).toString()
            }

        })

    }

    private fun saveBio() {
        val bio = binding.etAboutYourself.text.toString().trim()
        if (bio.isNotEmpty()) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO).setValue(bio)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Your bio is saved")
                        USER.bio = bio
                        findNavController().popBackStack()
                    }
                }
        } else {
            USER.bio = DEFAULT_BIO
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val MAX_LENGTH = 70
        private const val DEFAULT_BIO = "About yourself"
    }
}