#ifndef SAMPLER_ANDROID_AUDIOMANAGER_H
#define SAMPLER_ANDROID_AUDIOMANAGER_H

#include <oboe/Oboe.h>
#include <oboe/Definitions.h>
#include <oboe/AudioStream.h>

class AudioManager : oboe::AudioStreamCallback {

public:

    AudioManager();

    ~AudioManager();

    void Play();

private:
    oboe::AudioApi mAudioApi = oboe::AudioApi::Unspecified;
    int32_t mPlaybackDeviceId = oboe::kUnspecified;
    oboe::AudioStream *mPlayStream;

    int32_t mSampleRate;
    const int32_t mChannelCount;
    int32_t mFramesPerBurst;

    void createPlaybackStream();

    void setupPlaybackStreamParameters(oboe::AudioStreamBuilder *builder);

    void closeOutputStream();

    // oboe::StreamCallback methods
    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames);
};


#endif //SAMPLER_ANDROID_AUDIOMANAGER_H
