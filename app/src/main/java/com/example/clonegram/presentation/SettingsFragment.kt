package com.example.clonegram.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.SettingsFragmentBinding
import com.fragula2.utils.findSwipeController

class SettingsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: SettingsFragmentBinding? = null
    private val binding: SettingsFragmentBinding
        get() = _binding ?: throw RuntimeException("SettingsFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.settingsBtnChangeBio.setOnClickListener {

        }
        binding.settingsBtnChangeLogin.setOnClickListener {

        }
        binding.settingsBtnChangeNumberPhone.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}