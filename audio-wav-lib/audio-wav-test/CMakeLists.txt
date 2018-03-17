set(audio-wav-test_sources
        src/WavTest.cpp
        )

add_executable(audio-wav-test ${audio-wav-test_sources})

target_link_libraries(audio-wav-test audio-wav)

target_include_directories(audio-wav-test
        PRIVATE ../include
        )

target_compile_options(audio-wav-test
        PRIVATE -std=c++11
        PRIVATE -Wall
        )
