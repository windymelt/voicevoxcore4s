package com.github.windymelt.voicevoxcore4s

object Hello extends App {
  println("initializing")
  System.load("/usr/local/lib/libonnxruntime.so")
  System.load("/usr/local/lib/libcore.so")
  println(System.getProperty("java.library.path"))
  val core = new Core()
  val initialized = core.initialize(true)
  println(s"Hello, voicevoxcore4s! ($initialized)")
}

