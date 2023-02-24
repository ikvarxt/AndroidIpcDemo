package me.ikvarxt.ipc_conn

import android.os.Bundle
import android.util.Log


/**
 * @author ikvarxt
 */
object CommandCore {

    private const val TAG = "CommandCore"

    const val KEY_COMMAND = "key_command"
    const val KEY_MESSAGE = "key_message"

    const val CMD_PRINT_LOG = "cmd_print_log"

    fun doCommand(bundle: Bundle): Boolean {

        return when (bundle.getString(KEY_COMMAND)) {
            CMD_PRINT_LOG -> {
                val msg = bundle.getString(KEY_MESSAGE)
                Log.d(TAG, "doCommand: $msg")
                true
            }

            else -> false
        }
    }
}