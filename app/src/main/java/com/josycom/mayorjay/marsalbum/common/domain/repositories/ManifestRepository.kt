package com.josycom.mayorjay.marsalbum.common.domain.repositories

import com.josycom.mayorjay.marsalbum.common.domain.model.Manifest
import com.josycom.mayorjay.marsalbum.common.util.Resource

interface ManifestRepository {

    suspend fun getRoverManifest(roverName: String): Resource<Manifest>
}