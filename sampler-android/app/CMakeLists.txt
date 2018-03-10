cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -Werror")

set (OBOE_RELATIVE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../lib-oboe)
get_filename_component(OBOE_DIR
                       ${OBOE_RELATIVE_PATH}
                       ABSOLUTE)

# If the Oboe library directory doesn't exist, clone it from github
if ((NOT EXISTS ${OBOE_DIR}) OR
    (NOT EXISTS ${OBOE_DIR}/CMakeLists.txt))
    execute_process(COMMAND git clone
                            https://github.com/google/oboe.git
                            ${OBOE_DIR}
                    WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/..)
endif()

# Add the Oboe library as a subproject. Since Oboe is an out-of-tree source library we must also
# specify a binary directory
add_subdirectory(${OBOE_DIR} ./oboe)

# Include the Oboe headers
include_directories(${OBOE_DIR}/include)

### END OBOE INCLUDE SECTION ###

# Debug utilities
set (DEBUG_UTILS_PATH "../../debug-utils")
set (DEBUG_UTILS_SOURCES ${DEBUG_UTILS_PATH}/trace.cpp)
include_directories(${DEBUG_UTILS_PATH})

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( native-lib
             SHARED
             src/main/cpp/AudioManager.cpp
             src/main/cpp/AudioManager.h
             src/main/cpp/NativeAudioManagerBridge.cpp)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}

                        oboe)