package com.example.clonegram.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.ChangeNameFragmentBinding

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
        binding.accept.setOnClickListener {
            //ToDo
        }
        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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