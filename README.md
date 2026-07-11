# NSF Maven Repository

This project provides an NSF that can act as a basic Maven repository. It supports deployment and reading, and access control is handled via the database ACL and Domino's HTTP authentication.

Currently, the application is mostly a "dumb" repository: each NSF instance is a single repository, it has no support for proxying/caching from upstream repositories, it takes the uploaded artifacts and XML metadata from the client and stores them as-is.

### Requirements

This application requires Domino 14 or above and the [XPages Jakarta EE Support project](https://github.com/OpenNTF/org.openntf.xsp.jakartaee/) version 3.6.0 or above.

### Usage

To use this application, deploy the NSF on your Domino server and configure its ACL as desired. The NSF contains two scheduled agents to prune snapshot versions other than the latest and to auto-remove known "junk" files, and you should sign these and configure them to run on an appropriate server. It'd also be a good idea to make sure DAOS is on for the NSF (`load compact -DAOS on mvn.nsf`) and the design index is up to date for DQL (`load updall -e mvn.nsf`).

By default, the repository will be available with a base URL of "http(s)://yourserver.com/path/to/repo.nsf/xsp/repository" and can be configured as such in Maven commands and configurations.

Due to limitations in Domino's XPages environment, if you intend to access or deploy any artifacts with ".xsp" in their artifact or gruop IDs, you must configure the deployed NSF as a Jakarta Module in the XPage JEE project's "jakartaconfig.nsf". Refer to that project for documentation. When configured this way, the repository will be available with a base URL of "http(s)://yourserver.com/mappedpath/repository".

The layout of documents inside the NSF is compatible with the "NSF Document Layout" type of the [NSF File Server](https://github.com/IKSG/nsf-file-server/) project.

### License

This project is licensed using the Apache License 2.0.