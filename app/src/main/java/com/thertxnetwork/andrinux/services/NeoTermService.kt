package com.thertxnetwork.andrinux.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.wifi.WifiManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.thertxnetwork.andrinux.R
import com.thertxnetwork.andrinux.backend.EmulatorDebug
import com.thertxnetwork.andrinux.backend.TerminalSession
import com.thertxnetwork.andrinux.component.session.ShellParameter
import com.thertxnetwork.andrinux.component.session.XParameter
import com.thertxnetwork.andrinux.component.session.XSession
import com.thertxnetwork.andrinux.ui.term.NeoTermActivity
import com.thertxnetwork.andrinux.utils.NLog
import com.thertxnetwork.andrinux.utils.Terminals


/**
 * @author kiva
 */

class NeoTermService : Service() {
  inner class NeoTermBinder : Binder() {
    var service = this@NeoTermService
  }

  private val serviceBinder = NeoTermBinder()
  private val mTerminalSessions = ArrayList<TerminalSession>()
  private val mXSessions = ArrayList<XSession>()
  private var mWakeLock: PowerManager.WakeLock? = null
  private var mWifiLock: WifiManager.WifiLock? = null

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      ServiceCompat.startForeground(
        this,
        NOTIFICATION_ID,
        createNotification(),
        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
      )
    } else {
      startForeground(NOTIFICATION_ID, createNotification())
    }
  }

  override fun onBind(intent: Intent): IBinder {
    return serviceBinder
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val action = intent?.action
    when (action) {
      ACTION_SERVICE_STOP -> {
        for (i in mTerminalSessions.indices)
          mTerminalSessions[i].finishIfRunning()
        stopSelf()
      }

      ACTION_ACQUIRE_LOCK -> acquireLock()

      ACTION_RELEASE_LOCK -> releaseLock()
    }

    return START_NOT_STICKY
  }

  override fun onDestroy() {
    ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)

    for (i in mTerminalSessions.indices)
      mTerminalSessions[i].finishIfRunning()
    mTerminalSessions.clear()
  }

  val sessions: List<TerminalSession>
    get() = mTerminalSessions

  val xSessions: List<XSession>
    get() = mXSessions

  fun createTermSession(parameter: ShellParameter): TerminalSession {
    val session = createOrFindSession(parameter)
    updateNotification()
    return session
  }

  fun removeTermSession(sessionToRemove: TerminalSession): Int {
    val indexOfRemoved = mTerminalSessions.indexOf(sessionToRemove)
    if (indexOfRemoved >= 0) {
      mTerminalSessions.removeAt(indexOfRemoved)
      updateNotification()
    }
    return indexOfRemoved
  }

  fun createXSession(activity: AppCompatActivity, parameter: XParameter): XSession {
    val session = Terminals.createSession(activity, parameter)
    mXSessions.add(session)
    updateNotification()
    return session
  }

  fun removeXSession(sessionToRemove: XSession): Int {
    val indexOfRemoved = mXSessions.indexOf(sessionToRemove)
    if (indexOfRemoved >= 0) {
      mXSessions.removeAt(indexOfRemoved)
      updateNotification()
    }
    return indexOfRemoved
  }

  private fun createOrFindSession(parameter: ShellParameter): TerminalSession {
    if (parameter.willCreateNewSession()) {
      NLog.d("createOrFindSession: creating new session")
      val session = Terminals.createSession(this, parameter)
      mTerminalSessions.add(session)
      return session
    }

    val sessionId = parameter.sessionId!!
    NLog.d("createOrFindSession: find session by id $sessionId")

    val session = mTerminalSessions.find { it.mHandle == sessionId.sessionId }
      ?: throw IllegalArgumentException("cannot find session by given id")

    session.write(parameter.initialCommand + "\n")
    return session
  }

  private fun updateNotification() {
    val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    service.notify(NOTIFICATION_ID, createNotification())
  }

  private fun createNotification(): Notification {
    val notifyIntent = Intent(this, NeoTermActivity::class.java)
    notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    val pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, pendingIntentFlags)

    val sessionCount = mTerminalSessions.size
    val xSessionCount = mXSessions.size
    var contentText = getString(R.string.service_status_text, sessionCount, xSessionCount)

    val lockAcquired = mWakeLock != null
    if (lockAcquired) contentText += getString(R.string.service_lock_acquired)

    val builder = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
    builder.setContentTitle(getText(R.string.app_name))
    builder.setContentText(contentText)
    builder.setSmallIcon(R.drawable.ic_terminal_running)
    builder.setContentIntent(pendingIntent)
    builder.setOngoing(true)
    builder.setShowWhen(false)
    builder.color = 0xFF000000.toInt()

    builder.priority = if (lockAcquired) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_LOW

    val exitIntent = Intent(this, NeoTermService::class.java).setAction(ACTION_SERVICE_STOP)
    builder.addAction(
      android.R.drawable.ic_delete,
      getString(R.string.exit),
      PendingIntent.getService(this, 0, exitIntent, pendingIntentFlags)
    )

    val newWakeAction = if (lockAcquired) ACTION_RELEASE_LOCK else ACTION_ACQUIRE_LOCK
    val toggleWakeLockIntent = Intent(this, NeoTermService::class.java).setAction(newWakeAction)
    val actionTitle = getString(
      if (lockAcquired)
        R.string.service_release_lock
      else
        R.string.service_acquire_lock
    )
    val actionIcon = if (lockAcquired) android.R.drawable.ic_lock_idle_lock else android.R.drawable.ic_lock_lock
    builder.addAction(actionIcon, actionTitle, PendingIntent.getService(this, 0, toggleWakeLockIntent, pendingIntentFlags))

    return builder.build()
  }

  private fun createNotificationChannel() {
    val channel = NotificationChannel(DEFAULT_CHANNEL_ID, "Andrinux", NotificationManager.IMPORTANCE_LOW)
    channel.description = "Andrinux terminal notifications"
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
  }

  @SuppressLint("WakelockTimeout")
  private fun acquireLock() {
    if (mWakeLock == null) {
      val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
      mWakeLock = pm.newWakeLock(
        PowerManager.PARTIAL_WAKE_LOCK,
        EmulatorDebug.LOG_TAG + ":"
      )
      mWakeLock!!.acquire()

      val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
      mWifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, EmulatorDebug.LOG_TAG)
      mWifiLock!!.acquire()

      updateNotification()
    }
  }

  private fun releaseLock() {
    if (mWakeLock != null) {
      mWakeLock!!.release()
      mWakeLock = null

      mWifiLock!!.release()
      mWifiLock = null

      updateNotification()
    }
  }

  companion object {
    const val ACTION_SERVICE_STOP = "com.thertxnetwork.andrinux.action.service.stop"
    const val ACTION_ACQUIRE_LOCK = "com.thertxnetwork.andrinux.action.service.lock.acquire"
    const val ACTION_RELEASE_LOCK = "com.thertxnetwork.andrinux.action.service.lock.release"
    private const val NOTIFICATION_ID = 52019

    const val DEFAULT_CHANNEL_ID = "andrinux_notification_channel"
  }
}
