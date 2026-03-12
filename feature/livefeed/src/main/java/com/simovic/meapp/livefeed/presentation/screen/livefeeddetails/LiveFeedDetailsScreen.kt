package com.simovic.meapp.livefeed.presentation.screen.livefeeddetails

import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simovic.meapp.feature.base.common.res.Dimen
import com.simovic.meapp.feature.base.presentation.compose.composable.AppPreview
import com.simovic.meapp.feature.base.presentation.compose.composable.LoadingIndicator
import com.simovic.meapp.feature.base.presentation.ui.AppTheme
import com.simovic.meapp.feature.base.util.formatRssDate
import com.simovic.meapp.feature.livefeed.R
import com.simovic.meapp.livefeed.domain.model.FeedItem
import org.koin.androidx.compose.koinViewModel

private const val BODY_TEXT_SIZE_SP = 16f
private const val BODY_LINE_SPACING_MULTIPLIER = 1.6f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveFeedDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: LiveFeedDetailsViewModel = koinViewModel()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.news_feed)) },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when (val currentUiState = uiState) {
                LiveFeedDetailsUiState.Loading -> LoadingIndicator(modifier = Modifier.align(Alignment.Center))
                is LiveFeedDetailsUiState.Content -> LiveFeedDetailsContent(currentUiState)
            }
        }
    }
}

@Composable
private fun LiveFeedDetailsContent(content: LiveFeedDetailsUiState.Content) {
    val textColor = AppTheme.color.textMain.toArgb()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimen.screenContentPadding, vertical = Dimen.spaceM),
    ) {
        Text(
            content.feed.pubDate.formatRssDate(),
            style = AppTheme.typo.body3,
            color = AppTheme.color.textMuted,
        )
        Spacer(modifier = Modifier.height(Dimen.spaceS))
        Text(
            HtmlCompat.fromHtml(content.feed.title, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
            style = AppTheme.typo.head2,
            color = AppTheme.color.textMain,
        )
        Spacer(modifier = Modifier.height(Dimen.spaceL))
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                TextView(context).apply {
                    movementMethod = LinkMovementMethod.getInstance()
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, BODY_TEXT_SIZE_SP)
                    setLineSpacing(0f, BODY_LINE_SPACING_MULTIPLIER)
                }
            },
            update = { textView ->
                textView.setTextColor(textColor)
                textView.text =
                    HtmlCompat.fromHtml(
                        content.feed.description,
                        HtmlCompat.FROM_HTML_MODE_COMPACT,
                    )
            },
        )
        Spacer(modifier = Modifier.height(Dimen.spaceXL))
        OutlinedButton(
            onClick = { uriHandler.openUri(content.feed.link) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.open_full_article))
        }
        Spacer(modifier = Modifier.height(Dimen.spaceM))
    }
}

@Composable
@Preview
private fun Preview() {
    AppPreview {
        LiveFeedDetailsContent(
            LiveFeedDetailsUiState.Content(
                feed =
                    FeedItem(
                        guid = "guid id is this",
                        title = "Test",
                        link = "www.google.com",
                        pubDate = "05.05.1995",
                        description = "Hello this is a long description",
                    ),
            ),
        )
    }
}
