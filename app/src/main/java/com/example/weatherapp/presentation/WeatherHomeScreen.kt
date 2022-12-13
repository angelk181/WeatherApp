package com.example.weatherapp.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WeatherHomeScreen(
    viewModel: WeatherViewModel = hiltViewModel()
){
    val state = viewModel.weatherState.value

    Row(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
    ){
        state.results?.let {
            Text(text =  "Greater London")
            Text(text = it.temperature)

        }
    }


}