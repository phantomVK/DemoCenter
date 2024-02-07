package com.phantomvk.democenter

import android.app.Application
import com.phantomvk.democenter.infrastructure.AppEnv

class BaseApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    AppEnv.init(this)
  }
}