package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp

@Composable
fun ST_TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String? = null,
    focusRequester: FocusRequester? = null
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .then(if(focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = placeholderText?.let { {
            Text(text = placeholderText)
        } },
        maxLines = 10 ,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,

            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,

            focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),

            cursorColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}