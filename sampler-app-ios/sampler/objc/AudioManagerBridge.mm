#import "AudioManagerBridge.h"
#import "AudioManagerNative.h"

@implementation AudioManagerBridge

- (void) initalizeAudioManagerBridge {
    AudioManagerNative audioManagerNative;
    int nbFromCpp = audioManagerNative.addNums(12, 5);
    NSLog(@"[AudioManagerBridge] initalize: %d", nbFromCpp);
}

@end
