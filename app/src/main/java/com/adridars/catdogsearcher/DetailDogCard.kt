package com.adridars.catdogsearcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.adridars.catdogsearcher.apiServices.ApiService
import com.adridars.catdogsearcher.apiServices.CatDogDetailResponse
import com.adridars.catdogsearcher.apiServices.GetRetrofit
import com.adridars.catdogsearcher.databinding.ActivityDetailDogCardBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit


class DetailDogCard : AppCompatActivity() {
    companion object {
        const val DOG_ID = "dog_id"
    }

    private lateinit var binding: ActivityDetailDogCardBinding

    val getRetrofit: Retrofit = GetRetrofit().getDogRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailDogCardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        initUi()
    }

    private fun initUi() {
        val bundle = intent.extras
        val id = bundle?.getString(DOG_ID)
        if (id != null) {
            if (id.isNotEmpty()) {
                retrieveDogInfo(id)
            }
        }
    }

    private fun retrieveDogInfo(id: String) {
        binding.pbDogDetail.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            val specificDogResponse: Response<CatDogDetailResponse> =
                getRetrofit.create(ApiService::class.java).getCatDog(id)
            if (specificDogResponse.isSuccessful) {
                val dog = specificDogResponse.body()
                if (dog != null) {
                    runOnUiThread {
                        createDetailDog(dog)
                        binding.pbDogDetail.isVisible = false
                    }
                }
            }
        }
    }

    private fun createDetailDog(dog: CatDogDetailResponse) {
       Glide.with(this).load(dog.imageUrl).into(binding.ivDogDetail)
        val dogName = dog.breeds[0].temperament
        binding.tvDogName.text = dogName
    }
}