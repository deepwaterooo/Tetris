cd "${1:-.}" || exit 1
for file in * if NOT file == "openAllFiles.sh"
do
    emacs "$file" &
done
