/*
package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ixp0mt.supertodo.R

@Composable
fun ST_TaskCard(
    item: TaskInfo,
    onSpecialClick: () -> Unit,
    onElementClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = if(item.dateCompleted == null) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(0.5f)
            }
        ),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onElementClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.87f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onSpecialClick() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_task),
                        contentDescription = null
                    )
                }

                Column {
                    Text(
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = item.name,
                        style = if(item.dateCompleted == null) {
                            LocalTextStyle.current
                        } else {
                            LocalTextStyle.current.copy(
                                textDecoration = TextDecoration.LineThrough,
                            )
                        }
                    )
                    item.description?.let { strCounters ->
                        Text(
                            text = strCounters,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    }
                }
            }

            Icon(
                modifier = Modifier
                    .weight(0.13f),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = null
            )
        }
    }
}*/
