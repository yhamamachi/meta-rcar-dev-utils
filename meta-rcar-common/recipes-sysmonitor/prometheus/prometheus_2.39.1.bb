DESCRIPTION = "Prometheus"
LICENSE = "Apache-2.0"
VERSION = "2.39.1"
LIC_FILES_CHKSUM = "file://prometheus-${VERSION}.linux-arm64/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS_${PN} = "bash"

BRANCH = "main"
SRCREV = "dcd6af9e0d56165c6f5c64ebbc1fae798d24933a"
SRC_URI = "git://github.com/prometheus/prometheus.git;branch=${BRANCH};protocol=https"
SRC_URI_append = " https://github.com/prometheus/prometheus/releases/download/v${VERSION}/prometheus-${VERSION}.linux-arm64.tar.gz;name=bin"
SRC_URI[bin.sha256sum] = "8f8caf7520544827b1132737689d0e90748d5f42bdaa4e854ccfcbb9275f940f"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "prometheus.service"
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
    install -d ${D}/${USRBINPATH}/prometheus
    install -m 755 ${S}/prometheus-${VERSION}.linux-arm64/prometheus ${D}/${USRBINPATH}/prometheus
    install -m 755 ${S}/prometheus-${VERSION}.linux-arm64/promtool ${D}/${USRBINPATH}/prometheus
    install -m 644 ${S}/prometheus-${VERSION}.linux-arm64/prometheus.yml ${D}/${USRBINPATH}/prometheus
    ITEMS="console_libraries consoles"
    for item in ${ITEMS}; do \
        install -d ${D}/${USRBINPATH}/prometheus/${item}
        cp -rf ${S}/prometheus-${VERSION}.linux-arm64/${item}/* -t ${D}/${USRBINPATH}/prometheus/${item}
    done
}

