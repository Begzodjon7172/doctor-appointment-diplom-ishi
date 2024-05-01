package uz.bnabiyev.drappointment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.bnabiyev.drappointment.R
import uz.bnabiyev.drappointment.adapters.CategoryAdapter
import uz.bnabiyev.drappointment.databinding.FragmentHomeBinding
import uz.bnabiyev.drappointment.models.Category


class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadData()
        categoryAdapter = CategoryAdapter(categoryList) { category ->
            val bundle = Bundle()
            bundle.putString("category", category.title)
            findNavController().navigate(R.id.action_homeFragment_to_listFragment, bundle)
        }

        binding.rv.adapter = categoryAdapter

        return binding.root
    }

    private fun loadData() {
        categoryList = ArrayList()
        categoryList.add(Category(R.drawable.img, "ENDOKRINOLOG"))
        categoryList.add(Category(R.drawable.img_3, "NEUROPOTOLOG"))
        categoryList.add(Category(R.drawable.img_4, "STOMATOLOG"))
        categoryList.add(Category(R.drawable.img_5, "JARROH"))
        categoryList.add(Category(R.drawable.img_6, "LOR"))
        categoryList.add(Category(R.drawable.img_7, "GINEKOLOG"))
        categoryList.add(Category(R.drawable.img_8, "PULMONOLOG"))
        categoryList.add(Category(R.drawable.img_9, "OKULIST"))
        categoryList.add(Category(R.drawable.img_10, "TERAPEVT"))
        categoryList.add(Category(R.drawable.img_11, "UROLOG"))
        categoryList.add(Category(R.drawable.img_12, "DERMATOLOG"))
        categoryList.add(Category(R.drawable.img_13, "KARDIOLOG"))
    }

}