#include <inttypes.h>
#include <cstring>

#include "AudioManager.h"
#include "logging_macros.h"

constexpr int32_t kDefaultChannelCount = 2;

AudioManager::AudioManager() {
    mChannelCount = kDefaultChannelCount;
    createPlaybackStream();
}

AudioManager::~AudioManager() {
    closeOutputStream();
}

void AudioManager::createPlaybackStream() {
    oboe::AudioStreamBuilder builder;
    setupPlaybackStreamParameters(&builder);
    oboe::Result result = builder.openStream(&mPlayStream);
    if (result == oboe::Result::OK && mPlayStream != nullptr) {
        mFramesPerBurst = mPlayStream->getFramesPerBurst();
        int channelCount = mPlayStream->getChannelCount();
        if (channelCount != mChannelCount) {
            LOGW("Requested %d channels but received %d", mChannelCount, channelCount);
        }
        mPlayStream->setBufferSizeInFrames(mFramesPerBurst);
        mLatencyTuner = std::unique_ptr<oboe::LatencyTuner>(new oboe::LatencyTuner(*mPlayStream));
        result = mPlayStream->requestStart();
        if (result != oboe::Result::OK) {
            LOGE("Error starting stream. %s", oboe::convertToText(result));
        }
    } else {
        LOGE("Failed to create stream. Error: %s", oboe::convertToText(result));
    }
}

void AudioManager::setupPlaybackStreamParameters(oboe::AudioStreamBuilder *builder) {
    builder->setAudioApi(mAudioApi);
    builder->setDeviceId(mPlaybackDeviceId);
    builder->setChannelCount(mChannelCount);
    builder->setSharingMode(oboe::SharingMode::Exclusive);
    builder->setPerformanceMode(oboe::PerformanceMode::LowLatency);
    builder->setCallback(this);
}

void AudioManager::closeOutputStream() {
    if (mPlayStream != nullptr) {
        oboe::Result result = mPlayStream->requestStop();
        if (result != oboe::Result::OK) {
            LOGE("Error stopping output stream. %s", oboe::convertToText(result));
        }

        result = mPlayStream->close();
        if (result != oboe::Result::OK) {
            LOGE("Error closing output stream. %s", oboe::convertToText(result));
        }
    }
}

oboe::DataCallbackResult
AudioManager::onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames) {
    int32_t bufferSize = audioStream->getBufferSizeInFrames();
    if (mBufferSizeSelection == kBufferSizeAutomatic) {
        mLatencyTuner->tune();
    } else if (bufferSize != (mBufferSizeSelection * mFramesPerBurst)) {
        audioStream->setBufferSizeInFrames(mBufferSizeSelection * mFramesPerBurst);
    }
    int32_t channelCount = audioStream->getChannelCount();
    if (audioStream->getFormat() == oboe::AudioFormat::Float) {
        for (int i = 0; i < channelCount; ++i) {
            wavGenerator->render(static_cast<float *>(audioData) + i, i, channelCount,
                                 numFrames);
        }
    } else {
        for (int i = 0; i < channelCount; ++i) {
            wavGenerator->render(static_cast<int16_t *>(audioData) + i, i, channelCount,
                                 numFrames);
        }
    }
    return oboe::DataCallbackResult::Continue;
}

void AudioManager::onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) {
    if (error == oboe::Result::ErrorDisconnected) restartStream();
}

void AudioManager::restartStream() {
    LOGI("Restarting stream");
    if (mRestartingLock.try_lock()) {
        closeOutputStream();
        createPlaybackStream();
        mRestartingLock.unlock();
    } else {
        LOGW("Restart stream operation already in progress - ignoring this request");
    }
}
