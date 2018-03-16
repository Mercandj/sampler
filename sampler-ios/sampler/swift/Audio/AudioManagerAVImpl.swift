import Foundation
import AVFoundation

class AudioManagerAVImpl: AudioManager {

    var players = Array<AVAudioPlayer>()
    var samples: Array<Sample>?

    func load(samples: Array<Sample>) {
        print("[AudioManager] initialize")
        self.samples = samples
        var audioManagerBridge = AudioManagerBridge()
        audioManagerBridge.initalizeAudioManagerBridge()

        for sample in samples {
            guard let url = Bundle.main.url(
                forResource: sample.getForResource(),
                withExtension: sample.getWithExtension()) else { return }
            do {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
                try AVAudioSession.sharedInstance().setActive(true)
                let player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)
                players.append(player)
            } catch let error {
                print(error.localizedDescription)
            }
        }
    }

    func playSound(sampleId: Int) {
        players[sampleId].currentTime = 0
        players[sampleId].play()
    }
};
