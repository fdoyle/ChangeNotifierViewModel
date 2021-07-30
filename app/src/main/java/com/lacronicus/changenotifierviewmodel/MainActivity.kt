package com.lacronicus.changenotifierviewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
    var counter1 = 10
    var counter2 = 10
    var currentTimeMillis = 0L

    init {
        viewModelScope.launch {
            while (true) {
                delay(10) //give TTS some time to spin up. removing this results in the first line of speech being cut off. cause unknown, but doesn't look like a coroutine thing
                currentTimeMillis = System.currentTimeMillis()
                notifyListeners()
            }
        }
        viewModelScope.launch {
            while (true) {
                delay(100) //give TTS some time to spin up. removing this results in the first line of speech being cut off. cause unknown, but doesn't look like a coroutine thing
                increment1()
            }
        }

        viewModelScope.launch {
            while (true) {
                delay(1200) //give TTS some time to spin up. removing this results in the first line of speech being cut off. cause unknown, but doesn't look like a coroutine thing
                decrement2()
            }
        }
    }

    fun increment1() {
        counter1++
        notifyListeners()
    }

    fun decrement1() {
        if (counter1 > 0) {
            counter1--
            notifyListeners()
        }
    }

    fun increment2() {
        counter2++
        notifyListeners()
    }

    fun decrement2() {
        currentTimeMillis = System.currentTimeMillis()
        if (counter2 > 0) {
            counter2--
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

        Text(text = "current time: ${vm.currentTimeMillis}")
        Spacer(modifier = Modifier.height(30.dp))
        counter1()
        Spacer(modifier = Modifier.height(30.dp))
        counter2()


    }
}

@Composable
fun counter1() {
    val vm: MainViewModel = withChangeNotifier()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "This section listens to all changes")
        Text(
            text = "counter 1: ${vm.counter1}",
            modifier = Modifier.padding(bottom = 10.dp))
        Text(
            text = "counter 2: ${vm.counter2}",
            modifier = Modifier.padding(bottom = 10.dp))
        Button(onClick = { vm.increment1() }) {
            Text("Increment counter 1")
        }
    }
}

@Composable
fun counter2() {
    val counter2 = withChangeNotifier { vm: MainViewModel -> vm.counter2 }
    val vm: MainViewModel = withChangeNotifier(listen = false)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "This section only listens to counter2 changes",
        )
        Text(
            text = "counter 1: ${vm.counter1}",
            modifier = Modifier.padding(bottom = 10.dp))

        Text(
            text = "counter 2: $counter2",
            modifier = Modifier.padding(bottom = 10.dp))
        Button(onClick = { vm.increment2() }) {
            Text("Increment counter 2")
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