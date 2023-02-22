package com.example.clonegram.presentation.contacts

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
import com.example.clonegram.databinding.ContactsFragmentBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.contacts.contactAdapter.ContactsAdapter
import com.example.clonegram.utils.ViewModelFactory
import javax.inject.Inject

class ContactsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ContactViewModel


    private var _binding: ContactsFragmentBinding? = null
    private val binding: ContactsFragmentBinding
        get() = _binding ?: throw RuntimeException("ContactsFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[ContactViewModel::class.java]

        val adapter = ContactsAdapter()
        binding.contactsRecyclerView.adapter = adapter

        viewModel.contactListUseCase.invoke().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        adapter.onContactClickListener = object : ContactsAdapter.OnContactClickListener {
            override fun onContactClick(contact: UserInfo) {
                findNavController().navigate(
                    ContactsFragmentDirections.actionContactsFragmentToSingleChatFragment(
                        contact
                    )
                )
            }

        }

        binding.arrowBack.setOnClickListener { findNavController().popBackStack() }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}