//
//  GeoCalcButton.swift
//  GeoCalculator
//
//  Created by user128293 on 5/23/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import UIKit

class GeoCalcButton: UIButton {

    override func awakeFromNib() {
        self.layer.cornerRadius = 5.0
        self.layer.borderWidth = 1.0
        self.layer.borderColor = FOREGROUND_COLOR.cgColor
        self.backgroundColor = FOREGROUND_COLOR
        self.tintColor = BACKGROUND_COLOR
    }

}
