package com.nasdroid.apps.ui.installed.overview.logs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nasdroid.core.logviewer.LogViewer
import com.nasdroid.navigation.BackNavigationScaffold
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * A screen to allow users to configure and display logs for an application.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: LogsViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val selectedLogOption by viewModel.selectedLogOptions.collectAsState()
    val logs by viewModel.logs.collectAsState()

    var isOptionPickerVisible by rememberSaveable(selectedLogOption) {
        mutableStateOf(selectedLogOption == null)
    }

    BackNavigationScaffold(
        title = {
            Text("Logs")
        },
        onNavigateBack = {},
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = logs,
            label = "Log transition",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { currentLogs ->
            if (currentLogs != null) {
                LogViewer(
                    logContents = currentLogs,
                    modifier = Modifier.fillMaxSize().padding(it),
                    contentPadding = contentPadding
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().padding(it)) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }

    if (isOptionPickerVisible) {
        val logOptionPickerState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { false }
        )
        ModalBottomSheet(
            onDismissRequest = { /* no-op */ },
            sheetState = logOptionPickerState,
            dragHandle = { /* no-op */ }
        ) {
            val logOptions by viewModel.getLogOptions().collectAsState(initial = null)
            LogOptionsPicker(
                options = logOptions,
                onLogOptionsSelected = { selectedLogOptions ->
                    viewModel.setSelectedLogOptions(selectedLogOptions)
                    coroutineScope.launch {
                        logOptionPickerState.hide()
                        isOptionPickerVisible = false
                    }
                },
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}
