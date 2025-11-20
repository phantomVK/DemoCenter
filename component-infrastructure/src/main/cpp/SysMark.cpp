#include <jni.h>
#include <string>
#include <sys/stat.h>

const int BOOT_ID_LEN = 37;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_phantomvk_democenter_utility_SysMarkJni_getUpdateMark(JNIEnv *env, jobject) {
    struct stat sb;
    int update_sec{0};
    int update_nsec{0};

    if (stat("/data/data", &sb) != -1) {
        update_sec = (int) sb.st_atim.tv_sec;
        update_nsec = (int) sb.st_atim.tv_nsec;
    }

    std::string id = std::to_string(update_sec) + "." + std::to_string(update_nsec);
    return env->NewStringUTF(id.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_phantomvk_democenter_utility_SysMarkJni_getBootMark(JNIEnv *env, jobject) {
    FILE *fp = fopen("/proc/sys/kernel/random/boot_id", "r");
    if (fp != nullptr) {
        int index = 0;
        int charVal;
        char bootId[BOOT_ID_LEN];

        while (index < BOOT_ID_LEN && ((charVal = fgetc(fp)) != EOF)) {
            bootId[index] = charVal;
            index += 1;
        }

        if (index == BOOT_ID_LEN) {
            bootId[BOOT_ID_LEN - 1] = '\0';
            std::string str_boot = bootId;
            return env->NewStringUTF(str_boot.c_str());
        }
    }

    char errBootId[]{'\0'};
    std::string str_boot = errBootId;
    return env->NewStringUTF(str_boot.c_str());
}