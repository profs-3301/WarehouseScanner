package  com.goodswarehouse.scanner.data.di

import com.goodswarehouse.scanner.data.repo.LocalRepo
import com.goodswarehouse.scanner.data.repo.RestRepo
import com.goodswarehouse.scanner.data.track.TrackRepoDatabase
import com.goodswarehouse.scanner.domain.repo.RepoLocal
import com.goodswarehouse.scanner.domain.repo.RepoRest
import com.goodswarehouse.scanner.domain.track.TrackRepo
import dagger.Binds
import dagger.Module

@Module
abstract class RepoBindModule {

    @Binds
    abstract fun provideLocalRepo(localRepo: LocalRepo): RepoLocal

    @Binds
    abstract fun provideRestRepo(restRepo: RestRepo): RepoRest

    @Binds
    @Track(TRACK_DATABASE)
    abstract fun provideDatabaseTrackRepo(restRepo: TrackRepoDatabase): TrackRepo

}