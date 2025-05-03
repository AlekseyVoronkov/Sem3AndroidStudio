package com.example.navigation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.navigation.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args by navArgs<DetailFragmentArgs>()
        val imageUri = Uri.parse(args.imageUri)
        val description = args.imageDescription

        Glide.with(this)
            .load(imageUri)
            .into(binding.detailImageView)

        binding.descriptionTextView.text = if (description.isEmpty()) "No description" else description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
