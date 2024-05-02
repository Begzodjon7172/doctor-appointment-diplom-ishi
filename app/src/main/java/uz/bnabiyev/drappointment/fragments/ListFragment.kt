package uz.bnabiyev.drappointment.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.adapters.DoctorsAdapter
import uz.bnabiyev.drappointment.databinding.DoctorDialogBinding
import uz.bnabiyev.drappointment.databinding.FragmentListBinding
import uz.bnabiyev.drappointment.models.Doctor

private const val TAG = "ListFragment"
private const val ARG_PARAM1 = "category"

class ListFragment : Fragment() {

    private var param1: String? = null
    private lateinit var list: ArrayList<Doctor>
    private lateinit var doctorsAdapter: DoctorsAdapter
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    private val binding by lazy { FragmentListBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("Doctors")
        doctorsAdapter = DoctorsAdapter(list) { doctor: Doctor ->
            val builder = AlertDialog.Builder(requireContext())
            val doctorsBinding = DoctorDialogBinding.inflate(layoutInflater)

            builder.setView(doctorsBinding.root)

            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            doctorsBinding.apply {

                closeImg.setOnClickListener {
                    alertDialog.dismiss()
                }

                callBtn.setOnClickListener {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent = Intent(Intent.ACTION_CALL);
                        intent.data = Uri.parse("tel:${doctor.phoneNumber}")
                        startActivity(intent)
                        alertDialog.dismiss()
                    } else {
                        // Dialog chiqaradi
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
                        alertDialog.dismiss()
                    }

                }

                smsBtn.setOnClickListener {

                    // Ruxsat bor yo'qligini tekshiradi
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.SEND_SMS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val bundle = Bundle()
                        bundle.putString("name", doctor.firstName)
                        bundle.putString("number", doctor.phoneNumber)
                        findNavController().navigate(
                            R.id.action_listFragment_to_sendMessageFragment,
                            bundle
                        )
                        alertDialog.dismiss()
                    } else {
                        // Dialog chiqaradi
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), 1)
                        alertDialog.dismiss()
                    }

                }

                chatBtn.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_listFragment_to_chatFragment,
                        bundleOf("doctor" to doctor)
                    )
                    alertDialog.dismiss()
                }

                bookingBtn.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_listFragment_to_bookingFragment,
                        bundleOf("doctor" to doctor)
                    )
                    alertDialog.dismiss()
                }

                okBtn.setOnClickListener {
                    alertDialog.dismiss()
                }

            }
        }

        binding.rv.adapter = doctorsAdapter

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (i in snapshot.children) {
                    val doctor = i.getValue(Doctor::class.java)
                    Log.d(TAG, "onDataChange: $doctor")
                    if (doctor?.master == param1)
                        list.add(doctor!!)
                }
                doctorsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        Log.d(TAG, "onCreateView: $list")

        return binding.root
    }

}