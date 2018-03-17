#!/bin/bash

source ./script/config.sh

echo
echo
echo
echo
echo ------------------------------------------------------------
echo "   ${CONFIG_COLOR_CYAN}Clean project${CONFIG_COLOR_RESET} (delete build folders/files)"
echo "      Script File - ${CONFIG_COLOR_YELLOW}clean.sh${CONFIG_COLOR_RESET}"
echo ------------------------------------------------------------
echo

rm -rf build
rm -rf lib
rm -rf bin

rm -rf CMakeFiles
rm -rf cmake-build-debug
rm cmake_install.cmake
rm opusfile.cbp
rm Makefile
rm Project.cbp

exit 0
