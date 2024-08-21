package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.params.MenuBody
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.data.response.admin.menu.AddOrUpdateMenuResponse
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.databinding.FragmentAddOrUpdateMenuBinding
import com.am.projectinternalresto.service.source.Resource
import com.am.projectinternalresto.service.source.Status
import com.am.projectinternalresto.ui.adapter.manage_admin.SelectCategoryMenuAdapter
import com.am.projectinternalresto.ui.feature.admin.manage_category.ManageCategoryViewModel
import com.am.projectinternalresto.ui.feature.auth.AuthViewModel
import com.am.projectinternalresto.ui.widget.bottom_sheet.ChooseGalleryOrCameraBottomSheet
import com.am.projectinternalresto.utils.Formatter
import com.am.projectinternalresto.utils.Key
import com.am.projectinternalresto.utils.NotificationHandle
import com.am.projectinternalresto.utils.ProgressHandle
import com.am.projectinternalresto.utils.UiHandle
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject

class AddOrUpdateMenuFragment : Fragment() {
    private var _binding: FragmentAddOrUpdateMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManageMenuViewModel by inject()
    private val categoryViewModel: ManageCategoryViewModel by inject()
    private val authViewModel: AuthViewModel by inject()
    private val token: String by lazy { authViewModel.getTokenUser().toString() }
    private var selectIdCategoryMenu: String? = null
    private var uriSelectedImage: Uri? = null
    private lateinit var adapterDropdownSelectCategoryMenu: SelectCategoryMenuAdapter
    private val dataMenu: DataItemMenu? by lazy { arguments?.getParcelable(Key.BUNDLE_DATA_MENU) }
    private var unformattedPrice: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUpdateMenuBinding.inflate(inflater, container, false)
        setupDisplay()
        setupGetCategoryMenuFromApi()
        setupNavigation()
        return binding.root
    }

    private fun setupDisplay() {
        if (dataMenu != null) {
            binding.actionHeadline.textHeadline.text = getString(R.string.edit_data)
            binding.dropdownCategory.setText(dataMenu?.category?.nameCategory)
            binding.edtNameMenu.setText(dataMenu?.nameMenu)
            binding.edtNoMenu.setText(dataMenu?.noMenu)
            binding.edtQuotaMenu.setText(dataMenu?.quota.toString())
            binding.edtComposition.setText(dataMenu?.composition)
            binding.edtPriceMenu.setText(Formatter.formatCurrency(dataMenu?.price ?: 0))
            Glide.with(requireContext()).load(dataMenu?.imageMenu.toString())
                .into(binding.imageMenu)
        } else {
            binding.actionHeadline.textHeadline.text = getString(R.string.add_menu)
        }
        binding.buttonAddOrUpdateMenu.text = getString(R.string.save_data)
        UiHandle.setupDisableHintForField(
            binding.edlCategory,
            binding.edlComposition,
            binding.edlNoMenu,
            binding.edlNameMenu,
            binding.edlQuotaMenu,
            binding.EdlPriceMenu,
            binding.EdlTopping
        )
        binding.edtPriceMenu.addTextChangedListener(object : TextWatcher {
            private var currentText = ""
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.toString() != currentText) {
                    binding.edtPriceMenu.removeTextChangedListener(this)

                    val cleanString = text.toString().replace("[Rp,.]".toRegex(), "").trim()
                    if (cleanString.isNotEmpty()) {
                        val price = cleanString.toInt()
                        val formattedPrice = Formatter.formatCurrency(price)

                        currentText = formattedPrice
                        binding.edtPriceMenu.setText(formattedPrice)
                        binding.edtPriceMenu.setSelection(formattedPrice.length)
                        unformattedPrice = price
                    } else {
                        unformattedPrice = null
                    }

                    binding.edtPriceMenu.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setupNavigation() {
        binding.buttonAddOrUpdateMenu.setOnClickListener {
            UiHandle.setupHideKeyboard(it)
            if (dataMenu != null) {
                setupPutDataMenuApi()
            } else {
                setupPostDataMenuToApi()
            }
        }
        binding.actionHeadline.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cardImageMenu.setOnClickListener {
            ChooseGalleryOrCameraBottomSheet.showBottomSheet(
                childFragmentManager,
                binding.edtNameMenu.text.toString().lowercase().replace(" ", "")
            ) { uri ->
                uriSelectedImage = uri
                Glide.with(requireContext()).load(uri).into(binding.imageMenu)
            }
        }
    }

    private fun setupAdapterDropdown(data: List<DataItemCategory>) {
        adapterDropdownSelectCategoryMenu = SelectCategoryMenuAdapter(requireContext(), data)
        binding.dropdownCategory.apply {
            setAdapter(adapterDropdownSelectCategoryMenu)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                val selectCategoryMenu = adapterDropdownSelectCategoryMenu.getItem(i)
                selectIdCategoryMenu = selectCategoryMenu.id

                binding.dropdownCategory.setText(selectCategoryMenu.nameCategory)
                setupGetCategoryMenuFromApi()
            }
        }
    }

    private fun setupGetCategoryMenuFromApi() {
        categoryViewModel.getCategoryMenu(token).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    setupAdapterDropdown(resource.data?.data as List<DataItemCategory>)
                    adapterDropdownSelectCategoryMenu.notifyDataSetChanged()
                }

                Status.ERROR -> {
                    NotificationHandle.showErrorSnackBar(requireView(), resource.message.toString())
                }
            }
        }
    }

    private fun setupPostDataMenuToApi() {
        viewModel.addMenu(token, dataResultMenu())
            .observe(viewLifecycleOwner) { result -> handleApiStatus(result) }
    }

    private fun setupPutDataMenuApi() {
        viewModel.updateMenu(token, dataMenu?.idMenu.toString(), dataResultMenu())
            .observe(viewLifecycleOwner) { result ->
                handleApiStatus(result)
            }
    }


    private fun handleApiStatus(
        result: Resource<AddOrUpdateMenuResponse?>,
    ) {
        when (result.status) {
            Status.LOADING -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, true
                )
            }

            Status.SUCCESS -> {
                UiHandle.setupClearTextForField(
                    binding.edtNameMenu,
                    binding.edtNoMenu,
                    binding.edtPriceMenu,
                    binding.edtQuotaMenu,
                    binding.edtComposition,
                    binding.edtTopping
                )
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false
                )
                findNavController().popBackStack()
                NotificationHandle.showSuccessSnackBar(
                    requireView(),
                    result.data?.message.toString()
                )
            }

            Status.ERROR -> {
                ProgressHandle.setupVisibilityProgressBar(
                    binding.progressBar, binding.textLoading, false
                )
                NotificationHandle.showErrorSnackBar(requireView(), result.message.toString())
            }
        }
    }

    private fun dataResultMenu(): MenuBody {
        return MenuBody(
            idCategory = selectIdCategoryMenu ?: dataMenu?.category?.id.toString(),
            noMenu = binding.edtNoMenu.text.toString(),
            nameMenu = binding.edtNameMenu.text.toString(),
            composition = binding.edtComposition.text.toString(),
            quota = binding.edtQuotaMenu.text.toString().toInt(),
            price = unformattedPrice ?: dataMenu?.price!!,
            image = uriSelectedImage?.let { Formatter.uriToFile(it, requireContext()) }
        )
    }
}