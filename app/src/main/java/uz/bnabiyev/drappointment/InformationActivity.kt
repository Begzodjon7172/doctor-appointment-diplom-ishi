package uz.bnabiyev.drappointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.bnabiyev.drappointment.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityInformationBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}