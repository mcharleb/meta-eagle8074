# Common code for generating QRL images

LICENSE = "BSD-3-Clause-Clear" 
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qr-linux/COPYING;md5=7b4fa59a65c2beb4b3795e2b3fbb8551"

inherit image_types
inherit multistrap-image

IMAGE_FSTYPES = "ext4"
IMAGE_LINGUAS = " "

SRC_URI += " \
   file://apt.conf \
   file://config.sh \
   file://resize \
   file://fstab \
   file://init \
   file://installPkgs.sh \
   file://interfaces \
   file://wpa_supplicant.conf \
   file://udev_files_to_keep.grep \
   file://qrl-common-inc.sh \
   file://qrl-copy-firmware.sh \
   file://qrl-config-macaddr.sh \
   file://qrl-display-macaddr.sh \
   "

DEPENDS += "virtual/kernel update-rc.d-native"

PV = "LNX.LX.1.0.14.07.2"

# Overall multistrap configuration
#     - From this the multistrap.conf file will be generated

# define the possible multistrap sections
#   Each section will specify how multistrap will download the dpkg from
MULTISTRAP_SECTIONS = "Raring Modules Packages Binaries"

# The value of the default section must be one of the sections listed in the above MULTISTRAP_SECTIONS variable
MULTISTRAP_DEFAULT_SECTION = "Packages"

MULTISTRAP_GENERAL_noauth = 'true'
MULTISTRAP_GENERAL_unpack = 'true'
MULTISTRAP_GENERAL_arch   = 'armhf'
MULTISTRAP_GENERAL_configscript = '${WORKDIR}/config.sh'

MULTISTRAP_SOURCE_Raring = "http://crd-thor-01/ports//"
MULTISTRAP_SUITE_Raring = "raring"
MULTISTRAP_COMPONENTS_Raring = "main universe"
MULTISTRAP_DEBOOTSTRAP_Raring = "1"
MULTISTRAP_APTSOURCES_Raring = "1"
MULTISTRAP_BUILD_Raring = "0"

MULTISTRAP_SOURCE_Modules = "copy://${DEPLOY_DIR}/deb/${MACHINE_ARCH} ./"
MULTISTRAP_DEBOOTSTRAP_Modules = "1"
MULTISTRAP_APTSOURCES_Modules = "0"
MULTISTRAP_BUILD_Modules = "1"

MULTISTRAP_SOURCE_Packages = "copy://${DEPLOY_DIR}/deb/${TUNE_PKGARCH} ./"
MULTISTRAP_DEBOOTSTRAP_Packages = "1"
MULTISTRAP_APTSOURCES_Packages = "0"
MULTISTRAP_BUILD_Packages = "1"

# Define package groups here, which are lists of packages and the multistrap section they belong to
#      If a package group is defined and the multistrap section is not defined, it will be placed in the default section

# Some explanation for why some of these packages are included
# - vim-tiny, less: For basic editing
# - apt: Obvious
# - module-init-tools: For dealing with kernel modules
# - Networking:
#    - iputils-ping openssh-*: For basic networking
#    - iproute: To use iproute2 for network link management
#    - wpasupplicant: To manage Wi-Fi
#    - wireless-tools: To get iwconfig family of tools (deprecated) to manually manage Wi-Fi

PACKAGE_GROUP_ubuntu = "ubuntu-minimal vim-tiny less apt perl iputils-ping openssh-client openssh-server iproute wpasupplicant wireless-tools module-init-tools strace tcpdump iperf build-essential logrotate expect file bluetooth bluez bluez-tools obexftp python-gobject python-dbus ussp-push unzip ntp"
MULTISTRAP_SECTION_ubuntu = "Raring"

PACKAGE_GROUP_ubuntuXSlim = "xserver-xorg xterm x11-apps icewm firefox slim"
MULTISTRAP_SECTION_ubuntuXSlim = "Raring"

PACKAGE_GROUP_ubuntuXubuntu = "gdm xubuntu-desktop firefox"
MULTISTRAP_SECTION_ubuntuXubuntu = "Raring"

PACKAGE_GROUP_userpkgs = "android-tools serial-console glib-2.0 glib-2.0-bin"
MULTISTRAP_SECTION_userpkgs = "Packages"

fixup_conf() {
    # Convert flat directories to package repositories
    CURDIR=`pwd`
    for dir in `ls ${DEPLOY_DIR}/deb`
      do
         cd ${DEPLOY_DIR}/deb/${dir}
         dpkg-scanpackages . /dev/null | gzip -9c > Packages.gz
         dpkg-scansources . /dev/null | gzip -9c > Sources.gz
      done
    cd ${CURDIR}
}

MULTISTRAP_PREPROCESS_COMMAND = "fixup_conf"

fixup_sysroot() {
    CURDIR=`pwd`
    cd ${THISDIR}
    BUILDLABEL=$(git describe)
    cd ${CURDIR}
    install ${WORKDIR}/resize ${IMAGE_ROOTFS}${sysconfdir}/init.d/resize
    update-rc.d -r ${IMAGE_ROOTFS} resize start 20 2 .
    install -d ${IMAGE_ROOTFS}/usr/local/qr-linux
    install ${WORKDIR}/installPkgs.sh ${IMAGE_ROOTFS}/usr/local/qr-linux/installPkgs.sh
    install ${WORKDIR}/config.sh ${IMAGE_ROOTFS}/config.sh
    install -b -S .upstart ${WORKDIR}/init ${IMAGE_ROOTFS}/sbin/init
    install -m 644 ${WORKDIR}/fstab ${IMAGE_ROOTFS}${sysconfdir}/fstab
    install -m 644 ${WORKDIR}/interfaces ${IMAGE_ROOTFS}${sysconfdir}/network/interfaces
    install -m 644 ${WORKDIR}/wpa_supplicant.conf ${IMAGE_ROOTFS}${sysconfdir}/wpa_supplicant/wpa_supplicant.conf
    echo ${PN}-${PV}-`date '+%F-%T'`-`id -un`-${BUILDLABEL} > ${IMAGE_ROOTFS}${sysconfdir}/qrl-version
    sed -i -e 's/DEFAULT_RUNLEVEL=2/DEFAULT_RUNLEVEL=1/' ${IMAGE_ROOTFS}${sysconfdir}/init/rc-sysinit.conf
    sed -i -e 's/rmdir/rm -rf/' ${IMAGE_ROOTFS}/var/lib/dpkg/info/base-files.postinst
    find ${IMAGE_ROOTFS} -name \*.rules | grep -v -f ${WORKDIR}/udev_files_to_keep.grep | xargs rm -f

    ln -s ${IMAGE_ROOTFS}/usr/lib/insserv/insserv ${IMAGE_ROOTFS}/sbin 
    # Install qrl-*.sh
    mkdir -p ${IMAGE_ROOTFS}/usr/local/qr-linux
    install -m 644 ${WORKDIR}/qrl-common-inc.sh ${IMAGE_ROOTFS}/usr/local/qr-linux
    install -m 755 ${WORKDIR}/qrl-config-macaddr.sh ${IMAGE_ROOTFS}/usr/local/qr-linux
    install -m 755 ${WORKDIR}/qrl-copy-firmware.sh ${IMAGE_ROOTFS}/usr/local/qr-linux
    install -m 755 ${WORKDIR}/qrl-display-macaddr.sh ${IMAGE_ROOTFS}/usr/local/qr-linux
}

IMAGE_PREPROCESS_COMMAND = "fixup_sysroot"

rename_images() {
    rm -f ${DEPLOY_DIR_IMAGE}/out/userdata-${PN}.img
    rm -f ${DEPLOY_DIR_IMAGE}/out/userdata.img
    install -d ${DEPLOY_DIR_IMAGE}/out
    cp ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 ${DEPLOY_DIR_IMAGE}/out/userdata-${PN}.img
    ln -s userdata-${PN}.img ${DEPLOY_DIR_IMAGE}/out/userdata.img
}

IMAGE_POSTPROCESS_COMMAND = "rename_images"
