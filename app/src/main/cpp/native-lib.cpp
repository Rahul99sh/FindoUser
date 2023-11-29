#include <jni.h>
#include <string>

extern "C" __attribute__((unused)) JNIEXPORT jstring

JNICALL
Java_com_users_findo_security_Crypto_getIv(JNIEnv *env) {
    std::string encrypted_key = "RGdMdjJzOTQ3WGxKakNnaQ==";
    return env->NewStringUTF(encrypted_key.c_str());
}


extern "C" __attribute__((unused)) JNIEXPORT jstring

JNICALL
Java_com_users_findo_security_Crypto_getKey(JNIEnv *env) {
    std::string encrypted_key = "WlF2ektwTExvclVzNTJnbHlKWWlEY0JHcGNzSEhFcmk=";
    return env->NewStringUTF(encrypted_key.c_str());
}
