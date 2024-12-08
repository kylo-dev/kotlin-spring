package com.group.libraryapp.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {

  fun findByName(name: String) : User?

  @Query("select distinct u from User u left join u.userLoanHistory ulh")
  fun findAllWithHistories(): List<User>
}