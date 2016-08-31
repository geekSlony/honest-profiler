#ifndef THREAD_MAP_H
#define THREAD_MAP_H

#include <jvmti.h>
#include <jni.h>
#include "concurrent_map.h"


int gettid();

struct ThreadBucket {
    int tid;
    jthread thread;
};

template <typename PType>
struct PointerHasher {
	/* pointer hash bijection (collision-free) */
	static int64_t hash(void *p) {
		// remove LSB zeros in pointers
		return ((int64_t)p) / sizeof(PType);
	}
};

#define INITIAL_CONCURRENT_MAP_SIZE 256

template <typename MapProvider>
class ThreadMapBase {
private:
	MapProvider map;

public:

	ThreadMapBase(int capacity=INITIAL_CONCURRENT_MAP_SIZE) : map(capacity) {}

	void put(JNIEnv *jni_env, jthread thread) {
        put(jni_env, thread, gettid());
	}

	void put(JNIEnv *jni_env, jthread thread, int tid, bool globalRef=true) {
		ThreadBucket *info = new ThreadBucket;
        info->tid = tid;
        info->thread = globalRef ? thread : jni_env->NewGlobalRef(thread);
        map.put(jni_env, info);
	}

	ThreadBucket *get(JNIEnv *jni_env) {
		return reinterpret_cast<ThreadBucket*>(map.get(jni_env));
	}

	void remove(JNIEnv *jni_env) {
    	ThreadBucket *info = (ThreadBucket*)map.remove(jni_env);
    	if (info) {
    		jni_env->DeleteGlobalRef(info->thread);
    		delete info;
    	}
	}
};

typedef ThreadMapBase<map::ConcurrentMapProvider<PointerHasher<JNIEnv>, false> > ThreadMap;

#endif
