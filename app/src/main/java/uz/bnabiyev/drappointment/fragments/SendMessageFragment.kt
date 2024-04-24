package uz.bnabiyev.drappointment.fragments

import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.databinding.FragmentSendMessageBinding

private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "number"

class SendMessageFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val binding by lazy { FragmentSendMessageBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {

            tvName.text = param1
            tvNumber.text = param2

            btnSend.setOnClickListener {

                val message = edtMessage.text.toString()
                try {

//                    val smsManager:SmsManager = requireContext().getSystemService(SmsManager::class.java)
                    val smsManager = SmsManager.getDefault() as SmsManager
                    smsManager.sendTextMessage(param2, null, message, null, null)
                    // on below line we are sending text message.
//                    smsManager.sendTextMessage(phone, null, text, null, null)

                    Toast.makeText(requireContext(), "Xabar jo'natildi", Toast.LENGTH_LONG)
                        .show()

                } catch (e: Exception) {

                    // on catch block we are displaying toast message for error.
                    Toast.makeText(
                        requireContext(),
                        "Xabar jo'natisg=hda xatolik yuz berdi" + e.message.toString(),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }


                edtMessage.text.clear()

            }

        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

}