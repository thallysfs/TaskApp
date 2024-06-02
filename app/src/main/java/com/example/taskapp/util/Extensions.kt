package com.example.taskapp.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.taskapp.R
import com.example.taskapp.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.initToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener{ activity?.onBackPressed()}
}

// Bottom sheet
fun Fragment.showButtonSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String,
    onClick: () -> Unit = {}
) {
    // instanciar bottomSheet
    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

    // recebendo a referência do layout criado
    val binding: BottomSheetBinding =
        BottomSheetBinding.inflate(layoutInflater, null, false)

    binding.textTitle.text = getText(titleDialog ?: R.string.text_title_warning)
    binding.textMessage.text = message
    binding.btnOk.text = getText(titleButton ?: R.string.text_button_warning)
    binding.btnOk.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }

    bottomSheetDialog.setContentView(binding.root)
    // para mostrar componente
    bottomSheetDialog.show()

}