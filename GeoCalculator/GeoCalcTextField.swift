//
//  GeoCalcTextField.swift
//  GeoCalculator
//
//  Created by user128293 on 5/23/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import UIKit
import Foundation

class GeoCalcTextField: UITextField {

    override func awakeFromNib() {
        self.tintColor = FOREGROUND_COLOR
        self.textColor = FOREGROUND_COLOR
        self.backgroundColor = UIColor.clear
        self.layer.borderWidth = 1.0
        self.layer.cornerRadius = 5.0
        self.layer.borderColor = FOREGROUND_COLOR.cgColor
        
        guard let ph = self.placeholder else {
            return
        }
        self.attributedPlaceholder = NSAttributedString(string: ph, attributes: [NSForegroundColorAttributeName : FOREGROUND_COLOR])
    }

}
