set(audio-wav-benchmark_sources
        src/WavBenchmark.cpp
        )

add_executable(audio-wav-benchmark ${audio-wav-benchmark_sources})

target_link_libraries(audio-wav-benchmark audio-wav)

target_include_directories(audio-wav-benchmark
        PRIVATE ../include
        )

target_compile_options(audio-wav-benchmark
        PRIVATE -std=c++11
        PRIVATE -Wall
        )
