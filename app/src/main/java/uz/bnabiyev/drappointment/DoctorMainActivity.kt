package uz.bnabiyev.drappointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import uz.bnabiyev.drappointment.databinding.ActivityDoctorMainBinding

class DoctorMainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDoctorMainBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logOut) {
            // write logic for log out
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.information) {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }
        return true
    }
}