import UIKit
import AVFoundation

var player: AVAudioPlayer?

class ViewController: UIViewController {

    @IBOutlet weak var buttonA: UIButton!
    @IBOutlet weak var buttonB: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()

        buttonA.addTarget(self, action: #selector(self.buttonActionA(_:)), for: .touchUpInside)
        buttonB.addTarget(self, action: #selector(self.buttonActionB(_:)), for: .touchUpInside)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    @objc func buttonActionA(_ sender: UIButton!) {
        playSound(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_01", withExtension: "wav")
    }

    @objc func buttonActionB(_ sender: UIButton!) {
        playSound(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_02", withExtension: "wav")
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
}
