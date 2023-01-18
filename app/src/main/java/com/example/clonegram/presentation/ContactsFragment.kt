package com.example.clonegram.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clonegram.databinding.ContactsFragmentBinding

class ContactsFragment : Fragment() {

    private  var _binding : ContactsFragmentBinding? = null
    private  val binding : ContactsFragmentBinding
    get() = _binding ?: throw RuntimeException("ContactsFragmentBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater,container,false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance()=ContactsFragment()
    }
}