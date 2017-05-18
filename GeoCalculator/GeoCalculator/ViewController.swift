//
//  ViewController.swift
//  GeoCalculator
//
//  Created by user128293 on 5/17/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, SettingsViewControllerDelegate {

    @IBOutlet weak var latP1: UITextField!
    @IBOutlet weak var longP1: UITextField!
    @IBOutlet weak var latP2: UITextField!
    @IBOutlet weak var longP2: UITextField!
    @IBOutlet weak var distanceResult: UILabel!
    @IBOutlet weak var bearingResult: UILabel!
    
    var distSetting: String = "Kilometers"
    var bearSetting: String = "Degrees"
    
    func DegreesToRadians(degrees: Double) -> Double {
        return degrees * Double.pi / 180
    }
    
    func RadiansToDegrees(radians: Double) -> Double {
        return radians * 180 / Double.pi
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        self.view.addGestureRecognizer(tap)
    }

    func dismissKeyboard() {
        self.view.endEditing(true)
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func settingsChanged(distanceUnits: String, bearingUnits: String){
        distSetting = distanceUnits
        bearSetting = bearingUnits
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let dest = segue.destination as? SettingsViewController {
            dest.delegate = self
        }
    }

    @IBAction func calcBtnPressed(_ sender: UIButton) {
        if latP1.hasText && latP2.hasText && longP1.hasText && longP2.hasText {
            let latP1Val = NSString(string: latP1.text!).doubleValue
            let latP2Val = NSString(string: latP2.text!).doubleValue
            let longP1Val = NSString(string: longP1.text!).doubleValue
            let longP2Val = NSString(string: longP2.text!).doubleValue
            
            let p1 = CLLocation(latitude: latP1Val, longitude: longP1Val)
            let p2 = CLLocation(latitude: latP2Val, longitude: longP2Val)
        
            let distanceInKilometers = (p1.distance(from: p2))/1000
            if distSetting == "Kilometers" {
                let distanceString: String = String(format: "%.2f", distanceInKilometers)
                distanceResult.text = "\(distanceString) kilometers"
            } else {
                let distanceInMiles = distanceInKilometers * 0.621371
                let distanceString: String = String(format: "%.2f", distanceInMiles)
                distanceResult.text = "\(distanceString) miles"
            }
            
            //Bearing
            let dlon = DegreesToRadians(degrees: longP2Val) - DegreesToRadians(degrees: longP1Val)
            
            let y: Double = sin(dlon) * cos(latP2Val)
            let x: Double = cos(latP1Val) * sin(latP2Val) - sin(latP1Val) * cos(latP2Val) * cos(dlon)
            let radBearing: Double = atan2(y, x)
            if bearSetting == "Degrees" {
                let bearingString: String = String(format: "%.2f", RadiansToDegrees(radians: radBearing))
                bearingResult.text = "\(bearingString) degrees"
            } else {
                let milBearing = 17.777777777778 * RadiansToDegrees(radians: radBearing)
                let bearingString: String = String(format: "%.2f", milBearing)
                bearingResult.text = "\(bearingString) mils"
            }
        }
    }
    
    @IBAction func clearBtnPresed(_ sender: UIButton) {
        latP1.text = ""
        latP2.text = ""
        longP1.text = ""
        longP2.text = ""
        distanceResult.text = ""
        bearingResult.text = ""
    }
}


