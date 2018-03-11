#include <jni.h>
#include <malloc.h>
#include "AudioManager.h"

static AudioManager *audioManager = new AudioManager();

extern "C" {

JNIEXPORT void
JNICALL
Java_com_mercandalli_android_apps_sampler_audio_NativeAudioManager_nativeLoad(
        JNIEnv *env,
        jobject /* this */,
        jobjectArray internal_storage_paths_java) {
    int nbFilePaths = env->GetArrayLength(internal_storage_paths_java);
    const char **filePathsInput = (const char **) calloc((size_t) nbFilePaths, sizeof(char *));
    for (int i = 0; i < nbFilePaths; i++) {
        jstring input = (jstring) (env->GetObjectArrayElement(internal_storage_paths_java, i));
        filePathsInput[i] = env->GetStringUTFChars(input, 0);
    }
    audioManager->getWavGenerator()->load(filePathsInput, nbFilePaths);
}

JNIEXPORT void
JNICALL
Java_com_mercandalli_android_apps_sampler_audio_NativeAudioManager_nativePlay(
        JNIEnv *env,
        jobject /* this */,
        jstring filePathJava) {
    const char *filePath = env->GetStringUTFChars(filePathJava, 0);
    audioManager->getWavGenerator()->play(filePath);
}

}