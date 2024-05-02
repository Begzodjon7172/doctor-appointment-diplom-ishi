package uz.bnabiyev.drappointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.bnabiyev.drappointment.databinding.ActivityRegisterBinding
import uz.bnabiyev.drappointment.models.Doctor
import uz.bnabiyev.drappointment.models.User

private const val TAG = "RegisterActivity"

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth;
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var mastersList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference
        addData()


        binding.apply {
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == radioPatient.id) {
                    spinner.visibility = View.GONE
                } else {
                    spinner.visibility = View.VISIBLE
                }
            }

            nextButton.setOnClickListener {
                if (isValid()) {
                    val ism = edtFirstName.text.toString()
                    val familiya = edtLastName.text.toString()
                    val parol = edtPassword.text.toString()
                    val tel = "+998${edtPhoneNumber.text}"
                    val email = edtEmailAddress.text.toString()
                    val master = mastersList[spinner.selectedItemPosition]

                    auth.createUserWithEmailAndPassword(email, parol)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid
                                if (radioDoctor.isChecked) {
                                    val doctor =
                                        Doctor(ism, familiya, email, tel, parol, master, uid)
                                    reference.child("Doctors").child(uid!!).setValue(doctor)
                                } else {
                                    val user = User(ism, familiya, email, tel, parol, uid)
                                    reference.child("Users").child(uid!!).setValue(user)
                                }


                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Authentication failed!!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                        }


                }
            }
            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun addData() {
        mastersList = ArrayList()
        mastersList.add("ENDOKRINOLOG")
        mastersList.add("NEUROPOTOLOG")
        mastersList.add("STOMATOLOG")
        mastersList.add("JARROH")
        mastersList.add("LOR")
        mastersList.add("GINEKOLOG")
        mastersList.add("PULMONOLOG")
        mastersList.add("OKULIST")
        mastersList.add("TERAPEVT")
        mastersList.add("UROLOG")
        mastersList.add("DERMATOLOK")
        mastersList.add("KARDIOLOG")
    }

    private fun isValid(): Boolean {
        binding.apply {
            if (edtEmailAddress.text.toString() == "") {
                Toast.makeText(this@RegisterActivity, "Email kiritilmagan", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else if (edtPassword.text.toString() == "") {
                Toast.makeText(this@RegisterActivity, "Parol kiritilmagan", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else if (edtFirstName.text.toString() == "") {
                Toast.makeText(this@RegisterActivity, "Ism kiritilmagan", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else if (edtLastName.text.toString() == "") {
                Toast.makeText(this@RegisterActivity, "Familiya kiritilmagan", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else if (edtPhoneNumber.text.toString() == "") {
                Toast.makeText(
                    this@RegisterActivity,
                    "Telefon raqam kiritilmagan",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return false
            } else if (edtPassword.text.toString() != edtConfirmPassword.text.toString()) {
                Toast.makeText(this@RegisterActivity, "Parollar mos kelmadi", Toast.LENGTH_SHORT)
                    .show()
                return false
            } else return true
        }
    }
}