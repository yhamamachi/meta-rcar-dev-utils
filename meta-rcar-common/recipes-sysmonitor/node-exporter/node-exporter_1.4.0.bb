DESCRIPTION = "Node exporter"
LICENSE = "Apache-2.0"
VERSION = "1.4.0"
LIC_FILES_CHKSUM = "file://node_exporter-${VERSION}.linux-arm64/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS_${PN} = "bash"

BRANCH = "master"
SRCREV = "7da1321761b3b8dfc9e496e1a60e6a476fec6018"
SRC_URI = "git://github.com/prometheus/node_exporter.git;branch=${BRANCH};protocol=https"
SRC_URI_append = " https://github.com/prometheus/node_exporter/releases/download/v${VERSION}/node_exporter-${VERSION}.linux-arm64.tar.gz;name=bin"
SRC_URI[bin.sha256sum] = "0b20aa75385a42857a67ee5f6c7f67b229039a22a49c5c61c33f071356415b59"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "node_exporter.service"
SYSTEMD_SERVICE_${PN} = "${SYSTEMD_SERVICE_FILENAME}"

SRC_URI_append = " \
    file://${SYSTEMD_SERVICE_FILENAME} \
"

S = "${WORKDIR}"

# do_configure() nothing
do_configure[noexec] = "1"
# do_compile() nothing
do_compile[noexec] = "1"

do_install () {
    # service file
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SYSTEMD_SERVICE_FILENAME} ${D}/${systemd_unitdir}/system

    # binary
    install -d ${D}/${USRBINPATH}/node_exporter
    install -m 755 ${S}/node_exporter-${VERSION}.linux-arm64/node_exporter ${D}/${USRBINPATH}/node_exporter
}

