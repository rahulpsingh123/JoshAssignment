package com.l.joshassignment.responseModel

data class FlickerResponse(
        var photo : MutableList<FlickerData>? = null
)

data class FlickerData(
        val id: String? = null,
        val owner: String? = null,
        val secret: String? = null,
        val server: String? = null,
        val farm: Int? = null,
        val title: String? = null,
        val ispublic: Int? = null,
        val isfriend: Int? = null,
        val isfamily: Int? = null

)

class QueryAlreadyInProgress : Exception()

