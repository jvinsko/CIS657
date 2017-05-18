//
//  SettingsViewController.swift
//  GeoCalculator
//
//  Created by user128293 on 5/17/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import UIKit

protocol SettingsViewControllerDelegate {
    func settingsChanged(distanceUnits: String, bearingUnits: String)
}
class SettingsViewController: UIViewController {
    
    @IBOutlet weak var distancePicker: UIPickerView!
    @IBOutlet weak var bearingPicker: UIPickerView!
    @IBOutlet weak var distLabel: UILabel!
    @IBOutlet weak var bearLabel: UILabel!
    
    var distData: [String] = [String]()
    var bearData: [String] = [String]()
    var distSel: String = "Kilometer"
    var bearSel: String = "Degrees"
    var delegate: SettingsViewControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.distData = ["Kilometers","Miles"]
        self.distancePicker.tag = 0
        self.distancePicker.delegate = self
        self.distancePicker.dataSource = self
        self.distancePicker.isHidden = true
        self.bearData = ["Degrees","Mils"]
        self.bearingPicker.tag = 1
        self.bearingPicker.delegate = self
        self.bearingPicker.dataSource = self
        self.bearingPicker.isHidden = true
        
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.dismissPickers))
        self.view.addGestureRecognizer(tap)
        
        let distTapGesture = UITapGestureRecognizer(target: self, action: #selector(SettingsViewController.distTap))
        distLabel.addGestureRecognizer(distTapGesture)
        
        let bearTapGesture = UITapGestureRecognizer(target: self, action: #selector(SettingsViewController.bearTap))
        bearLabel.addGestureRecognizer(bearTapGesture)
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    func dismissPickers() {
        bearingPicker.isHidden = true
        distancePicker.isHidden = true
        distLabel.text = distSel
        bearLabel.text = bearSel
    }
    
    @IBAction func saveButtonPressed(_ sender: Any) {
        if let d = self.delegate {
            d.settingsChanged(distanceUnits: distSel, bearingUnits: bearSel)
        }
        if let navController = self.navigationController {
            navController.popViewController(animated: true)
        }
    }
    
    @IBAction func cancelButtonPressed(_ sender: Any) {
        if let navController = self.navigationController {
            navController.popViewController(animated: true)
        }
    }
    
    func distTap() {
        self.distancePicker.isHidden = false
        self.bearingPicker.isHidden = true
        distLabel.text = distSel
        bearLabel.text = bearSel
    }

    func bearTap() {
        self.bearingPicker.isHidden = false
        self.distancePicker.isHidden = true
        distLabel.text = distSel
        bearLabel.text = bearSel
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

extension SettingsViewController: UIPickerViewDataSource, UIPickerViewDelegate {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView.tag == 0 {
            return self.distData[row]
        } else {
            return self.bearData[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView.tag == 0 {
            self.distSel = self.distData[row]
        } else {
            self.bearSel = self.bearData[row]
        }
    }
}
