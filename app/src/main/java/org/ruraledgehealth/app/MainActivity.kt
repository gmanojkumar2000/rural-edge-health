package org.ruraledgehealth.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.ruraledgehealth.app.ui.RuralEdgeHealthApp
import org.ruraledgehealth.app.ui.theme.RuralEdgeHealthTheme

/**
 * Android launches this Activity first.
 *
 * Its only job is to create the Compose UI. Application logic belongs in the
 * ViewModel and domain classes so it can be understood and tested separately.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // The theme supplies shared colors and typography to every screen.
            RuralEdgeHealthTheme {
                RuralEdgeHealthApp()
            }
        }
    }
}
