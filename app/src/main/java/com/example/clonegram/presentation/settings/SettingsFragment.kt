package com.example.clonegram.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.SettingsFragmentBinding
import com.example.clonegram.utils.AUTH
import com.example.clonegram.utils.UID
import com.example.clonegram.utils.USER

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
        initFields()

        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.settingsBtnChangeBio.setOnClickListener {
            startFragment(ChangeBioFragment.newInstance())
        }
        binding.settingsBtnChangeLogin.setOnClickListener {
            startFragment(ChangeUserIdFragment.newInstance())
        }
        binding.settingsBtnChangeNumberPhone.setOnClickListener {
            //TODO
        }
        binding.settingsChangePhoto.setOnClickListener {

        }
        binding.changeName.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChangeNameFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        binding.exit.setOnClickListener {
            AUTH.signOut()
            requireActivity().finish()
        }
    }

    private fun initFields() {
        with(binding) {
            settingsUsername.text = USER.name
            settingsPhoneNumber.text = USER.phone
            settingsLogin.text = USER.id
            settingsBio.text = USER.bio
        }
    }

    private fun startFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}