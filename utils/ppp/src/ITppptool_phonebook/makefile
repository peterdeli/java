all: ITppptool_gzip
	
ITppptool_gzip: ITppptool_pkg 
	./pack.sh ./pkgs/ITppptool

ITppptool_pkg:
	/bin/rm -rf ./pkgs/ITppptool
	/usr/bin/pkgmk -o -d ./pkgs -r .

clean:
	/bin/rm -rf  ./pkgs/ITppptool ./pkgs/ITppptool.tar.gz

pkgrm:
	/usr/sbin/pkgrm -a admin ITppptool

pkgadd:
	/usr/sbin/pkgadd -a admin -d ./pkgs ITppptool

install: pkgrm pkgadd
