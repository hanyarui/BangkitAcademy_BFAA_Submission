    package com.dicoding.submission.githubfavorituser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission.githubfavorituser.R
import com.dicoding.submission.githubfavorituser.adapter.FollowingAdapter
import com.dicoding.submission.githubfavorituser.databinding.FragmentFollowerBinding
import com.dicoding.submission.githubfavorituser.local.model.UserSearch
import com.dicoding.submission.githubfavorituser.ui.activity.DetailActivity
import com.dicoding.submission.githubfavorituser.util.Loading
import com.dicoding.submission.githubfavorituser.viewmodel.FollowingViewModel

    class FollowingFragment : Fragment() {
        private var _binding: FragmentFollowerBinding? = null
        private val binding get() = _binding!!
        private lateinit var viewModel: FollowingViewModel
        private val Load = Loading()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                FollowingViewModel::class.java)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentFollowerBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            viewModel.isLoading.observe(viewLifecycleOwner) {
                Load.showLoading(it, binding.progressBar)
            }
            viewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                setDataToFragment(listFollowing)
            }

            viewModel.getFollowing(arguments?.getString(DetailActivity.EXTRA_FRAGMENT).toString())
        }

        /**
         * Function to set the data from API into fragment's view.
         */
        private fun setDataToFragment(listFollowing: List<UserSearch>) {
            val listUser = ArrayList<UserSearch>()
            with(binding) {
                for (user in listFollowing) {
                    listUser.clear()
                    listUser.addAll(listFollowing)
                }
                rvFollower.layoutManager = LinearLayoutManager(context)
                val adapter = FollowingAdapter(listFollowing)
                rvFollower.adapter = adapter
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }
    }