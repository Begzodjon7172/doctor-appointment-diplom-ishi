package uz.bnabiyev.drappointment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.databinding.ItemUserBinding
import uz.bnabiyev.drappointment.models.User

class UserAdapter(
    private val userList: ArrayList<User>,
    private val itemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.Vh>() {

    inner class Vh(private val itemUsersBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemUsersBinding.root) {
        fun onBind(user: User) {
            itemUsersBinding.image.setImageResource(R.drawable.img_2)
            itemUsersBinding.tvName.text = user.firstName
            itemUsersBinding.tvNumber.text = "Call : ${user.phoneNumber}"
            itemView.setOnClickListener {
                itemClick.invoke(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(userList[position])
    }

}