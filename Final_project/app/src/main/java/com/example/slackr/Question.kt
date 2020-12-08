package com.example.slackr

// Contains a question and answer choice for each learning style

data class Question (
    val id: Int,
    val question: String,
    val tactile: String,
    val visual : String,
    val audible : String



)