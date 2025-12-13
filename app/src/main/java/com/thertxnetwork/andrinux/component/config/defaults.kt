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
  private var rootPath: String? = null
  
  fun init(context: Context) {
    // Use actual app files directory for better compatibility
    rootPath = context.filesDir.absolutePath
  }
  
  @SuppressLint("SdCardPath")
  val ROOT_PATH: String
    get() = rootPath ?: "/data/data/com.thertxnetwork.andrinux/files"
    
  val USR_PATH: String
    get() = "$ROOT_PATH/usr"
    
  val HOME_PATH: String
    get() = "$ROOT_PATH/home"
    
  val APT_BIN_PATH: String
    get() = "$USR_PATH/bin/apt"
    
  val LIB_PATH: String
    get() = "$USR_PATH/lib"

  val CUSTOM_PATH: String
    get() = "$HOME_PATH/.neoterm"
    
  val NEOTERM_LOGIN_SHELL_PATH: String
    get() = "$CUSTOM_PATH/shell"
    
  val EKS_PATH: String
    get() = "$CUSTOM_PATH/eks"
    
  val EKS_DEFAULT_FILE: String
    get() = "$EKS_PATH/default.nl"
    
  val FONT_PATH: String
    get() = "$CUSTOM_PATH/font"
    
  val COLORS_PATH: String
    get() = "$CUSTOM_PATH/color"
    
  val USER_SCRIPT_PATH: String
    get() = "$CUSTOM_PATH/script"
    
  val PROFILE_PATH: String
    get() = "$CUSTOM_PATH/profile"

  val SOURCE_FILE: String
    get() = "$USR_PATH/etc/apt/sources.list"
    
  val PACKAGE_LIST_DIR: String
    get() = "$USR_PATH/var/lib/apt/lists"

  private const val SOURCE = "https://raw.githubusercontent.com/NeoTerm/NeoTerm-repo/main"

  val DEFAULT_MAIN_PACKAGE_SOURCE: String

  init {
    DEFAULT_MAIN_PACKAGE_SOURCE = SOURCE
  }
}
