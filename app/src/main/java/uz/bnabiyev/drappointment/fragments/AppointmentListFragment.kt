package uz.bnabiyev.drappointment.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.adapters.BookingAdapter
import uz.bnabiyev.drappointment.adapters.DoctorsAdapter
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
        bookingAdapter = BookingAdapter(list)
        binding.rv.adapter = bookingAdapter


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (i in snapshot.children) {
                    val booking = i.getValue(Booking::class.java)
                    Log.d(TAG, "onDataChange: $booking")
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