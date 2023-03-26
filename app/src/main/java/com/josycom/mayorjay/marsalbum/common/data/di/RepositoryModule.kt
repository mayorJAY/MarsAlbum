package com.josycom.mayorjay.marsalbum.common.data.di

import com.josycom.mayorjay.marsalbum.common.data.api.impl.ManifestRepositoryImpl
import com.josycom.mayorjay.marsalbum.common.domain.repositories.ManifestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindManifestRepository(repository: ManifestRepositoryImpl): ManifestRepository
}