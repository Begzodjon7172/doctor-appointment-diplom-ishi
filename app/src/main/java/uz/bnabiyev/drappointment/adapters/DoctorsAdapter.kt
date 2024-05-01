package uz.bnabiyev.drappointment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.databinding.ItemDoctorsBinding
import uz.bnabiyev.drappointment.models.Doctor

class DoctorsAdapter(
    private val doctorList: ArrayList<Doctor>,
    private val itemClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorsAdapter.Vh>() {

    inner class Vh(private val itemDoctorsBinding: ItemDoctorsBinding) :
        RecyclerView.ViewHolder(itemDoctorsBinding.root) {
        fun onBind(doctor: Doctor) {
            itemDoctorsBinding.image.setImageResource(R.drawable.img_1)
            itemDoctorsBinding.tvName.text = "Doctor : ${doctor.firstName}"
            itemDoctorsBinding.tvNumber.text = "Call : ${doctor.phoneNumber}"
            itemView.setOnClickListener {
                itemClick.invoke(doctor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemDoctorsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(doctorList[position])
    }

}