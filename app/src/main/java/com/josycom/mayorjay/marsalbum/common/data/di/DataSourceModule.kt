package com.josycom.mayorjay.marsalbum.common.data.di

import com.josycom.mayorjay.marsalbum.common.data.api.datasource.ManifestDataSource
import com.josycom.mayorjay.marsalbum.common.data.api.datasource.ManifestDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    abstract fun bindManifestDataSource(dataSource: ManifestDataSourceImpl): ManifestDataSource
}