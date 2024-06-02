package com.example.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentLoginBinding
import com.example.taskapp.databinding.FragmentSplashBinding
import com.example.taskapp.util.showButtonSheet

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners(){
        binding.btnLogin.setOnClickListener{
            validateData()
        }

        binding.btnRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnRecovery.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    // função para validar campos
    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if(email.isNotEmpty()){
            if(password.isNotEmpty()){
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                showButtonSheet(message= getString(R.string.password_empty))
            }
        }else {
            showButtonSheet(message= getString(R.string.email_empty))
        }
    }

    //setar binding como nulo ao sair da tela
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}