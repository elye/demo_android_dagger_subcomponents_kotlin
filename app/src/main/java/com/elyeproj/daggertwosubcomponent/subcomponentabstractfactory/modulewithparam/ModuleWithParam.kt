package com.elyeproj.daggertwosubcomponent.subcomponentabstractfactory.modulewithparam

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

data class Data(private val content: String)

class RequestHandler(private val data: Data) {
    fun writeResponse(code: Int, message: String) {
        println("$code $message $data")
    }
}

@Module
class RequestModule(private val data: Data) {
    @Provides
    fun getRequestHandler() = RequestHandler(data)
}

@Subcomponent(modules = [RequestModule::class])
interface RequestComponent {
    val requestHandler: RequestHandler
}

@Singleton
@Component
interface ServerComponent {
    fun requestComponent(requestModule: RequestModule): RequestComponent
}

fun main() {
    val component = DaggerServerComponent.create()
            .requestComponent(RequestModule(Data("My Data")))
    component.requestHandler.writeResponse(200, "Hello")
}
