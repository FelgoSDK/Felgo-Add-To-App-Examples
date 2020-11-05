package com.felgo.demos.NativeIntegrationExamples.ui.chartview

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChartViewModel : ViewModel() {

    private val maxEntries = 10
    private val maxBars = 5
    
    class ChartData {
        val barData: MutableList<MutableList<Double>> = ArrayList()
        val barNames: MutableList<String> = ArrayList()
        val valueNames: MutableList<String> = ArrayList()
    }

    val chartData = MutableLiveData<ChartData>()

    private val m_mainThreadHandler = Handler()

    init {
        val data = ChartData()
        val numBars = 3
        val numEntries = 3
        for (b in 0 until numBars) {
            val barEntries: MutableList<Double> = ArrayList()
            for (i in 0 until numEntries) {
                barEntries.add(Math.random() - 0.5)
                if (b == 0) {
                    data.valueNames.add("V" + (i + 1))
                }
            }
            data.barData.add(barEntries)
            data.barNames.add("Bar #" + (b + 1))
        }
        chartData.value = data
    }

    fun removeDataEntry() {
        val data = chartData.value!!

        if(data.valueNames.size <= 1) {
            return
        }

        for (series in data.barData) {
            series.removeAt(series.size - 1)
        }
        data.valueNames.removeAt(data.valueNames.size - 1)
        chartData.value = data
    }

    fun addDataEntry() {
        val data = chartData.value!!

        if(data.valueNames.size >= maxEntries) {
            return
        }

        for (series in data.barData) {
            series.add(Math.random() - 0.5)
        }
        data.valueNames.add("V" + (data.valueNames.size + 1))
        chartData.value = data
    }

    fun removeBar() {
        val data = chartData.value!!

        if(data.barNames.size <= 1) {
            return
        }

        data.barData.removeAt(data.barData.size - 1)
        data.barNames.removeAt(data.barNames.size - 1)
        chartData.value = data
    }

    fun addBar() {
        val data = chartData.value!!

        if(data.barNames.size >= maxBars) {
            return
        }

        val values: MutableList<Double> = ArrayList()
        for (i in data.valueNames.indices) {
            values.add(Math.random() - 0.5)
        }
        data.barData.add(values)
        data.barNames.add("Bar #" + (data.barNames.size + 1))
        chartData.value = data
    }

    fun updateData() {
        val data = chartData.value

        // add or subtract random value from all bars
        for (series in data!!.barData) {
            for (i in series.indices) {
                var value = series[i]
                val randVal = Math.random() - 0.5
                value += randVal * 0.01
                series[i] = value
            }
        }
        postUpdate()
    }

    private fun postUpdate() {
        m_mainThreadHandler.post { chartData.value = chartData.value }
    }
}