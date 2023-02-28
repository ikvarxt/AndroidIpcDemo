package me.ikvarxt.androidipcdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import me.ikvarxt.androidipcdemo.ui.theme.AndroidIpcDemoTheme
import me.ikvarxt.ipc_conn.CommandCore
import me.ikvarxt.ipc_conn.IConnection

class MainActivity : ComponentActivity() {

    private var iconn: IConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidIpcDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android") {
                        repeat(100) {
                            clickAction(10000)
                        }
                    }
                }
            }
        }

        Intent().also {
            // use action & package
            // it.action = "me.ikvarxt.ipc_conn.service"
            // it.`package` = "me.ikvarxt.app_target"

            // or component
            it.component = ComponentName("me.ikvarxt.app_target", "me.ikvarxt.ipc_conn.MyService")
            val status = bindService(it, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    if (service != null) {
                        log("service connected")
                        iconn = IConnection.Stub.asInterface(service)
                    } else {
                        log("remote service is null")
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    log("service disconnected")
                }
            }, Context.BIND_AUTO_CREATE)
            log("bind service status: $status")
        }
    }

    private fun clickAction(stringCount: Int) {
        val b = bundleOf(
            CommandCore.KEY_COMMAND to CommandCore.CMD_PRINT_LOG,
            CommandCore.KEY_MESSAGE to "Hello Android!".repeat(stringCount)
        )
        if (iconn != null) {
            iconn?.doCommand(b)
        } else {
            Toast.makeText(this@MainActivity, "iconn null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun log(msg: String) {
        Log.d("MainActivity", msg)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, click: () -> Unit) {
    TextButton(
        modifier = modifier,
        onClick = click
    ) {
        Text(text = "Hello $name!")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidIpcDemoTheme {
        Greeting("Android") {

        }
    }
}