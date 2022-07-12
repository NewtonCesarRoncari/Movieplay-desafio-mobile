package com.nroncari.movieplay.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nroncari.movieplay.R
import com.nroncari.movieplay.data.model.MovieDTO
import com.nroncari.movieplay.databinding.FragmentMyListBinding
import com.nroncari.movieplay.domain.mapper.MovieDTOToPresentationMapper
import com.nroncari.movieplay.presentation.ui.adapter.MyListAdapter
import com.nroncari.movieplay.presentation.viewmodel.MyListViewModel
import com.nroncari.movieplay.presentation.viewmodel.StateAppComponentsViewModel
import com.nroncari.movieplay.presentation.viewmodel.VisualComponents
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyListFragment : Fragment() {

    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!
    private val appComponentsViewModel: StateAppComponentsViewModel by sharedViewModel()
    private val viewModel: MyListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appComponentsViewModel.havComponent = VisualComponents(true)

        listener()
    }

    private fun listener() {
        binding.networkErrorAnimation.setAnimation("anim/list_empty.json")

        viewModel.listAll().observe(viewLifecycleOwner) {
            if (it.isEmpty()) initAnimationEmpty()
            initAdapter(it)
        }
    }

    private fun initAdapter(movieList: List<MovieDTO>) {
        binding.rvMyMovies.adapter = MyListAdapter(
            movieList.map {
                MovieDTOToPresentationMapper().map(it)
            }
        ) { movieId -> goToMovieDetailFragment(movieId) }
    }

    private fun goToMovieDetailFragment(movieId: Long) {
        val direction = MyListFragmentDirections
            .actionMyListFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(direction)
    }

    private fun initAnimationEmpty() {
        with(binding.networkErrorAnimation) {
            scaleX = ANIMATION_SCALE
            scaleY = ANIMATION_SCALE
            visibility = View.VISIBLE
            playAnimation()
        }
        binding.errorMsgMovies.visibility = View.VISIBLE
        binding.errorMsgMovies.text = getString(R.string.message_list_empty)
    }

    companion object Const {
        const val ANIMATION_SCALE = 0.5f
    }
}