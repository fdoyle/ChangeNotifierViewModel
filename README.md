# ChangeNotifierViewModel
This project is a port of Flutter's ChangeNotifierProvider, implemented as a ViewModel base class

https://pub.dev/documentation/provider/latest/provider/ChangeNotifierProvider-class.html

https://jitpack.io/#fdoyle/ChangeNotifierViewModel

    implementation 'com.github.fdoyle.ChangeNotifierViewModel:changenotifier:main-SNAPSHOT'

You make a viewmodel with regular old fields, extend ChangeNotifierProvider, then whenever you change something call notifyListeners()

    class MainViewModel : ChangeNotifierViewModel() {
        var counter = 0

        fun increment() {
            counter++
            notifyListeners()
        }
    }


Just grab your ViewModel...

    val vm = withChangeNotifier<MainViewModel>()

and access your fields.

    Text(
        text = "${vm.counter}",
        modifier = Modifier.padding(10.dp))
    Button(onClick = { vm.increment() }) {
        Text("Increment!")
    }

That's it. 

State without fuss.
