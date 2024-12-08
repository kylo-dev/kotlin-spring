package com.group.libraryapp.domain.user.loadhistory

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long> {

  fun findByBookNameAndStatus(bookName: String, status: UserLoanStatus) : UserLoanHistory?

}