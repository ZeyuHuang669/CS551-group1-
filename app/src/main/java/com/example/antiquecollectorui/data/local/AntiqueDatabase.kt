package com.example.antiquecollectorui.data.local

    import androidx.room.Database
    import androidx.room.RoomDatabase
    import com.example.antiquecollectorui.data.dao.AntiqueItemDao
    import com.example.antiquecollectorui.data.dao.CollectionEventDao
    import com.example.antiquecollectorui.data.dao.MuseumCacheDao
    import com.example.antiquecollectorui.data.entity.AntiqueItemEntity
    import com.example.antiquecollectorui.data.entity.CollectionEventEntity
    import com.example.antiquecollectorui.data.entity.MuseumCacheEntity


        @Database(
        entities = [
            AntiqueItemEntity::class,
            CollectionEventEntity::class,
            MuseumCacheEntity::class
        ],
        version = 1,
        exportSchema = false
    )
    abstract class AntiqueDatabase : RoomDatabase() {
        abstract fun antiqueItemDao(): AntiqueItemDao
        abstract fun collectionEventDao(): CollectionEventDao
        abstract fun museumCacheDao(): MuseumCacheDao
    }