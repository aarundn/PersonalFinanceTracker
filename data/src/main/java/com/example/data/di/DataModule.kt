package com.example.data.di

import androidx.room.Room
import com.example.data.local.TrackerDatabase
import com.example.data.repository.BudgetRepositoryImp
import com.example.data.repository.TransactionRepositoryImp
import com.example.domain.repo.BudgetRepository
import com.example.domain.repo.TransactionRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    single<TrackerDatabase> {
        Room.databaseBuilder(
                androidContext(),
                TrackerDatabase::class.java,
                TrackerDatabase.DATABASE_NAME
            ).fallbackToDestructiveMigration(false).build()
    }

    singleOf(::TransactionRepositoryImp) { bind<TransactionRepository>() }
    singleOf(::BudgetRepositoryImp) { bind<BudgetRepository>() }

}