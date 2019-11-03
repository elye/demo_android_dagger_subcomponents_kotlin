package com.elyeproj.daggertwosubcomponent.subcomponentandscope

import dagger.Component
import dagger.Subcomponent
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SessionScope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class RequestScope

@Singleton @Component
interface RootComponent2 {
    val sessionComponent: SessionComponent.Builder
}

@SessionScope @Subcomponent
interface SessionComponent {
    val fooRequestComponent: FooRequestComponent.Builder
    val barRequestComponent: BarRequestComponent.Builder

    @Subcomponent.Builder
    interface Builder {
        fun build(): SessionComponent
    }
}

@RequestScope @Subcomponent
interface FooRequestComponent  {
    @Subcomponent.Builder
    interface Builder {
        fun build(): FooRequestComponent
    }
}

@RequestScope @Subcomponent
interface BarRequestComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): BarRequestComponent
    }
}

fun main() {
    DaggerRootComponent2.create()
}
