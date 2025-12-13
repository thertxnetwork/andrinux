package com.thertxnetwork.andrinux.ui.other

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thertxnetwork.andrinux.R
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.Serializable

/**
 * @author kiva
 */
class CrashActivity : AppCompatActivity() {
  private var crashInfo: String = ""
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.ui_crash)
    setSupportActionBar(findViewById(R.id.crash_toolbar))

    val modelInfo = collectModelInfo()
    val appInfo = collectAppInfo()
    val exceptionInfo = collectExceptionInfo()
    
    (findViewById<TextView>(R.id.crash_model)).text = getString(R.string.crash_model, modelInfo)
    (findViewById<TextView>(R.id.crash_app_version)).text = getString(R.string.crash_app, appInfo)
    (findViewById<TextView>(R.id.crash_details)).text = exceptionInfo
    
    // Build full crash report
    crashInfo = buildString {
      appendLine("=== Andrinux Crash Report ===")
      appendLine()
      appendLine("Model: $modelInfo")
      appendLine("App: $appInfo")
      appendLine()
      appendLine("Stack Trace:")
      appendLine(exceptionInfo)
    }
    
    // Setup action buttons
    findViewById<Button>(R.id.crash_copy_button)?.setOnClickListener {
      copyToClipboard()
    }
    
    findViewById<Button>(R.id.crash_share_button)?.setOnClickListener {
      shareReport()
    }
    
    findViewById<Button>(R.id.crash_restart_button)?.setOnClickListener {
      restartApp()
    }
  }
  
  private fun copyToClipboard() {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Crash Report", crashInfo)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, R.string.crash_copied, Toast.LENGTH_SHORT).show()
  }
  
  private fun shareReport() {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "Andrinux Crash Report")
      putExtra(Intent.EXTRA_TEXT, crashInfo)
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.crash_share)))
  }
  
  private fun restartApp() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    finish()
  }

  private fun collectExceptionInfo(): String {
    // First try the new way (string extras)
    val exceptionMessage = intent.getStringExtra("exception_message")
    val exceptionClass = intent.getStringExtra("exception_class")
    val stackTrace = intent.getStringExtra("exception_stacktrace")
    
    if (stackTrace != null) {
      return stackTrace
    }
    
    // Fallback to old way (serializable extra)
    val extra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getSerializableExtra("exception", Throwable::class.java)
    } else {
      @Suppress("DEPRECATION")
      intent.getSerializableExtra("exception") as? Throwable
    }
    
    if (extra != null) {
      val byteArrayOutput = ByteArrayOutputStream()
      val printStream = PrintStream(byteArrayOutput)
      (extra.cause ?: extra).printStackTrace(printStream)
      return byteArrayOutput.use {
        byteArrayOutput.toString("utf-8")
      }
    }
    
    return "are.you.kidding.me.NoExceptionFoundException: This is a bug, please contact developers!"
  }

  private fun collectAppInfo(): String {
    val pm = packageManager
    val info = pm.getPackageInfo(packageName, 0)
    return "${info.versionName} (${info.versionCode})"
  }

  private fun collectModelInfo(): String {
    return "${Build.MODEL} (Android ${Build.VERSION.RELEASE} ${determineArchName()})"
  }

  private fun determineArchName(): String {
    for (androidArch in Build.SUPPORTED_ABIS) {
      when (androidArch) {
        "arm64-v8a" -> return "aarch64"
        "armeabi-v7a" -> return "arm"
        "x86_64" -> return "x86_64"
        "x86" -> return "i686"
      }
    }
    return "Unknown Arch"
  }
}
