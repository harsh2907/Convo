package com.example.uchat.di


import com.example.uchat.data.repository.ChatRepositoryImpl
import com.example.uchat.data.repository.EndPoints
import com.example.uchat.data.repository.UserRepositoryImpl
import com.example.uchat.domain.repository.ChatRepository
import com.example.uchat.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserRepo():UserRepository = UserRepositoryImpl(
        dbRef = FirebaseFirestore.getInstance().collection(EndPoints.Users.route)
    )

    @Singleton
    @Provides
    fun provideChatRepo():ChatRepository = ChatRepositoryImpl(
        dbRef = FirebaseFirestore.getInstance().collection(EndPoints.Chat.route)
    )


}