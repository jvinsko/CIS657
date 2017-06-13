//
//  Weather.swift
//  GeoCalculator
//
//  Created by user128293 on 6/13/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import Foundation
struct Weather {
    var iconName : String
    var temperature : Double
    var summary : String
    
    init(iconName: String, temperature: Double, summary: String) {
        self.iconName = iconName
        self.temperature = temperature
        self.summary = summary
    }
}

protocol WeatherService {
    func getWeatherForDate(date: Date, forLocation location: (Double, Double), completion: @escaping (Weather?) -> Void)
}
