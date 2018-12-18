//
// Created by kui on 2018/5/4.
//
#include "lameutil.h"
#include "lame.h"

#define BUFFER_SIZE 4096

lame_t lame = NULL;


JNIEXPORT void JNICALL Java_com_linglong_lame_Lame_init(JNIEnv *env, jobject obj, jint channel, jint sampleRate, jint brate){
    lame = lame_init();
    lame_set_num_channels(lame, channel);
    lame_set_in_samplerate(lame, sampleRate);
    lame_set_brate(lame, brate);
    lame_set_mode(lame, 1);
    lame_set_quality(lame, 2);
    lame_init_params(lame);
}

JNIEXPORT jbyteArray JNICALL Java_com_linglong_lame_Lame_encode(JNIEnv *env, jobject obj, jshortArray buffer, jint len)
{
    int nb_write = 0;
    char output[BUFFER_SIZE];

// 转换为本地数组
    jshort *input = (*env)->GetShortArrayElements(env, buffer, NULL);

// 压缩mp3
    nb_write = lame_encode_buffer(lame, input, input, len, output, BUFFER_SIZE);

// 局部引用，创建一个byte数组
    jbyteArray result = (*env)->NewByteArray(env, nb_write);

// 给byte数组设置值
    (*env)->SetByteArrayRegion(env, result, 0, nb_write, (jbyte *)output);

// 释放本地数组(避免内存泄露)
    (*env)->ReleaseShortArrayElements(env, buffer, input, 0);

    return result;
}


JNIEXPORT void JNICALL Java_com_linglong_lame_Lame_destroy(JNIEnv *env, jobject obj){
    if(lame!=NULL)
    lame_close(lame);
    lame=NULL;
}
/*
jint Java_com_clam314_lame_SimpleLame_encode(JNIEnv *env, jclass type, jshortArray buffer_l_,
                                             jshortArray buffer_r_, jint samples, jbyteArray mp3buf_) {
    jshort *buffer_l = env->GetShortArrayElements(buffer_l_, NULL);
    jshort *buffer_r = env->GetShortArrayElements(buffer_r_, NULL);
    jbyte *mp3buf = env->GetByteArrayElements(mp3buf_, NULL);

    const jsize mp3buf_size = env->GetArrayLength(mp3buf_);

    int result =lame_encode_buffer(glf, buffer_l, buffer_r, samples, (u_char*)mp3buf, mp3buf_size);

    env->ReleaseShortArrayElements(buffer_l_, buffer_l, 0);
    env->ReleaseShortArrayElements(buffer_r_, buffer_r, 0);
    env->ReleaseByteArrayElements(mp3buf_, mp3buf, 0);

    return result;

}*/