cmake_minimum_required(VERSION 3.4.1)
message(STATUS ABI_INFO  = ${ANDROID_ABI})

add_library(exawallet
             SHARED
             ${CMAKE_SOURCE_DIR}/src/main/cpp/exawallet.cpp)

set(EXTERNAL_LIBS_DIR ${CMAKE_SOURCE_DIR}/../external-libs)

############
# Sodium
############

add_library(sodium STATIC IMPORTED)
set_target_properties(sodium PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/libsodium.a)

############
# OpenSSL
############

add_library(crypto STATIC IMPORTED)
set_target_properties(crypto PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/libcrypto.a)

add_library(ssl STATIC IMPORTED)
set_target_properties(ssl PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/libssl.a)

############
# Boost
############

add_library(boost_chrono STATIC IMPORTED)
set_target_properties(boost_chrono PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_chrono.a)

add_library(boost_date_time STATIC IMPORTED)
set_target_properties(boost_date_time PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_date_time.a)

add_library(boost_filesystem STATIC IMPORTED)
set_target_properties(boost_filesystem PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_filesystem.a)

add_library(boost_program_options STATIC IMPORTED)
set_target_properties(boost_program_options PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_program_options.a)

add_library(boost_regex STATIC IMPORTED)
set_target_properties(boost_regex PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_regex.a)

add_library(boost_serialization STATIC IMPORTED)
set_target_properties(boost_serialization PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_serialization.a)

add_library(boost_system STATIC IMPORTED)
set_target_properties(boost_system PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_system.a)

add_library(boost_thread STATIC IMPORTED)
set_target_properties(boost_thread PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_thread.a)

add_library(boost_wserialization STATIC IMPORTED)
set_target_properties(boost_wserialization PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/boost/libboost_wserialization.a)

#############
# Monero
#############

add_library(wallet_merged STATIC IMPORTED)
set_target_properties(wallet_merged PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/monero/libwallet_merged.a)

add_library(epee STATIC IMPORTED)
set_target_properties(epee PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/monero/libepee.a)

add_library(easylogging STATIC IMPORTED)
set_target_properties(easylogging PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/monero/libeasylogging.a)

add_library(unbound STATIC IMPORTED)
set_target_properties(unbound PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/monero/libunbound.a)

add_library(lmdb STATIC IMPORTED)
set_target_properties(lmdb PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/monero/liblmdb.a)


#############
# System
#############

find_library( log-lib log )

include_directories(${EXTERNAL_LIBS_DIR}/monero/include)

#link_directories(${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI}/monero/ ${EXTERNAL_LIBS_DIR}/monero/lib/${ANDROID_ABI})

message(STATUS EXTERNAL_LIBS_DIR: ${EXTERNAL_LIBS_DIR})

target_link_libraries(exawallet

                        wallet_merged
                        epee
                        easylogging
                        lmdb
                        unbound

                        boost_chrono
                        boost_date_time
                        boost_filesystem
                        boost_program_options
                        boost_regex
                        boost_serialization
                        boost_system
                        boost_thread
                        boost_wserialization

                        ssl
                        crypto
                        sodium
                        Z

                        ${log-lib}
)