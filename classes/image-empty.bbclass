IMAGE_INSTALL = ""
IMAGE_INSTALL = ""
IMAGE_LINGUAS = ""
PACKAGE_INSTALL = ""

inherit image

# delete the old image files that already exist
python do_rootfs_prepend() {
    os.system("rm -rf ${DEPLOY_DIR_IMAGE}/*${PN}*")
}

python do_rootfs_append() {
    # Remove the RPM handling additions
    os.system("rm -rf ${WORKDIR}/rootfs/etc ${WORKDIR}/rootfs/var")
    os.system("ext2simg -v ${DEPLOY_DIR_IMAGE}/${PN}.ext4 ${DEPLOY_DIR_IMAGE}/{PN}.img")
}
