DESCRIPTION = "Simple Video Streaming Application"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

BRANCH = "main"
SRC_URI = "git://github.com/yhamamachi/simple-video-streaming.git;branch=${BRANCH};protocol=https"
SRCREV = "b5d830df0a2851fd0ead7f0ea521a25e314d19bb"

PV = "git${SRCPV}"

ALLOW_EMPTY_${PN} = "1"
ALLOW_EMPTY_${PN}-dev = "1"
ALLOW_EMPTY_${PN}-staticdev = "1"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "simple-video-streaming.service"
SYSTEMD_SERVICE_${PN} = "${SYSTEMD_SERVICE_FILENAME}"

# For debug purpose service file
TEST_SYSTEMD_SERVICE_FILENAME = "simple-video-streaming-test.service"
FILES_${PN} = " \
    ${systemd_unitdir}/system/${TEST_SYSTEMD_SERVICE_FILENAME} \
    ${USRBINPATH}/* \
"

SRC_URI_append = " \
    file://${SYSTEMD_SERVICE_FILENAME} \
    file://${TEST_SYSTEMD_SERVICE_FILENAME} \
"

# do_configure() nothing
do_configure[noexec] = "1"
# do_compile() nothing
do_compile[noexec] = "1"
# do_install() nothing
# do_install[noexec] = "1"

do_install() {
    # Service
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SYSTEMD_SERVICE_FILENAME} ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${TEST_SYSTEMD_SERVICE_FILENAME} ${D}/${systemd_unitdir}/system

    # Copy contents
    install -d ${D}/${USRBINPATH}/${PN}
    for file in `ls -F ${S}/ | grep -v /`; do
        install -m 0644 ${S}/${file} ${D}/${USRBINPATH}/${PN}
    done
}

