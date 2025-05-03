package com.example.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.navigation.databinding.FragmentGalleryBinding
import androidx.fragment.app.activityViewModels

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImageAdapter(emptyList(), viewModel) { image ->
            val action = GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(
                image.uri.toString(),
                image.description
            )
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            this.adapter = adapter
        }

        viewModel.images.observe(viewLifecycleOwner) { images ->
            adapter.updateImages(images)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
