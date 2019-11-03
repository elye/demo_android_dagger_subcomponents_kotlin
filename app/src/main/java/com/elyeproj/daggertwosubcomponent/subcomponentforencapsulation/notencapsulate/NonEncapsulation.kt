package com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.notencapsulate

import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.Database
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.DatabaseConnectionPool
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.DatabaseSchema
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.NumberOfCores
import com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation.encapsulate.DaggerApplicationComponent
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
internal interface ApplicationComponent {
    val database: Database
    val databaseScheme: DatabaseSchema
    val databaseConnectionPool: DatabaseConnectionPool
}

@Module
class DatabaseModule {

    @NumberOfCores
    @Provides
    fun getNumberOfCore() = 10

    @Provides
    fun provideDatabaseConnectionPool(
        @NumberOfCores concurrencyLevel: Int): DatabaseConnectionPool {
        return DatabaseConnectionPool(concurrencyLevel)
    }

    @Provides
    fun provideDatabaseSchema(): DatabaseSchema {
        return DatabaseSchema()
    }

    @Provides
    @Singleton
    fun provideDatabase(databaseSchema: DatabaseSchema,
                        databaseConnectionPool: DatabaseConnectionPool): Database {
        return Database(databaseSchema, databaseConnectionPool)
    }
}

fun main() {
    DaggerApplicationComponent.create()
}
