import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var buttonA: UIButton!
    @IBOutlet weak var buttonB: UIButton!
    @IBOutlet weak var buttonC: UIButton!

    var audioManager = ApplicationGraph.instance.getAudioManager()

    override func viewDidLoad() {
        super.viewDidLoad()

        var samples = Array<Sample>()
        samples.append(Sample(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_01", withExtension: "wav"))
        samples.append(Sample(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_02", withExtension: "wav"))
        samples.append(Sample(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_03", withExtension: "wav"))
        samples.append(Sample(forResource: "assets/wav/shape-of-you/dpm_shape_of_you_a_melody_04", withExtension: "wav"))
        audioManager.load(samples: samples)

        buttonA.addTarget(self, action: #selector(self.buttonActionA(_:)), for: .touchUpInside)
        buttonB.addTarget(self, action: #selector(self.buttonActionB(_:)), for: .touchUpInside)
        buttonC.addTarget(self, action: #selector(self.buttonActionC(_:)), for: .touchUpInside)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    @objc func buttonActionA(_ sender: UIButton!) {
        audioManager.playSound(sampleId: 1)
    }

    @objc func buttonActionB(_ sender: UIButton!) {
        audioManager.playSound(sampleId: 2)
    }

    @objc func buttonActionC(_ sender: UIButton!) {
        audioManager.playSound(sampleId: 3)
    }
}
