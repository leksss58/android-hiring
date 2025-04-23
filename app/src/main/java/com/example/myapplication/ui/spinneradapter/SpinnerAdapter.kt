package com.example.myapplication.ui.spinneradapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.databinding.CustomSpinnerItemBinding
import com.example.myapplication.databinding.CustomSpinnerPreviewItemBinding

class SpinnerAdapter(
    context: Context,
    private val items: List<Int>,
    private val selected: () -> Int
) : ArrayAdapter<Int>(context, 0, items) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = CustomSpinnerItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        val value = getItem(position) ?: return binding.root
        binding.textViewItem.text = "$value"

        if (value == selected()) {
            binding.textViewItem.setTypeface(null, Typeface.BOLD)
            binding.checkIcon.visibility = View.VISIBLE
        } else {
            binding.textViewItem.setTypeface(null, Typeface.NORMAL)
            binding.checkIcon.visibility = View.INVISIBLE
        }

        return binding.root
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = CustomSpinnerPreviewItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        val value = getItem(position) ?: return binding.root
        binding.textViewItem.text = "$value"

        return binding.root
    }
}