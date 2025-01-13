package com.am.projectinternalresto.ui.feature.admin.manage_menu

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.am.projectinternalresto.R
import com.am.projectinternalresto.data.body_params.ItemTopping
import com.am.projectinternalresto.data.body_params.MenuBody
import com.am.projectinternalresto.data.response.admin.category.DataItemCategory
import com.am.projectinternalresto.data.response.admin.menu.AddOrUpdateMenuResponse
import com.am.projectinternalresto.data.response.admin.menu.DataItemMenu
import com.am.projectinternalresto.data.response.admin.menu.ToppingItem
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
import com.am.projectinternalresto.utils.setMinusPrice
import com.am.projectinternalresto.utils.setPriceWatcherUtils
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
            binding.edtPriceMenu.setText(Formatter.formatCurrency(dataMenu?.price ?: 0))
            Glide.with(requireContext()).load(dataMenu?.imageMenu.toString())
                .into(binding.imageMenu)
            dataMenu?.topping?.forEach { topping ->
                addToppingLayout(topping)
            }
        } else {
            binding.actionHeadline.textHeadline.text = getString(R.string.add_menu)
        }
        binding.buttonAddOrUpdateMenu.text = getString(R.string.save_data)
        UiHandle.setupDisableHintForField(
            binding.edlCategory,
            binding.edlNameMenu,
            binding.EdlPriceMenu,
        )
        binding.textAddTopping.setOnClickListener {
            addToppingLayout()
            it.visibility = View.INVISIBLE
        }
        binding.edtPriceMenu.setPriceWatcherUtils { price ->
            unformattedPrice = price
        }

    }

    private fun addToppingLayout(existingTopping: ToppingItem? = null) {
        if (existingTopping != null) {
            binding.textAddTopping.visibility = View.INVISIBLE
        }
        val newToppingView = layoutInflater.inflate(
            R.layout.item_content_topping,
            binding.layoutToppingContainer,
            false
        )
        newToppingView.tag = "topping_item"

        val textAddMoreTopping = newToppingView.findViewById<TextView>(R.id.textAddMoreTopping)
        val edlTextNameTopping = newToppingView.findViewById<TextInputLayout>(R.id.edlToppingName)
        val edtTextNameTopping = newToppingView.findViewById<TextInputEditText>(R.id.edtToppingName)
        val edlTextPriceTopping = newToppingView.findViewById<TextInputLayout>(R.id.edlToppingPrice)
        val edtTextPriceTopping =
            newToppingView.findViewById<TextInputEditText>(R.id.edtToppingPrice)

        UiHandle.setupDisableHintForField(edlTextNameTopping, edlTextPriceTopping)
        edtTextPriceTopping.setMinusPrice { }

        existingTopping?.let {
            edtTextNameTopping.setText(it.nama)
            edtTextPriceTopping.setText(Formatter.formatCurrency(it.harga ?: 0))
        }

        binding.layoutToppingContainer.addView(newToppingView)
        textAddMoreTopping.setOnClickListener {
            it.visibility = View.GONE
            addToppingLayout()
        }

        if (existingTopping == null) {
            binding.textAddTopping.visibility = View.VISIBLE
        }
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
        Log.e("Check_data_menu", "data : ${dataResultMenu()}")
        viewModel.updateMenu(
            token,
            dataMenu?.idMenu.toString(),
            dataResultMenu()
        ).observe(viewLifecycleOwner) { result -> handleApiStatus(result) }
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
                UiHandle.setupClearTextForField(binding.edtNameMenu, binding.edtPriceMenu)
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

    private fun collectToppingData(): List<ItemTopping> {
        val toppings = mutableListOf<ItemTopping>()

        for (i in 0 until binding.layoutToppingContainer.childCount) {
            val toppingView = binding.layoutToppingContainer.getChildAt(i)
            if (toppingView.tag == "topping_item") {

                val edtTextNameTopping: TextInputEditText =
                    toppingView.findViewById(R.id.edtToppingName)
                val edtTextPriceTopping: TextInputEditText =
                    toppingView.findViewById(R.id.edtToppingPrice)

                val name = edtTextNameTopping.text.toString()
                val priceText = edtTextPriceTopping.text.toString()

                if (name.isNotBlank() && priceText.isNotBlank()) {
                    val price = try {
                        val cleanPrice = priceText.replace("[Rp,.]".toRegex(), "").trim()
                            .replace("\\s+".toRegex(), "")
                        cleanPrice.toInt()
                    } catch (e: NumberFormatException) {
                        Log.e("ToppingParsing", "Invalid price format: $priceText")
                        0
                    }

                    toppings.add(ItemTopping(name, price))
                }
            }
        }
        return toppings
    }

    private fun dataResultMenu(): MenuBody {
        return MenuBody(
            idCategory = selectIdCategoryMenu ?: dataMenu?.category?.id.toString(),
            nameMenu = binding.edtNameMenu.text.toString(),
            price = unformattedPrice ?: dataMenu?.price!!,
            image = uriSelectedImage?.let { Formatter.uriToFile(it, requireContext()) },
            itemToppings = collectToppingData()
        )
    }
}