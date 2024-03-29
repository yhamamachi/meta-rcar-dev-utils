# meta-rcar-gen3

## Description

This layer adds useful package to R-Car BSP development.  
Currently, it supports following packages:
- [renesas-bsp-rom-writer](https://github.com/morimoto/renesas-bsp-rom-writer)(includes [flash-writer](https://github.com/renesas-rcar/flash_writer))
- core-image-busybox: Very small and simple rootfs inspired by [tiny-rootfs](https://github.com/morimoto/tiny-rootfs)

## Quick guide(How to use)

### Adding renesas-bsp-rom-writer into deploy direcotry

Follow the build instruction published on eLinux wiki up to steps before executing bitbake.
- https://elinux.org/R-Car/Boards/Yocto-Gen3
- https://elinux.org/R-Car/Boards/kingfisher/Yocto-Gen3
- https://elinux.org/R-Car/Boards/CCPF-SK/Yocto-Gen3

After that, executing following commands to build the BSP:
```bash
git clone https://github.com/yhamamachi/meta-rcar-dev-utils -b <branch_name> ${WORK}/meta-rcar-dev-utils
bitbake-layers add-layer ${WORK}/meta-rcar-dev-utils/meta-rcar-gen3
bitbake core-image-weston
```
- Supported build target
    - core-image-weston
    - core-image-minimal

Then, required files are stored in following path.
```
${WORK}/build/tmp/deploy/images/<board_name>/
```

### build kernel image with initramfs(core-image-busybox)

Follow the build instruction published on eLinux wiki up to steps before executing bitbake.
- https://elinux.org/R-Car/Boards/Yocto-Gen3
- https://elinux.org/R-Car/Boards/kingfisher/Yocto-Gen3
- https://elinux.org/R-Car/Boards/CCPF-SK/Yocto-Gen3

After that, executing following commands to build the BSP:
```bash
BUILD_DIR=build-h3ulcb-initramfs
cd ${WORK}
git clone https://github.com/yhamamachi/meta-rcar-dev-utils -b <branch_name> ${WORK}/meta-rcar-dev-utils
TEMPLATECONF=$PWD/meta-renesas-ccpf/meta-rcar-gen3/docs/sample/initramfs/ \
    source poky/oe-init-build-env ${BUILD_DIR}
echo 'MACHINE = h3ulcb' >> ./conf/local.conf
bitbake-layers add-layer ../meta-rcar-dev-utils/meta-rcar-gen3
bitbake core-image-busybox
```

## How to select branch

"main" branch supports latest Yocto BSP in general.  
If you want to use old version of BSP(such as stable version),  
please use branch as same as Yocto BSP version.  
ex.) "v5.9.0" branch

## R-Car Generation 3 Information

Refer to the following for more information from eLinux website

https://elinux.org/R-Car

## Maintainer

- Yuya Hamamachi <yuya.hamamachi.sx at renesas.com>


