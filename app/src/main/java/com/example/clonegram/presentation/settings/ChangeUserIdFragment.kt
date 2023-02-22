package com.example.clonegram.presentation.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.ChangeUserIdFragmentBinding
import com.example.clonegram.presentation.settings.viewModels.ChangeUserIdViewModel
import com.example.clonegram.utils.*
import javax.inject.Inject

class ChangeUserIdFragment : Fragment() {

    private lateinit var userId: String

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: ChangeUserIdFragmentBinding? = null
    private val binding: ChangeUserIdFragmentBinding
        get() = _binding ?: throw RuntimeException("ChangeUserIdFragmentBinding is null")

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ChangeUserIdViewModel

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
        viewModel = ViewModelProvider(this, factory)[ChangeUserIdViewModel::class.java]
        binding.etUserId.setText(USER.idName)
        binding.arrowBack.setOnClickListener { findNavController().popBackStack() }
        binding.accept.setOnClickListener {
            userId = binding.etUserId.text.toString().trim()
            if (userId.isEmpty()) {
                showToast("This field should not be empty")
            } else {
                viewModel.changeUserIdUseCase(userId) {
                    Log.d("tag","log")
                    showToast("Your id is saved")
                    USER.idName = userId
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}