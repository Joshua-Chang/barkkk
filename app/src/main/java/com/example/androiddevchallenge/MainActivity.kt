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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.data.DemoDataProvider
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    val vm: NavViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(vm)
            }
        }
    }
    override fun onBackPressed() {
        if (!vm.onBack()) {
            super.onBackPressed()
        }
    }
}
sealed class Screen {
    object HomeScreen : Screen()
    data class DetailsScreen(val puppy: Puppy) : Screen()
}

class NavViewModel : ViewModel() {

    private val _screen = MutableLiveData<Screen>(Screen.HomeScreen)

    var curScreen: LiveData<Screen> = _screen

    @MainThread
    fun onBack(): Boolean {
        if (_screen.value != Screen.HomeScreen) {
            _screen.value = Screen.HomeScreen
            return true
        }
        return false
    }

    @MainThread
    fun navigateToDetail(puppy: Puppy) {
        _screen.value = Screen.DetailsScreen(puppy)
    }
}

// Start building your app here!
@ExperimentalFoundationApi
@Composable
fun MyApp(vm: NavViewModel) {
    val curScreen by vm.curScreen.observeAsState(Screen.HomeScreen)

    Crossfade(curScreen) {
        Surface(color = MaterialTheme.colors.background) {

            when (curScreen) {
                is Screen.HomeScreen -> HomePageList(vm)
                is Screen.DetailsScreen -> DetailView(vm, (curScreen as Screen.DetailsScreen).puppy)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun HomePageList(vm: NavViewModel) {
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
            ) {
                vm.navigateToDetail(it)
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

@ExperimentalFoundationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(NavViewModel())
    }
}

@ExperimentalFoundationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(NavViewModel())
    }
}
