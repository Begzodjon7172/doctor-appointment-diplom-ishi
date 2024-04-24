package uz.bnabiyev.drappointment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.bnabiyev.drappointment.databinding.ActivityLoginBinding
import uz.bnabiyev.drappointment.models.Doctor

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth;
    private lateinit var reference: DatabaseReference
    private lateinit var list: ArrayList<Doctor>
    private var isDoctor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
//        if (auth.currentUser != null) {
//            finish()
//            startActivity(Intent(this, MainActivity::class.java))
//        }

        reference = FirebaseDatabase.getInstance().getReference("Doctors")
        list = ArrayList()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (i in snapshot.children) {
                    val doctor = i.getValue(Doctor::class.java)
                    list.add(doctor!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.apply {
            loginBtn.setOnClickListener {
                if (isValid()) {
                    val email = edtEmail.text.toString()
                    val password = edtPassword.text.toString()
                    for (i in 0..<list.size) {
                        if (list[i].email == email) {
                            isDoctor = true
                            break
                        }
                    }
                    if (isDoctor) {
                        loginDoctor(email, password)
                    } else {
                        loginUser(email, password)
                    }
                }
            }
        }


    }

    private fun loginUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for loggin in user

                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        baseContext,
                        "User does not exist!!!",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }

    private fun loginDoctor(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for loggin in user

                    val intent = Intent(this, DoctorMainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        baseContext,
                        "User does not exist!!!",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }


    private fun isValid(): Boolean {
        binding.apply {
            if (edtEmail.text.toString() == "") {
                Toast.makeText(this@LoginActivity, "Email kiritilmagan", Toast.LENGTH_SHORT).show()
                return false
            } else if (edtPassword.text.toString() == "") {
                Toast.makeText(this@LoginActivity, "Password kiritilmagan", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else return true
        }
    }
}