#pragma once

#include <jni.h>

jfieldID getHandleField(JNIEnv *env, jobject obj, const char* fieldName = "handle") {
    jclass c = env->GetObjectClass(obj);
    return env->GetFieldID(c, fieldName, "J"); // of type long
}

template <typename T>
T *getHandle(JNIEnv *env, jobject obj, const char* fieldName = "handle") {
    jlong handle = env->GetLongField(obj, getHandleField(env, obj, fieldName));
    return reinterpret_cast<T *>(handle);
}

void setHandleFromLong(JNIEnv *env, jobject obj, jlong handle) {
    env->SetLongField(obj, getHandleField(env, obj), handle);
}

template <typename T>
void setHandle(JNIEnv *env, jobject obj, T *t) {
    jlong handle = reinterpret_cast<jlong>(t);
    setHandleFromLong(env, obj, handle);
}
