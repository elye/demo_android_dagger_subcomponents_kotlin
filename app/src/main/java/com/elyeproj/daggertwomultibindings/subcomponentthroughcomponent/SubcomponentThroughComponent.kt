package com.elyeproj.daggertwomultibindings.subcomponentthroughcomponent

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

data class Data(private val content: String)

class RequestHandler(private val data: Data) {
    fun writeResponse(code: Int, message: String) {
        println("$code $message $data")
    }
}

@Singleton
class RequestRouter @Inject constructor(private val requestComponentProvider: Provider<RequestComponent.Builder>) {
    fun dataReceived(data: Data) {
        val requestComponent = requestComponentProvider.get()
            .requestModule(RequestModule())
            .data(data)
            .build()
        requestComponent.requestHandler.writeResponse(200, "Hello")
    }
}

@Module
class RequestModule {
    @Provides
    fun getRequestHandler(data: Data) = RequestHandler(data)
}

@Subcomponent(modules = [RequestModule::class])
interface RequestComponent {
    val requestHandler: RequestHandler

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun data(data: Data): Builder
        fun requestModule(requestModule: RequestModule): Builder
        fun build(): RequestComponent
    }
}

@Singleton
@Component
interface ServerComponent {
    val requestRouter: RequestRouter
    val requestComponentBuilder: RequestComponent.Builder
}

fun main() {
    val a = DaggerServerComponent.create().requestRouter
    a.dataReceived(Data("My Data"))
    a.dataReceived(Data("Your Data"))
}
