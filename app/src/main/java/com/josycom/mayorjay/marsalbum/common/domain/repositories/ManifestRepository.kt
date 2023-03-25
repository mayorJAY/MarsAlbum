package com.josycom.mayorjay.marsalbum.common.domain.repositories

import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest

interface ManifestRepository {

    suspend fun getRoverManifest(roverName: String): Manifest
}