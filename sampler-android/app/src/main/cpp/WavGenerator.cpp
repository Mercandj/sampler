#include "WavGenerator.h"
#include <string>
#include <malloc.h>
#include <cstring>
#include "AudioManager.h"
#include "logging_macros.h"

static const long MAX_FILE_SIZE = 10000000;
static short buf[MAX_FILE_SIZE];
static long size;

static float shortToFloat(int16_t i);

static void extractWAV(const char *filePath) {
    FILE *fp = fopen(filePath, "rb");
    if (fp == NULL) {
        LOGE("extractWAV file open failed!");
        return;
    }
    fseek(fp, 44, 0);
    size = fread(buf, sizeof(short), MAX_FILE_SIZE, fp);
    LOGD("extractWAV file open succeeded: %s %ld", filePath, size);
    fclose(fp);
}

WavGenerator::WavGenerator() {
}

void WavGenerator::render(int16_t *buffer, int channel, int32_t channelStride, int32_t numFrames) {
    // TODO
}

void WavGenerator::render(float *buffer, int channel, int32_t channelCount, int32_t numFrames) {
    if (currentPositionL < 0 || currentPositionR < 0) {
        memset(buffer, 0, sizeof(float) * numFrames);
        return;
    }
    if (channel == 0) {
        for (int i = 0, sampleIndex = 0; i < numFrames; i++) {
            buffer[sampleIndex] = shortToFloat(buf[currentPositionL]);
            sampleIndex += channelCount;
            currentPositionL += channelCount;
        }
        if (currentPositionL >= size) {
            currentPositionL = -1;
        }
    } else {
        for (int i = 0, sampleIndex = 0; i < numFrames; i++) {
            buffer[sampleIndex] = shortToFloat(buf[currentPositionR]);
            sampleIndex += channelCount;
            currentPositionR += channelCount;
        }
        if (currentPositionR >= size) {
            currentPositionR = -1;
        }
    }
}

const void WavGenerator::load(const char **filePaths, int nbFilePaths) {
    extractWAV(filePaths[0]);
}

const void WavGenerator::play(const char *filePath) {
    currentPositionL = 0;
    currentPositionR = 1;
}

static float shortToFloat(int16_t i) {
    float f;
    f = ((float) i) / (float) 32768;
    if (f > 1) f = 1;
    if (f < -1) f = -1;
    return f;
}
