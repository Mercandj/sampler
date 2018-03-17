#!/usr/bin/env bash

BUILD_DIR=build
ANDROID_SYSTEM_VERSION=19

echo
echo
echo
echo
echo ------------------------------------------------------------
echo "   ${CONFIG_COLOR_CYAN}Build all android${CONFIG_COLOR_RESET} (see ${BUILD_DIR} folder. API: ${ANDROID_SYSTEM_VERSION})"
echo "      Script File - ${CONFIG_COLOR_YELLOW}build_all_android.sh${CONFIG_COLOR_RESET}"
echo ------------------------------------------------------------
echo

if [ -z "$ANDROID_NDK" ]; then
  echo "Please set ANDROID_NDK to the Android NDK folder"
  exit 1
fi

echo "Using the ANDROID_NDK path $ANDROID_NDK"

CMAKE_ARGS="-H. \
  -DCMAKE_BUILD_TYPE=RelWithDebInfo \
  -DCMAKE_ANDROID_NDK_TOOLCHAIN_VERSION=clang \
  -DCMAKE_SYSTEM_NAME=Android \
  -DCMAKE_SYSTEM_VERSION=${ANDROID_SYSTEM_VERSION} \
  -DCMAKE_ANDROID_STL_TYPE=c++_static \
  -DCMAKE_ANDROID_NDK=$ANDROID_NDK \
  -DCMAKE_INSTALL_PREFIX=."

function build_for_android {
  ABI=$1
  ABI_BUILD_DIR=build/${ABI}
  STAGING_DIR=staging

  echo "Building for ${ABI}"

  mkdir -p ${ABI_BUILD_DIR} ${ABI_BUILD_DIR}/${STAGING_DIR}

  cmake -B${ABI_BUILD_DIR} \
        -DCMAKE_ANDROID_ARCH_ABI=${ABI} \
        -DCMAKE_ARCHIVE_OUTPUT_DIRECTORY=${STAGING_DIR}/lib/${ABI} \
        ${CMAKE_ARGS}

  pushd ${ABI_BUILD_DIR}
    make -j5
  popd
}

build_for_android armeabi
build_for_android armeabi-v7a
build_for_android arm64-v8a
build_for_android x86
build_for_android x86_64
build_for_android mips
