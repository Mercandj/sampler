import Foundation

class Sample {

    let forResource: String
    let withExtension: String

    init(forResource: String, withExtension: String) {
        self.forResource = forResource
        self.withExtension = withExtension
    }

    func getForResource() -> String {
        return forResource
    }

    func getWithExtension() -> String {
        return withExtension
    }

}
