package com.adridars.catdogsearcher.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adridars.catdogsearcher.apiServices.BreedsCatDogDataResponse
import com.adridars.catdogsearcher.databinding.ItemCatDogBinding

class CatDogViewHolder(view: View):RecyclerView.ViewHolder(view) {
    private val binding = ItemCatDogBinding.bind(view)

    fun itemCatBread (catDog: BreedsCatDogDataResponse, clickedCatDog: (String) -> Unit){
        binding.tvCatName.text = catDog.name
        binding.tvCatDescription.text = catDog.temperament
        itemView.setOnClickListener{clickedCatDog(catDog.name.toString())}
    }
}