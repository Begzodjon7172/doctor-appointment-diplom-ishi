package uz.bnabiyev.drappointment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.bnabiyev.drappointment.databinding.ItemCategoryBinding
import uz.bnabiyev.drappointment.models.Category

class CategoryAdapter(
    private val categoryList: ArrayList<Category>,
    private val itemClick: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoryAdapter.Vh>() {

    inner class Vh(private val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun onBind(category: Category) {
            itemCategoryBinding.img.setImageResource(category.img)
            itemCategoryBinding.title.text = category.title
            itemView.setOnClickListener {
                itemClick.invoke(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(categoryList[position])
    }
}