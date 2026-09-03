package de.vegnpunk.mentalmatics.ui.di

import de.vegnpunk.mentalmatics.core.di.coreModule
import de.vegnpunk.mentalmatics.data.di.dataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Starts Koin with every layer's module (ADR-007). Each platform shell
 * calls this once at launch, passing platform-specific setup (e.g.
 * Android's `androidContext`) via [platformDeclaration].
 */
fun initKoin(platformDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        platformDeclaration?.invoke(this)
        modules(coreModule, dataModule, uiModule)
    }
}
