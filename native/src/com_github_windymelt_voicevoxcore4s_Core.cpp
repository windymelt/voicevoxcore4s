#include "include/com_github_windymelt_voicevoxcore4s_Core.h"
#include "include/core.h"

JNIEXPORT jboolean JNICALL Java_com_github_windymelt_voicevoxcore4s_Core_initialize
(JNIEnv *, jobject, jboolean use_gpu, jint cpu_num_threads, jboolean load_all_models)
{
    return 1; //initialize(use_gpu, cpu_num_threads, load_all_models);
}