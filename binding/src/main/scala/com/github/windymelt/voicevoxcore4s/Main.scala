package com.github.windymelt.voicevoxcore4s

object Hello extends App {
  println("initializing")
  println(System.getProperty("java.library.path"))
  val core = new Core()
  val initialized = core.initialize(true)
  println(s"Hello, voicevoxcore4s! ($initialized)")
}

