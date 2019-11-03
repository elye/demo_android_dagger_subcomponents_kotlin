package com.elyeproj.daggertwosubcomponent.subcomponentabstractfactory.modulenoparam

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
class RequestModule {
    @Provides
    fun getRequestHandler() = RequestHandler(Data("Module made Data"))
}

@Subcomponent(modules = [RequestModule::class])
interface RequestComponent {
    val requestHandler: RequestHandler
}

@Singleton
@Component
interface ServerComponent {
    val requestComponent: RequestComponent
}

fun main() {
    val component = DaggerServerComponent.create()
            .requestComponent
    component.requestHandler.writeResponse(200, "Hello")
}
