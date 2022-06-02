package ro.valentin.cinemania.presentation.movie_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ro.valentin.cinemania.R
import ro.valentin.cinemania.core.Constants
import ro.valentin.cinemania.core.Constants.LOG_TAG
import ro.valentin.cinemania.core.Utils.Companion.hide
import ro.valentin.cinemania.core.Utils.Companion.show
import ro.valentin.cinemania.domain.model.Response
import ro.valentin.cinemania.presentation.auth.AuthActivity
import ro.valentin.cinemania.presentation.auth.AuthViewModel
@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var loaderProgressBar: ProgressBar
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        Log.d(LOG_TAG, "MovieDetailsActivity reached")

        setNavController()
        initProgressBar()
        getAuthState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.signOut -> {
                signOut()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        viewModel.firebaseSignOut().observe(this) {  response ->
            when(response) {
                is Response.Loading -> loaderProgressBar.show()
                is Response.Success -> loaderProgressBar.hide()
                is Response.Error -> {
                    loaderProgressBar.hide()
                    Log.d(LOG_TAG, response.error)
                }
            }

        }
    }


    private fun initProgressBar() {
        loaderProgressBar = findViewById(R.id.loadingProgressBar)
    }

    private fun getAuthState() {
        viewModel.authStateListener().observe(this) { state ->
            if(state) {
                goToAuthActivity(intent.extras?.getInt("movieId")!!)
            }
        }
    }

    private fun goToAuthActivity(movieId: Int) {
        val authIntent = Intent(this, AuthActivity::class.java)
        authIntent.putExtra("movieId", movieId)

        Log.d(LOG_TAG, "from goToAuthActivity() movieId = $movieId")

        startActivity(authIntent)
        finish()
    }

    private fun setNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_movie_details) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



}