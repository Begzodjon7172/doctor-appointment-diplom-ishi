package uz.bnabiyev.drappointment.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.adapters.BookingAdapter
import uz.bnabiyev.drappointment.adapters.DoctorsAdapter
import uz.bnabiyev.drappointment.databinding.CustomAlertDialogBinding
import uz.bnabiyev.drappointment.databinding.FragmentAppointmentListBinding
import uz.bnabiyev.drappointment.models.Booking
import uz.bnabiyev.drappointment.models.Doctor

private const val ARG_PARAM1 = "doctorUid"

private const val TAG = "AppointmentListFragment"

class AppointmentListFragment : Fragment() {

    private var param1: Doctor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Doctor?
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
        val doctor = arguments?.getSerializable("doctorUid") as Doctor?

        val doctorUid = doctor?.uid
        val userUid = FirebaseAuth.getInstance().currentUser?.uid

        userRoom = doctorUid + userUid
        doctorRoom = userUid + doctorUid

        list = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("appointments").child(userRoom!!)
            .child("messages")

        bookingAdapter = BookingAdapter(list) { booking ->
            val builder = AlertDialog.Builder(requireContext())
            val customAlertDialogBinding = CustomAlertDialogBinding.inflate(layoutInflater)

            builder.setView(customAlertDialogBinding.root)

            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            alertDialog.show()

//            customAlertDialogBinding.endBtn.setOnClickListener {
//
//                reference.child("appointments").child(booking.userUid + booking.doctorUid)
//                    .child("messages")
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (i in snapshot.children) {
//                                val booking1 = i.getValue(Booking::class.java)
//                                if (booking.date == booking1?.date && booking.time == booking1?.time) {
//                                    reference.child("appointments")
//                                        .child(booking.userUid + booking.doctorUid)
//                                        .child("messages").child(i.key!!).removeValue()
//                                        .addOnSuccessListener {
//                                            list.remove(booking1)
//                                        }
//                                }
//                            }
//                            bookingAdapter.notifyDataSetChanged()
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//
//                        }
//                    })
//
//                reference.child("appointments").child(booking.doctorUid + booking.userUid)
//                    .child("messages")
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (j in snapshot.children) {
//                                val booking2 = j.getValue(Booking::class.java)
//                                if (booking2?.date == booking.date && booking2?.time == booking.time) {
//                                    reference.child("appointments")
//                                        .child(booking.doctorUid + booking.userUid)
//                                        .child("messages").child(j.key!!).removeValue()
//                                }
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//
//                        }
//                    })
//
//                reference.child("booking").child(doctorUid!!)
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (i in snapshot.children){
//                                val booking3 = i.getValue(Booking::class.java)
//                                if (booking3?.date == booking.date && booking3?.time == booking.time) {
//                                    reference.child("booking")
//                                        .child(doctorUid).child(i.key!!).removeValue()
//                                }
//
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//
//                        }
//
//                    })
//
//
//                alertDialog.dismiss()
//
//
//            }
        }

        binding.rv.adapter = bookingAdapter


        reference.addValueEventListener(object : ValueEventListener {
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