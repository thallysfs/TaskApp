package com.example.taskapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentRecoverAccountBinding
import com.example.taskapp.ui.BaseFragment
import com.example.taskapp.util.FirebaseHelper
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showButtonSheet

class RecoverAccountFragment : BaseFragment() {
    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        initListeners()
    }

    private fun initListeners() {
        binding.btnRecover.setOnClickListener {
            validateData()
        }
    }

    // função para validar campos
    private fun validateData() {
        val email =
            binding.editEmail.text
                .toString()
                .trim()

        if (email.isNotEmpty()) {
            hideKeyboard()
            binding.progressBar.isVisible = true

            recoverAccount(email)
        } else {
            showButtonSheet(message = getString(R.string.email_empty))
        }
    }

    // setar binding como nulo ao sair da tela
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun recoverAccount(email: String) {
        val emailTrim = email.toString().trim()
        FirebaseHelper.getAuth().sendPasswordResetEmail(emailTrim).addOnCompleteListener { task ->
            binding.progressBar.isVisible = false
            if (task.isSuccessful) {
                showButtonSheet(
                    message = getString(R.string.text_message_toolbar_recover_account_fragment),
                )
            } else {
                showButtonSheet(
                    message = getString(FirebaseHelper.valideError(task.exception?.message.toString())),
                )
            }
        }
    }
}
