package com.nks.interactive.remoteplayer.controller.koin

import androidx.lifecycle.viewmodel.compose.viewModel
import com.nks.interactive.remoteplayer.controller.ui.screens.SettingsScreen
import com.nks.interactive.remoteplayer.controller.viewmodels.ControllerScreenVM
import com.nks.interactive.remoteplayer.controller.viewmodels.SettingsScreenVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SettingsScreenVM(get(),get()) }
    viewModel { ControllerScreenVM(get()) }
}