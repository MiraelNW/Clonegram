package com.example.clonegram.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.ChangeNameFragmentBinding
import com.example.clonegram.presentation.settings.viewModels.ChangeUserNameViewModel
import com.example.clonegram.utils.*
import javax.inject.Inject

class ChangeNameFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: ChangeNameFragmentBinding? = null
    private val binding: ChangeNameFragmentBinding
        get() = _binding ?: throw RuntimeException("ChangeNameFragmentBinding is null")

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel : ChangeUserNameViewModel

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
        viewModel = ViewModelProvider(this,factory)[ChangeUserNameViewModel::class.java]
        binding.etChangeName.setText(USER.name)
        binding.accept.setOnClickListener {
            changeName()
        }
        binding.arrowBack.setOnClickListener {
           findNavController().popBackStack()
        }
    }

    private fun changeName() {
        val name = binding.etChangeName.text.toString().trim()
        if (name.isNotEmpty()) {
            viewModel.changeUserNameUseCase(name){
                showToast("Your name is saved")
                USER.name = name
                findNavController().popBackStack()
            }
        } else {
            showToast("Name must not be empty")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}