package com.example.clonegram.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clonegram.databinding.ChangeBioFragmentBinding

class ChangeBioFragment : Fragment() {

    private var _binding : ChangeBioFragmentBinding? = null
    private val binding : ChangeBioFragmentBinding
    get() = _binding ?: throw RuntimeException("ChangeBioFragmentBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChangeBioFragmentBinding.inflate(inflater,container,false)
        return(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()

        }
        binding.accept.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}