package uz.bnabiyev.drappointment.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.adapters.BookingAdapter
import uz.bnabiyev.drappointment.databinding.CustomAlertDialogBinding
import uz.bnabiyev.drappointment.databinding.FragmentAppointmentListBinding
import uz.bnabiyev.drappointment.databinding.UserDialogBinding
import uz.bnabiyev.drappointment.models.Booking
import uz.bnabiyev.drappointment.models.Doctor
import uz.bnabiyev.drappointment.models.User

private const val ARG_PARAM1 = "user"

class DoctorAppointmentListFragment : Fragment() {

    private var param1: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as User?
        }
    }

    private val binding by lazy { FragmentAppointmentListBinding.inflate(layoutInflater) }
    private lateinit var reference: DatabaseReference
    private lateinit var list: ArrayList<Booking>
    private lateinit var bookingAdapter: BookingAdapter
    var doctorRoom: String? = null
    var userRoom: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val doctor = arguments?.getSerializable("user") as User?

        val doctorUid = doctor?.uid
        val userUid = FirebaseAuth.getInstance().currentUser?.uid

        userRoom = doctorUid + userUid
        doctorRoom = userUid + doctorUid

        list = ArrayList()
        reference = FirebaseDatabase.getInstance().reference
        bookingAdapter = BookingAdapter(list) { booking ->
            val builder = AlertDialog.Builder(requireContext())
            val customAlertDialogBinding = CustomAlertDialogBinding.inflate(layoutInflater)

            builder.setView(customAlertDialogBinding.root)

            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            customAlertDialogBinding.endBtn.setOnClickListener {

//                reference.removeValue().addOnSuccessListener {
//                    FirebaseDatabase.getInstance().getReference("appointments")
//                        .child(doctorRoom!!)
//                        .removeValue()
//                    alertDialog.dismiss()
//                    bookingAdapter.notifyDataSetChanged()
//                }
//                FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().currentUser?.uid!!)
//                    .child(param1?.uid!!).removeValue()

                reference.child("appointments").child(userRoom!!).child("messages")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            list.clear()
                            for (i in snapshot.children) {
                                val booking1 = i.getValue(Booking::class.java)
                                if (booking1?.date != booking.date && booking1?.time != booking.time) {
                                    list.add(booking1!!)
                                } else {
                                    reference.child("appointments").child(userRoom!!)
                                        .child("messages").child(i.key!!).removeValue()
                                }
                            }
                            bookingAdapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                reference.child("appointments").child(doctorRoom!!).child("messages")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (i in snapshot.children) {
                                val booking1 = i.getValue(Booking::class.java)
                                if (booking1?.date == booking.date && booking1?.time == booking.time) {
                                    reference.child("appointments").child(doctorRoom!!)
                                        .child("messages").child(i.key!!).removeValue()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                alertDialog.dismiss()


            }
        }
        binding.rv.adapter = bookingAdapter


        reference.child("appointments").child(userRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (i in snapshot.children) {
                        val booking = i.getValue(Booking::class.java)
                        if (booking != null) {
                            list.add(booking)
                        }
                    }
                    bookingAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        return binding.root
    }

}