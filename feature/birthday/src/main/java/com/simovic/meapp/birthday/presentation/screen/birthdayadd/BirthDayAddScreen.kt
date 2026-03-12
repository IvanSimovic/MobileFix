package com.simovic.meapp.birthday.presentation.screen.birthdayadd

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anhaki.picktime.PickDayMonth
import com.simovic.meapp.feature.base.common.res.Dimen
import com.simovic.meapp.feature.base.presentation.compose.composable.AppButton
import com.simovic.meapp.feature.base.presentation.compose.composable.AppPreview
import com.simovic.meapp.feature.base.presentation.compose.composable.AppTextField
import com.simovic.meapp.feature.birthday.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDayAddScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: BirthDayAddViewModel = koinViewModel()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var day by remember { mutableIntStateOf(1) }
    var month by remember { mutableIntStateOf(1) }
    val currentOnFinish by rememberUpdatedState(onFinish)

    LaunchedEffect(uiState) {
        if (uiState == BirthDayAddUiState.Success) currentOnFinish()
    }

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.add_birthday)) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = onFinish) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (uiState is BirthDayAddUiState.Empty) {
                BirthDayAddScreen(
                    name = name,
                    initialDay = day,
                    initialMonth = month,
                    onNameChange = { newName: String -> name = newName },
                    onDayChange = { newDay: Int -> day = newDay },
                    onMonthChange = { newMonth: Int -> month = newMonth },
                    onFinish = { viewModel.add(name, day, month) },
                )
            }
        }
    }
}

@Composable
private fun BirthDayAddScreen(
    name: String,
    initialDay: Int,
    initialMonth: Int,
    onNameChange: (String) -> Unit,
    onDayChange: (Int) -> Unit,
    onMonthChange: (Int) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = Dimen.screenContentPadding, vertical = Dimen.spaceXL),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppTextField(
            value = name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
        )
        Spacer(Modifier.height(Dimen.spaceL))
        PickDayMonth(
            initialDay = initialDay,
            onDayChange = onDayChange,
            initialMonth = initialMonth,
            onMonthChange = onMonthChange,
        )
        Spacer(Modifier.height(Dimen.spaceXL))
        AppButton(onClick = { onFinish() }, text = stringResource(R.string.add), modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun BirthDayAddScreenPreview() {
    AppPreview {
        BirthDayAddScreen(
            name = "John",
            initialDay = 1,
            initialMonth = 1,
            onNameChange = {},
            onDayChange = {},
            onMonthChange = {},
            onFinish = { },
        )
    }
}
