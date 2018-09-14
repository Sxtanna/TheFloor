package com.sxtanna.bby.data

data class Employee(val name: Name, val store: Store) {

    data class Name(val fore: String, val last: String)

    data class Store(val number: Int)


    companion object {

        val DEFAULT = Employee(Name("John", "Doe"), Store(960))

    }

}