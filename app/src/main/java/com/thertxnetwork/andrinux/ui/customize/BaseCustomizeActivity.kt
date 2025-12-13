package com.thertxnetwork.andrinux.ui.customize

import android.annotation.SuppressLint
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.thertxnetwork.andrinux.R
import com.thertxnetwork.andrinux.backend.TerminalSession
import com.thertxnetwork.andrinux.component.config.NeoTermPath
import com.thertxnetwork.andrinux.component.session.ShellParameter
import com.thertxnetwork.andrinux.frontend.session.terminal.BasicSessionCallback
import com.thertxnetwork.andrinux.frontend.session.terminal.BasicViewClient
import com.thertxnetwork.andrinux.frontend.session.view.TerminalView
import com.thertxnetwork.andrinux.frontend.session.view.extrakey.ExtraKeysView
import com.thertxnetwork.andrinux.utils.Terminals

/**
 * @author kiva
 */
@SuppressLint("Registered")
open class BaseCustomizeActivity : AppCompatActivity() {
  lateinit var terminalView: TerminalView
  lateinit var viewClient: BasicViewClient
  lateinit var sessionCallback: BasicSessionCallback
  lateinit var session: TerminalSession
  lateinit var extraKeysView: ExtraKeysView

  fun initCustomizationComponent(layoutId: Int) {
    setContentView(layoutId)

    val toolbar = findViewById<Toolbar>(R.id.custom_toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    terminalView = findViewById(R.id.terminal_view)
    extraKeysView = findViewById(R.id.custom_extra_keys)
    viewClient = BasicViewClient(terminalView)
    sessionCallback = BasicSessionCallback(terminalView)
    Terminals.setupTerminalView(terminalView, viewClient)
    Terminals.setupExtraKeysView(extraKeysView)

    val script = resources.getStringArray(R.array.custom_preview_script_colors)
    val parameter = ShellParameter()
      .executablePath("${NeoTermPath.USR_PATH}/bin/echo")
      .arguments(arrayOf("echo", "-e", *script))
      .callback(sessionCallback)
      .systemShell(false)

    session = Terminals.createSession(this, parameter)
    terminalView.attachSession(session)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> finish()
    }
    return super.onOptionsItemSelected(item)
  }
}
