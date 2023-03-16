DESCRIPTION = "Cluster-app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/git/LICENSE;md5=785e89a3128be938c1446025250da013"

PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS_${PN} = "bash"

inherit npm
export NODE_OPTIONS="--max-old-space-size=8192"

BRANCH = "main"
SRCREV = "62997cfac85a804ad9fa4d9a6c3be0089b19dd27"
SRC_URI = "git://github.com/yhamamachi/automotive-viss2-client.git;branch=${BRANCH};protocol=https"
UPSTREAM_CHECK_COMMITS = "1"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "cluster-app.service"
SYSTEMD_SERVICE_${PN} = "${SYSTEMD_SERVICE_FILENAME}"

SRC_URI_append = " \
    file://${SYSTEMD_SERVICE_FILENAME} \
"

S = "${WORKDIR}"
B = "${S}/git"

do_configure() {
    :
}

do_compile_prepend() {
    cd ${B}/cluster-app
    npm install
}

do_compile() {
    cd ${B}/cluster-app
    npm run build
}

do_install () {
    # service file
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SYSTEMD_SERVICE_FILENAME} ${D}/${systemd_unitdir}/system

    # copy web root directory
    install -d ${D}/${USRBINPATH}/cluster-app
    cp -rf ${B}/cluster-app/build/* -t ${D}/${USRBINPATH}/cluster-app
}

