package com.adridars.catdogsearcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.adridars.catdogsearcher.DetailDogCard.Companion.DOG_ID
import com.adridars.catdogsearcher.adapter.CatDogAdapter
import com.adridars.catdogsearcher.apiServices.ApiService
import com.adridars.catdogsearcher.apiServices.BreedsCatDogDataResponse
import com.adridars.catdogsearcher.apiServices.GetRetrofit
import com.adridars.catdogsearcher.databinding.ActivityMainDogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList


class MainActivityDog : AppCompatActivity() {
    private lateinit var binding: ActivityMainDogBinding
    private lateinit var breedAdapter: CatDogAdapter
    private var retrofit: Retrofit = GetRetrofit().getDogRetrofit()
    private var dogList: ArrayList<BreedsCatDogDataResponse> = ArrayList()
    private var filteredDogList: ArrayList<BreedsCatDogDataResponse> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainDogBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        initComponents()
        initUi()
    }

    private fun initComponents() {
        //Init RecyclerView
        val layoutManager = LinearLayoutManager(this)
        breedAdapter = CatDogAdapter() { idDog -> clickedDog(idDog) }
        binding.rvDogBreeds.adapter = breedAdapter
        binding.rvDogBreeds.layoutManager = layoutManager

        binding.fabNavigateToCat.setOnClickListener {
            navigateToCatActivity()
        }
    }

    private fun navigateToCatActivity() {
        val intent = Intent(this, MainActivityCatDefault::class.java)
        startActivity(intent)
    }

    private fun initUi() {
        //init dog breed list and paint it in RView list
        initBreedRequest()

        //init sv listener
        binding.svDog.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
              val filteredDog =  dogList.filter { it.name == query }
                navigateToSpecificDog(filteredDog[0].referenceImageId.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredDogList.clear()
                var typedDog = newText!!.lowercase(Locale.getDefault())
                if (typedDog.isNotEmpty()){
                    dogList.forEach {
                        if(it.name!!.lowercase(Locale.getDefault()).contains(typedDog)){
                            filteredDogList.add(it)
                        }
                    }
                    breedAdapter.updateList(filteredDogList)
                }else{
                    breedAdapter.updateList(dogList)
                }
                return false
            }
        })
    }

    private fun navigateToSpecificDog(dogId:String) {
       val intent = Intent(this, DetailDogCard::class.java).putExtra(DOG_ID, dogId)
        startActivity(intent)
    }

    private fun initBreedRequest() {
        binding.pbDogList.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<ArrayList<BreedsCatDogDataResponse>> =
                retrofit.create(ApiService::class.java).getBreeds()
            if (response.isSuccessful) {
                val dogieBreeds = response.body()
                if (dogieBreeds != null) {
                    runOnUiThread {
                        Log.i("RasaDevelops", "$dogieBreeds")
                        dogList = dogieBreeds
                        breedAdapter.updateList(dogList)
                        binding.pbDogList.isVisible = false
                    }
                } else {
                    Log.i("RasaDevelops!!", "NOOOO")
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivityDog, "No breed found", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun clickedDog(idDog: String) {
        binding.svDog.setQuery(idDog, false)
    }
}