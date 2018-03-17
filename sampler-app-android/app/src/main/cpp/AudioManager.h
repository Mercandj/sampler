#ifndef SAMPLER_AUDIO_MANAGER_H
#define SAMPLER_AUDIO_MANAGER_H

#include <thread>
#include <array>
#include <oboe/Oboe.h>
#include <mutex>
#include "WavGenerator.h"

constexpr int32_t kBufferSizeAutomatic = 0;

class AudioManager : oboe::AudioStreamCallback {

public:
    AudioManager();

    ~AudioManager();

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames);

    void onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error);

    inline WavGenerator *getWavGenerator() {
        return wavGenerator;
    }

private:
    oboe::AudioApi mAudioApi = oboe::AudioApi::Unspecified;
    int32_t mPlaybackDeviceId = oboe::kUnspecified;
    int32_t mChannelCount;
    int32_t mFramesPerBurst;
    int32_t mBufferSizeSelection = kBufferSizeAutomatic;
    oboe::AudioStream *mPlayStream;
    std::unique_ptr<oboe::LatencyTuner> mLatencyTuner;
    std::mutex mRestartingLock;

    WavGenerator *wavGenerator = new WavGenerator();

    void createPlaybackStream();

    void closeOutputStream();

    void restartStream();

    void setupPlaybackStreamParameters(oboe::AudioStreamBuilder *builder);
};

#endif // SAMPLER_AUDIO_MANAGER_H
