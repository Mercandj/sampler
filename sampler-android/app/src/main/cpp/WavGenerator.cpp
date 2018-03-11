#include "WavGenerator.h"
#include <string>
#include <malloc.h>
#include <cstring>
#include "AudioManager.h"

static const long MAX_FILE_SIZE = 30000000;

static float shortToFloat(int16_t i);

WavGenerator::WavGenerator() {
}

void WavGenerator::render(int16_t *buffer, int channel, int32_t channelStride, int32_t numFrames) {
    // TODO
}

void WavGenerator::render(float *buffer, int channel, int32_t channelCount, int32_t numFrames) {
    if (!loaded || currentPositionL[lastPlayedId] < 0 || currentPositionR[lastPlayedId] < 0) {
        memset(buffer, 0, sizeof(float) * numFrames);
        return;
    }
    if (channel == 0) {
        for (int i = 0, sampleIndex = 0; i < numFrames; i++) {
            buffer[sampleIndex] = shortToFloat(
                    buffers[lastPlayedId][currentPositionL[lastPlayedId]]);
            sampleIndex += channelCount;
            currentPositionL[lastPlayedId] += channelCount;
        }
        if (currentPositionL[lastPlayedId] >= sizes[lastPlayedId]) {
            currentPositionL[lastPlayedId] = -1;
        }
    } else {
        for (int i = 0, sampleIndex = 0; i < numFrames; i++) {
            buffer[sampleIndex] = shortToFloat(
                    buffers[lastPlayedId][currentPositionR[lastPlayedId]]);
            sampleIndex += channelCount;
            currentPositionR[lastPlayedId] += channelCount;
        }
        if (currentPositionR[lastPlayedId] >= sizes[lastPlayedId]) {
            currentPositionR[lastPlayedId] = -1;
        }
    }
}

const void WavGenerator::load(const char **filePaths, int nbFilePaths) {
    buffers = (int16_t **) calloc((size_t) nbFilePaths, sizeof(int16_t *));
    sizes = (long *) calloc((size_t) nbFilePaths, sizeof(long));
    currentPositionL = (long *) calloc((size_t) nbFilePaths, sizeof(long));
    currentPositionR = (long *) calloc((size_t) nbFilePaths, sizeof(long));
    for (int i = 0; i < nbFilePaths; i++) {
        buffers[i] = (int16_t *) calloc((size_t) MAX_FILE_SIZE, sizeof(int16_t));
        sizes[i] = extractWav(filePaths[i], i);
        currentPositionL[i] = -1;
        currentPositionR[i] = -1;
    }
    loaded = true;
}

const void WavGenerator::play(int id) {
    lastPlayedId = id;
    currentPositionL[id] = 0;
    currentPositionR[id] = 1;
}

long WavGenerator::extractWav(const char *filePath, int id) {
    FILE *fp = fopen(filePath, "rb");
    if (fp == NULL) {
        return -1;
    }
    fseek(fp, 44, 0);
    long size = fread(buffers[id], sizeof(short), MAX_FILE_SIZE, fp);
    fclose(fp);
    return size;
}

static float shortToFloat(int16_t i) {
    float f;
    f = ((float) i) / (float) 32768;
    if (f > 1) f = 1;
    if (f < -1) f = -1;
    return f;
}
