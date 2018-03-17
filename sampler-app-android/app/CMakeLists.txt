cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -Werror")

set (OBOE_RELATIVE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../lib-oboe)
get_filename_component(OBOE_DIR
                       ${OBOE_RELATIVE_PATH}
                       ABSOLUTE)

if ((NOT EXISTS ${OBOE_DIR}) OR
    (NOT EXISTS ${OBOE_DIR}/CMakeLists.txt))
    execute_process(COMMAND git clone
                            https://github.com/google/oboe.git
                            ${OBOE_DIR}
                    WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/..)
endif()

add_subdirectory(${OBOE_DIR} ./oboe)
include_directories(${OBOE_DIR}/include)

set (DEBUG_UTILS_PATH "../../debug-utils")
set (DEBUG_UTILS_SOURCES ${DEBUG_UTILS_PATH}/trace.cpp)
include_directories(${DEBUG_UTILS_PATH})

add_library( native-lib
             SHARED
             src/main/cpp/AudioManager.cpp
             src/main/cpp/AudioManager.h
             src/main/cpp/logging_macros.h
             src/main/cpp/NativeAudioManagerBridge.cpp
             src/main/cpp/WavGenerator.cpp
             src/main/cpp/WavGenerator.h)

find_library( log-lib
              log )

target_link_libraries( native-lib
                       android
                       ${log-lib}
                       oboe)