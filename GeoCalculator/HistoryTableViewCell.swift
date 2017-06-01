//
//  HistoryTableViewCell.swift
//  GeoCalculator
//
//  Created by user128293 on 5/31/17.
//  Copyright © 2017 Grand Valley State University. All rights reserved.
//

import UIKit

class HistoryTableViewCell: UITableViewCell {

    @IBOutlet weak var origPoint: UILabel!
    @IBOutlet weak var destPoint: UILabel!
    @IBOutlet weak var timestamp: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
