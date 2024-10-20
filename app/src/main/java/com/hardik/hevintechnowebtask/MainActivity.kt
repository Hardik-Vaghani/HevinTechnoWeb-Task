package com.hardik.hevintechnowebtask

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hardik.hevintechnowebtask.databinding.ActivityMainBinding
import com.hardik.hevintechnowebtask.domain.model.UserModel
import com.hardik.hevintechnowebtask.domain.use_case.GetUserUseCase
import com.hardik.hevintechnowebtask.presentation.MainViewModel
import com.hardik.hevintechnowebtask.presentation.ui.home.HomeFragment
import com.hardik.hevintechnowebtask.presentation.viewModelFactory
import com.hardik.hevintechnowebtask.utillities.FragmentSessionUtils

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.appBarMain.toolbar)
        // Enable the Up button
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentSessionUtils = FragmentSessionUtils.getInstance()

        if (savedInstanceState == null){
            val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
            if (currentFragment !is HomeFragment) { fragmentSessionUtils.switchFragment(
                supportFragmentManager,
                HomeFragment(),//HomeFragment.newInstance(param1 = "file1", param2 = "file2"),//HomeFragment(),
                false,
            )
            }
        }
        mainViewModel = ViewModelProvider(this, viewModelFactory { MainViewModel(GetUserUseCase(MyApp.appModule)) }).get(MainViewModel::class.java)


        mainViewModel.state.observe(this){
            it?.let {
                if (it.isLoading){
                    Log.d(TAG, "onCreate: It Is Loading!!!")
                }
                if (it.error.isNotBlank()){
                    Log.d(TAG, "onCreate: It Has Error!!!\t${it.error}")
                }
                if (it.users.isNotEmpty()){
                    for (user: UserModel in it.users){
                        Log.e(TAG, "onCreate: user: ${user.firstName}" )
                    }
                }
            }
        }

        Thread {
            try {
                Thread.sleep(5000L) // Sleep for 2 seconds

//                fragmentSessionUtils.switchFragment(supportFragmentManager, HomeFragment(),true)
                Log.v(TAG, "onCreate: ${mainViewModel.state.value}")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start() // Start the thread
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}