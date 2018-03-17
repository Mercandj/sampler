set(audio-wav_sources
        src/WavGenerator.cpp
        )

add_library(audio-wav STATIC ${audio-wav_sources})

target_include_directories(audio-wav
        PRIVATE ../include)
