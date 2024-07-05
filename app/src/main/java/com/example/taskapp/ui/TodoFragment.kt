package com.example.taskapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.R
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.databinding.FragmentTodoBinding
import com.example.taskapp.ui.adapter.TaskAdapter
import com.example.taskapp.util.FirebaseHelper
import com.example.taskapp.util.showButtonSheet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    lateinit var taskAdapter: TaskAdapter

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initListener()

        initRecyclerView()

        getTasks()
    }

    private fun initListener() {
        binding.fabAdd.setOnClickListener {
            val action =
                HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }

        observeViewModel()
    }

    private fun initRecyclerView() {
        taskAdapter =
            TaskAdapter(requireContext()) { task, option ->
                optionSelected(task, option)
            }

        with(binding.rvTasks) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.taskUpdate.observe(viewLifecycleOwner) { updateTask ->
            if (updateTask.status == Status.TODO) {
                // Armazena a lista atual do adapter
                val oldList = taskAdapter.currentList

                // Gera uma nova lista atualizada a partir da lista antiga
                val newList =
                    oldList.toMutableList().apply {
                        find { it.id == updateTask.id }?.description = updateTask.description
                    }

                // Armazena a posição da tarefa a ser atualizada
                val position = newList.indexOfFirst { it.id == updateTask.id }

                // Envia a lista atualizada pro adapter
                taskAdapter.submitList(newList)

                // Atualiza tarefa pela posição do adapter
                taskAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun optionSelected(
        task: Task,
        option: Int,
    ) {
        when (option) {
            TaskAdapter.SELECT_REMOVE -> {
                showButtonSheet(
                    titleDialog = R.string.text_title_dialog_delete,
                    message = getString(R.string.text_message_dialog_delete),
                    titleButton = R.string.text_button_dialog_confirm_logout,
                    onClick = { deleteTask(task) },
                )
            }
            TaskAdapter.SELECT_EDIT -> {
                val action =
                    HomeFragmentDirections
                        .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT).show()
            }
            TaskAdapter.SELECT_NEXT -> {
                task.status = Status.DOING
                updateTask(task)
            }
        }
    }

    private fun getTasks() {
        FirebaseHelper
            .getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getIdUser())
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val taskList = mutableListOf<Task>()
                        for (ds in snapshot.children) {
                            val task = ds.getValue(Task::class.java) as Task
                            if (task.status == Status.TODO) {
                                taskList.add(task)
                            }
                        }
                        binding.progressBar.isVisible = false
                        listEmpty(taskList)

                        taskList.reverse()
                        taskAdapter.submitList(taskList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.i("INFOTESTE", "onCancelled: ")
                    }
                },
            )
    }

    private fun deleteTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .removeValue()
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), R.string.text_delete_success_task, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast
                        .makeText(
                            requireContext(),
                            R.string.text_save_success_frm_task_fragment,
                            Toast.LENGTH_SHORT,
                        ).show()
                } else {
                    Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun listEmpty(tasksList: List<Task>) {
        binding.textInfo.text =
            if (tasksList.isEmpty()) {
                getString(R.string.text_list_task_empty)
            } else {
                ""
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
