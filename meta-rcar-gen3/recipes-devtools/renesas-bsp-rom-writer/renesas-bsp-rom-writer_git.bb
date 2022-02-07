DESCRIPTION = "Renesas BSP ROM Writer"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PACKAGE_ARCH = "${MACHINE_ARCH}"
ARCH = "${@'32' if d.getVar('TARGET_ARCH') == 'arm' else '64'}"

inherit deploy

S = "${WORKDIR}/git"

BRANCH = "master"
SRC_URI = "git://github.com/morimoto/renesas-bsp-rom-writer.git;branch=${BRANCH};protocol=https"
SRCREV = "4817260fdc4cd6a46ed4557c336c28d8d18d9cae"

PV = "git${SRCPV}"

COMPATIBLE_MACHINE = "(ulcb)"
COMPATIBLE_MACHINE_append = "|(qemuarm)"

ALLOW_EMPTY_${PN} = "1"
ALLOW_EMPTY_${PN}-dev = "1"
ALLOW_EMPTY_${PN}-staticdev = "1"

SRC_URI_append = " \
    file://0001-script-python-base-Change-Flash-writer-path.patch \
    file://0002-starterkit-config-os-yocto-Support-Yocto-5.9.0.patch \
    ${@'' if d.getVar('TARGET_ARCH') == 'arm' else \
        'file://0003-starterkit-config-mot-Change-to-use-64bit-mode.patch'} \
"

# do_compile() nothing
do_compile[noexec] = "1"
# do_install() nothing
do_install[noexec] = "1"

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}

    # Copy to deploy folder
    rm -rf ${DEPLOYDIR}/${PN}
    cp -rpf ${S} ${DEPLOYDIR}
    rm -rf ${DEPLOYDIR}/git/.git
    mv -f ${DEPLOYDIR}/git ${DEPLOYDIR}/${PN}_${ARCH}bit
}

addtask deploy before do_build after do_compile

