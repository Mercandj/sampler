#include "AudioManager.h"
#include "logging_macros.h"

constexpr int32_t kDefaultChannelCount = 2; // Stereo

AudioManager::AudioManager() :
        mChannelCount(kDefaultChannelCount) {
    createPlaybackStream();
}

AudioManager::~AudioManager() {
    closeOutputStream();
}

void AudioManager::Play() {
    LOGD("Play");
}

void AudioManager::createPlaybackStream() {
    oboe::AudioStreamBuilder builder;
    setupPlaybackStreamParameters(&builder);
    oboe::Result result = builder.openStream(&mPlayStream);

    if (result == oboe::Result::OK && mPlayStream != nullptr) {
        mSampleRate = mPlayStream->getSampleRate();
        mFramesPerBurst = mPlayStream->getFramesPerBurst();
        int channelCount = mPlayStream->getChannelCount();
        if (channelCount != mChannelCount) {
            LOGW("Requested %d channels but received %d", mChannelCount, channelCount);
        }
        mPlayStream->setBufferSizeInFrames(mFramesPerBurst);
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
    // TODO
    return oboe::DataCallbackResult::Continue;
}