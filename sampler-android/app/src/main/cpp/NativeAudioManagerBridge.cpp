//
// Created by Jonathan on 10/03/2018.
//

#include <jni.h>
#include <string>
#include "AudioManager.h"

static AudioManager *audioManager = new AudioManager();

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_mercandalli_android_apps_sampler_audio_NativeAudioManager_nativePlay(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Play from C++";
    audioManager->setToneOn(true);
    return env->NewStringUTF(hello.c_str());
}
