package com.example.assignmentindividu.items


data class Transaction(
    var userID: Int?=null,
    var path: String?=null,
    var dollID: Int?=null,
    var transactionID: Int?=null,
    var dollName: String?=null,
    var transactionQuantity: Int?=null,
    var transactionDate:String?=null
)
