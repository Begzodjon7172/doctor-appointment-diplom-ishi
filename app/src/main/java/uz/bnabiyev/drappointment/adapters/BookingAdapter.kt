package uz.bnabiyev.drappointment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.databinding.ItemBookingBinding
import uz.bnabiyev.drappointment.models.Booking
import uz.bnabiyev.drappointment.models.Category

class BookingAdapter(
    private val bookingList: ArrayList<Booking>,
    private val itemClick: (Booking) -> Unit
) :
    RecyclerView.Adapter<BookingAdapter.Vh>() {

    inner class Vh(private val itemBookingBinding: ItemBookingBinding) :
        RecyclerView.ViewHolder(itemBookingBinding.root) {
        fun onBind(booking: Booking) {
            itemBookingBinding.image.setImageResource(R.drawable.img_2)
            itemBookingBinding.tvName.text = "Mijoz : ${booking.userName}"
            itemBookingBinding.tvDate.text = "Kuni va vaqti : ${booking.date}, ${booking.time}"
            itemView.setOnClickListener {
                itemClick.invoke(booking)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(bookingList[position])
    }
}