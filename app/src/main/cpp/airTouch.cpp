#include <jni.h>
#include <string>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <linux/fb.h>
#include <sys/mman.h>
#include <string.h>

#include <unistd.h>
#include "fflog.h"
#include <unistd.h>

#define CURSOR_SET_DATA  0x5810
#define CURSOR_GET_DATA  0x5811

typedef unsigned char   u8;

struct packets
{
    u8 	start_h,start_l;/*接收包起始码 正确为 0xff 0xff*/
    u8  y_h,y_l;		/*Y轴高低共12bit有效数据*/
    u8  x_h,x_l;		/*x轴高低共12bit有效数据*/
    u8	press,cmd;		/*按键按下标志，预留命令*/
    u8	end_h,end_l;	/*接收包尾码 正确为 0xfe 0xfe*/
}pAriTouch;						/**/




extern "C" {
int x,y;


void initCoord() {
    int coordX = 550;
    int coordY = 1700;
    pAriTouch.start_h = 0xff;
    pAriTouch.start_l = 0xff;
    pAriTouch.y_h = (coordY) >> 8;
    pAriTouch.y_l = coordY & 0xFF;
    pAriTouch.x_h = coordX >> 8;
    pAriTouch.x_l = coordX & 0xFF;
    pAriTouch.press = 0x55;
    pAriTouch.end_h = 0xfe;
    pAriTouch.end_l = 0xfe;
}
void setCoord(int coordX, int coordY) {
    x = coordX;
    y = coordY;
    pAriTouch.y_h = (coordY) >> 8;
    pAriTouch.y_l = coordY & 0xFF;
    pAriTouch.x_h = coordX >> 8;
    pAriTouch.x_l = coordX & 0xFF;
    pAriTouch.press = 0x55;

}

int ioctlClick() {
    int fd_gesture, ret;
    fd_gesture = open("/dev/gesture", O_RDWR); /*open device*/ //修改设备名为 gesture
    if (fd_gesture == -1) {
        printf("open device fd error!\n");
        return 0;
    }
    LOGFI("click x %d y %d",x,y);
    ret = ioctl(fd_gesture, CURSOR_SET_DATA, &pAriTouch);
    pAriTouch.press = 0x80;
    ret = ioctl(fd_gesture, CURSOR_SET_DATA, &pAriTouch);

    close(fd_gesture);

    LOGFI("fd %d ret %d",fd_gesture,ret);
    return ret;
}
JNIEXPORT jstring JNICALL
Java_com_touchclick_AirTouchJNI_clickFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    int ret;
    LOGFI("jni in");

    initCoord();

    setCoord(550,1700);//移动打卡 将产生浮窗
    ret = ioctlClick();
    sleep(10);

    setCoord(1080-825,445);//todo 关闭加班浮窗 坐标未知
    ret += ioctlClick();
    setCoord(1080-825,580);//todo 关闭加班浮窗 坐标未知
    ret += ioctlClick();
    sleep(5);

    if (ret < 0)
        return env->NewStringUTF("finish");
    else
        return env->NewStringUTF("finish");

}

JNIEXPORT jstring JNICALL
Java_com_touchclick_AirTouchJNI_mainFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char hello[100] = {"fd "};
    int ret;
    initCoord();
    LOGFI("jni in");

    int fd_gesture;
    fd_gesture = open("/dev/gesture", O_RDWR); /*open device*/ //修改设备名为 gesture
    if (fd_gesture == -1) {
        printf("open device fd error!\n");
        return 0;
    }
    ret = ioctl(fd_gesture, CURSOR_SET_DATA, &pAriTouch);
    pAriTouch.press = 0x80;
    ret = ioctl(fd_gesture, CURSOR_SET_DATA, &pAriTouch);

    close(fd_gesture);


    char temp[64];

    sprintf(temp, "%d", fd_gesture);
    strcat(hello, temp);

    strcat(hello, "  ret ");
    sprintf(temp, "%d", ret);
    strcat(hello, temp);

    LOGFI("fd %d ret %d",fd_gesture,ret);

    if (ret < 0)
        return env->NewStringUTF(hello);
    else
        return env->NewStringUTF(hello);

}
}