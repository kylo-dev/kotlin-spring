package com.group.libraryapp.domain.user.loadhistory

import com.group.libraryapp.domain.user.User
import jakarta.persistence.*

@Entity
class UserLoanHistory(

  @ManyToOne
  val user: User,

  val bookName: String,

  var isReturn: Boolean,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
) {

  fun doReturn() {
    this.isReturn = true
  }
}