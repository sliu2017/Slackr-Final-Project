package com.example.slackr

class User {

        var fname: String? = null
        var lname: String? = null
        var email: String? = null
        var style_of_learning: String? =null
        var major: String? = null
        var school_name: String? = null
        var minor: String?=null


        constructor() {}
        constructor(
            fname: String?,
            lname: String?,
            email: String?

        ) {
            this.fname = fname
            this.lname = lname
            this.email = email
            style_of_learning=""
            school_name=""
            major=""
            minor=""


        }

}