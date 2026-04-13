package com.example.antiquecollectorui.di


    import com.example.antiquecollectorui.data.repository.AntiqueRepository
    import com.example.antiquecollectorui.data.repository.AntiqueRepositoryImpl
    import dagger.Binds
    import dagger.Module
    import dagger.hilt.InstallIn
    import dagger.hilt.components.SingletonComponent
    import javax.inject.Singleton
class RepositoryModule {
    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepositoryModule {

        @Binds
        @Singleton
        abstract fun bindAntiqueRepository(
            impl: AntiqueRepositoryImpl
        ): AntiqueRepository
    }
}