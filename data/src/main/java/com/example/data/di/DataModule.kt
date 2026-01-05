package com.example.data.di

import com.example.data.repository.TransactionRepositoryImp
import com.example.domain.repo.TransactionRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    singleOf(::TransactionRepositoryImp) { bind<TransactionRepository>() }

}