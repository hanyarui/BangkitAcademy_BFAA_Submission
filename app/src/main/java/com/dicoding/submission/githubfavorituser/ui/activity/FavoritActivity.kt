package com.dicoding.submission.githubfavorituser.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission.githubfavorituser.adapter.FavoritAdapter
import com.dicoding.submission.githubfavorituser.databinding.ActivityFavoritBinding
import com.dicoding.submission.githubfavorituser.local.model.UserSearch
import com.dicoding.submission.githubfavorituser.viewmodel.FavoritViewModel

class FavoritActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritBinding
    private lateinit var viewModel: FavoritViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(FavoritViewModel::class.java)

        viewModel.getFavorite()?.observe(this) { favoritUser ->
            setFavoriteData(favoritUser)
        }

        val layoutManager = LinearLayoutManager(this@FavoritActivity)
        binding.rvFavorit.layoutManager = layoutManager
    }

    private fun setFavoriteData(favoriteUsers: List<UserSearch>) {
        val listUser = ArrayList<UserSearch>()
        for (user in favoriteUsers) {
            listUser.clear()
            listUser.addAll(favoriteUsers)
        }
        Log.d("Data", "Data: $listUser")
        val adapter = FavoritAdapter(listUser)
        binding.rvFavorit.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoritAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserSearch) {
                showSelectedUser(data)

            }
        })
    }

    private fun showSelectedUser(data: UserSearch) {
        val moveWithParcelableIntent = Intent(this@FavoritActivity, DetailActivity::class.java)
        moveWithParcelableIntent.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(moveWithParcelableIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}