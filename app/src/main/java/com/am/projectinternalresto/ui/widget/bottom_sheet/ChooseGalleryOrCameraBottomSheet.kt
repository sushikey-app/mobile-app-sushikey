package com.am.projectinternalresto.ui.widget.bottom_sheet

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import com.am.projectinternalresto.R
import com.am.projectinternalresto.databinding.FragmentChooseGalleryOrCameraBottomSheetBinding
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.NotificationHandle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.model.AspectRatio
import com.yalantis.ucrop.view.CropImageView
import java.io.File

class ChooseGalleryOrCameraBottomSheet(
    private val callbackImageUri: (Uri) -> Unit,
    private val fileName: String
) :
    BottomSheetDialogFragment() {
    private var _binding: FragmentChooseGalleryOrCameraBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentImageUri: Uri
    private val launchIntentToCamera: ActivityResultLauncher<Uri> = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            startCrop(currentImageUri, fileName)
        } else {
            NotificationHandle.showErrorSnackBar(requireView(), "Gagal mengambil foto")
        }
    }

    private val launchIntentToGallery: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            startCrop(it, fileName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentChooseGalleryOrCameraBottomSheetBinding.inflate(inflater, container, false)
        setupDisplay()
        setupNavigation()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // This function is used to set behavior of the bottom sheet when exiting full screen
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                callbackImageUri.invoke(it)
                dismiss()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            NotificationHandle.showErrorSnackBar(
                requireView(),
                "Gagal melakukan crop: ${cropError?.message}"
            )
        }
    }

    private fun setupDisplay() {
        binding.layoutTakePhotoFromGallery.apply {
            textTitle.text = getString(R.string.text_take_photo_from_gallery)
            imageIcon.setImageResource(R.drawable.icon_gallery)
        }
    }

    private fun setupNavigation() {
        binding.textCancel.setOnClickListener { dismiss() }
        binding.layoutTakePhotoFromGallery.root.setOnClickListener { actionTakePhotoFromGallery() }
        binding.layoutTakePhotoFromCamera.root.setOnClickListener { actionTakePhotoFromCamera() }
    }

    private fun actionTakePhotoFromGallery() {
        launchIntentToGallery.launch("image/*")
    }

    private fun actionTakePhotoFromCamera() {
        currentImageUri = Formatter.getImageUri(requireContext())
        launchIntentToCamera.launch(currentImageUri)
    }

    private fun startCrop(uri: Uri, fileName: String) {
        val destinationUri = Uri.fromFile(
            File(
                requireContext().cacheDir, "$fileName.jpg"
            )
        )
        val options = UCrop.Options().apply {
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setCompressionQuality(80)
            setAspectRatioOptions(
                0, AspectRatio("1:1", 1f, 1f), AspectRatio("3:4", 3f, 4f), AspectRatio(
                    "Original",
                    CropImageView.DEFAULT_ASPECT_RATIO,
                    CropImageView.DEFAULT_ASPECT_RATIO
                )
            )
        }

        UCrop.of(uri, destinationUri).withOptions(options).withAspectRatio(1f, 1f)
            .start(requireContext(), this)
    }

    companion object {
        fun showBottomSheet(
            fragmentManager: FragmentManager,
            fileName: String, callbackImageUri: (Uri) -> Unit,
        ) {
            val bottomSheet = ChooseGalleryOrCameraBottomSheet(callbackImageUri, fileName)
            bottomSheet.show(fragmentManager, bottomSheet.tag)
        }
    }
}