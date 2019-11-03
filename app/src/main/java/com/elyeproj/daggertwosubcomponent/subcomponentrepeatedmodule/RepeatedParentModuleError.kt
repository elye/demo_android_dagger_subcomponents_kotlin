package com.elyeproj.daggertwosubcomponent.subcomponentrepeatedmodule

import dagger.Component
import dagger.Module
import dagger.Subcomponent

@Module
class RepeatedModule

@Component(modules = [RepeatedModule::class])
interface ComponentRoot {
    val componentOne: ComponentOne
    // Comment out due to Compile Error
    // As a repeated module clash with what the parent provided
    // fun componentTwo(repeatedModule: RepeatedModule): ComponentTwo
    val componentThreeBuilder: ComponentThree.Builder
}

@Subcomponent(modules = [RepeatedModule::class])
interface ComponentOne

@Subcomponent(modules = [RepeatedModule::class])
interface ComponentTwo

@Subcomponent(modules = [RepeatedModule::class])
interface ComponentThree {
    @Subcomponent.Builder
    interface Builder {
        fun repeatedModule(repeatedModule: RepeatedModule): Builder
        fun build(): ComponentThree
    }
}

fun main() {
    val componentRoot = DaggerComponentRoot.create()
    componentRoot.componentThreeBuilder
        // Comment out due to UnsupportedOperationException!
        // As a repeated module clash with what the parent provided
        // .repeatedModule(RepeatedModule())
        .build()

    componentRoot.componentOne
}