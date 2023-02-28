package me.ikvarxt.app_target

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
import me.ikvarxt.app_target.ui.theme.AndroidIpcDemoTheme
import me.ikvarxt.ipc_conn.CommandCore
import me.ikvarxt.ipc_conn.IConnection
import me.ikvarxt.ipc_conn.MyService

class TargetActivity : ComponentActivity() {

    private var iconn: IConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidIpcDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("I'm target process") {
//                        click()
                    }
                }
            }
        }

//        bindService()
    }

    private fun click() {
        if (iconn != null) {
            val b = bundleOf(
                CommandCore.KEY_COMMAND to CommandCore.CMD_PRINT_LOG,
                CommandCore.KEY_MESSAGE to "Hello Android!"
            )
            iconn?.doCommand(b)
        } else Toast.makeText(this, "iconn null", Toast.LENGTH_SHORT).show()
    }

    private fun bindService() {
        Intent(this, MyService::class.java).also {
            bindService(it, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    if (service != null) {
                        iconn = IConnection.Stub.asInterface(service)
                    } else {
                        Toast.makeText(this@TargetActivity, "service null", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    TODO("Not yet implemented")
                }
            }, BIND_AUTO_CREATE)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, click: () -> Unit) {
    TextButton(
        modifier = modifier,
        onClick = click
    ) {
        Text(text = "hello $name!")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidIpcDemoTheme {
        Greeting("Android") {}
    }
}