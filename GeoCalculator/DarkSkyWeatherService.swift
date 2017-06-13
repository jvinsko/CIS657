//
//  DarkSkyWeatherService.swift
//  GeoCalculator
//
//  Created by user128293 on 6/13/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import Foundation
let sharedDarkSkyInstance = DarkSkyWeatherService()

class DarkSkyWeatherService: WeatherService {
    
    let API_BASE = "https://api.darksky.net/forecast/"
    let DARK_SKY_WEATHER_API_KEY = "f4459b5422839400741ea97d7ada62df"
    var urlSession = URLSession.shared
    
    class func getInstance() -> DarkSkyWeatherService {
        return sharedDarkSkyInstance
    }
    
    func getWeatherForDate(date: Date, forLocation location: (Double, Double), completion: @escaping (Weather?) -> Void)
    {
        let urlStr = API_BASE + DARK_SKY_WEATHER_API_KEY + "/\(location.0),\(location.1)"
        let url = URL(string: urlStr)
        
        let task = self.urlSession.dataTask(with: url!) {
            (data, response, error) in
            if let error = error {
                print(error.localizedDescription)
            } else if let _ = response {
                let parsedObj : Dictionary<String,AnyObject>!
                do {
                    parsedObj = try JSONSerialization.jsonObject(with: data!, options: .allowFragments) as? Dictionary<String,AnyObject>
                    guard let currently = parsedObj["currently"],
                        let summary = currently["summary"] as? String,
                        let temperature = currently["temperature"] as? Double,
                        let iconName = currently["icon"] as? String
                        else {
                            completion(nil)
                            return
                    }
                    
                    let weather = Weather(iconName: iconName, temperature: temperature, summary: summary)
                    completion(weather)
                    
                } catch {
                    completion(nil)
                }
            }
        }
        
        task.resume()
    }
}
