package com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.encapsulate

import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.Database
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.DatabaseConnectionPool
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.DatabaseSchema
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.NumberOfCores
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.PrivateToDatabase
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
internal interface ApplicationComponent {
    val database: Database
    // Comment out as it will Error
    // val databaseScheme: DatabaseSchema // Error: No access
    // val databaseConnectionPool: DatabaseConnectionPool // Error: No access
}

@Module(subcomponents = [DatabaseComponent::class])
class DatabaseModule {

    @NumberOfCores
    @Provides
    fun getNumberOfCore() = 10

    @Provides
    @Singleton
    fun provideDatabase(
        @NumberOfCores numberOfCores: Int,
        databaseComponentBuilder: DatabaseComponent.Builder): Database {
        return databaseComponentBuilder
            .databaseImplModule(DatabaseImplModule(numberOfCores / 2))
            .build()
            .database()
    }
}

@Module
class DatabaseImplModule(private val concurrencyLevel: Int) {
    @Provides
    fun provideDatabaseConnectionPool(): DatabaseConnectionPool {
        return DatabaseConnectionPool(concurrencyLevel)
    }

    @Provides
    fun provideDatabaseSchema(): DatabaseSchema {
        return DatabaseSchema()
    }

    @PrivateToDatabase
    @Provides
    fun provideDatabase(databaseSchema: DatabaseSchema,
                        databaseConnectionPool: DatabaseConnectionPool): Database {
        return Database(databaseSchema, databaseConnectionPool)
    }
}

@Subcomponent(modules = [DatabaseImplModule::class])
interface DatabaseComponent {
    @PrivateToDatabase
    fun database(): Database

    @Subcomponent.Builder
    interface Builder {
        fun databaseImplModule(databaseImplModule: DatabaseImplModule): Builder
        fun build(): DatabaseComponent
    }
}

fun main() {
    DaggerApplicationComponent.create()
}
