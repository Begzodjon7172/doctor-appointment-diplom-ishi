package uz.bnabiyev.drappointment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import uz.bnabiyev.drappointment.adapters.TimeAdapter
import uz.bnabiyev.drappointment.databinding.FragmentBookingBinding
import uz.bnabiyev.drappointment.models.Booking
import uz.bnabiyev.drappointment.models.Doctor
import uz.bnabiyev.drappointment.models.User
import java.text.SimpleDateFormat
import java.util.Calendar

private const val ARG_PARAM1 = "doctor"

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
    private lateinit var timeAdapter: TimeAdapter
    private lateinit var timeList: ArrayList<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var mUser: User
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
        loadTimes()
        timeAdapter = TimeAdapter(timeList) { time ->

        }
//        binding.rv.adapter = timeAdapter

        getDate()
        reference.child("Users").child(userUid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mUser = snapshot.getValue(User::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Toast.makeText(requireContext(), "$year/${month + 1}/$dayOfMonth", Toast.LENGTH_SHORT)
                .show()
            date1 = "$dayOfMonth/${month + 1}/$year"
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

    private fun loadTimes() {
        timeList = ArrayList()
        timeList.add("09:00")
        timeList.add("09:30")
        timeList.add("10:00")
        timeList.add("10:30")
        timeList.add("11:00")
        timeList.add("11:30")
        timeList.add("12:00")
        timeList.add("13:00")
        timeList.add("13:30")
        timeList.add("14:00")
        timeList.add("14:30")
        timeList.add("15:00")
        timeList.add("15:30")
        timeList.add("16:00")
        timeList.add("16:30")
    }

    fun getDate() {
        val date = binding.calendarView.date
        var simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        calendar.timeInMillis = date
        var selectedDate = simpleDateFormat.format(calendar.time)
        Toast.makeText(requireContext(), selectedDate, Toast.LENGTH_SHORT).show()
        date1 = selectedDate
    }

//    fun setDate(day: Int, month: Int, year: Int) {
//        calendar.set(Calendar.YEAR, year)
//        calendar.set(Calendar.MONTH, month - 1)
//        calendar.set(Calendar.DAY_OF_MONTH, day)
//        var millis = calendar.timeInMillis
//        binding.calendarView.date = millis
//    }

}