package com.adridars.catdogsearcher


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.adridars.catdogsearcher.apiServices.ApiService
import com.adridars.catdogsearcher.apiServices.CatDogDetailResponse
import com.adridars.catdogsearcher.apiServices.GetRetrofit
import com.adridars.catdogsearcher.databinding.ActivityDetailCatCardBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

class DetailCatCard : AppCompatActivity() {
    companion object {
        const val CAT_ID = "cat_id"
    }

    private lateinit var retrofit: Retrofit
    private lateinit var binding: ActivityDetailCatCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCatCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initComponents()
        initUiDetail()
    }

    private fun initComponents() {
        retrofit = getRetrofit()
    }

    private fun initUiDetail() {
        val bundle = intent.extras
        val id: String? = bundle?.getString(CAT_ID)
        if (id != null) {
            if (id.isNotEmpty()) getCatDetail(id)
        }
    }

    private fun getCatDetail(id: String) {
        binding.pbCatDetail.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            val catResponse: Response<CatDogDetailResponse> =
                getRetrofit().create(ApiService::class.java).getCatDog(id)
            Log.i("RasaDevelops", "${catResponse.body()}")
            if (catResponse != null) {
                val cat: CatDogDetailResponse = catResponse.body()!!
                runOnUiThread {
                    createCatDetail(cat)
                    binding.pbCatDetail.isVisible = false;
                }
            } else {
                Log.e("RasaDevelops", "${catResponse.errorBody().toString()}")
            }

        }
    }

    private fun createCatDetail(cat: CatDogDetailResponse) {
        Glide.with(this).load(cat.imageUrl).into(binding.ivCatDetail)
        val catName = cat.breeds[0].description
        binding.tvCatName.text = catName
    }

    private fun getRetrofit(): Retrofit {
        val get = GetRetrofit().getCatRetrofit()
        return get
    }
}