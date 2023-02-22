package com.example.clonegram.presentation.settings

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.SettingsFragmentBinding
import com.example.clonegram.presentation.settings.viewModels.SettingsViewModel
import com.example.clonegram.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    private var _binding: SettingsFragmentBinding? = null
    private val binding: SettingsFragmentBinding
        get() = _binding ?: throw RuntimeException("SettingsFragmentBinding is null")

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: SettingsViewModel

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
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.settingsBtnChangeBio.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changeBioFragment)
        }
        binding.settingsBtnChangeLogin.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changeUserIdFragment)
        }
        binding.settingsBtnChangeNumberPhone.setOnClickListener {
        }
        binding.settingsChangePhoto.setOnClickListener {
            changeUserPhoto()
        }
        binding.changeName.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changeNameFragment)
        }
        binding.exit.setOnClickListener {
            UserState.updateState(UserState.OFFLINE)
            AUTH.signOut()
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        initFields()
    }

    private fun initFields() {
        with(binding) {
            settingsUsername.text = USER.name
            settingsPhoneNumber.text = USER.phone
            settingsLogin.text = USER.idName
            settingsLogin.text = USER.idName
            settingsBio.text = USER.bio
            settingsUserPhoto.downloadAndSetImage(USER.photoUrl)
            userState.text = UserState.ONLINE.state
        }
    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setRequestedSize(250, 250)
            .start(requireActivity(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            viewModel.insertImage(uri).observe(viewLifecycleOwner){
                USER.photoUrl = uri.toString()
                binding.settingsUserPhoto.downloadAndSetImage(uri.toString())
                showToast("Your photo is saved")
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}