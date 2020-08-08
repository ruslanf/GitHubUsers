package studio.bz_soft.githubusers.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import studio.bz_soft.githubusers.BuildConfig
import studio.bz_soft.githubusers.data.db.DbClient
import studio.bz_soft.githubusers.data.db.DbClientImpl
import studio.bz_soft.githubusers.data.http.ApiClientImpl
import studio.bz_soft.githubusers.data.http.ApiClient
import studio.bz_soft.githubusers.data.models.vm.UserInfoVM
import studio.bz_soft.githubusers.data.models.vm.UsersListVM
import studio.bz_soft.githubusers.data.repository.LocalStorageImpl
import studio.bz_soft.githubusers.data.repository.LocalStorage
import studio.bz_soft.githubusers.data.repository.RepositoryImpl
import studio.bz_soft.githubusers.data.repository.Repository
import studio.bz_soft.githubusers.root.App
import studio.bz_soft.githubusers.root.Constants.API_MAIN_URL
import studio.bz_soft.githubusers.root.Constants.DEV_API_URL

val applicationModule = module {
    single { androidApplication() as App }
}

val networkModule = module {
    single<ApiClient> {
        ApiClientImpl(
            if (BuildConfig.DEBUG) DEV_API_URL else API_MAIN_URL, androidContext()
        )
    }
}

val storageModule = module {
    factory<SharedPreferences> {
        androidContext().getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }
    single<DbClient> { DbClientImpl(androidApplication()) }
}

val repositoryModule = module {
    single<LocalStorage> { LocalStorageImpl(get()) }
    single<Repository> { RepositoryImpl(get(), get(), get()) }
}

val presenterModule = module {  }

val controllerModule = module {  }

val liveDataModule = module {
    viewModel { UsersListVM(get()) }
    viewModel { UserInfoVM(get()) }
}