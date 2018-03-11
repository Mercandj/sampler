import Foundation
import AVFoundation

var player: AVAudioPlayer?

class AudioManager {

    func initialize() {
        print("[AudioManager] initialize")
        var audioManagerBridge = AudioManagerBridge()
        audioManagerBridge.initalizeAudioManagerBridge()
    }

    func playSound(forResource: String?, withExtension: String?) {
        guard let url = Bundle.main.url(forResource: forResource, withExtension: withExtension) else { return }
        do {
            try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
            try AVAudioSession.sharedInstance().setActive(true)
            player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)
            guard let player = player else { return }
            player.play()
        } catch let error {
            print(error.localizedDescription)
        }
    }
};
