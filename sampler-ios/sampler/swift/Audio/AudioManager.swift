import Foundation

protocol AudioManager {

    func load(samples: Array<Sample>)
    func playSound(sampleId: Int)

}
