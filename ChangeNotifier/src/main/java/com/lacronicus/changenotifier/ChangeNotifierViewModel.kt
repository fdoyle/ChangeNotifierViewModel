package com.lacronicus.changenotifier


import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

//based on Flutter ChangeNotifier
open class ChangeNotifierViewModel : ViewModel(), ChangeNotifier {
    val listeners: MutableSet<Listener> = mutableSetOf()

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun notifyListeners() {
        listeners.forEach {
            it.onDataChanged()
        }
    }

    override fun equals(other: Any?): Boolean {
        return false // compose will ignore updates if the new value is equal to the old. skip this check
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

interface ChangeNotifier {
    fun notifyListeners()
}

fun interface Listener {
    fun onDataChanged()
}


//@Composable
//inline fun <reified VM : ChangeNotifierViewModel> withVM(vm: VM = viewModel(), listen: Boolean = true): VM {
//    if(!listen){
//        return vm
//    }
//    val vmState = remember { mutableStateOf(vm) }
//    DisposableEffect(vmState) {
//        val listener = Listener { vmState.value = vmState.value }
//        vm.addListener(listener)
//
//        onDispose {
//            vm.removeListener(listener)
//        }
//    }
//    return vmState.value
//}

@Composable
inline fun <reified VM : ChangeNotifierViewModel> withChangeNotifier(vm: VM = viewModel(), listen: Boolean = true): VM {
    if(!listen){
        return vm
    }
    val vmState = remember { mutableStateOf(vm) }
    DisposableEffect(vmState) {
        val listener = Listener { vmState.value = vmState.value }
        vm.addListener(listener)

        onDispose {
            vm.removeListener(listener)
        }
    }
    return vmState.value
}

@Composable
inline fun <reified VM : ChangeNotifierViewModel, R> withChangeNotifier(
    vm: VM = viewModel(),
    crossinline block: @DisallowComposableCalls (VM) -> R,
): R {
    val state = remember { mutableStateOf(block(vm)) }

    DisposableEffect(state) {
        val listener = Listener { state.value = block(vm) }
        vm.addListener(listener)
        onDispose {
            vm.removeListener(listener)
        }
    }

    return state.value
}
