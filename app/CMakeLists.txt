# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
#设置变量SRC_DIR为lamemp3的所在路径
set(SRC_DIR src/main/cpp/lamemp3)

#指定头文件所在，可以多次调用，指定多个路径
include_directories(src/main/cpp/libmp3lame)
#添加自自定义的so库时，有两种方式，一种添加一个目录，一种一个个文件添加

#设定一个目录
aux_source_directory(src/main/cpp/libmp3lame SRC_LIST)

#将前面目录下所有的文件都添加进去
add_library(lamemp3 SHARED src/main/cpp/  ${SRC_LIST})


#一个个文件的加
#add_library(lame-mp3
#            SHARED
#            lameutil.c
#            ${SRC_DIR}/bitstream.c
#            ${SRC_DIR}/encoder.c
#            ${SRC_DIR}/fft.c
#            ${SRC_DIR}/gain_analysis.c
#            ${SRC_DIR}/id3tag.c
#            ${SRC_DIR}/lame.c
#            ${SRC_DIR}/mpglib_interface.c
#            ${SRC_DIR}/newmdct.c
#            ${SRC_DIR}/presets.c
#            ${SRC_DIR}/psymodel.c
#            ${SRC_DIR}/quantize.c
#            ${SRC_DIR}/quantize_pvt.c
#            ${SRC_DIR}/reservoir.c
#            ${SRC_DIR}/set_get.c
#            ${SRC_DIR}/tables.c
#            ${SRC_DIR}/takehiro.c
#            ${SRC_DIR}/util.c
#            ${SRC_DIR}/vbrquantize.c
#            ${SRC_DIR}/VbrTag.c
#            ${SRC_DIR}/version.c
#            )

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       lamemp3

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )