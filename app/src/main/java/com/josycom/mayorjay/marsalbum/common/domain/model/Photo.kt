package com.josycom.mayorjay.marsalbum.common.domain.model

data class Photo(
    val camera: Camera = Camera(),
    val earthDate: String = "",
    val id: Int = -1,
    val imgSrc: String = "",
    val rover: Rover = Rover(),
    val sol: Int = -1
)