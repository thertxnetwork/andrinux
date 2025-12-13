package com.thertxnetwork.andrinux.component.config

import android.annotation.SuppressLint
import android.content.Context

object DefaultValues {
  const val fontSize = 30

  const val enableBell = false
  const val enableVibrate = false
  const val enableExecveWrapper = true
  const val enableAutoCompletion = false
  const val enableFullScreen = false
  const val enableAutoHideToolbar = false
  const val enableSwitchNextTab = false
  const val enableExtraKeys = true
  const val enableExplicitExtraKeysWeight = false
  const val enableBackButtonBeMappedToEscape = false
  const val enableSpecialVolumeKeys = false
  const val enableWordBasedIme = false

  const val loginShell = "bash"
  const val initialCommand = ""
  const val defaultFont = "SourceCodePro"
}

object NeoTermPath {
  @Volatile
  private var rootPath: String? = null
  
  @JvmStatic
  fun init(context: Context) {
    // Use actual app files directory for better compatibility
    rootPath = context.filesDir.absolutePath
  }
  
  @get:JvmStatic
  val ROOT_PATH: String
    get() = rootPath ?: throw IllegalStateException(
      "NeoTermPath.init() must be called in Application.onCreate() before accessing paths"
    )
    
  @get:JvmStatic
  val USR_PATH: String
    get() = "$ROOT_PATH/usr"
    
  @get:JvmStatic
  val HOME_PATH: String
    get() = "$ROOT_PATH/home"
    
  @get:JvmStatic
  val APT_BIN_PATH: String
    get() = "$USR_PATH/bin/apt"
    
  @get:JvmStatic
  val LIB_PATH: String
    get() = "$USR_PATH/lib"

  @get:JvmStatic
  val CUSTOM_PATH: String
    get() = "$HOME_PATH/.neoterm"
    
  @get:JvmStatic
  val NEOTERM_LOGIN_SHELL_PATH: String
    get() = "$CUSTOM_PATH/shell"
    
  @get:JvmStatic
  val EKS_PATH: String
    get() = "$CUSTOM_PATH/eks"
    
  @get:JvmStatic
  val EKS_DEFAULT_FILE: String
    get() = "$EKS_PATH/default.nl"
    
  @get:JvmStatic
  val FONT_PATH: String
    get() = "$CUSTOM_PATH/font"
    
  @get:JvmStatic
  val COLORS_PATH: String
    get() = "$CUSTOM_PATH/color"
    
  @get:JvmStatic
  val USER_SCRIPT_PATH: String
    get() = "$CUSTOM_PATH/script"
    
  @get:JvmStatic
  val PROFILE_PATH: String
    get() = "$CUSTOM_PATH/profile"

  @get:JvmStatic
  val SOURCE_FILE: String
    get() = "$USR_PATH/etc/apt/sources.list"
    
  @get:JvmStatic
  val PACKAGE_LIST_DIR: String
    get() = "$USR_PATH/var/lib/apt/lists"

  private const val SOURCE = "https://raw.githubusercontent.com/NeoTerm/NeoTerm-repo/main"

  @get:JvmStatic
  val DEFAULT_MAIN_PACKAGE_SOURCE: String
    get() = SOURCE
}
