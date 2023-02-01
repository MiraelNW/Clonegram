package com.example.clonegram.presentation.settings

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.SettingsFragmentBinding
import com.example.clonegram.presentation.MainActivity
import com.example.clonegram.utils.*
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

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
            changeUserPhoto()
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

    override fun onResume() {
        super.onResume()
        initFields()
    }

    private fun initFields() {
        with(binding) {
            settingsUsername.text = USER.name
            settingsPhoneNumber.text = USER.phone
            settingsLogin.text = USER.id
            settingsBio.text = USER.bio
            settingsUserPhoto.downloadAndSetImage(USER.photoUrl)
        }
    }

    private fun changeUserPhoto() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setRequestedSize(300,300)
            .start(requireActivity(),this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(UID)
            putImageToStorage(uri,path){
                getUrlFromStorage(path){
                    putUrlToDatabase(it){
                        USER.photoUrl = it
                            binding.settingsUserPhoto.downloadAndSetImage(it)
                        showToast("Your photo is saved")

                    }
                }
            }
        }
    }

    private fun putUrlToDatabase(url: String, function: () -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .child(CHILD_PHOTO).setValue(url)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showToast(it.message.toString()) }
    }

    private fun getUrlFromStorage(path: StorageReference, function: (url : String) -> Unit) {
        path.downloadUrl
            .addOnSuccessListener { function(it.toString()) }
            .addOnFailureListener { showToast(it.message.toString()) }
    }

    private fun putImageToStorage(uri: Uri, path: StorageReference, function: () -> Unit) {
        path.putFile(uri)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showToast(it.message.toString()) }
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