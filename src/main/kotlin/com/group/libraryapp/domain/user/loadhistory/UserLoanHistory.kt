package com.group.libraryapp.domain.user.loadhistory

import com.group.libraryapp.domain.user.User
import jakarta.persistence.*

@Entity
class UserLoanHistory(

  @ManyToOne
  val user: User,

  val bookName: String,

  var status: UserLoanStatus = UserLoanStatus.LOANED,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
) {

  fun doReturn() {
    this.status = UserLoanStatus.RETURNED
  }

  val isReturn: Boolean
    get() = this.status == UserLoanStatus.RETURNED

  init {
    user.userLoanHistory.add(this)
  }

  companion object {
    fun fixture(
      user: User,
      bookName: String,
      status: UserLoanStatus = UserLoanStatus.LOANED,
      id: Long? = null,
    ) : UserLoanHistory {
      return UserLoanHistory(
        user, bookName, status
      )
    }
  }
}