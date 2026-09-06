package org.ruraledgehealth.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ruraledgehealth.app.domain.model.AssessmentResult
import org.ruraledgehealth.app.domain.model.SensorStatus
import org.ruraledgehealth.app.ui.assessment.AssessmentUiState
import org.ruraledgehealth.app.ui.assessment.AssessmentViewModel

/**
 * Top-level Compose function for the current one-screen application.
 *
 * It observes ViewModel state and forwards user actions. It does not classify,
 * connect to sensors, or save records itself.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuralEdgeHealthApp(viewModel: AssessmentViewModel = viewModel()) {
    val state = viewModel.uiState

    Scaffold(
        topBar = { TopAppBar(title = { Text("RuralEdgeHealth") }) },
    ) { padding ->
        AssessmentContent(
            state = state,
            onHeartRateChange = viewModel::updateHeartRate,
            onTemperatureChange = viewModel::updateTemperature,
            onSystolicChange = viewModel::updateSystolicBp,
            onDiastolicChange = viewModel::updateDiastolicBp,
            onSpo2Change = viewModel::updateSpo2,
            onAssess = viewModel::assess,
            contentPadding = padding,
        )
    }
}

@Composable
private fun AssessmentContent(
    state: AssessmentUiState,
    onHeartRateChange: (String) -> Unit,
    onTemperatureChange: (String) -> Unit,
    onSystolicChange: (String) -> Unit,
    onDiastolicChange: (String) -> Unit,
    onSpo2Change: (String) -> Unit,
    onAssess: () -> Unit,
    contentPadding: PaddingValues,
) {
    // Passing values and callbacks separately keeps this UI function reusable in
    // previews and tests without requiring a real ViewModel.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Manual sensor entry",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Starter flow only - the trained ONNX model and BLE input come next.",
            style = MaterialTheme.typography.bodyMedium,
        )

        ReadingField("Heart rate (bpm)", state.heartRate, onHeartRateChange, false)
        ReadingField("Temperature (C)", state.temperature, onTemperatureChange, true)
        ReadingField("Systolic BP", state.systolicBp, onSystolicChange, false)
        ReadingField("Diastolic BP", state.diastolicBp, onDiastolicChange, false)
        ReadingField("SpO2 (%)", state.spo2, onSpo2Change, false)

        state.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = onAssess,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Run offline demo assessment")
        }

        state.result?.let { ResultCard(it) }

        Text(
            text = "Educational prototype only. Do not use for diagnosis or treatment decisions.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ReadingField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    decimal: Boolean,
) {
    // Numeric keyboard types make phone entry easier but do not replace validation.
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (decimal) KeyboardType.Decimal else KeyboardType.Number,
        ),
    )
}

@Composable
private fun ResultCard(result: AssessmentResult) {
    // Color supports quick recognition, while the text label keeps the result
    // understandable for color-blind users and screen readers.
    val containerColor = when (result.status) {
        SensorStatus.NORMAL -> Color(0xFFDDF4E6)
        SensorStatus.MODERATE -> Color(0xFFFFE9B5)
        SensorStatus.CRITICAL -> Color(0xFFFFDAD6)
        SensorStatus.UNCERTAIN -> Color(0xFFE5E7EB)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(result.status.name, style = MaterialTheme.typography.titleLarge)
            Text("Demo confidence: ${(result.confidence * 100).toInt()}%")
            Text(result.message)
        }
    }
}
