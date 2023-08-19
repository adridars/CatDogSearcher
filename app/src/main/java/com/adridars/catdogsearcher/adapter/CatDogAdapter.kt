package com.adridars.catdogsearcher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adridars.catdogsearcher.apiServices.BreedsCatDogDataResponse
import com.adridars.catdogsearcher.R

class CatDogAdapter(
    private var catDogList: ArrayList<BreedsCatDogDataResponse> = ArrayList(),
    private val clickedCatDog: (String) -> Unit
) :
    RecyclerView.Adapter<CatDogViewHolder>() {
    fun updateList(retrievedList: ArrayList<BreedsCatDogDataResponse>) {
       this.catDogList = retrievedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatDogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CatDogViewHolder(
            layoutInflater
                .inflate(R.layout.item_cat_dog, parent, false)
        )
    }

    override fun getItemCount(): Int = catDogList.size


    override fun onBindViewHolder(holder: CatDogViewHolder, position: Int) {
        val catItem = catDogList[position]

        holder.itemCatBread(catItem,clickedCatDog)
    }
}