package com.xctbtx.cleanarchitectsample.di

import com.xctbtx.cleanarchitectsample.data.auth.AuthenticationRepositoryImpl
import com.xctbtx.cleanarchitectsample.data.conversation.ConversationRepositoryImpl
import com.xctbtx.cleanarchitectsample.data.message.MessageRepositoryImpl
import com.xctbtx.cleanarchitectsample.data.post.PostRepositoryImpl
import com.xctbtx.cleanarchitectsample.data.user.UserRepositoryImpl
import com.xctbtx.cleanarchitectsample.domain.auth.repository.AuthenticationRepository
import com.xctbtx.cleanarchitectsample.domain.conversation.repository.ConversationRepository
import com.xctbtx.cleanarchitectsample.domain.message.repository.MessageRepository
import com.xctbtx.cleanarchitectsample.domain.post.repository.PostRepository
import com.xctbtx.cleanarchitectsample.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPostRepository(
        impl: PostRepositoryImpl
    ): PostRepository

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindConversationRepository(
        impl: ConversationRepositoryImpl
    ): ConversationRepository

    @Binds
    abstract fun bindMessageRepository(
        impl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthenticationRepositoryImpl
    ): AuthenticationRepository
}