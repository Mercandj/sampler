#ifndef SAMPLER_WAV_GENERATOR_H
#define SAMPLER_WAV_GENERATOR_H

#include <math.h>
#include <cstdint>

class WavGenerator {
public:
    WavGenerator();

    ~WavGenerator() = default;

    void render(int16_t *buffer, int channel, int32_t channelStride, int32_t numFrames);

    void render(float *buffer, int channel, int32_t channelStride, int32_t numFrames);

    const void load(const char **filePaths, int nbFilePaths);

    const void play(int id);

private:
    int lastPlayedId = 0;
    long *currentPositionL;
    long *currentPositionR;
    int16_t **buffers;
    long *sizes;
    bool loaded = false;

    long extractWav(const char *filePath, int id);
};

#endif /* SAMPLER_WAV_GENERATOR_H */
