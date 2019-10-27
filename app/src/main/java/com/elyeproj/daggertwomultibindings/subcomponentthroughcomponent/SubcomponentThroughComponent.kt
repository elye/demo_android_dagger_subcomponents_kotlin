package com.elyeproj.daggertwomultibindings.subcomponentthroughcomponent

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class Data(private val code: Int, private val message: String) {
    override fun toString(): String {
        return "$code: $message"
    }
}

class RequestHandler {
    fun writeResponse(data: Data) {
        println(data)
    }
}

@Singleton
class RequestRouter @Inject constructor(private val requestComponentProvider: Provider<RequestComponent.Builder>) {
    fun dataReceived(data: Data) {
        val requestComponent = requestComponentProvider.get()
            .build()
        requestComponent.requestHandler.writeResponse(data)
    }
}

@Module
class RequestModule {
    @Provides
    fun getRequestHandler() = RequestHandler()
}

@Subcomponent(modules = [RequestModule::class])
interface RequestComponent {
    val requestHandler: RequestHandler

    @Subcomponent.Builder
    interface Builder {
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
    a.dataReceived(Data(200, "My Message"))
    a.dataReceived(Data(400, "Your Message"))
}
