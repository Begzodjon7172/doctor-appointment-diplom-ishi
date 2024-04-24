package uz.bnabiyev.drappointment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bnabiyev.drappointment.databinding.ItemTimeBinding

class TimeAdapter(val list: ArrayList<String>, private val itemClick: (String) -> Unit) :
    RecyclerView.Adapter<TimeAdapter.Vh>() {

    inner class Vh(private val itemTimeBinding: ItemTimeBinding) :
        RecyclerView.ViewHolder(itemTimeBinding.root) {
        fun onBind(time: String) {
            itemTimeBinding.tvTime.text = time
            itemView.setOnClickListener {
                itemClick.invoke(time)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }
}