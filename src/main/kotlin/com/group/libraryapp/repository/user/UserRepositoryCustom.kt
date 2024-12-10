package com.group.libraryapp.repository.user

import com.group.libraryapp.domain.user.User

interface UserRepositoryCustom {

  fun findAllWithHistories(): List<User>
}