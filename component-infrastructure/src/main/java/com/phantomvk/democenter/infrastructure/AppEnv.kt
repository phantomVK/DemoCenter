package com.phantomvk.democenter.infrastructure

import android.app.Application


object AppEnv {

  private lateinit var application: Application

  fun init(application: Application) {
    this.application = application
  }

  fun getApplication(): Application {
    return application
  }
}