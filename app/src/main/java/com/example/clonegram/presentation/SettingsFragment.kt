package com.example.clonegram.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clonegram.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private var _binding : SettingsFragmentBinding? = null
    private val binding : SettingsFragmentBinding
    get() = _binding ?: throw RuntimeException("SettingsFragmentBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater,container,false)

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).addFragment(SettingsFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    companion object{
        fun newInstance()=SettingsFragment()
    }
}