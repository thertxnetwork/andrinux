package com.thertxnetwork.andrinux.component.profile

import com.thertxnetwork.andrinux.neolang.frontend.ConfigVisitor
import com.thertxnetwork.andrinux.component.ComponentManager
import com.thertxnetwork.andrinux.component.ConfigFileBasedObject
import com.thertxnetwork.andrinux.component.codegen.CodeGenObject
import com.thertxnetwork.andrinux.component.codegen.CodeGenParameter
import com.thertxnetwork.andrinux.component.codegen.CodeGenerator
import com.thertxnetwork.andrinux.component.codegen.NeoProfileGenerator
import com.thertxnetwork.andrinux.component.config.ConfigureComponent
import com.thertxnetwork.andrinux.component.config.NeoConfigureFile
import com.thertxnetwork.andrinux.utils.NLog
import org.jetbrains.annotations.TestOnly
import java.io.File

abstract class NeoProfile : CodeGenObject, ConfigFileBasedObject {
  companion object {
    private const val PROFILE_NAME = "name"
  }

  abstract val profileMetaName: String
  private val profileMetaPath
    get() = arrayOf(profileMetaName)

  var profileName = "Unknown Profile"

  override fun onConfigLoaded(configVisitor: ConfigVisitor) {
    profileName = configVisitor.getProfileString(PROFILE_NAME, profileName)
  }

  override fun getCodeGenerator(parameter: CodeGenParameter): CodeGenerator {
    return NeoProfileGenerator(parameter)
  }

  @TestOnly
  fun testLoadConfigure(file: File): Boolean {
    val loaderService = ComponentManager.getComponent<ConfigureComponent>()

    val configure: NeoConfigureFile?
    try {
      configure = loaderService.newLoader(file).loadConfigure()
      if (configure == null) {
        throw RuntimeException("Parse configuration failed.")
      }
    } catch (e: Exception) {
      NLog.e("Profile", "Failed to load profile: ${file.absolutePath}: ${e.localizedMessage}")
      return false
    }

    val visitor = configure.getVisitor()
    onConfigLoaded(visitor)
    return true
  }

  protected fun ConfigVisitor.getProfileString(key: String, fallback: String): String {
    return getProfileString(key) ?: fallback
  }

  protected fun ConfigVisitor.getProfileBoolean(key: String, fallback: Boolean): Boolean {
    return getProfileBoolean(key) ?: fallback
  }

  protected fun ConfigVisitor.getProfileString(key: String): String? {
    return this.getStringValue(profileMetaPath, key)
  }

  protected fun ConfigVisitor.getProfileBoolean(key: String): Boolean? {
    return this.getBooleanValue(profileMetaPath, key)
  }
}
