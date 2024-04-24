package uz.bnabiyev.drappointment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import uz.bnabiyev.drappointment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
//        val fragmentContainer =
//            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//        val navController = fragmentContainer.navController
//        binding.bottomNavView.setupWithNavController(navController)


    }

}