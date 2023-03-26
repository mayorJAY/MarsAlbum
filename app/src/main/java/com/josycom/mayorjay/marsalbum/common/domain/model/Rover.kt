package com.josycom.mayorjay.marsalbum.common.domain.model

import java.io.Serializable

data class Rover(
    val id: Int = -1,
    val landingDate: String = "",
    val launchDate: String = "",
    val name: String = "",
    val status: String = ""
): Serializable