package com.adridars.catdogsearcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.adridars.catdogsearcher.DetailCatCard.Companion.CAT_ID
import com.adridars.catdogsearcher.adapter.CatDogAdapter
import com.adridars.catdogsearcher.apiServices.ApiService
import com.adridars.catdogsearcher.apiServices.BreedsCatDogDataResponse
import com.adridars.catdogsearcher.apiServices.GetRetrofit
import com.adridars.catdogsearcher.databinding.ActivityMainCatDefaultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class MainActivityCatDefault : AppCompatActivity() {


    private lateinit var retrofit: Retrofit
    private lateinit var binding: ActivityMainCatDefaultBinding
    private lateinit var breedAdapter: CatDogAdapter
    private var catList: ArrayList<BreedsCatDogDataResponse> = ArrayList()
    private var filteredCatList: ArrayList<BreedsCatDogDataResponse> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCatDefaultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initComponents()

        initUi()
    }

    private fun initComponents() {
        //InitRetrofit
        retrofit = getRetrofit()

        //InitRecyclerView
        val catsDogsManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        breedAdapter = CatDogAdapter { idCat -> clickedCat(idCat) }
        binding.rvCatBreeds.layoutManager = catsDogsManager
        binding.rvCatBreeds.adapter = breedAdapter
    }

    private fun initUi() {
        //Get Breed request and fill the list for first time
        initBreedRequest()
        //Go to Dog Searcher
        binding.fabNavigateToDog.setOnClickListener {
            navigateToDogSearcher()
        }
        //In base of what typed filter and actualize the showing list
        binding.svCat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredCat = catList.filter { it.name == query }
                navigateToSpecificCat(filteredCat[0].referenceImageId.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredCatList.clear()
                val searchBread = newText!!.lowercase(Locale.getDefault())
                if (searchBread.isNotEmpty()) {
                    catList.forEach {
                        if (it.name!!.lowercase(Locale.getDefault()).contains(searchBread)) {
                            filteredCatList.add(it)
                        }
                    }
                    breedAdapter.updateList(filteredCatList)
                } else {
                    breedAdapter.updateList(catList)
                }
                return false
            }
        })

    }

    private fun navigateToDogSearcher() {
        val intent = Intent(this, MainActivityDog::class.java)
        startActivity(intent)
    }

    //Go to Detail Clicked cat
    private fun navigateToSpecificCat(catId: String) {
        val intent = Intent(this, DetailCatCard::class.java).putExtra(CAT_ID, catId)
        startActivity(intent)
    }

    //Auto complete the search view box whit the clicked element from the list
    private fun clickedCat(idCat: String) {
        Log.i("RasaDevelops", "$idCat")
        binding.svCat.setQuery(idCat, false)
    }

    //Make API call
    private fun initBreedRequest() {
        binding.pbCatList.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<ArrayList<BreedsCatDogDataResponse>> =
                retrofit.create(ApiService::class.java).getBreeds()
            if (response.isSuccessful) {
                val myResponse = response.body();
                if (myResponse != null) {
                    runOnUiThread {
                        catList = myResponse
                        breedAdapter.updateList(catList)
                        binding.pbCatList.isVisible = false
                    }
                }
            } else {
                Log.i("RasaDevelops!!", "NOOOO")
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivityCatDefault, "No breed found", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit = GetRetrofit().getCatRetrofit()

}



