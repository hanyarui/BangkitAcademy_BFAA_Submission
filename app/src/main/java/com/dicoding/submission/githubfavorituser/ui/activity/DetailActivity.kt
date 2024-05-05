package com.dicoding.submission.githubfavorituser.ui.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.submission.githubfavorituser.R
import com.dicoding.submission.githubfavorituser.adapter.PagerAdapter
import com.dicoding.submission.githubfavorituser.databinding.ActivityDetailBinding
import com.dicoding.submission.githubfavorituser.local.model.DetailResponse
import com.dicoding.submission.githubfavorituser.local.model.UserSearch
import com.dicoding.submission.githubfavorituser.util.Loading
import com.dicoding.submission.githubfavorituser.viewmodel.DetailViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FRAGMENT = "extra_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = "Github User Detail"
        supportActionBar?.title = title

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        viewModel.listDetail.observe(this) { detailList ->
            setDataToView(detailList)
        }

        viewModel.isLoading.observe(this) {
            Loading().showLoading(it, binding.progressBar)
        }

        setTabLayoutView()
        addToFavorite()
    }

    private fun setTabLayoutView() {
        val userIntent = intent.getParcelableExtra<UserSearch>(EXTRA_USER) as UserSearch
        viewModel.getGithubUser(userIntent.login)

        val sectionPagerAdapter = PagerAdapter(this, Bundle().apply { putString(EXTRA_FRAGMENT, userIntent.login) })
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        val tabTitle = resources.getStringArray(R.array.tab_title)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    private fun setDataToView(detailList: DetailResponse) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(detailList.avatarUrl)
                .circleCrop()
                .into(detailsIvAvatar)
            detailsTvName.text = detailList.name ?: "No Name."
            detailsTvUsername.text = detailList.login
            detailsTvBio.text = if (detailList.bio == null) "This person haven\'t set their bio yet." else detailList.bio.toString()
            detailsTvFollower.text = resources.getString(R.string.follower, detailList.followers)
            detailsTvFollowing.text = resources.getString(R.string.following, detailList.following)

        }
    }

    private fun addToFavorite() {
        val user = intent.getParcelableExtra<UserSearch>(EXTRA_USER) as UserSearch
        val login = user.login
        val id = user.id
        val avatarUrl = user.avatarUrl
        val htmlUrl = user.htmlUrl

        var isFavorite = false

        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if(count != null){
                    if(count>0){
                        binding.btnFavorite.changeIconColor(R.color.red)
                        isFavorite = true
                    }
                    else{
                        binding.btnFavorite.changeIconColor(R.color.white)
                        isFavorite =false
                    }
                }
            }
        }

        binding.btnFavorite.setOnClickListener{
            isFavorite = !isFavorite
            if (isFavorite){
                viewModel.addFavorite(login, avatarUrl, htmlUrl, id)
                binding.btnFavorite.changeIconColor(R.color.red)
            }
            else{
                viewModel.removeFavorite(id)
                binding.btnFavorite.changeIconColor(R.color.white)
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
    }
}