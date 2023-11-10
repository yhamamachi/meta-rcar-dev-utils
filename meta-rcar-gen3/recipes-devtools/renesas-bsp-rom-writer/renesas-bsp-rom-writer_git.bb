DESCRIPTION = "Renesas BSP ROM Writer"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PACKAGE_ARCH = "${MACHINE_ARCH}"
ARCH = "${@'32' if d.getVar('TARGET_ARCH') == 'arm' else '64'}"

inherit deploy

S = "${WORKDIR}/git"

BRANCH = "master"
SRC_URI = "git://github.com/morimoto/renesas-bsp-rom-writer.git;branch=${BRANCH};protocol=https"
SRCREV = "62cdd9832aa314bf3a76216535b0593c48f3cc7e"

PV = "git${SRCPV}"

COMPATIBLE_MACHINE = "(ulcb)|(ebisu)|(draak)|(salvator-x)"
COMPATIBLE_MACHINE:append = "|(qemuarm)"

ALLOW_EMPTY:${PN} = "1"
ALLOW_EMPTY:${PN}-dev = "1"
ALLOW_EMPTY:${PN}-staticdev = "1"

SRC_URI:append = " \
    file://0001-starterkit-config-mot-Change-mot-filepath.patch \
    ${@'' if d.getVar('TARGET_ARCH') == 'arm' else \
        'file://0002-starterkit-config-mot-Change-to-use-64bit-mode.patch'} \
    file://0003-ebisu-config-sw-Fix-SW10-state.patch \
"

# do_configure() nothing
do_configure[noexec] = "1"
# do_compile() nothing
do_compile[noexec] = "1"
# do_install() nothing
do_install[noexec] = "1"

do_deploy() {
    # Create deploy folder
    rm -rf ${DEPLOYDIR}
    install -d ${DEPLOYDIR}

    # Copy to deploy folder
    cp -rf ${S} ${DEPLOYDIR}/${PN}_${ARCH}bit
    rm -rf ${DEPLOYDIR}/${PN}_${ARCH}bit/.git
}

addtask deploy before do_build after do_compile

