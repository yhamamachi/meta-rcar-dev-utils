DESCRIPTION = "Cluster-app"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/git/LICENSE;md5=785e89a3128be938c1446025250da013"

RDEPENDS:${PN} = "bash"

DEPENDS:append = " nodejs-native"
export NODE_OPTIONS="--max-old-space-size=8192"

BRANCH = "main"
SRCREV = "52b90c5d5fb6b316e60d0329a6b3e34c0453b15e"
SRC_URI = "git://github.com/yhamamachi/automotive-viss2-client.git;branch=${BRANCH};protocol=https"
UPSTREAM_CHECK_COMMITS = "1"

inherit systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_FILENAME = "cluster-app.service"
SYSTEMD_SERVICE:${PN} = "${SYSTEMD_SERVICE_FILENAME}"

SRC_URI:append = " \
    file://${SYSTEMD_SERVICE_FILENAME} \
"

S = "${WORKDIR}"
B = "${S}/git"

do_configure() {
    :
}

do_compile:prepend() {
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

