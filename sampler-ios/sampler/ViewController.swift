import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var buttonA: UIButton!
    @IBOutlet weak var buttonB: UIButton!
    @IBOutlet weak var buttonC: UIButton!
    
    var audioManager = AudioManager()

    override func viewDidLoad() {
        super.viewDidLoad()
        audioManager.initialize()

        buttonA.addTarget(self, action: #selector(self.buttonActionA(_:)), for: .touchUpInside)
        buttonB.addTarget(self, action: #selector(self.buttonActionB(_:)), for: .touchUpInside)
        buttonC.addTarget(self, action: #selector(self.buttonActionC(_:)), for: .touchUpInside)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    @objc func buttonActionA(_ sender: UIButton!) {
        audioManager.playSound(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_01", withExtension: "wav")
    }

    @objc func buttonActionB(_ sender: UIButton!) {
        audioManager.playSound(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_02", withExtension: "wav")
    }

    @objc func buttonActionC(_ sender: UIButton!) {
        audioManager.playSound(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_03", withExtension: "wav")
    }
}
