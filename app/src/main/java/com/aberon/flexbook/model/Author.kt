package com.aberon.flexbook.model

class Author(
    val lastName: String,
    val middleName: String,
    val firstName: String,
    val fullName: String,
    val nickname: String,
    val emails: List<String>
)