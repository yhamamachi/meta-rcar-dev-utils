FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append_ulcb = " \
    file://uvc.cfg \
"

