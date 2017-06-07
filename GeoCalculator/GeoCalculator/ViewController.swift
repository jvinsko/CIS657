//
//  ViewController.swift
//  GeoCalculator
//
//  Created by user128293 on 5/17/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import UIKit
import CoreLocation
import Firebase

class ViewController: UIViewController, SettingsViewControllerDelegate, HistoryTableViewControllerDelegate {

    @IBOutlet weak var latP1: UITextField!
    @IBOutlet weak var longP1: UITextField!
    @IBOutlet weak var latP2: UITextField!
    @IBOutlet weak var longP2: UITextField!
    @IBOutlet weak var distanceResult: UILabel!
    @IBOutlet weak var bearingResult: UILabel!
    @IBOutlet weak var calcBtn: GeoCalcButton!
    
    
    var distSetting: String = "Kilometers"
    var bearSetting: String = "Degrees"
    
    fileprivate var ref : DatabaseReference?
    
    var entries : [LocationLookup] = [LocationLookup(origLat: 90.0, origLng: 0.0, destLat: -90.0, destLng: 0.0, timestamp: Date.distantPast),
                                      LocationLookup(origLat: -90.0, origLng: 0.0, destLat: 90.0, destLng: 0.0, timestamp: Date.distantFuture)]
    
    func DegreesToRadians(degrees: Double) -> Double {
        return degrees * Double.pi / 180
    }
    
    func RadiansToDegrees(radians: Double) -> Double {
        return radians * 180 / Double.pi
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        self.view.backgroundColor = BACKGROUND_COLOR
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        self.view.addGestureRecognizer(tap)
        
        self.ref = Database.database().reference()
        self.registerForFireBaseUpdates()
    }

    fileprivate func registerForFireBaseUpdates()
    {
        self.ref!.child("history").observe(.value, with: { snapshot in
            if let postDict = snapshot.value as? [String : AnyObject] {
                var tmpItems = [LocationLookup]()
                for (_,val) in postDict.enumerated() {
                    let entry = val.1 as! Dictionary<String,AnyObject>
                    let timestamp = entry["timestamp"] as! String?
                    let origLat = entry["origLat"] as! Double?
                    let origLng = entry["origLng"] as! Double?
                    let destLat = entry["destLat"] as! Double?
                    let destLng = entry["destLng"] as! Double?
                    tmpItems.append(LocationLookup(origLat: origLat!,
                                                   origLng: origLng!, destLat: destLat!,
                                                   destLng: destLng!,
                                                   timestamp: (timestamp?.dateFromISO8601)!))
                }
                self.entries = tmpItems
            }
        })
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
        calcBtn.sendActions(for: .touchUpInside)
    }
    
    func selectEntry(entry: LocationLookup) {
        var printStr: String = String(format: "%.4f", entry.origLat)
        latP1.text = printStr
        printStr = String(format: "%.4f", entry.origLng)
        longP1.text = printStr
        printStr = String(format: "%.4f", entry.destLat)
        latP2.text = printStr
        printStr = String(format: "%.4f", entry.destLng)
        longP2.text = printStr
        calcBtn.sendActions(for: .touchUpInside)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "historySegue" {
            if let dest = segue.destination as? HistoryTableViewController {
                dest.entries = self.entries
                dest.historyDelegate = self
            }
        } else if segue.identifier == "searchSegue" {
            if let dest = segue.destination as? LocationSearchViewController {
                dest.delegate = self
            }
        } else {
            if let dest = segue.destination as? SettingsViewController {
                dest.delegate = self
            }
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
            
            // save history to firebase
            let entry = LocationLookup(origLat: latP1Val, origLng: longP1Val, destLat: latP2Val, destLng: longP2Val, timestamp: Date())
            let newChild = self.ref?.child("history").childByAutoId()
            newChild?.setValue(self.toDictionary(vals: entry))
        }
    }
    
    func toDictionary(vals: LocationLookup) -> NSDictionary {
        return [
            "timestamp": NSString(string: (vals.timestamp.iso8601)),
            "origLat" : NSNumber(value: vals.origLat),
            "origLng" : NSNumber(value: vals.origLng),
            "destLat" : NSNumber(value: vals.destLat),
            "destLng" : NSNumber(value: vals.destLng),
        ]
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

extension ViewController: LocationSearchDelegate {
    func set(calculationData: LocationLookup)
    {
        self.latP1.text = "\(calculationData.origLat)"
        self.longP1.text = "\(calculationData.origLng)"
        self.latP2.text = "\(calculationData.destLat)"
        self.longP2.text = "\(calculationData.destLng)"
        self.calcBtn.sendActions(for: .touchUpInside)
    }
}

extension Date {
    struct Formatter {
        static let iso8601: DateFormatter = {
            let formatter = DateFormatter()
            formatter.calendar = Calendar(identifier: .iso8601)
            formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSxxx"
            return formatter
        }()
        
        static let short: DateFormatter = {
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd"
            return formatter
        }()
    }
    
    var short: String {
        return Formatter.short.string(from: self)
    }
    
    var iso8601: String {
        return Formatter.iso8601.string(from: self)
    }
}

extension String {
    var dateFromISO8601: Date? {
        return Date.Formatter.iso8601.date(from: self)
    }
}
