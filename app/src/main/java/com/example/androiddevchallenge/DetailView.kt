/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.graphics.Bitmap
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.example.androiddevchallenge.data.Puppy

@Composable
fun DetailView(vm: NavViewModel, puppy: Puppy) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { vm.onBack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = puppy.name,
                            Modifier.offset(-Icons.Filled.ArrowBack.defaultWidth)
                        )
                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                .offset(-Icons.Filled.ArrowBack.defaultWidth),
                            colorFilter = ColorFilter.tint(puppy.gender.color),
                            imageVector = puppy.gender.label,
                            contentDescription = puppy.gender.str
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                },
                elevation = 8.dp
            )
        }
    ) {
        BodyContent(
            puppy,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}

@Composable
fun BodyContent(puppy: Puppy, modifier: Modifier) {
    val image = ImageBitmap.imageResource(puppy.images.first())
    val bitmap = image.asAndroidBitmap()
    val mutedColor = createPaletteSync(bitmap).getMutedColor(Color.White.toArgb())
    var showAnimaState by remember { mutableStateOf(false) }

    Box {
        LazyColumn {
            item {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = modifier
                        .heightIn(max = 300.dp)
                        .shadow(20.dp)
                ) {
                    Box(modifier = Modifier.clickable { }) {
                        val imageSrc = ImageBitmap.imageResource(puppy.images.first())
                        Image(
                            bitmap = imageSrc,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                        )
                        Column(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.8f))
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.BottomStart)
                                .heightIn(min = 100.dp)
                        ) {
                            Row {
                                Icon(
                                    Icons.Filled.Category,
                                    contentDescription = "breed",
                                    tint = Color(mutedColor)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = puppy.breed,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }
                            Row {
                                Icon(
                                    Icons.Filled.LocationCity,
                                    contentDescription = "location",
                                    tint = Color(mutedColor)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = puppy.location,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }
                            Row {
                                Icon(
                                    Icons.Filled.Timelapse,
                                    contentDescription = "age",
                                    tint = Color(mutedColor)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = puppy.age,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }
                            Row {
                                Icon(
                                    Icons.Filled.ColorLens,
                                    contentDescription = "color",
                                    tint = Color(mutedColor)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = puppy.color,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }
                        }
                    }
                }
            }
            item {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = modifier
                        .shadow(20.dp)
                ) {
                    Text(
                        text = "\t\t${puppy.story}",
                        modifier = modifier.padding(16.dp),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            items(puppy.images.size - 1) { index ->
                val img = ImageBitmap.imageResource(puppy.images[index + 1])
                Image(
                    bitmap = img,
                    contentDescription = puppy.name,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(5.dp)
                        )
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .size(50.dp, 50.dp)
                .align(alignment = Alignment.BottomEnd)
                .offset((-30).dp, (-30).dp),
            backgroundColor = colorResource(id = R.color.purple_500).copy(alpha = 0.8f),
            onClick = {
                showAnimaState = !showAnimaState
            }
        ) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (showAnimaState) {
            var scaleUp by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                if (scaleUp) 1.1f else 0.9f,
                finishedListener = { scaleUp = !scaleUp },
                animationSpec = spring(stiffness = Spring.StiffnessLow),
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .scale(scale = scale),
                bitmap = ImageBitmap.imageResource(id = R.drawable.congurtulations),
                contentDescription = "congratulations"
            )
        }
    }
}

// Generate palette synchronously and return it
fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()
