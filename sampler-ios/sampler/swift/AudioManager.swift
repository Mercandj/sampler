//
//  AudioManager.swift
//  sampler
//
//  Created by Jonathan on 12/03/2018.
//  Copyright © 2018 Jonathan. All rights reserved.
//

import Foundation

protocol AudioManager {

    func load(samples: Array<Sample>)
    func playSound(sampleId: Int)

}
