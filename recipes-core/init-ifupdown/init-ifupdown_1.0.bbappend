SRC_URI += "file://interfaces"

do_install_append() {
   install ${S}/interfaces ${D}/usr/network/interfaces
}
