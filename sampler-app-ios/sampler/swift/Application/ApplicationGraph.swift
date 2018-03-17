import Foundation

public class ApplicationGraph {

    static let instance = ApplicationGraph()

    private var audioManager: AudioManager?

    private init() {

    }

    func getAudioManager() -> AudioManager {
        if (audioManager == nil) {
            audioManager = AudioModule().provideAudioManager()
        }
        return audioManager!
    }

}
