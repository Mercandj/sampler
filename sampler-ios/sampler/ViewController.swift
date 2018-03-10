import UIKit
import AVFoundation

var player: AVAudioPlayer?

class ViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let button = UIButton(frame: CGRect(x: 100, y: 100, width: 100, height: 50))
        button.backgroundColor = .black
        button.setTitle("A", for: .normal)
        button.addTarget(self, action: #selector(self.buttonAction(_:)), for: .touchUpInside)
        self.view.addSubview(button)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @objc func buttonAction(_ sender: UIButton!) {
        print("Button tapped")
    }
    
    // https://stackoverflow.com/questions/32036146/how-to-play-a-sound-using-swift
    func playSound() {
        guard let url = Bundle.main.url(forResource: "soundName", withExtension: "mp3") else { return }
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
