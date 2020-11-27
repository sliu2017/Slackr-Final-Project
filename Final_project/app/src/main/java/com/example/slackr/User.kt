package com.example.slackr

class User {

        var fname: String? = null
        var lname: String? = null
        var email: String? = null


        constructor() {}
        constructor(
            fname: String?,
            lname: String?,
            email: String?

        ) {
            this.fname = fname
            this.lname = lname
            this.email = email

        }

}