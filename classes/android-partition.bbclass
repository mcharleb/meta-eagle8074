LICENSE = "MIT"

IMAGE_INSTALL = ""
IMAGE_INSTALL = ""
IMAGE_LINGUAS = ""
PACKAGE_INSTALL = ""

inherit image

IMAGE_PREPROCESS_COMMAND = "remove_extra_files"

# delete the old image files that already exist
python do_rootfs_prepend() {
    os.system("rm -rf ${DEPLOY_DIR_IMAGE}/*${PN}*")
}

python remove_extra_files() {
    # Remove the RPM handling additions
    os.system("rm -rf ${WORKDIR}/rootfs/etc ${WORKDIR}/rootfs/var")
}

python do_rootfs_append() {
    os.system("ext2simg -v ${DEPLOY_DIR_IMAGE}/${PN}-${MACHINE}.ext4 ${DEPLOY_DIR_IMAGE}/${PN}-${MACHINE}.img")
}
