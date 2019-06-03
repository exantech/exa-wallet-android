LOCAL_PATH := $(call my-dir)
# prepare libexawallet
include $(CLEAR_VARS)
LOCAL_MODULE    := libexawallet
LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/monero/libwallet_merged.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/monero/liblmdb.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/monero/libepee.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/monero/libeasylogging.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/monero/libunbound.a

LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_chrono.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_date_time.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_filesystem.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_program_options.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_regex.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_serialization.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_system.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_thread.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/boost/libboost_wserialization.a

LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/libsodium.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/libcrypto.a
LOCAL_SRC_FILES += $(TARGET_ARCH_ABI)/libssl.a

include $(PREBUILT_STATIC_LIBRARY)

# build JNI
include $(CLEAR_VARS)
LOCAL_STATIC_LIBRARIES := exawallet
LOCAL_MODULE    := exawalletjni
LOCAL_SRC_FILES := exawallet.cpp
include $(BUILD_SHARED_LIBRARY)
