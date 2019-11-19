package com.mercandalli.android.apps.sampler.sample

class SampleModule {

    fun provideSampleManager(): SampleManager {
        return SampleManagerImpl()
    }
}
