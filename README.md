# ChangeNotifierViewModel
This project is a port of Flutter's ChangeNotifierProvider, implemented as a ViewModel base class

https://pub.dev/documentation/provider/latest/provider/ChangeNotifierProvider-class.html

https://jitpack.io/#fdoyle/ChangeNotifierViewModel

    maven { url 'https://jitpack.io' }

    implementation 'com.github.fdoyle.ChangeNotifierViewModel:changenotifier:1.0.0'

You make a viewmodel with regular old fields, extend ChangeNotifierProvider, then whenever you change something call notifyListeners()

    class MainViewModel : ChangeNotifierViewModel() {
        var counter = 0

        fun increment() {
            counter++
            notifyListeners()
        }
    }


Just grab your ViewModel...

    val vm: MainViewModel = withChangeNotifier()

and access your fields.

    Text(
        text = "${vm.counter}",
        modifier = Modifier.padding(10.dp))
    Button(onClick = { vm.increment() }) {
        Text("Increment!")
    }

That's it. 

State without fuss.

By default, recomposition will occur on all changes to the viewmodel. 

If you want to listen to updates to a specific field...

    val value = withChangeNotifier { vm: MainViewModel -> vm.value }

and you'll only get updates when vm.value changes.

If you don't want to listen to updates at all...

    val vm: MainViewModel = withChangeNotifier(listen = false)

and you'll never get updates. This is helpful for composables that change data but don't show data (like buttons).
