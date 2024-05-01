package uz.bnabiyev.drappointment.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.adapters.UserAdapter
import uz.bnabiyev.drappointment.databinding.FragmentDoctorHomeBinding
import uz.bnabiyev.drappointment.databinding.UserDialogBinding
import uz.bnabiyev.drappointment.models.User

class DoctorHomeFragment : Fragment() {

    private val binding by lazy { FragmentDoctorHomeBinding.inflate(layoutInflater) }
    private lateinit var reference: DatabaseReference
    private lateinit var userUidList: HashSet<String>
    private lateinit var registeredUserList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private var userRoom = ""
    private var doctorRoom = ""

    //    var doctorRoom: String? = null
//    var userRoom: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference
        registeredUserList = ArrayList()


        userAdapter = UserAdapter(registeredUserList) { user ->

            val builder = AlertDialog.Builder(requireContext())
            val userDialogBinding = UserDialogBinding.inflate(layoutInflater)

            builder.setView(userDialogBinding.root)

            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            userDialogBinding.apply {

                closeImg.setOnClickListener {
                    alertDialog.dismiss()
                }

                callBtn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_CALL);
                    intent.data = Uri.parse("tel:${user.phoneNumber}")
                    startActivity(intent)
                    alertDialog.dismiss()
                }

                smsBtn.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("name", user.firstName)
                    bundle.putString("number", user.phoneNumber)
                    findNavController().navigate(
                        R.id.sendMessageFragment2,
                        bundle
                    )
                    alertDialog.dismiss()
                }

                chatBtn.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_doctorHomeFragment_to_doctorChatFragment,
                        bundleOf("user" to user)
                    )
                    alertDialog.dismiss()
                }

                bookingBtn.setOnClickListener {
                    findNavController().navigate(
                        R.id.doctorAppointmentListFragment,
                        bundleOf("user" to user)
                    )
                    alertDialog.dismiss()
                }

                okBtn.setOnClickListener {
                    alertDialog.dismiss()
                }

                deleteBtn.setOnClickListener {

                    userRoom = auth.currentUser?.uid + user.uid
                    doctorRoom = user.uid + auth.currentUser?.uid

                    reference.child(auth.currentUser?.uid!!)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (i in snapshot.children) {
                                    val userUid = i.getValue(String::class.java)
                                    if (userUid == user.uid) {
                                        reference.child(auth.currentUser?.uid!!).child(i.key!!)
                                            .removeValue()
                                            .addOnSuccessListener {
                                                registeredUserList.remove(user)
                                                reference.child("appointments").child(userRoom)
                                                    .removeValue()
                                            }
                                        reference.child("appointments").child(doctorRoom)
                                            .removeValue()
                                        break
                                    }
                                }
                                onResume()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                    alertDialog.dismiss()
                }

            }
        }


        return binding.root
    }


    private fun getUsers() {
        reference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                registeredUserList.clear()
                for (i in snapshot.children) {
                    val user = i.getValue(User::class.java)
                    userUidList.forEach {
                        if (user?.uid == it) {
                            registeredUserList.add(user)
                        }
                    }
                    userAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getUsersUid() {
        userUidList = HashSet()
        reference.child(auth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userUidList.clear()
                for (i in snapshot.children) {
                    val userUid = i.getValue(String::class.java)
                    userUidList.add(userUid!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onResume() {
        super.onResume()

        getUsersUid()
        getUsers()

        binding.rv.adapter = userAdapter
    }

}