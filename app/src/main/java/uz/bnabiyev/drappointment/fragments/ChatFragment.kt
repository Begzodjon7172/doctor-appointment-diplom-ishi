package uz.bnabiyev.drappointment.fragments

import android.os.Bundle
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
import uz.bnabiyev.drappointment.adapters.MessageAdapter
import uz.bnabiyev.drappointment.databinding.FragmentChatBinding
import uz.bnabiyev.drappointment.models.Doctor
import uz.bnabiyev.drappointment.models.Message

private const val ARG_PARAM1 = "doctor"

class ChatFragment : Fragment() {

    private var param1: Doctor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Doctor?

        }
    }

    private val binding by lazy { FragmentChatBinding.inflate(layoutInflater) }

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var reference: DatabaseReference

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val receiverUid = param1?.uid
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        reference = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        binding.tvName.text = param1?.firstName

        messageList = ArrayList()
        messageAdapter = MessageAdapter(requireContext(), messageList)
        binding.rv.adapter = messageAdapter

        reference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (i in snapshot.children) {
                        val message = i.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        // adding the message to database
        binding.btnSend.setOnClickListener {

            val message = binding.messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            reference.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    reference.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            binding.messageBox.setText("")

        }


        return binding.root
    }

}