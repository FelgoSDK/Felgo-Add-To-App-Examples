package com.felgo.demos.NativeIntegrationExamples.ui.chartview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.felgo.demos.NativeIntegrationExamples.R
import com.felgo.ui.FelgoAndroidFragment
import java.util.*

class ChartViewFragment : Fragment() {

    private val m_felgo: FelgoAndroidFragment by lazy {
        childFragmentManager.findFragmentById(R.id.qt_fragment_container) as FelgoAndroidFragment
    }

    private val m_chartViewModel by lazy {
        ViewModelProvider(this).get(ChartViewModel::class.java)
    }

    private var m_updatesEnabled = false
    private var m_timer: Timer = Timer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        m_chartViewModel.chartData.observe(viewLifecycleOwner, Observer { updateChartView() })

        val root = inflater.inflate(R.layout.fragment_chartview, container, false)

        m_felgo.setQmlInitializedListener { updateChartView() }

        root.findViewById<View>(R.id.btn_add_entry).setOnClickListener { m_chartViewModel.addDataEntry() }
        root.findViewById<View>(R.id.btn_remove_entry).setOnClickListener { m_chartViewModel.removeDataEntry() }
        root.findViewById<View>(R.id.btn_add_bar).setOnClickListener { m_chartViewModel.addBar() }
        root.findViewById<View>(R.id.btn_remove_bar).setOnClickListener { m_chartViewModel.removeBar() }

        val buttonEnableUpdates = root.findViewById<Button>(R.id.btn_enable_updates)
        buttonEnableUpdates.setOnClickListener {
            m_updatesEnabled = !m_updatesEnabled
            buttonEnableUpdates.setText(if (m_updatesEnabled) R.string.updates_enabled else R.string.updates_disabled)
            if (m_updatesEnabled) {
                m_timer = Timer()
                m_timer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        m_chartViewModel.updateData()
                    }
                }, 100, 100)
            } else {
                m_timer.cancel()
            }
        }

        return root
    }

    private fun updateChartView() {
        val data = m_chartViewModel.chartData.value!!
        m_felgo.setQmlProperty("barData", data.barData)
        m_felgo.setQmlProperty("barNames", data.barNames)
        m_felgo.setQmlProperty("valueNames", data.valueNames)
    }
}