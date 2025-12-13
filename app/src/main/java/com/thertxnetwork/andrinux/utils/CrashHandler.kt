package com.thertxnetwork.andrinux.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log
import com.thertxnetwork.andrinux.App
import com.thertxnetwork.andrinux.ui.other.CrashActivity
import kotlin.system.exitProcess

/**
 * @author kiva
 */
object CrashHandler : Thread.UncaughtExceptionHandler {
  private const val TAG = "CrashHandler"
  private var defaultHandler: Thread.UncaughtExceptionHandler? = null

  fun init() {
    defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler(this)
    Log.d(TAG, "CrashHandler initialized")
  }

  override fun uncaughtException(t: Thread, e: Throwable) {
    try {
      Log.e(TAG, "Uncaught exception in thread ${t.name}", e)
      e.printStackTrace()

      // Try to show crash activity
      try {
        val context = App.get()
        val intent = Intent(context, CrashActivity::class.java).apply {
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
          putExtra("exception_message", e.message ?: "Unknown error")
          putExtra("exception_class", e.javaClass.name)
          putExtra("exception_stacktrace", getStackTraceString(e))
        }
        context.startActivity(intent)
        
        // Give the activity time to start
        Thread.sleep(500)
      } catch (ex: Exception) {
        Log.e(TAG, "Failed to start CrashActivity", ex)
        ex.printStackTrace()
      }
      
      // Kill the process cleanly
      Process.killProcess(Process.myPid())
      exitProcess(10)
      
    } catch (ex: Exception) {
      Log.e(TAG, "Error in crash handler", ex)
      ex.printStackTrace()
      // Fallback to default handler
      defaultHandler?.uncaughtException(t, e)
    }
  }
  
  private fun getStackTraceString(e: Throwable): String {
    val sb = StringBuilder()
    sb.append(e.javaClass.name)
    if (e.message != null) {
      sb.append(": ").append(e.message)
    }
    sb.append("\n")
    
    for (element in e.stackTrace) {
      sb.append("\tat ").append(element.toString()).append("\n")
    }
    
    var cause = e.cause
    while (cause != null) {
      sb.append("Caused by: ").append(cause.javaClass.name)
      if (cause.message != null) {
        sb.append(": ").append(cause.message)
      }
      sb.append("\n")
      for (element in cause.stackTrace) {
        sb.append("\tat ").append(element.toString()).append("\n")
      }
      cause = cause.cause
    }
    
    return sb.toString()
  }
}
