package com.josycom.mayorjay.marsalbum.common.domain.model

import java.io.Serializable

data class Camera(
    val fullName: String = "",
    val id: Int = -1,
    val name: String = "",
    val roverId: Int = -1
): Serializable