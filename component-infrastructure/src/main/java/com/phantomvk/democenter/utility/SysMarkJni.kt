package com.phantomvk.democenter.utility

object SysMarkJni {
  init {
    System.loadLibrary("sys_mark")
  }

  external fun getUpdateMark(): String
  external fun getBootMark(): String
}