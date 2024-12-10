package com.group.libraryapp.repository.user.loanhistory

import com.group.libraryapp.domain.user.loadhistory.QUserLoanHistory.userLoanHistory
import com.group.libraryapp.domain.user.loadhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loadhistory.UserLoanStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserLoanHistoryQuerydslRepository(
  private val queryFactory: JPAQueryFactory,
) {

  fun find(bookName: String, status: UserLoanStatus? = null): UserLoanHistory? {
    return queryFactory.selectFrom(userLoanHistory)
      .where(
        userLoanHistory.bookName.eq(bookName),
        status?.let { userLoanHistory.status.eq(status) })
      .fetchOne()
  }

  fun count(status: UserLoanStatus): Long {
    return queryFactory.select(userLoanHistory.count())
      .from(userLoanHistory)
      .where(userLoanHistory.status.eq(status))
      .fetchOne() ?: 0L
  }

}