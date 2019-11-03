package com.elyeproj.daggertwosubcomponent.subcomponentandscope

import dagger.Component
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ChildScope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class RootScope

@RootScope @Component
interface RootComponent {
    // Commented out as it error
    // val rootScopeChildComponent: RootScopeComponent.Builder // ERROR!
    val siblingComponentOne: SiblingComponentOne.Builder
    val siblingComponentTwo: SiblingComponentTwo.Builder
}

@RootScope @Subcomponent
interface RootScopeComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): RootScopeComponent
    }
}

@ChildScope @Subcomponent
interface SiblingComponentOne {
    @Subcomponent.Builder
    interface Builder {
        fun build(): SiblingComponentOne
    }
}

@ChildScope @Subcomponent
interface SiblingComponentTwo {
    @Subcomponent.Builder
    interface Builder {
        fun build(): SiblingComponentTwo
    }
}

@Component
interface UnscopeComponent {
    val rootScopeChildComponent: RootScopeComponent.Builder // OK
}

fun main() {
    DaggerRootComponent.create()
    DaggerUnscopeComponent.create()
}