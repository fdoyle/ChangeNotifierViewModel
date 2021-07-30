package com.lacronicus.changenotifierviewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.lacronicus.changenotifier.ChangeNotifierViewModel
import com.lacronicus.changenotifier.withChangeNotifier
import com.lacronicus.changenotifierviewmodel.ui.theme.ChangeNotifierViewModelTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChangeNotifierViewModelTheme() {
                Surface(color = MaterialTheme.colors.background) {
                    MyScreen()
                }
            }
        }
    }
}

class MainViewModel : ChangeNotifierViewModel() {
    var counter = 10

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000) //give TTS some time to spin up. removing this results in the first line of speech being cut off. cause unknown, but doesn't look like a coroutine thing
                decrement()

            }
        }
    }

    fun increment() {
        counter++
        notifyListeners()
    }

    fun decrement() {
        if (counter > 0) {
            counter--
            notifyListeners()
        }
    }
}

@Composable
fun MyScreen() {
    val vm = withChangeNotifier<MainViewModel>()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${vm.counter}",
            modifier = Modifier.padding(bottom = 10.dp))
        Button(onClick = { vm.increment() }) {
            Text("Increment!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChangeNotifierViewModelTheme {
        MyScreen()
    }
}