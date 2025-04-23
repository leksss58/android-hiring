package com.example.myapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.models.GenderType
import com.example.myapplication.ui.spinneradapter.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = checkNotNull(_binding)
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAgeSpinner()
        setGenderButton()
        setSendMessage()

        viewModel
            .watchAge
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { age -> setAgeSpinner(age) }
            .launchIn(lifecycleScope)

        viewModel
            .watchGender
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { gender -> renderGenderButton(gender) }
            .launchIn(lifecycleScope)

        viewModel
            .responses
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { response ->
                response?.let {
                    Toast.makeText(this, "Ответ: $it", Toast.LENGTH_LONG).show()
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setSendMessage() {
        binding.submitButton.setOnClickListener {
            viewModel.sendMessage()
        }
    }

    private fun setGenderButton() {
        with(binding) {
            genderLayout.maleContainer.setOnClickListener {
                viewModel.setGender(GenderType.MALE)
            }

            genderLayout.femaleContainer.setOnClickListener {
                viewModel.setGender(GenderType.FEMALE)
            }
        }
    }

    private fun renderGenderButton(genderType: GenderType) {
        when (genderType) {
            GenderType.MALE -> {
                binding.genderLayout.femaleContainer.isSelected = false
                binding.genderLayout.maleContainer.isSelected = true
                binding.submitButton.isEnabled = true
            }

            GenderType.FEMALE -> {
                binding.genderLayout.femaleContainer.isSelected = true
                binding.genderLayout.maleContainer.isSelected = false
                binding.submitButton.isEnabled = true
            }

            GenderType.NONE -> {
                binding.genderLayout.femaleContainer.isSelected = false
                binding.genderLayout.maleContainer.isSelected = false
                binding.submitButton.isEnabled = false
            }
        }
    }

    private fun setupAgeSpinner() {
        val ages = (16..30).toList()
        var selectedAge = ages.first()
        val adapter = SpinnerAdapter(this, ages) { selectedAge }
        binding.ageLayout.ageSpinner.adapter = adapter

        binding.ageLayout.ageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedAge = ages[position]
                    adapter.notifyDataSetChanged()
                    viewModel.setAge(selectedAge)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setAgeSpinner(age: Int) {
        binding.ageLayout.ageSpinner.setSelection(age - 16)
    }
}