package com.l.joshassignment.viewModel

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.l.joshassignment.responseModel.FlickerResponse
import com.l.joshassignment.responseModel.JoshRepo
import com.l.joshassignment.responseModel.QueryAlreadyInProgress
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONObject

class JoshViewModel : ViewModel() {
    private val repo = JoshRepo()
    private val gson = Gson()
    private var queryInProgress = false

    fun getPhotoBasedOnSearchTerm(searchTerm: String, page: Int): Single<FlickerResponse>? {
        return when {
            queryInProgress -> Single.error(QueryAlreadyInProgress())
            else -> {
                queryInProgress = true
                repo.getPhotoBasedOnSearchTerm(searchTerm, page)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.flatMap {
                        val response = gson.fromJson(
                            JSONObject(it).optJSONObject("photos")?.toString(),
                            FlickerResponse::class.java
                        )
                        Single.just(response)
                    }
                    ?.doFinally {
                        queryInProgress = false
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.clear()
    }
}