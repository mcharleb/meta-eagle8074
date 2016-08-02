This README file contains information on building the `meta-eagle8074`
BSP layer, and programming the images using fastboot.
Please see the corresponding sections below for details.

Table of Contents
=================

- [Dependencies](https://github.qualcomm.com/mcharleb/meta-eagle8074#dependencies)
- [Prerequisites](https://github.qualcomm.com/mcharleb/meta-eagle8074#prerequisites)
- [Patches](https://github.qualcomm.com/mcharleb/meta-eagle8074#patches)
- [Building the `meta-eagle8074` BSP layer](https://github.qualcomm.com/mcharleb/meta-eagle8074#building-the-meta-eagle8074-bsp-layer)
- [Programming the OS image using `fastboot`](https://github.qualcomm.com/mcharleb/meta-eagle8074#programming-the-os-image-using-fastboot)
- [Programming the eagle image using `fastboot`](https://github.qualcomm.com/mcharleb/meta-eagle8074#programming-the-eagle-image-using-fastboot)
- Notes and tips
    * [Speed up your repo sync](https://github.qualcomm.com/mcharleb/meta-eagle8074#speed-up-your-repo-sync)
    * [How to debug a user program](https://github.qualcomm.com/mcharleb/meta-eagle8074#how-to-debug-a-user-program)


Dependencies
============

This layer depends on:

    URI: git://git.yoctoproject.org/poky
    layers: meta
    layers: meta-yocto
    layers: meta-android
    layers: meta-oe
    branch: jethro

Prerequisites
=============

Workstation used for build must be a linux x86-64 with Ubuntu 14.04.
Workstation must have a gcc 4.8 or latest with the support for both 64bit
and 32bit c/c++ support.

Following packages are required to be installed on the development machine.

```
sudo apt-get install libsdl1.2-dev texinfo chrpath
```

Patches
=======

Please submit any patches against this BSP to the atlanticus.sw.platform
(atlanticus.sw.platform@qti.qualcomm.com)


Building the `meta-eagle8074` BSP layer
============================================

In order to build an image with meta-eagle8074, you need to download
the source repositories. This can be done wih repo

    $ repo init -u git@github.qualcomm.com:mcharleb/manifest.git
    $ repo sync -j 16 -c --no-tags

This will setup the directories with all of the necessary layers and 
dependencies in place.

    $ cd poky
    $ source meta-eagle8074/scripts/set_bb_env.sh

You should then be able to build a minimal 8x96 image as such:

    $ bitbake 8x74-image


Multimedia capable build image can be made with:

    $ bitbake eagle-image

Programming the OS image using `fastboot`
=========================================

    $ fastboot erase boot
    $ fastboot flash boot build/tmp/deploy/images/qcom-bsp/boot.img
    $ fastboot erase system
    $ fastboot flash system build/tmp/deploy/images/qcom-bsp/8x96-image-qcom-bsp.ext4

Programming the eagle image using `fastboot`
===============================================

When you bake the eagle-image, in build/tmp/deploy/images/eagle8074, you will find `flash_build.py`. Run that script with the device connected to fastboot:

    $ python flash_build.py emmc
    
    
Programming the eagle image using an SD card
================================================
To find the name of your sd card, you can type

    $ lsblk

##### All files are located in 

    $ /prj/atlanticus/software/Excelsior/20160721-sdcard-boot-minimized/
    
More documentation is also available at the above location.

### Writing image file to SD card (slower, but easiest/reliable)

By default, the SD card image has been written a single image file to 
singleimage.bin. This can be flashed to the SD card with the dd command:

    $ sudo dd if=singleimage.bin of=<SD card device root>

EXAMPLE <WARNING - USE THE CORRECT /dev/sd* device for your system otherwise 
you may overwite your hard drive!>

    $ sudo dd if=singleimage.bin of=/dev/sdx


Notes and tips
===============

### Speed up your repo sync

A `repo sync` walks the list of git repositories in the manifest file and performs a fetch and checkout. Given the QuIC server hosted repositories tend to get bigger due to the meta-data associated with a repositories, we can speed up partially by dropping off one typical data element representing the tags. Git tags are usefull to identify a snapshot of file versions by name. Given manifest file doesn't call for any tags, following command is useful:

    $ repo sync -j 16 -c --no-tags

Note if you observe any failure, it may be due the fact manifest contains a project with a revision identified by tag. You will notice the failure during that project's checkout. A normal `repo sync` will subsequently update your projects with tags included.

TODO : Further improvement can be achieved if the git fetch is limited to the branch of interest (single branch and shallow history)

### How to debug a user program

##### Copy and Install the debug symbols for the program on target

Using adb on the host machine, push the program and its shared libraries along with debug package. Debug packages can be found under tmp/deploy/rpm.

    $ adb push tmp/deploy/rpm/armv7a_vfp_neon/camera-dbg-0.1-full.armv7a_vfp_neon.rpm /home/root

Open the target shell prompt with adb shell. Install the package on the device. For additional help with RPM visit : http://www.tecmint.com/20-practical-examples-of-rpm-commands-in-linux/

![Install debug package](images/install-debug-package.png)

##### Invoke gdb

Invoke the corresponding debugger, observe the debug symbols are extracted for the program being debugged. If you don't find it discover the symbols, check directory search path for with `show debug-file-directory`. 

![Invoke GDB](images/invoke-gdb.png)

##### Using cgdb

cgdb presents a better ncurses interface to gdb. This is merely a front end visualizer to gdb. The visual presentation is convenient for navigating the source files, breakpoints and stepping through the code. cgdb discovers the gdb from path. As the gdb is named differently based on the 32-bit or 64-bit arch, you will need to add symlink to /usr/bin/gdb.

    $ ln -s /usr/bin/arm-poky-linux-gnueabi-gdb /usr/bin/gdb

Run cgdb invoking the program for debug

    $ cgdb /usr/bin/camera-test

Visit this page for the comprehensive documentation on using the cgdb https://cgdb.github.io/docs/cgdb.html 

![Invoke CGDB](images/invoke-cgdb.png)

### Add current directory to command prompt

    export PS1='$(whoami)@$(hostname):$(pwd)$ '
