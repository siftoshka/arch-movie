package cz.mamiyaza.mvp.di

import cz.mamiyaza.mvp.main.MainPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent

/**
 * TODO add class description
 */
@Module
@InstallIn(FragmentComponent::class)
class MainModule {

    @Provides
    fun providesMovieListPresenter(): MainPresenter = MainPresenter()

}