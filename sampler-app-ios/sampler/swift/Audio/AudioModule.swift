import Foundation

class AudioModule {

    func provideAudioManager() -> AudioManager {
        return AudioManagerAVImpl()
    }

}
