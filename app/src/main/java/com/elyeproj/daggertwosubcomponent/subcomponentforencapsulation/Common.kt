package com.elyeproj.daggertwosubcomponent.subcomponentforencapsulation

import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PrivateToDatabase

@Qualifier
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class NumberOfCores

class Database(private val databaseScheme: DatabaseSchema,
               private val databaseConnectionPool: DatabaseConnectionPool)
class DatabaseSchema
class DatabaseConnectionPool(private val concurrencyLevel: Int)
