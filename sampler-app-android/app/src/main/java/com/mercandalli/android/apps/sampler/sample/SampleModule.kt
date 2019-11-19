package com.mercandalli.android.apps.sampler.sample

class SampleModule {

    fun createSampleManager(): SampleManager {
        return SampleManagerImpl()
    }
}
