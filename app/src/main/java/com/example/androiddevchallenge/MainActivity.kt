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
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import com.example.androiddevchallenge.data.DemoDataProvider
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}


// Start building your app here!
@ExperimentalFoundationApi
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "page_home_list") {
        composable("page_home_list") { HomePageList(navController) }
        composable("page_puppy_detail") { DetailView(navController) }
    }
}

@ExperimentalFoundationApi
@Composable
fun HomePageList(navController: NavHostController) {
    val list = remember { DemoDataProvider.puppyList }
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(list.size) { index ->
            val puppy = list[index]
            GridItemView(
                puppy = puppy,
                modifier = if (index % 2 == 0) Modifier.padding(
                    start = 6.dp,
                    end = 4.dp,
                    top = 8.dp,
                    bottom = 4.dp
                ) else Modifier.padding(
                    start = 4.dp, end = 6.dp, top = 8.dp,
                    bottom = 4.dp
                )
            ) { puppy: Puppy ->
                navController.navigate("page_puppy_detail")
            }
        }
    }
}

@Composable
private fun GridItemView(
    puppy: Puppy,
    modifier: Modifier = Modifier,
    onClick: (puppy: Puppy) -> Unit
) {
    val image = ImageBitmap.imageResource(puppy.images.first())
    /*val bitmap = image.asAndroidBitmap()
    val paletteColor = createPaletteSync(bitmap).getLightVibrantColor(0xBB86FC)*/
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.shadow(6.dp),
        /*backgroundColor = (Color(paletteColor).copy(alpha = 0.02f))*/
    ) {
        Box(modifier = Modifier.clickable { onClick.invoke(puppy) }) {
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = puppy.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.padding(1.dp))
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    colorFilter = ColorFilter.tint(puppy.gender.color),
                    imageVector = puppy.gender.label, contentDescription = null
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .fillMaxWidth(0.5f)
                    .height(240.dp),
            ) {

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = puppy.breed,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = puppy.location,
                    style = MaterialTheme.typography.body2
                )
            }
            Image(
                bitmap = image,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 30.dp, start = 6.dp, end = 6.dp)
                    .heightIn(max = 180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
            )
        }
    }
}

// Generate palette synchronously and return it
fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()


@ExperimentalFoundationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalFoundationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
