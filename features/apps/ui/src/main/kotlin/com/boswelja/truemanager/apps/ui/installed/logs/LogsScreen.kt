package com.boswelja.truemanager.apps.ui.installed.logs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.boswelja.truemanager.core.logviewer.LogViewer
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

/**
 * A screen to allow users to configure and display logs for an application.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: LogsViewModel = getViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val selectedLogOption by viewModel.selectedLogOptions.collectAsState()
    val logs by viewModel.logs.collectAsState()

    val logOptionPickerState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { false }
    )

    LaunchedEffect(selectedLogOption) {
        if (selectedLogOption == null) logOptionPickerState.show()
    }

    AnimatedContent(
        targetState = logs,
        label = "Log transition",
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        if (it != null) {
            LogViewer(logContents = it, modifier = modifier.padding(contentPadding))
        } else {
            // TODO Loading
        }
    }

    ModalBottomSheet(
        onDismissRequest = { /* no-op */ },
        sheetState = logOptionPickerState
    ) {
        val logOptions by viewModel.getLogOptions().collectAsState(initial = null)
        LogOptionsPicker(
            options = logOptions,
            onLogOptionsSelected = {
                viewModel.setSelectedLogOptions(it)
                coroutineScope.launch {
                    logOptionPickerState.hide()
                }
            }
        )
    }
}
