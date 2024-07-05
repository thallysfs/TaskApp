package com.example.taskapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentRegisterBinding
import com.example.taskapp.ui.BaseFragment
import com.example.taskapp.util.FirebaseHelper
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showButtonSheet

class RegisterFragment : BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }

    // função para validar campos
    private fun validateData() {
        val email =
            binding.editEmail.text
                .toString()
                .trim()
        val password =
            binding.editPassword.text
                .toString()
                .trim()

        if (email.isNotEmpty()) {
            hideKeyboard()

            if (password.isNotEmpty()) {
                binding.progressBar.isVisible = true

                registerUser(email, password)
            } else {
                if (password.isEmpty()) {
                    showButtonSheet(message = getString(R.string.password_empty_register_fragment))
                }
            }
        } else {
            showButtonSheet(message = getString(R.string.email_empty_register_fragment))
        }
    }

    private fun registerUser(
        email: String,
        password: String,
    ) {
        FirebaseHelper
            .getAuth()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    binding.progressBar.isVisible = false

                    showButtonSheet(
                        message = getString(FirebaseHelper.valideError(task.exception?.message.toString())),
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
