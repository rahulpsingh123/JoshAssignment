package com.l.joshassignment.responseModel

import com.l.joshassignment.network.APIManager
import com.l.joshassignment.network.NetworkClient
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class JoshRepo : BaseRepo() {

    fun getPhotoBasedOnSearchTerm(searchTerm: String, page: Int): Single<String>? {
        return APIManager.instance?.searchItem(searchTerm, page)?.let {
            NetworkClient
                .getResult(it)
                .subscribeOn(Schedulers.io())
        }
    }
}