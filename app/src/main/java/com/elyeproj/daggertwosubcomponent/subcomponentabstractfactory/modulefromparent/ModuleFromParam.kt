package com.elyeproj.daggertwosubcomponent.subcomponentabstractfactory.modulefromparent

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
class ShareModule(private val data: Data) {
    @Provides
    fun getRequestHandler() = RequestHandler(data)
}

@Subcomponent(modules = [ShareModule::class])
interface RequestComponent {
    val requestHandler: RequestHandler
}

@Singleton
@Component(modules = [ShareModule::class])
interface ServerComponent {
    val requestComponent: RequestComponent
}

fun main() {
    val component = DaggerServerComponent
        .builder()
        .shareModule(ShareModule(Data("Parent Module")))
        .build()
        .requestComponent
    component.requestHandler.writeResponse(200, "Hello")
}
