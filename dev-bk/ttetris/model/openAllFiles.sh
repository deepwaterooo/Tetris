cd "${1:-.}" || exit 1
for file in * 
do
    emacs "$file" &
done
