package com.app.utilities

import android.content.Context
import android.os.Build
import android.telephony.*
import androidx.annotation.RequiresApi

class SignalStrengthService(val context: Context) : PhoneStateListener() {

    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private var signalStrengthListener: (Int) -> Unit = { }

    // Initialise the signal strength changes
    fun listenToSignalStrengthChanges(){
        when {
            // API >= 23
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> telephonyManager.listen(this, LISTEN_SIGNAL_STRENGTHS)
            // API >= 17
            else -> telephonyManager.listen(this, LISTEN_CELL_INFO)
        }
    }

    // Listen to signal strength changes
    fun listenSignalStrengths(listener: (Int) -> Unit) {
        this.signalStrengthListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)
        val currentSignalStrength = signalStrength?.level
        if(currentSignalStrength != null) signalStrengthListener(currentSignalStrength)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCellInfoChanged(cellInfos: MutableList<CellInfo>?) {
        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED || androidx.core.app.ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_PHONE_STATE
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        super.onCellInfoChanged(cellInfos)
        val currentSignalStrength = if (cellInfos != null && cellInfos.isNotEmpty()) {
            cellInfos.map { cellInfo ->
                when (cellInfo) {
                    is CellInfoGsm -> cellInfo.cellSignalStrength.level
                    is CellInfoLte -> cellInfo.cellSignalStrength.level
                    is CellInfoCdma -> cellInfo.cellSignalStrength.level
                    is CellInfoWcdma -> cellInfo.cellSignalStrength.level
                    is CellInfoNr -> cellInfo.cellSignalStrength.level
                    is CellInfoTdscdma -> cellInfo.cellSignalStrength.level
                    else -> -1
                }
            }.maxByOrNull { signalStrength -> signalStrength }.takeIf { res -> res != null && res >= 0 }
        } else {
            null
        }
        if(currentSignalStrength != null) signalStrengthListener(currentSignalStrength)
    }
}