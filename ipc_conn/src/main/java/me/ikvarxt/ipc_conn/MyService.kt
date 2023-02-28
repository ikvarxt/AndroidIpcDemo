package me.ikvarxt.ipc_conn

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder


/**
 * @author ikvarxt
 */
class MyService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return MyBinder
    }
}

object MyBinder : IConnection.Stub() {

    override fun doCommand(bundle: Bundle): Boolean {
        return CommandCore.doCommand(bundle)
    }
}