package com.example.getfactorial

sealed class State
object Error : State()
object Progress : State()
class Result(val value:String):State()