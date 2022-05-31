package ro.valentin.cinemania.core

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible

class Utils {
    companion object {
        fun ProgressBar.show() {
            if(!isVisible) {
                visibility = View.VISIBLE
            }
        }

        fun ProgressBar.hide() {
            if(isVisible) {
                visibility = View.GONE
            }
        }
    }
}