package com.example.antiquecollectorui.di

import android.content.Context
import androidx.room.Room
import com.example.antiquecollectorui.data.local.AntiqueDatabase
import com.example.antiquecollectorui.data.dao.AntiqueItemDao
import com.example.antiquecollectorui.data.dao.CollectionEventDao
import com.example.antiquecollectorui.data.dao.MuseumCacheDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AntiqueDatabase =
        Room.databaseBuilder(
            context,
            AntiqueDatabase::class.java,
            "antique_collector.db"
        ).build()

    @Provides
    fun provideAntiqueItemDao(db: AntiqueDatabase): AntiqueItemDao = db.antiqueItemDao()

    @Provides
    fun provideCollectionEventDao(db: AntiqueDatabase): CollectionEventDao = db.collectionEventDao()

    @Provides
    fun provideMuseumCacheDao(db: AntiqueDatabase): MuseumCacheDao = db.museumCacheDao()
}