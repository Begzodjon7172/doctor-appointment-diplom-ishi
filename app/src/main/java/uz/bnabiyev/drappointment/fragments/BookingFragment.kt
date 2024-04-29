package uz.bnabiyev.drappointment.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.databinding.FragmentBookingBinding
import uz.bnabiyev.drappointment.models.Booking
import uz.bnabiyev.drappointment.models.Doctor
import uz.bnabiyev.drappointment.models.User
import uz.bnabiyev.drappointment.utils.TimeGenerator
import java.text.SimpleDateFormat
import java.util.Calendar

private const val ARG_PARAM1 = "doctor"

private const val TAG = "BookingFragment"

class BookingFragment : Fragment() {

    private var param1: Doctor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Doctor
        }
    }

    private val binding by lazy { FragmentBookingBinding.inflate(layoutInflater) }
    private lateinit var calendar: Calendar
    private lateinit var timeList: ArrayList<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var mUser: User
    private lateinit var arrayAdapter: ArrayAdapter<String>
    var doctorRoom: String? = null
    var userRoom: String? = null
    private var date1 = ""
    private var time = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar = Calendar.getInstance()
        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        val doctorUid = param1?.uid
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)

        val selectedDate = "$day/$month/$year"
        Log.d(TAG, "onCreateView: kun: $selectedDate")

        getDate(selectedDate)

        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, timeList)
        binding.spinner.adapter = arrayAdapter


        reference.child("Users").child(userUid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mUser = snapshot.getValue(User::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date1 = "$dayOfMonth/${month + 1}/$year"
            getDate(date1)
        }

        binding.listBtn.setOnClickListener {
            if (param1?.uid != null) {
                val bundle = Bundle()
                bundle.putSerializable("doctorUid", param1)
                findNavController().navigate(
                    R.id.action_bookingFragment_to_appointmentListFragment,
                    bundle
                )
            }
        }

        reference = FirebaseDatabase.getInstance().reference
//        getUserData()
        userRoom = doctorUid + userUid
        doctorRoom = userUid + doctorUid


        binding.saveBtn.setOnClickListener {
            time = timeList[binding.spinner.selectedItemPosition]

            val booking =
                Booking(
                    date1,
                    time,
                    auth.currentUser?.uid,
                    param1?.uid,
                    mUser.firstName,
                    param1?.firstName
                )

            reference.child("appointments").child(userRoom!!).child("messages").push()
                .setValue(booking).addOnSuccessListener {
                    reference.child("appointments").child(doctorRoom!!).child("messages").push()
                        .setValue(booking)
                    Toast.makeText(requireContext(), "Saqlandi!!!", Toast.LENGTH_SHORT).show()
                }

            reference.child(doctorUid!!).child("dataAndTime").push()
                .setValue(booking)

            FirebaseDatabase.getInstance().reference.child(param1?.uid!!).push()
                .setValue(userUid).addOnSuccessListener {
                    Toast.makeText(requireContext(), "emailgayam saqlandi", Toast.LENGTH_SHORT)
                        .show()
                }

        }

        return binding.root
    }

//    private fun getUserData() {
//        reference.child("Users").child(userUid!!)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    mUser = snapshot.getValue(User::class.java)!!
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })
//    }

    fun getDate(dateAndTime: String) {

        timeList = TimeGenerator.generateTimes()

        reference.child(param1?.uid!!).child("dataAndTime")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val booking1 = i.getValue(Booking::class.java)
                        if (booking1?.date == dateAndTime) {
                            Log.d(TAG, "onDataChange: remove date : ${booking1.time}")
                            timeList.remove(booking1.time)
                        }
                    }
                    arrayAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        Toast.makeText(requireContext(), dateAndTime, Toast.LENGTH_SHORT).show()
        date1 = dateAndTime
    }

//    fun setDate(day: Int, month: Int, year: Int) {
//        calendar.set(Calendar.YEAR, year)
//        calendar.set(Calendar.MONTH, month - 1)
//        calendar.set(Calendar.DAY_OF_MONTH, day)
//        var millis = calendar.timeInMillis
//        binding.calendarView.date = millis
//    }

}