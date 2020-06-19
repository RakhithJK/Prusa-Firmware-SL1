SUMMARY = "Extra machine specific configuration files"
HOMEPAGE = "https://wiki.gentoo.org/wiki/Eudev"
DESCRIPTION = "Extra machine specific configuration files for udev, specifically blacklist information."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
       file://automount.rules \
       file://mount.sh \
       file://mount.blacklist \
	   file://usbremount.cpp \
"

S = "${WORKDIR}"

do_compile() {
	${CXX} -std=c++17 ${CFLAGS} ${LDFLAGS} usbremount.cpp -o usbremount -lsystemd
}

do_install() {
	install -d ${D}${nonarch_base_libdir}/udev/rules.d
	install -d ${D}${nonarch_base_libdir}/udev/scripts
	install -d ${D}${bindir}

	install -m 0644 ${WORKDIR}/automount.rules	${D}${nonarch_base_libdir}/udev/rules.d/automount.rules
	install -m 0644 ${WORKDIR}/mount.blacklist	${D}${nonarch_base_libdir}/udev/
	install -m 0755 ${WORKDIR}/mount.sh		${D}${nonarch_base_libdir}/udev/scripts/mount.sh
	install -m 6755 ${WORKDIR}/usbremount	${D}${bindir}/usbremount

	sed -i 's|@base_sbindir@|${base_sbindir}|g' ${D}${nonarch_base_libdir}/udev/scripts/mount.sh
	sed -i 's|@nonarch_base_libdir@|${nonarch_base_libdir}|g' ${D}${nonarch_base_libdir}/udev/scripts/mount.sh
	sed -i 's|@systemd_unitdir@|${systemd_unitdir}|g' ${D}${nonarch_base_libdir}/udev/scripts/mount.sh
	sed -i 's|@sysconfdir@|${sysconfdir}|g' ${D}${nonarch_base_libdir}/udev/scripts/mount.sh

	sed -i 's|@nonarch_base_libdir@|${nonarch_base_libdir}|g' ${D}${nonarch_base_libdir}/udev/rules.d/automount.rules
}

FILES_${PN} = "\
	${nonarch_base_libdir}/udev \
	${bindir}/usbremount \
"
DEPENDS="systemd util-linux"
RDEPENDS_${PN} = "udev"
