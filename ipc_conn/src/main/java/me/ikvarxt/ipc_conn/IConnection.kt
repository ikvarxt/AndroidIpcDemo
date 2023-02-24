package me.ikvarxt.ipc_conn

import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel


/**
 * @author ikvarxt
 */
interface IConnection : IInterface {

    fun doCommand(bundle: Bundle): Boolean

    abstract class Stub : Binder(), IConnection {

        init {
            kotlin.run {
                attachInterface(this, DESCRIPTOR)
            }
        }

        companion object {
            const val DESCRIPTOR = "me.ikvarxt.ipc_conn.IConnection.Stub"

            const val TRANS_doCommand = FIRST_CALL_TRANSACTION + 1

            fun asInterface(remote: IBinder): IConnection {
                val iConn = remote.queryLocalInterface(DESCRIPTOR)
                if (iConn != null && iConn is IConnection) {
                    return iConn
                }
                return Proxy(remote)
            }
        }

        override fun asBinder(): IBinder = this

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {

            val desc = DESCRIPTOR

            when (code) {
                INTERFACE_TRANSACTION -> {
                    reply?.writeString(desc)
                    return true
                }

                TRANS_doCommand -> {
                    data.enforceInterface(desc)
                    val bundle = data.readBundle()
                    if (bundle != null) {
                        val status = doCommand(bundle)
                        reply?.writeNoException()
                        reply?.writeInt(status.toString().toInt())
                    }
                    return true
                }

                else -> {
                    return super.onTransact(code, data, reply, flags)
                }
            }
        }

        class Proxy(private val mRemote: IBinder) : IConnection {

            override fun doCommand(bundle: Bundle): Boolean {
                val data = Parcel.obtain()
                val reply = Parcel.obtain()

                val result: Boolean?

                try {
                    data.enforceInterface(DESCRIPTOR)
                    bundle.writeToParcel(data, 0)
                    mRemote.transact(TRANS_doCommand, data, reply, 0)

                    reply.readException()
                    result = reply.readInt() == 1
                } finally {
                    data.recycle()
                    reply.recycle()
                }
                return result ?: false
            }

            override fun asBinder(): IBinder = mRemote
        }
    }
}