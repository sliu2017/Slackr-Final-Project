package com.example.slackr

class User {

    // Stores data for each user

        var fname: String? = null
        var lname: String? = null
        var email: String? = null
        var style_of_learning: String?=null


        constructor() {}
        constructor(
            fname: String?,
            lname: String?,
            email: String?,
            style_of_learning: String?


        ) {
            this.fname = fname
            this.lname = lname
            this.email = email
            this.style_of_learning=style_of_learning

        }

}