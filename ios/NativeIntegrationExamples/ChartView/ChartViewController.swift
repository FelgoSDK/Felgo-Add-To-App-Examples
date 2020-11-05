//
//  ChartViewController.swift
//  NativeIntegrationExamples
//
//  Created by Christian Bartsch on 22.04.20.
//  Copyright © 2020 Christian Bartsch. All rights reserved.
//

import Foundation
import UIKit

class ChartViewController : UIViewController, UITextViewDelegate {
    
    @IBOutlet weak var felgoView: FelgoIOSView!
    
    private let maxEntries = 10
    private let maxBars = 5
    
    var barNames : [String]
    var valueNames : [String]
    var barData : [[Double]]
    
    var timer : Timer? = nil
    
    @IBOutlet weak var btnEnableUpdates: UIButton!
    
    required init?(coder: NSCoder) {
        barNames = ["Bar 1", "Bar 2", "Bar 3"]
        valueNames = ["V1", "V2", "V3"]
        
        let rv = ChartViewController.randomValue
        barData = [
            [rv(), rv(), rv()],
            [rv(), rv(), rv()],
            [rv(), rv(), rv()]
        ]
        
        super.init(coder: coder)
    }
    
    override func viewDidLoad() {
        self.felgoView.qmlInitBlock = {
            self.updateValues()
        }
        
        self.felgoView.qmlSource = Bundle.main.url(forResource: "ChartView", withExtension: "qml", subdirectory: "qml")!
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        if(self.timer != nil) {
            self.timer?.invalidate()
        }
    }
    
    func updateValues() {
        for i in self.barData.indices {
            var arr = self.barData[i]
            for j in arr.indices {
                arr[j] += ChartViewController.randomValue() * 0.01
            }
            self.barData[i] = arr
        }
    
        self.updateQmlData()
    }
    
    static func randomValue() -> Double {
        return Double.random(in: -0.5 ... 0.5)
    }

    func updateQmlData() {
        self.felgoView.setQmlProperty("barNames", value:self.barNames)
        self.felgoView.setQmlProperty("valueNames", value:self.valueNames)
        self.felgoView.setQmlProperty("barData", value:self.barData)
    }
    
    @IBAction func onAddEntryClicked(_ sender: Any) {
        if(self.valueNames.count >= maxEntries) {
            return
        }
        
        self.valueNames.append(String.init(format: "V%d", self.valueNames.count + 1))
        
        for i in self.barData.indices {
            self.barData[i].append(ChartViewController.randomValue())
        }
        
        self.updateQmlData()
    }
    
    @IBAction func onRemoveEntryClicked(_ sender: Any) {
        if(self.valueNames.count <= 1) {
            return
        }
        
        self.valueNames.removeLast()
        
        for i in self.barData.indices {
            self.barData[i].removeLast()
        }
        
        self.updateQmlData()
    }
    
    @IBAction func onAddBarClicked(_ sender: Any) {
        if(self.barNames.count >= maxBars) {
            return
        }
        
        self.barNames.append(String.init(format: "Bar %d", self.valueNames.count + 1))
        
        var arr:[Double] = []
        for _ in self.valueNames.indices {
            arr.append(ChartViewController.randomValue())
        }
        
        self.barData.append(arr)
        
        self.updateQmlData()
    }
    
    @IBAction func onRemoveBarClicked(_ sender: Any) {
        if(self.barNames.count <= 1) {
            return
        }
        
        self.barNames.removeLast()
        
        self.barData.removeLast()
        
        self.updateQmlData()
    }
    
    @IBAction func onUpdatesEnabledClicked(_ sender: Any) {
        if(self.timer != nil) {
            self.timer!.invalidate()
            self.timer = nil
            
            self.btnEnableUpdates.setTitle("Updates disabled", for: UIControl.State.normal)
        }
        else {
            self.timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { (timer:Timer) in
                self.updateValues()
            }
            
            self.btnEnableUpdates.setTitle("Updates enabled", for: UIControl.State.normal)
        }
    }
}
